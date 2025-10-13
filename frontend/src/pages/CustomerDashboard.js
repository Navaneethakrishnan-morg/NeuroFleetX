import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { vehicleService, bookingService } from '../services/api';

const CustomerDashboard = () => {
  const navigate = useNavigate();
  const [vehicles, setVehicles] = useState([]);
  const [bookings, setBookings] = useState([]);
  const [filterType, setFilterType] = useState('ALL');
  const [filterElectric, setFilterElectric] = useState(false);
  const fullName = localStorage.getItem('fullName') || 'Customer';
  const username = localStorage.getItem('username');

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [vehiclesRes, bookingsRes] = await Promise.all([
        vehicleService.getAll(),
        bookingService.getCustomerBookings(username),
      ]);

      setVehicles(vehiclesRes.data);
      setBookings(bookingsRes.data);
    } catch (error) {
      console.error('Error loading data:', error);
    }
  };

  const handleLogout = () => {
    localStorage.clear();
    navigate('/');
  };

  const filteredVehicles = vehicles.filter(v => {
    if (v.status !== 'AVAILABLE') return false;
    if (filterType !== 'ALL' && v.type !== filterType) return false;
    if (filterElectric && !v.isElectric) return false;
    return true;
  });

  return (
    <div className="min-h-screen bg-gray-100">
      <nav className="gradient-bg text-white p-4 shadow-lg">
        <div className="max-w-7xl mx-auto flex justify-between items-center">
          <h1 className="text-2xl font-bold">👤 Customer Portal</h1>
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
        <h2 className="text-3xl font-bold text-gray-800 mb-6">Book Your Vehicle</h2>

        <div className="bg-white rounded-lg shadow-card p-6 mb-8">
          <h3 className="text-xl font-bold text-gray-800 mb-4">Filters</h3>
          <div className="flex flex-wrap gap-4">
            <div>
              <label className="block text-gray-700 text-sm font-bold mb-2">
                Vehicle Type
              </label>
              <select
                className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                value={filterType}
                onChange={(e) => setFilterType(e.target.value)}
              >
                <option value="ALL">All Types</option>
                <option value="SEDAN">Sedan</option>
                <option value="SUV">SUV</option>
                <option value="VAN">Van</option>
                <option value="TRUCK">Truck</option>
              </select>
            </div>

            <div className="flex items-end">
              <label className="flex items-center gap-2 cursor-pointer">
                <input
                  type="checkbox"
                  checked={filterElectric}
                  onChange={(e) => setFilterElectric(e.target.checked)}
                  className="w-4 h-4"
                />
                <span className="text-gray-700 text-sm font-bold">Electric Only</span>
              </label>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-card p-6 mb-8">
          <h3 className="text-xl font-bold text-gray-800 mb-4">Available Vehicles</h3>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {filteredVehicles.map((vehicle, index) => (
              <div key={vehicle.id} className="bg-gray-50 rounded-lg p-4 border border-gray-200 relative">
                {index === 0 && (
                  <div className="absolute top-2 right-2 bg-gradient-to-r from-yellow-400 to-yellow-600 text-white px-3 py-1 rounded-full text-xs font-bold">
                    ✨ Recommended
                  </div>
                )}
                
                <div className="mb-3">
                  <p className="font-bold text-gray-800 text-lg">{vehicle.model}</p>
                  <p className="text-sm text-gray-600">{vehicle.vehicleNumber}</p>
                </div>
                
                <div className="space-y-2 text-sm mb-4">
                  <div className="flex justify-between">
                    <span className="text-gray-600">Type:</span>
                    <span className="font-semibold">{vehicle.type}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-gray-600">Capacity:</span>
                    <span className="font-semibold">{vehicle.capacity} seats</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-gray-600">Fuel Type:</span>
                    <span className="font-semibold">{vehicle.isElectric ? '⚡ Electric' : '⛽ Petrol'}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-gray-600">Price:</span>
                    <span className="font-semibold text-green-600">$50/day</span>
                  </div>
                </div>

                <button className="w-full bg-gradient-to-r from-green-600 to-teal-600 text-white py-2 px-4 rounded-lg font-semibold hover:shadow-lg transition duration-300">
                  Book Now
                </button>
              </div>
            ))}
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-card p-6">
          <h3 className="text-xl font-bold text-gray-800 mb-4">My Bookings</h3>
          <div className="space-y-3">
            {bookings.length > 0 ? (
              bookings.map((booking) => (
                <div key={booking.id} className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                  <div>
                    <p className="font-semibold text-gray-800">Booking #{booking.id}</p>
                    <p className="text-sm text-gray-600">
                      {new Date(booking.startTime).toLocaleDateString()} - {new Date(booking.endTime).toLocaleDateString()}
                    </p>
                  </div>
                  <span className={`status-chip ${booking.status === 'CONFIRMED' ? 'status-available' : booking.status === 'IN_PROGRESS' ? 'status-in-use' : 'bg-gray-100 text-gray-800'}`}>
                    {booking.status}
                  </span>
                </div>
              ))
            ) : (
              <p className="text-gray-600 text-center py-8">No bookings yet. Start by booking a vehicle above!</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default CustomerDashboard;
