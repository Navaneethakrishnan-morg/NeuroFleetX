package com.neurofleetx.service;

import com.neurofleetx.model.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@Slf4j
public class RouteOptimizationEngine {
    
    private static class Node implements Comparable<Node> {
        String location;
        double cost;
        double distance;
        List<String> path;
        
        Node(String location, double cost, double distance, List<String> path) {
            this.location = location;
            this.cost = cost;
            this.distance = distance;
            this.path = new ArrayList<>(path);
        }
        
        @Override
        public int compareTo(Node other) {
            return Double.compare(this.cost, other.cost);
        }
    }
    
    public List<Route> generateOptimizedRoutes(
            String startLocation, String endLocation,
            Double startLat, Double startLon,
            Double endLat, Double endLon,
            Long vehicleId) {
        
        List<Route> routes = new ArrayList<>();
        
        Map<String, List<Edge>> graph = buildRouteGraph(startLat, startLon, endLat, endLon);
        
        Route fastestRoute = dijkstraOptimization(
            graph, startLocation, endLocation, 
            startLat, startLon, endLat, endLon, 
            vehicleId, Route.OptimizationType.FASTEST
        );
        routes.add(fastestRoute);
        
        Route energyRoute = dijkstraOptimization(
            graph, startLocation, endLocation, 
            startLat, startLon, endLat, endLon, 
            vehicleId, Route.OptimizationType.ENERGY_EFFICIENT
        );
        routes.add(energyRoute);
        
        Route balancedRoute = dijkstraOptimization(
            graph, startLocation, endLocation, 
            startLat, startLon, endLat, endLon, 
            vehicleId, Route.OptimizationType.BALANCED
        );
        routes.add(balancedRoute);
        
        return routes;
    }
    
    private Route dijkstraOptimization(
            Map<String, List<Edge>> graph,
            String start, String end,
            Double startLat, Double startLon,
            Double endLat, Double endLon,
            Long vehicleId,
            Route.OptimizationType type) {
        
        PriorityQueue<Node> queue = new PriorityQueue<>();
        Map<String, Double> distances = new HashMap<>();
        Set<String> visited = new HashSet<>();
        
        queue.add(new Node(start, 0, 0, Arrays.asList(start)));
        distances.put(start, 0.0);
        
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            
            if (visited.contains(current.location)) continue;
            visited.add(current.location);
            
            if (current.location.equals(end)) {
                return createRouteFromPath(
                    current.path, current.distance, current.cost,
                    startLat, startLon, endLat, endLon,
                    vehicleId, type, start, end
                );
            }
            
            List<Edge> neighbors = graph.getOrDefault(current.location, new ArrayList<>());
            for (Edge edge : neighbors) {
                if (visited.contains(edge.destination)) continue;
                
                double edgeCost = calculateEdgeCost(edge, type);
                double newCost = current.cost + edgeCost;
                double newDistance = current.distance + edge.distance;
                
                if (newCost < distances.getOrDefault(edge.destination, Double.MAX_VALUE)) {
                    distances.put(edge.destination, newCost);
                    List<String> newPath = new ArrayList<>(current.path);
                    newPath.add(edge.destination);
                    queue.add(new Node(edge.destination, newCost, newDistance, newPath));
                }
            }
        }
        
