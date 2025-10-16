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
    <div className="min-h-screen bg-gradient-to-b from-emerald-950 via-green-950 to-emerald-900">
      <nav className="bg-emerald-800/50 backdrop-blur-sm border-b border-emerald-600/30 text-white p-4 shadow-lg">
        <div className="max-w-7xl mx-auto flex justify-between items-center">
          <h1 className="text-2xl font-bold text-emerald-100">👤 Customer Portal</h1>
          <div className="flex items-center gap-4">
            <span className="text-sm text-emerald-200">Welcome, {fullName}</span>
            <button
              onClick={handleLogout}
              className="bg-emerald-600 hover:bg-emerald-500 text-white px-4 py-2 rounded-lg font-semibold transition duration-300"
            >
              Logout
            </button>
          </div>
        </div>
      </nav>

      <div className="max-w-7xl mx-auto p-6">
        <h2 className="text-3xl font-bold text-emerald-100 mb-6">Book Your Vehicle</h2>

        <div className="bg-emerald-900/40 backdrop-blur-sm border border-emerald-500/30 rounded-lg shadow-lg p-6 mb-8">
          <h3 className="text-xl font-bold text-emerald-100 mb-4">Filters</h3>
          <div className="flex flex-wrap gap-4">
            <div>
              <label className="block text-emerald-200 text-sm font-bold mb-2">
                Vehicle Type
              </label>
              <select
                className="px-4 py-2 bg-emerald-800/50 border border-emerald-600/50 text-emerald-100 rounded-lg focus:outline-none focus:ring-2 focus:ring-emerald-500"
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
                  className="w-4 h-4 accent-emerald-500"
                />
                <span className="text-emerald-200 text-sm font-bold">Electric Only</span>
              </label>
            </div>
          </div>
        </div>

        <div className="bg-emerald-900/40 backdrop-blur-sm border border-emerald-500/30 rounded-lg shadow-lg p-6 mb-8">
          <h3 className="text-xl font-bold text-emerald-100 mb-4">Available Vehicles</h3>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {filteredVehicles.map((vehicle, index) => (
              <div key={vehicle.id} className="bg-emerald-800/30 backdrop-blur-sm rounded-lg p-4 border border-emerald-500/40 relative hover:border-emerald-400/60 transition-all duration-300">
                {index === 0 && (
                  <div className="absolute top-2 right-2 bg-gradient-to-r from-emerald-500 to-green-500 text-white px-3 py-1 rounded-full text-xs font-bold">
                    ✨ Recommended
                  </div>
                )}
                
                <div className="mb-3">
                  <p className="font-bold text-emerald-100 text-lg">{vehicle.model}</p>
                  <p className="text-sm text-emerald-300">{vehicle.vehicleNumber}</p>
                </div>
                
                <div className="space-y-2 text-sm mb-4">
                  <div className="flex justify-between">
                    <span className="text-emerald-300">Type:</span>
                    <span className="font-semibold text-emerald-100">{vehicle.type}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-emerald-300">Capacity:</span>
                    <span className="font-semibold text-emerald-100">{vehicle.capacity} seats</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-emerald-300">Fuel Type:</span>
                    <span className="font-semibold text-emerald-100">{vehicle.isElectric ? '⚡ Electric' : '⛽ Petrol'}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-emerald-300">Price:</span>
                    <span className="font-semibold text-emerald-400">$50/day</span>
                  </div>
                </div>

                <button className="w-full bg-emerald-600 hover:bg-emerald-500 text-white py-2 px-4 rounded-lg font-semibold hover:shadow-lg transition duration-300">
                  Book Now
                </button>
              </div>
            ))}
          </div>
        </div>

        <div className="bg-emerald-900/40 backdrop-blur-sm border border-emerald-500/30 rounded-lg shadow-lg p-6">
          <h3 className="text-xl font-bold text-emerald-100 mb-4">My Bookings</h3>
          <div className="space-y-3">
            {bookings.length > 0 ? (
              bookings.map((booking) => (
                <div key={booking.id} className="flex items-center justify-between p-4 bg-emerald-800/30 backdrop-blur-sm border border-emerald-500/40 rounded-lg">
                  <div>
                    <p className="font-semibold text-emerald-100">Booking #{booking.id}</p>
                    <p className="text-sm text-emerald-300">
                      {new Date(booking.startTime).toLocaleDateString()} - {new Date(booking.endTime).toLocaleDateString()}
                    </p>
                  </div>
                  <span className={`px-3 py-1 rounded-full text-sm font-semibold ${booking.status === 'CONFIRMED' ? 'bg-emerald-600 text-white' : booking.status === 'IN_PROGRESS' ? 'bg-yellow-600 text-white' : 'bg-emerald-800/50 text-emerald-200'}`}>
                    {booking.status}
                  </span>
                </div>
              ))
            ) : (
              <p className="text-emerald-200 text-center py-8">No bookings yet. Start by booking a vehicle above!</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default CustomerDashboard;
