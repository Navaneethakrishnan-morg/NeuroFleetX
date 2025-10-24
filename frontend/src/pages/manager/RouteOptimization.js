import React from 'react';
import { RouteIcon, LocationIcon } from '../../components/Icons';

const RouteOptimization = () => {
  const routes = [
    { id: 1, name: 'Downtown Loop', vehicles: 5, avgTime: 25, efficiency: 92, distance: 12.5 },
    { id: 2, name: 'Airport Route', vehicles: 3, avgTime: 45, efficiency: 88, distance: 28.3 },
    { id: 3, name: 'Suburban Express', vehicles: 4, avgTime: 35, efficiency: 95, distance: 18.7 },
    { id: 4, name: 'City Center', vehicles: 6, avgTime: 20, efficiency: 90, distance: 8.2 },
  ];

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-3xl font-bold text-white mb-2 flex items-center gap-3">
          <RouteIcon size="lg" className="text-accent-green" />
          Route Optimization
        </h2>
        <p className="text-white/50">Optimize routes for maximum efficiency</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="glass-card p-6">
          <h3 className="text-xl font-bold text-white mb-4">Active Routes</h3>
          <div className="space-y-3">
            {routes.map((route) => (
              <div key={route.id} className="glass-card-hover p-4">
                <div className="flex items-center justify-between mb-3">
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 rounded-lg bg-gradient-to-br from-accent-green to-accent-cyan flex items-center justify-center">
                      <RouteIcon size="sm" className="text-white" />
                    </div>
                    <div>
                      <h4 className="font-bold text-white">{route.name}</h4>
                      <p className="text-sm text-white/50">{route.vehicles} vehicles</p>
                    </div>
                  </div>
                  <span className={`px-3 py-1 rounded-full text-xs font-bold ${
                    route.efficiency >= 90 ? 'bg-accent-green/20 text-accent-green' : 'bg-accent-cyan/20 text-accent-cyan'
                  }`}>
                    {route.efficiency}% efficient
                  </span>
                </div>
                <div className="grid grid-cols-2 gap-3 text-sm">
                  <div className="flex justify-between">
                    <span className="text-white/60">Avg Time:</span>
                    <span className="text-white font-semibold">{route.avgTime} min</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-white/60">Distance:</span>
                    <span className="text-white font-semibold">{route.distance} km</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="glass-card p-6">
          <h3 className="text-xl font-bold text-white mb-4">Optimization Suggestions</h3>
          <div className="space-y-3">
            <div className="p-4 bg-accent-green/10 border border-accent-green/30 rounded-xl">
              <div className="flex items-start gap-3">
                <span className="text-2xl">💡</span>
                <div>
                  <p className="text-accent-green font-semibold mb-1">Downtown Loop Optimization</p>
                  <p className="text-white/70 text-sm">Reduce avg time by 15% with alternate route via Park Ave</p>
                </div>
              </div>
            </div>
            <div className="p-4 bg-accent-cyan/10 border border-accent-cyan/30 rounded-xl">
              <div className="flex items-start gap-3">
                <span className="text-2xl">⚡</span>
                <div>
                  <p className="text-accent-cyan font-semibold mb-1">Airport Route Enhancement</p>
                  <p className="text-white/70 text-sm">Add 2 more vehicles to handle peak hour traffic</p>
                </div>
              </div>
            </div>
            <div className="p-4 bg-accent-purple/10 border border-accent-purple/30 rounded-xl">
              <div className="flex items-start gap-3">
                <span className="text-2xl">🚦</span>
                <div>
                  <p className="text-accent-purple font-semibold mb-1">Traffic Pattern Analysis</p>
                  <p className="text-white/70 text-sm">Avoid Main St between 8-10 AM for 20% faster routes</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="glass-card p-6">
        <h3 className="text-xl font-bold text-white mb-4">Route Map</h3>
        <div className="bg-dark-700/40 rounded-xl border border-white/10 p-8 text-center aspect-video flex flex-col items-center justify-center">
          <div className="w-24 h-24 rounded-full bg-gradient-to-br from-accent-green to-accent-cyan flex items-center justify-center mb-6 animate-pulse">
            <LocationIcon size="xl" className="text-white" />
          </div>
          <p className="text-white/70 text-lg mb-4">🗺️ Interactive Route Map</p>
          <p className="text-white/50 text-sm max-w-md">
            AI-powered route optimization with real-time traffic data and emerald-highlighted efficient paths
          </p>
        </div>
      </div>
    </div>
  );
};

export default RouteOptimization;