        return createDirectRoute(start, end, startLat, startLon, endLat, endLon, vehicleId, type);
    }
    
    private double calculateEdgeCost(Edge edge, Route.OptimizationType type) {
        return switch (type) {
            case FASTEST -> edge.time * (1 + edge.trafficFactor);
            case ENERGY_EFFICIENT -> edge.distance * edge.energyFactor;
            case BALANCED -> (edge.time * 0.5 + edge.distance * 0.3 + edge.energyFactor * 20);
            case SHORTEST -> edge.distance;
        };
    }
    
    private Route createRouteFromPath(
            List<String> path, double distance, double cost,
            Double startLat, Double startLon, Double endLat, Double endLon,
            Long vehicleId, Route.OptimizationType type,
            String start, String end) {
        
        Route route = new Route();
        route.setVehicleId(vehicleId);
        route.setStartLocation(start);
        route.setEndLocation(end);
        route.setStartLatitude(startLat);
        route.setStartLongitude(startLon);
        route.setEndLatitude(endLat);
        route.setEndLongitude(endLon);
        route.setDistanceKm(distance);
        route.setEtaMinutes((int) Math.ceil(cost));
        route.setOptimizationType(type);
        route.setOptimizedPath(String.join(" -> ", path));
        route.setStatus(Route.RouteStatus.PENDING);
        
        route.setTrafficLevel(estimateTrafficLevel(cost, distance));
        route.setEnergyCost(calculateEnergyCost(distance, type));
        
        return route;
    }
    
    private Route createDirectRoute(
            String start, String end,
            Double startLat, Double startLon,
            Double endLat, Double endLon,
            Long vehicleId,
            Route.OptimizationType type) {
        
        double distance = calculateHaversineDistance(startLat, startLon, endLat, endLon);
        int eta = (int) Math.ceil(distance / 0.5);
        
        Route route = new Route();
        route.setVehicleId(vehicleId);
        route.setStartLocation(start);
        route.setEndLocation(end);
        route.setStartLatitude(startLat);
        route.setStartLongitude(startLon);
        route.setEndLatitude(endLat);
        route.setEndLongitude(endLon);
        route.setDistanceKm(distance);
        route.setEtaMinutes(eta);
        route.setOptimizationType(type);
        route.setOptimizedPath(start + " -> " + end);
        route.setStatus(Route.RouteStatus.PENDING);
        route.setTrafficLevel(Route.TrafficLevel.MEDIUM);
        route.setEnergyCost(calculateEnergyCost(distance, type));
        
        return route;
    }
    
    private Map<String, List<Edge>> buildRouteGraph(
            Double startLat, Double startLon, 
            Double endLat, Double endLon) {
        
        Map<String, List<Edge>> graph = new HashMap<>();
        
        Random random = new Random(42);
        List<String> waypoints = generateWaypoints(startLat, startLon, endLat, endLon);
        
        for (int i = 0; i < waypoints.size(); i++) {
            graph.put(waypoints.get(i), new ArrayList<>());
            
            for (int j = 0; j < waypoints.size(); j++) {
                if (i != j && random.nextDouble() > 0.5) {
                    double distance = 2 + random.nextDouble() * 8;
                    double time = distance / (0.4 + random.nextDouble() * 0.3);
                    double trafficFactor = random.nextDouble() * 0.3;
                    double energyFactor = 0.8 + random.nextDouble() * 0.4;
                    
                    graph.get(waypoints.get(i)).add(
                        new Edge(waypoints.get(j), distance, time, trafficFactor, energyFactor)
                    );
                }
            }
        }
        
        return graph;
    }
    
    private List<String> generateWaypoints(Double startLat, Double startLon, Double endLat, Double endLon) {
        List<String> waypoints = new ArrayList<>();
        waypoints.add("Start");
        
        waypoints.add("Highway-1");
        waypoints.add("Junction-A");
        waypoints.add("Downtown");
        waypoints.add("Bridge-North");
        waypoints.add("Industrial-Zone");
        waypoints.add("Park-Avenue");
        waypoints.add("Main-Street");
        waypoints.add("Station-Central");
        
        waypoints.add("End");
        
        return waypoints;
    }
    
    private Route.TrafficLevel estimateTrafficLevel(double time, double distance) {
        double speed = distance / (time / 60.0);
        if (speed > 50) return Route.TrafficLevel.LOW;
        if (speed > 30) return Route.TrafficLevel.MEDIUM;
        if (speed > 20) return Route.TrafficLevel.HIGH;
        return Route.TrafficLevel.SEVERE;
    }
    
    private double calculateEnergyCost(double distance, Route.OptimizationType type) {
        double baseCost = distance * 0.15;
        return switch (type) {
            case ENERGY_EFFICIENT -> baseCost * 0.7;
            case FASTEST -> baseCost * 1.3;
            case BALANCED -> baseCost;
            case SHORTEST -> baseCost * 0.9;
        };
    }
    
    public double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
    
    private static class Edge {
        String destination;
        double distance;
        double time;
        double trafficFactor;
        double energyFactor;
        
        Edge(String destination, double distance, double time, double trafficFactor, double energyFactor) {
            this.destination = destination;
            this.distance = distance;
            this.time = time;
            this.trafficFactor = trafficFactor;
            this.energyFactor = energyFactor;
        }
    }
}
