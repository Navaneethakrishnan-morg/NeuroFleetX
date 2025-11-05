"""
AI-Powered Route Optimization Service
Predicts ETA, optimal routes, and provides speed/feedback analysis
"""

import mysql.connector
import pandas as pd
import numpy as np
from datetime import datetime, timedelta
from sklearn.ensemble import RandomForestRegressor
from sklearn.preprocessing import StandardScaler
import joblib
import os
import json

class RouteOptimizer:
    def __init__(self, db_config):
        self.db_config = db_config
        self.model = None
        self.scaler = StandardScaler()
        self.model_path = 'models/eta_model.pkl'
        self.scaler_path = 'models/scaler.pkl'
        
        # Create models directory if it doesn't exist
        os.makedirs('models', exist_ok=True)
        
        # Load or train model
        if os.path.exists(self.model_path) and os.path.exists(self.scaler_path):
            self.model = joblib.load(self.model_path)
            self.scaler = joblib.load(self.scaler_path)
        else:
            self.train_model()
    
    def get_db_connection(self):
        """Create database connection"""
        return mysql.connector.connect(**self.db_config)
    
    def fetch_historical_data(self):
        """Fetch historical trip data from MySQL"""
        conn = self.get_db_connection()
        query = """
        SELECT 
            t.id,
            t.distance,
            t.duration,
            t.start_time,
            t.end_time,
            v.type as vehicle_type,
            v.speed,
            v.is_electric,
            b.pickup_location,
            b.dropoff_location,
            HOUR(t.start_time) as hour_of_day,
            DAYOFWEEK(t.start_time) as day_of_week
        FROM trips t
        JOIN bookings b ON t.booking_id = b.id
        JOIN vehicles v ON b.vehicle_id = v.id
        WHERE t.end_time IS NOT NULL
        AND t.distance > 0
        AND t.duration > 0
        """
        
        try:
            df = pd.read_sql(query, conn)
            conn.close()
            return df
        except Exception as e:
            print(f"Error fetching data: {e}")
            conn.close()
            return pd.DataFrame()
    
    def prepare_features(self, df):
        """Prepare features for ML model"""
        # Calculate actual duration in minutes
        if 'start_time' in df.columns and 'end_time' in df.columns:
            df['actual_duration'] = (pd.to_datetime(df['end_time']) - 
                                    pd.to_datetime(df['start_time'])).dt.total_seconds() / 60
        
        # Encode categorical variables
        vehicle_type_map = {'SEDAN': 1, 'SUV': 2, 'VAN': 3, 'TRUCK': 4, 'BUS': 5, 'BIKE': 0}
        df['vehicle_type_encoded'] = df['vehicle_type'].map(vehicle_type_map)
        df['is_electric_encoded'] = df['is_electric'].astype(int)
        
        # Time-based features
        df['is_rush_hour'] = df['hour_of_day'].apply(
            lambda x: 1 if (7 <= x <= 9) or (17 <= x <= 19) else 0
        )
        df['is_weekend'] = df['day_of_week'].apply(lambda x: 1 if x in [1, 7] else 0)
        
        # Feature set for prediction
        feature_columns = [
            'distance', 'vehicle_type_encoded', 'is_electric_encoded',
            'hour_of_day', 'day_of_week', 'is_rush_hour', 'is_weekend', 'speed'
        ]
        
        # Fill missing values
        for col in feature_columns:
            if col not in df.columns:
                df[col] = 0
            df[col] = df[col].fillna(0)
        
        X = df[feature_columns]
        y = df['actual_duration'] if 'actual_duration' in df.columns else None
        
        return X, y, feature_columns
    
    def train_model(self):
        """Train ETA prediction model"""
        print("Training ETA prediction model...")
        df = self.fetch_historical_data()
        
        if df.empty:
            print("No historical data available. Using default model.")
            self.model = RandomForestRegressor(n_estimators=100, random_state=42)
            # Train on dummy data
            X_dummy = np.random.rand(100, 8)
            y_dummy = np.random.rand(100) * 60 + 10
            self.scaler.fit(X_dummy)
            X_scaled = self.scaler.transform(X_dummy)
            self.model.fit(X_scaled, y_dummy)
        else:
            X, y, feature_columns = self.prepare_features(df)
            
            if y is not None and len(y) > 10:
                # Scale features
                X_scaled = self.scaler.fit_transform(X)
                
                # Train model
                self.model = RandomForestRegressor(
                    n_estimators=100,
                    max_depth=10,
                    random_state=42,
                    n_jobs=-1
                )
                self.model.fit(X_scaled, y)
                
                # Save model and scaler
                joblib.dump(self.model, self.model_path)
                joblib.dump(self.scaler, self.scaler_path)
                
                print(f"Model trained successfully with {len(X)} samples")
            else:
                print("Insufficient data for training")
    
    def predict_eta(self, distance, vehicle_type, is_electric, hour_of_day, day_of_week, current_speed=30):
        """Predict ETA for a route"""
        # Prepare features
        vehicle_type_map = {'SEDAN': 1, 'SUV': 2, 'VAN': 3, 'TRUCK': 4, 'BUS': 5, 'BIKE': 0}
        vehicle_type_encoded = vehicle_type_map.get(vehicle_type.upper(), 1)
        is_electric_encoded = 1 if is_electric else 0
        is_rush_hour = 1 if (7 <= hour_of_day <= 9) or (17 <= hour_of_day <= 19) else 0
        is_weekend = 1 if day_of_week in [1, 7] else 0
        
        features = np.array([[
            distance, vehicle_type_encoded, is_electric_encoded,
            hour_of_day, day_of_week, is_rush_hour, is_weekend, current_speed
        ]])
        
        # Scale and predict
        features_scaled = self.scaler.transform(features)
        predicted_duration = self.model.predict(features_scaled)[0]
        
        # Add some randomness for confidence interval
        confidence_interval = predicted_duration * 0.15
        
        return {
            'estimated_duration_minutes': round(predicted_duration, 2),
            'estimated_duration_hours': round(predicted_duration / 60, 2),
            'confidence_interval': round(confidence_interval, 2),
            'min_duration': round(predicted_duration - confidence_interval, 2),
            'max_duration': round(predicted_duration + confidence_interval, 2),
            'current_speed': current_speed,
            'recommended_speed': round(distance / (predicted_duration / 60), 2)
        }
    
    def calculate_best_route(self, start_lat, start_lng, end_lat, end_lng, optimization_type='FASTEST'):
        """Calculate best route based on optimization type"""
        # Calculate distance (simplified Haversine distance)
        lat_diff = abs(end_lat - start_lat)
        lng_diff = abs(end_lng - start_lng)
        distance_km = np.sqrt(lat_diff**2 + lng_diff**2) * 111  # Rough conversion
        
        # Generate route points (simplified)
        num_points = max(int(distance_km / 2), 3)
        lat_points = np.linspace(start_lat, end_lat, num_points)
        lng_points = np.linspace(start_lng, end_lng, num_points)
        
        route_points = [
            {'latitude': float(lat), 'longitude': float(lng)}
            for lat, lng in zip(lat_points, lng_points)
        ]
        
        # Calculate metrics based on optimization type
        if optimization_type == 'FASTEST':
            avg_speed = 50
            fuel_efficiency = 0.8
        elif optimization_type == 'ENERGY_EFFICIENT':
            avg_speed = 40
            fuel_efficiency = 0.95
        elif optimization_type == 'BALANCED':
            avg_speed = 45
            fuel_efficiency = 0.87
        else:
            avg_speed = 45
            fuel_efficiency = 0.85
        
        estimated_time = (distance_km / avg_speed) * 60  # in minutes
        
        return {
            'distance_km': round(distance_km, 2),
            'route_points': route_points,
            'estimated_time_minutes': round(estimated_time, 2),
            'optimization_type': optimization_type,
            'average_speed': avg_speed,
            'fuel_efficiency': fuel_efficiency,
            'route_quality_score': round(np.random.uniform(0.7, 0.95), 2)
        }
    
    def generate_speed_feedback(self, vehicle_id):
        """Generate speed and feedback analysis for a vehicle"""
        conn = self.get_db_connection()
        
        query = """
        SELECT 
            v.id,
            v.vehicle_number,
            v.speed,
            v.type,
            t.distance,
            t.duration,
            t.start_time
        FROM vehicles v
        LEFT JOIN trips t ON t.booking_id IN (
            SELECT id FROM bookings WHERE vehicle_id = v.id
        )
        WHERE v.id = %s
        ORDER BY t.start_time DESC
        LIMIT 50
        """
        
        try:
            cursor = conn.cursor(dictionary=True)
            cursor.execute(query, (vehicle_id,))
            trips = cursor.fetchall()
            cursor.close()
            conn.close()
            
            if not trips:
                return {
                    'vehicle_id': vehicle_id,
                    'avg_speed': 0,
                    'max_speed': 0,
                    'min_speed': 0,
                    'speed_variance': 0,
                    'feedback': 'No trip data available'
                }
            
            speeds = [trip['speed'] for trip in trips if trip['speed']]
            
            if not speeds:
                speeds = [0]
            
            avg_speed = np.mean(speeds)
            max_speed = np.max(speeds)
            min_speed = np.min(speeds)
            speed_variance = np.var(speeds)
            
            # Generate feedback
            if avg_speed > 60:
                feedback = "⚠️ Average speed is high. Consider promoting safer driving."
            elif avg_speed < 20:
                feedback = "🐌 Average speed is low. Check for route efficiency issues."
            else:
                feedback = "✅ Speed is within optimal range."
            
            return {
                'vehicle_id': vehicle_id,
                'avg_speed': round(avg_speed, 2),
                'max_speed': round(max_speed, 2),
                'min_speed': round(min_speed, 2),
                'speed_variance': round(speed_variance, 2),
                'speed_trend': 'stable' if speed_variance < 100 else 'variable',
                'feedback': feedback,
                'total_trips_analyzed': len(speeds)
            }
        
        except Exception as e:
            print(f"Error generating speed feedback: {e}")
            conn.close()
            return {'error': str(e)}
    
    def export_route_data_to_csv(self, filename='route_data.csv'):
        """Export route data to CSV"""
        conn = self.get_db_connection()
        
        query = """
        SELECT 
            r.id,
            r.start_location,
            r.end_location,
            r.distance,
            r.estimated_duration,
            r.actual_duration,
            r.optimization_type,
            r.created_at,
            v.vehicle_number,
            v.type as vehicle_type,
            v.manufacturer,
            v.model
        FROM routes r
        LEFT JOIN vehicles v ON r.vehicle_id = v.id
        ORDER BY r.created_at DESC
        """
        
        try:
            df = pd.read_sql(query, conn)
            conn.close()
            
            # Add calculated fields
            df['duration_accuracy'] = (
                (df['actual_duration'] - df['estimated_duration']) / df['estimated_duration'] * 100
            ).round(2)
            
            df['efficiency_score'] = (
                100 - abs(df['duration_accuracy'])
            ).clip(0, 100).round(2)
            
            # Export to CSV
            output_path = f'exports/{filename}'
            os.makedirs('exports', exist_ok=True)
            df.to_csv(output_path, index=False)
            
            print(f"Exported {len(df)} routes to {output_path}")
            return output_path
        
        except Exception as e:
            print(f"Error exporting data: {e}")
            conn.close()
            return None

if __name__ == '__main__':
    # Example usage
    db_config = {
        'host': 'localhost',
        'user': 'root',
        'password': 'root',
        'database': 'neurofleetx',
        'port': 3306
    }
    
    optimizer = RouteOptimizer(db_config)
    
    # Test ETA prediction
    eta = optimizer.predict_eta(
        distance=25.5,
        vehicle_type='SEDAN',
        is_electric=False,
        hour_of_day=14,
        day_of_week=3,
        current_speed=45
    )
    print("ETA Prediction:", json.dumps(eta, indent=2))
    
    # Test route calculation
    route = optimizer.calculate_best_route(
        start_lat=40.7128,
        start_lng=-74.0060,
        end_lat=40.7580,
        end_lng=-73.9855,
        optimization_type='FASTEST'
    )
    print("\nRoute Calculation:", json.dumps(route, indent=2))
    
    # Export data
    csv_path = optimizer.export_route_data_to_csv()
    print(f"\nData exported to: {csv_path}")
