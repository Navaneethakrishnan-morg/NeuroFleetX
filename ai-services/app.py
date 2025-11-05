"""
Flask API for AI Route Optimization Service
"""

from flask import Flask, request, jsonify, send_file
from flask_cors import CORS
from route_optimizer import RouteOptimizer
import os
from datetime import datetime

app = Flask(__name__)
CORS(app)

# Database configuration
DB_CONFIG = {
    'host': os.getenv('DB_HOST', 'localhost'),
    'user': os.getenv('DB_USER', 'root'),
    'password': os.getenv('DB_PASSWORD', 'root'),
    'database': os.getenv('DB_NAME', 'neurofleetx'),
    'port': int(os.getenv('DB_PORT', 3306))
}

# Initialize optimizer
optimizer = RouteOptimizer(DB_CONFIG)

@app.route('/health', methods=['GET'])
def health_check():
    """Health check endpoint"""
    return jsonify({
        'status': 'healthy',
        'service': 'AI Route Optimizer',
        'timestamp': datetime.now().isoformat()
    })

@app.route('/api/ai/predict-eta', methods=['POST'])
def predict_eta():
    """Predict ETA for a route"""
    try:
        data = request.json
        
        # Extract parameters
        distance = float(data.get('distance', 10))
        vehicle_type = data.get('vehicleType', 'SEDAN')
        is_electric = data.get('isElectric', False)
        hour_of_day = int(data.get('hourOfDay', datetime.now().hour))
        day_of_week = int(data.get('dayOfWeek', datetime.now().weekday() + 1))
        current_speed = float(data.get('currentSpeed', 30))
        
        # Predict ETA
        result = optimizer.predict_eta(
            distance=distance,
            vehicle_type=vehicle_type,
            is_electric=is_electric,
            hour_of_day=hour_of_day,
            day_of_week=day_of_week,
            current_speed=current_speed
        )
        
        return jsonify({
            'success': True,
            'data': result
        })
    
    except Exception as e:
        return jsonify({
            'success': False,
            'error': str(e)
        }), 400

@app.route('/api/ai/calculate-route', methods=['POST'])
def calculate_route():
    """Calculate best route"""
    try:
        data = request.json
        
        start_lat = float(data.get('startLatitude', 40.7128))
        start_lng = float(data.get('startLongitude', -74.0060))
        end_lat = float(data.get('endLatitude', 40.7580))
        end_lng = float(data.get('endLongitude', -73.9855))
        optimization_type = data.get('optimizationType', 'FASTEST')
        
        result = optimizer.calculate_best_route(
            start_lat=start_lat,
            start_lng=start_lng,
            end_lat=end_lat,
            end_lng=end_lng,
            optimization_type=optimization_type
        )
        
        return jsonify({
            'success': True,
            'data': result
        })
    
    except Exception as e:
        return jsonify({
            'success': False,
            'error': str(e)
        }), 400

@app.route('/api/ai/speed-feedback/<int:vehicle_id>', methods=['GET'])
def speed_feedback(vehicle_id):
    """Get speed feedback for a vehicle"""
    try:
        result = optimizer.generate_speed_feedback(vehicle_id)
        
        return jsonify({
            'success': True,
            'data': result
        })
    
    except Exception as e:
        return jsonify({
            'success': False,
            'error': str(e)
        }), 400

@app.route('/api/ai/export-route-data', methods=['GET'])
def export_route_data():
    """Export route data to CSV"""
    try:
        filename = request.args.get('filename', f'route_data_{datetime.now().strftime("%Y%m%d_%H%M%S")}.csv')
        
        csv_path = optimizer.export_route_data_to_csv(filename)
        
        if csv_path and os.path.exists(csv_path):
            return send_file(
                csv_path,
                mimetype='text/csv',
                as_attachment=True,
                download_name=filename
            )
        else:
            return jsonify({
                'success': False,
                'error': 'Failed to generate CSV file'
            }), 500
    
    except Exception as e:
        return jsonify({
            'success': False,
            'error': str(e)
        }), 400

@app.route('/api/ai/retrain-model', methods=['POST'])
def retrain_model():
    """Retrain the ETA prediction model"""
    try:
        optimizer.train_model()
        
        return jsonify({
            'success': True,
            'message': 'Model retrained successfully'
        })
    
    except Exception as e:
        return jsonify({
            'success': False,
            'error': str(e)
        }), 400

@app.route('/api/ai/batch-optimize', methods=['POST'])
def batch_optimize():
    """Batch optimize multiple routes"""
    try:
        data = request.json
        routes = data.get('routes', [])
        
        results = []
        for route in routes:
            eta = optimizer.predict_eta(
                distance=route.get('distance', 10),
                vehicle_type=route.get('vehicleType', 'SEDAN'),
                is_electric=route.get('isElectric', False),
                hour_of_day=route.get('hourOfDay', datetime.now().hour),
                day_of_week=route.get('dayOfWeek', datetime.now().weekday() + 1),
                current_speed=route.get('currentSpeed', 30)
            )
            
            route_calc = optimizer.calculate_best_route(
                start_lat=route.get('startLatitude', 40.7128),
                start_lng=route.get('startLongitude', -74.0060),
                end_lat=route.get('endLatitude', 40.7580),
                end_lng=route.get('endLongitude', -73.9855),
                optimization_type=route.get('optimizationType', 'FASTEST')
            )
            
            results.append({
                'route_id': route.get('id'),
                'eta': eta,
                'route': route_calc
            })
        
        return jsonify({
            'success': True,
            'data': results
        })
    
    except Exception as e:
        return jsonify({
            'success': False,
            'error': str(e)
        }), 400

if __name__ == '__main__':
    print("🚀 Starting AI Route Optimization Service...")
    print(f"📊 Database: {DB_CONFIG['database']} at {DB_CONFIG['host']}:{DB_CONFIG['port']}")
    print("🌐 API running on http://localhost:5000")
    app.run(host='0.0.0.0', port=5000, debug=True)
