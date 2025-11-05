# AI Route Optimization Service

## Setup Instructions

### 1. Install Python Dependencies
```bash
pip install -r requirements.txt
```

### 2. Configure Database
Create a `.env` file or update `app.py` with your MySQL credentials:
```env
DB_HOST=localhost
DB_USER=root
DB_PASSWORD=root
DB_NAME=neurofleetx
DB_PORT=3306
```

### 3. Start the Service
```bash
python app.py
```

The service will start on `http://localhost:5000`

## API Endpoints

### Health Check
```bash
GET http://localhost:5000/health
```

### Predict ETA
```bash
POST http://localhost:5000/api/ai/predict-eta
Content-Type: application/json

{
  "distance": 25.5,
  "vehicleType": "SEDAN",
  "isElectric": false,
  "hourOfDay": 14,
  "dayOfWeek": 3,
  "currentSpeed": 45
}
```

### Calculate Best Route
```bash
POST http://localhost:5000/api/ai/calculate-route
Content-Type: application/json

{
  "startLatitude": 40.7128,
  "startLongitude": -74.0060,
  "endLatitude": 40.7580,
  "endLongitude": -73.9855,
  "optimizationType": "FASTEST"
}
```

### Get Speed Feedback
```bash
GET http://localhost:5000/api/ai/speed-feedback/1
```

### Export Route Data
```bash
GET http://localhost:5000/api/ai/export-route-data?filename=routes.csv
```

### Retrain Model
```bash
POST http://localhost:5000/api/ai/retrain-model
```

## Features

- **ETA Prediction**: Machine learning-based travel time estimation
- **Route Optimization**: Multiple strategies (FASTEST, ENERGY_EFFICIENT, BALANCED)
- **Speed Analysis**: Vehicle speed feedback and recommendations
- **CSV Export**: Export route data for analysis
- **Auto-Training**: Model trains on real trip data from MySQL

## File Structure

```
ai-services/
├── app.py                  # Flask API server
├── route_optimizer.py      # ML model and optimization logic
├── requirements.txt        # Python dependencies
├── models/                 # Trained ML models (auto-created)
│   ├── eta_model.pkl
│   └── scaler.pkl
└── exports/               # CSV exports (auto-created)
    └── route_data_*.csv
```

## How It Works

1. **Data Collection**: Fetches historical trip data from MySQL
2. **Feature Engineering**: Extracts relevant features (distance, vehicle type, time, etc.)
3. **Model Training**: Random Forest Regressor learns patterns
4. **Prediction**: Provides ETA with confidence intervals
5. **Optimization**: Calculates best routes based on selected strategy
6. **Export**: Generates CSV reports for analysis

## Requirements

- Python 3.8+
- MySQL 8.0+
- 4GB RAM minimum
- Internet connection (for initial package installation)

## Troubleshooting

### Connection Error
- Check MySQL is running
- Verify database credentials
- Ensure `neurofleetx` database exists

### Import Errors
- Run `pip install -r requirements.txt`
- Use virtual environment: `python -m venv venv && venv\Scripts\activate`

### Port Already in Use
- Change port in `app.py`: `app.run(port=5001)`

## Testing

### Quick Test
```bash
python route_optimizer.py
```

This will:
- Connect to database
- Train/load ML model
- Run example predictions
- Export sample CSV

### Integration Test
```bash
curl http://localhost:5000/health
curl -X POST http://localhost:5000/api/ai/predict-eta -H "Content-Type: application/json" -d '{"distance":20,"vehicleType":"SEDAN","isElectric":false,"hourOfDay":10,"dayOfWeek":2,"currentSpeed":40}'
```

## Performance

- **Prediction Time**: < 100ms
- **Training Time**: 2-5 seconds (depends on data size)
- **Memory Usage**: ~150MB
- **Concurrent Requests**: Supports multiple simultaneous requests

## Future Enhancements

- Real-time traffic data integration
- Weather condition factors
- Driver behavior analysis
- Deep learning models for improved accuracy
- Caching for frequently requested routes
