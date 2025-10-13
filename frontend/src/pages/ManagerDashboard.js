import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { vehicleService, maintenanceService } from '../services/api';

const ManagerDashboard = () => {
  const navigate = useNavigate();
  const [vehicles, setVehicles] = useState([]);
  const [maintenance, setMaintenance] = useState([]);
  const fullName = localStorage.getItem('fullName') || 'Manager';

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [vehiclesRes, maintenanceRes] = await Promise.all([
        vehicleService.getAll(),
        maintenanceService.getAll(),
      ]);

      setVehicles(vehiclesRes.data);
      setMaintenance(maintenanceRes.data);
    } catch (error) {
      console.error('Error loading data:', error);
    }
  };

  const handleLogout = () => {
    localStorage.clear();
    navigate('/');
  };

  const updateTelemetry = async (vehicleId) => {
    try {
      await vehicleService.updateTelemetry(vehicleId);
      loadData();
    } catch (error) {
      console.error('Error updating telemetry:', error);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <nav className="gradient-bg text-white p-4 shadow-lg">
        <div className="max-w-7xl mx-auto flex justify-between items-center">
          <h1 className="text-2xl font-bold">🧩 Manager Portal</h1>
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
        <h2 className="text-3xl font-bold text-gray-800 mb-6">Fleet Operations</h2>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          <div className="bg-white rounded-lg shadow-card p-6 card-hover">
            <p className="text-gray-600 text-sm font-semibold">Total Vehicles</p>
            <p className="text-3xl font-bold text-blue-600">{vehicles.length}</p>
          </div>
          <div className="bg-white rounded-lg shadow-card p-6 card-hover">
            <p className="text-gray-600 text-sm font-semibold">Available</p>
            <p className="text-3xl font-bold text-green-600">
              {vehicles.filter(v => v.status === 'AVAILABLE').length}
            </p>
          </div>
          <div className="bg-white rounded-lg shadow-card p-6 card-hover">
            <p className="text-gray-600 text-sm font-semibold">In Use</p>
            <p className="text-3xl font-bold text-blue-600">
              {vehicles.filter(v => v.status === 'IN_USE').length}
            </p>
          </div>
          <div className="bg-white rounded-lg shadow-card p-6 card-hover">
            <p className="text-gray-600 text-sm font-semibold">Maintenance</p>
            <p className="text-3xl font-bold text-yellow-600">
              {vehicles.filter(v => v.status === 'MAINTENANCE').length}
            </p>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-card p-6 mb-8">
          <h3 className="text-xl font-bold text-gray-800 mb-4">Fleet Inventory</h3>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            {vehicles.map((vehicle) => (
              <div key={vehicle.id} className="bg-gray-50 rounded-lg p-4 border border-gray-200">
                <div className="flex justify-between items-start mb-3">
                  <div>
                    <p className="font-bold text-gray-800">{vehicle.vehicleNumber}</p>
                    <p className="text-sm text-gray-600">{vehicle.model}</p>
                  </div>
                  <span className={`status-chip ${vehicle.status === 'AVAILABLE' ? 'status-available' : vehicle.status === 'IN_USE' ? 'status-in-use' : 'status-maintenance'}`}>
                    {vehicle.status}
                  </span>
                </div>
                
                <div className="space-y-2 text-sm">
                  <div className="flex justify-between">
                    <span className="text-gray-600">📍 Location:</span>
                    <span className="font-semibold">
                      {vehicle.latitude?.toFixed(2)}, {vehicle.longitude?.toFixed(2)}
                    </span>
                  </div>
                  
                  {vehicle.isElectric ? (
                    <div className="flex justify-between">
                      <span className="text-gray-600">🔋 Battery:</span>
                      <span className="font-semibold">{vehicle.batteryLevel}%</span>
                    </div>
                  ) : (
                    <div className="flex justify-between">
                      <span className="text-gray-600">⛽ Fuel:</span>
                      <span className="font-semibold">{vehicle.fuelLevel}%</span>
                    </div>
                  )}
                  
                  <div className="flex justify-between">
                    <span className="text-gray-600">💪 Health:</span>
                    <span className="font-semibold">{vehicle.healthScore}%</span>
                  </div>
                </div>

                <button
                  onClick={() => updateTelemetry(vehicle.id)}
                  className="w-full mt-3 bg-blue-600 text-white py-2 px-4 rounded-lg text-sm font-semibold hover:bg-blue-700 transition duration-300"
                >
                  Update Telemetry
                </button>
              </div>
            ))}
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-card p-6">
          <h3 className="text-xl font-bold text-gray-800 mb-4">Maintenance Schedule</h3>
          <div className="space-y-3">
            {maintenance.slice(0, 10).map((item) => (
              <div key={item.id} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                <div>
                  <p className="font-semibold text-gray-800">
                    Vehicle #{item.vehicle?.vehicleNumber || 'N/A'}
                  </p>
                  <p className="text-sm text-gray-600">{item.issueType}</p>
                </div>
                <span className={`status-chip ${item.status === 'PENDING' ? 'status-maintenance' : item.status === 'IN_PROGRESS' ? 'status-in-use' : 'status-available'}`}>
                  {item.status}
                </span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ManagerDashboard;
