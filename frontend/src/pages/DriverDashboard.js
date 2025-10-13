import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { vehicleService } from '../services/api';

const DriverDashboard = () => {
  const navigate = useNavigate();
  const [vehicles, setVehicles] = useState([]);
  const fullName = localStorage.getItem('fullName') || 'Driver';

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const response = await vehicleService.getAll();
      setVehicles(response.data);
    } catch (error) {
      console.error('Error loading data:', error);
    }
  };

  const handleLogout = () => {
    localStorage.clear();
    navigate('/');
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <nav className="gradient-bg text-white p-4 shadow-lg">
        <div className="max-w-7xl mx-auto flex justify-between items-center">
          <h1 className="text-2xl font-bold">🚗 Driver Portal</h1>
          <div className="flex items-center gap-4">
            <span className="text-sm">Welcome, {fullName}</span>
            <button
              onClick={handleLogout}
              className="bg-white text-blue-600 px-4 py-2 rounded-lg font-semibold hover:bg-gray-100 transition duration-300"
            >
              Logout
            </button>
          </div>
        </div>
      </nav>

      <div className="max-w-7xl mx-auto p-6">
        <h2 className="text-3xl font-bold text-gray-800 mb-6">My Trips & Routes</h2>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div className="bg-white rounded-lg shadow-card p-6 card-hover">
            <p className="text-gray-600 text-sm font-semibold">Today's Trips</p>
            <p className="text-3xl font-bold text-blue-600">5</p>
          </div>
          <div className="bg-white rounded-lg shadow-card p-6 card-hover">
            <p className="text-gray-600 text-sm font-semibold">Distance Covered</p>
            <p className="text-3xl font-bold text-green-600">124 km</p>
          </div>
          <div className="bg-white rounded-lg shadow-card p-6 card-hover">
            <p className="text-gray-600 text-sm font-semibold">Current Trip</p>
            <p className="text-3xl font-bold text-purple-600">In Progress</p>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-card p-6 mb-8">
          <h3 className="text-xl font-bold text-gray-800 mb-4">Current Route</h3>
          <div className="bg-gray-100 rounded-lg p-6 text-center">
            <p className="text-gray-600 mb-4">🗺️ Map view would be displayed here</p>
            <div className="space-y-3">
              <div className="flex items-center justify-between bg-white p-4 rounded-lg">
                <div className="flex items-center gap-3">
                  <span className="text-2xl">📍</span>
                  <div>
                    <p className="font-semibold text-gray-800">Start Location</p>
                    <p className="text-sm text-gray-600">123 Main Street</p>
                  </div>
                </div>
              </div>
              <div className="flex items-center justify-center">
                <span className="text-2xl">⬇️</span>
              </div>
              <div className="flex items-center justify-between bg-white p-4 rounded-lg">
                <div className="flex items-center gap-3">
                  <span className="text-2xl">🏁</span>
                  <div>
                    <p className="font-semibold text-gray-800">Destination</p>
                    <p className="text-sm text-gray-600">456 Oak Avenue</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-card p-6">
          <h3 className="text-xl font-bold text-gray-800 mb-4">Available Vehicles</h3>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {vehicles.filter(v => v.status === 'AVAILABLE').slice(0, 6).map((vehicle) => (
              <div key={vehicle.id} className="bg-gray-50 rounded-lg p-4 border border-gray-200">
                <div className="flex justify-between items-start mb-3">
                  <div>
                    <p className="font-bold text-gray-800">{vehicle.vehicleNumber}</p>
                    <p className="text-sm text-gray-600">{vehicle.model}</p>
                  </div>
                  <span className="status-chip status-available">AVAILABLE</span>
                </div>
                
                <div className="space-y-2 text-sm">
                  <div className="flex justify-between">
                    <span className="text-gray-600">Type:</span>
                    <span className="font-semibold">{vehicle.type}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-gray-600">Capacity:</span>
                    <span className="font-semibold">{vehicle.capacity} seats</span>
                  </div>
                </div>

                <button className="w-full mt-3 bg-teal-600 text-white py-2 px-4 rounded-lg text-sm font-semibold hover:bg-teal-700 transition duration-300">
                  Start Trip
                </button>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default DriverDashboard;
