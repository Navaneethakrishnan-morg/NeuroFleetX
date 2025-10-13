import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { vehicleService, bookingService, maintenanceService } from '../services/api';
import Logo from '../components/Logo';
import { 
  VehicleIcon, 
  RouteIcon, 
  RevenueIcon, 
  MaintenanceIcon, 
  BookingIcon, 
  LogoutIcon,
  AlertIcon,
  TrendingUpIcon,
  ChartIcon
} from '../components/Icons';

const AdminDashboard = () => {
  const navigate = useNavigate();
  const [vehicles, setVehicles] = useState([]);
  const [bookings, setBookings] = useState([]);
  const [maintenance, setMaintenance] = useState([]);
  const [stats, setStats] = useState({
    totalFleet: 0,
    activeTrips: 0,
    revenue: 0,
    maintenanceDue: 0,
  });

  const fullName = localStorage.getItem('fullName') || 'Admin';

  useEffect(() => {
    loadDashboardData();
  }, []);

  const loadDashboardData = async () => {
    try {
      const [vehiclesRes, bookingsRes, maintenanceRes] = await Promise.all([
        vehicleService.getAll(),
        bookingService.getAll(),
        maintenanceService.getAll(),
      ]);

      setVehicles(vehiclesRes.data);
      setBookings(bookingsRes.data);
      setMaintenance(maintenanceRes.data);

      setStats({
        totalFleet: vehiclesRes.data.length,
        activeTrips: bookingsRes.data.filter(b => b.status === 'IN_PROGRESS').length,
        revenue: bookingsRes.data.reduce((sum, b) => sum + (b.totalPrice || 0), 0),
        maintenanceDue: maintenanceRes.data.filter(m => m.status === 'PENDING').length,
      });
    } catch (error) {
      console.error('Backend not connected, using demo data:', error);
      
      const mockVehicles = [
        { id: 1, vehicleNumber: 'NF-001', model: 'Tesla Model S', status: 'AVAILABLE' },
        { id: 2, vehicleNumber: 'NF-002', model: 'Toyota Camry', status: 'IN_USE' },
        { id: 3, vehicleNumber: 'NF-003', model: 'Ford Explorer', status: 'AVAILABLE' },
        { id: 4, vehicleNumber: 'NF-004', model: 'Honda Accord', status: 'MAINTENANCE' },
        { id: 5, vehicleNumber: 'NF-005', model: 'Nissan Leaf', status: 'AVAILABLE' },
      ];
      
      const mockBookings = [
        { id: 1, status: 'CONFIRMED', totalPrice: 150, createdAt: new Date().toISOString() },
        { id: 2, status: 'IN_PROGRESS', totalPrice: 200, createdAt: new Date().toISOString() },
        { id: 3, status: 'PENDING', totalPrice: 100, createdAt: new Date().toISOString() },
        { id: 4, status: 'COMPLETED', totalPrice: 250, createdAt: new Date().toISOString() },
      ];
      
      const mockMaintenance = [
        { id: 1, vehicle: { vehicleNumber: 'NF-004' }, issueType: 'Engine Check', priority: 'HIGH', isPredictive: true },
        { id: 2, vehicle: { vehicleNumber: 'NF-002' }, issueType: 'Brake System', priority: 'CRITICAL', isPredictive: true },
        { id: 3, vehicle: { vehicleNumber: 'NF-005' }, issueType: 'Battery Health', priority: 'MEDIUM', isPredictive: false },
      ];
      
      setVehicles(mockVehicles);
      setBookings(mockBookings);
      setMaintenance(mockMaintenance);
      
      setStats({
        totalFleet: mockVehicles.length,
        activeTrips: mockBookings.filter(b => b.status === 'IN_PROGRESS').length,
        revenue: mockBookings.reduce((sum, b) => sum + (b.totalPrice || 0), 0),
        maintenanceDue: mockMaintenance.length,
      });
    }
  };

  const handleLogout = () => {
    localStorage.clear();
    navigate('/');
  };

  const getStatusStyle = (status) => {
    const statusMap = {
      'AVAILABLE': 'status-available',
      'IN_USE': 'status-in-use',
      'MAINTENANCE': 'status-maintenance',
      'CONFIRMED': 'status-available',
      'IN_PROGRESS': 'status-in-use',
      'PENDING': 'status-maintenance',
      'CRITICAL': 'status-critical',
      'HIGH': 'status-critical',
      'MEDIUM': 'status-maintenance',
    };
    return statusMap[status] || 'status-maintenance';
  };

  const kpiCards = [
    { 
      title: 'Total Fleet', 
      value: stats.totalFleet, 
      icon: VehicleIcon, 
      gradient: 'from-accent-cyan to-accent-blue',
      bgGlow: 'bg-accent-cyan/20',
    },
    { 
      title: 'Active Trips', 
      value: stats.activeTrips, 
      icon: RouteIcon, 
      gradient: 'from-accent-green to-accent-cyan',
      bgGlow: 'bg-accent-green/20',
    },
    { 
      title: 'Total Revenue', 
      value: `$${stats.revenue.toFixed(2)}`, 
      icon: RevenueIcon, 
      gradient: 'from-accent-purple to-accent-pink',
      bgGlow: 'bg-accent-purple/20',
    },
    { 
      title: 'Maintenance Due', 
      value: stats.maintenanceDue, 
      icon: AlertIcon, 
      gradient: 'from-accent-pink to-accent-purple',
      bgGlow: 'bg-accent-pink/20',
    },
  ];

  return (
    <div className="min-h-screen bg-gradient-dark relative overflow-hidden">
      <div className="absolute inset-0 bg-gradient-mesh opacity-20"></div>
      
      <nav className="relative bg-dark-800/40 backdrop-blur-glass border-b border-white/10 shadow-glass">
        <div className="max-w-7xl mx-auto px-6 py-4">
          <div className="flex justify-between items-center">
            <div className="flex items-center gap-4">
              <Logo size="sm" animate={false} showText={true} />
              <div className="h-8 w-px bg-white/20"></div>
              <div>
                <h1 className="text-lg font-bold text-white flex items-center gap-2">
                  Admin Portal
                </h1>
                <p className="text-xs text-white/50">Full System Control</p>
              </div>
            </div>
            <div className="flex items-center gap-4">
              <div className="text-right">
                <p className="text-sm text-white/70">Welcome back,</p>
                <p className="text-sm font-semibold text-white">{fullName}</p>
              </div>
              <button
                onClick={handleLogout}
                className="btn-secondary flex items-center gap-2"
              >
                <LogoutIcon size="sm" />
                <span>Logout</span>
              </button>
            </div>
          </div>
        </div>
      </nav>

      <div className="relative max-w-7xl mx-auto p-6">
        <div className="mb-8 animate-fade-in-down">
          <h2 className="text-3xl font-bold text-white mb-2 flex items-center gap-3">
            <ChartIcon size="lg" className="text-accent-cyan" />
            Dashboard Overview
          </h2>
          <p className="text-white/50">Monitor and manage your entire fleet operations</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          {kpiCards.map((card, index) => {
            const IconComponent = card.icon;
            return (
              <div
                key={index}
                className="kpi-card text-white group hover:scale-105 transition-transform duration-300 animate-fade-in-up"
                style={{ animationDelay: `${index * 0.1}s` }}
              >
                <div className={`absolute inset-0 ${card.bgGlow} opacity-0 group-hover:opacity-100 transition-opacity duration-300 rounded-2xl`}></div>
                <div className="relative z-10">
                  <div className="flex items-start justify-between mb-4">
                    <div className="flex-1">
                      <p className="text-white/60 text-sm font-semibold mb-2">{card.title}</p>
                      <p className={`text-4xl font-bold bg-gradient-to-r ${card.gradient} bg-clip-text text-transparent`}>
                        {card.value}
                      </p>
                    </div>
                    <div className={`p-3 rounded-xl bg-gradient-to-br ${card.gradient} shadow-lg group-hover:scale-110 transition-transform duration-300`}>
                      <IconComponent size="md" className="text-white" />
                    </div>
                  </div>
                  <div className="flex items-center gap-2 text-accent-green text-sm">
                    <TrendingUpIcon size="sm" />
                    <span>+5.2% from last month</span>
                  </div>
                </div>
              </div>
            );
          })}
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
          <div className="glass-card p-6 animate-fade-in-up" style={{ animationDelay: '0.5s' }}>
            <div className="flex items-center gap-3 mb-6">
              <VehicleIcon size="md" className="text-accent-cyan" />
              <h3 className="text-xl font-bold text-white">Fleet Status</h3>
            </div>
            <div className="space-y-3">
              {vehicles.slice(0, 5).map((vehicle, index) => (
                <div 
                  key={vehicle.id} 
                  className="flex items-center justify-between p-4 bg-dark-700/40 rounded-xl border border-white/5 hover:border-white/10 hover:bg-dark-700/60 transition-all duration-300"
                  style={{ animationDelay: `${(index + 5) * 0.1}s` }}
                >
                  <div>
                    <p className="font-semibold text-white">{vehicle.vehicleNumber}</p>
                    <p className="text-sm text-white/50">{vehicle.model}</p>
                  </div>
                  <span className={`status-badge ${getStatusStyle(vehicle.status)}`}>
                    <span className="w-2 h-2 rounded-full bg-current animate-pulse"></span>
                    {vehicle.status}
                  </span>
                </div>
              ))}
            </div>
          </div>

          <div className="glass-card p-6 animate-fade-in-up" style={{ animationDelay: '0.6s' }}>
            <div className="flex items-center gap-3 mb-6">
              <BookingIcon size="md" className="text-accent-purple" />
              <h3 className="text-xl font-bold text-white">Recent Bookings</h3>
            </div>
            <div className="space-y-3">
              {bookings.slice(0, 5).map((booking, index) => (
                <div 
                  key={booking.id} 
                  className="flex items-center justify-between p-4 bg-dark-700/40 rounded-xl border border-white/5 hover:border-white/10 hover:bg-dark-700/60 transition-all duration-300"
                >
                  <div>
                    <p className="font-semibold text-white">Booking #{booking.id}</p>
                    <p className="text-sm text-white/50">{new Date(booking.createdAt).toLocaleDateString()}</p>
                  </div>
                  <span className={`status-badge ${getStatusStyle(booking.status)}`}>
                    <span className="w-2 h-2 rounded-full bg-current animate-pulse"></span>
                    {booking.status}
                  </span>
                </div>
              ))}
            </div>
          </div>
        </div>

        <div className="glass-card p-6 animate-fade-in-up" style={{ animationDelay: '0.7s' }}>
          <div className="flex items-center gap-3 mb-6">
            <AlertIcon size="md" className="text-accent-pink" />
            <h3 className="text-xl font-bold text-white">Predictive Maintenance Alerts</h3>
            <span className="ml-auto px-3 py-1 bg-accent-pink/20 text-accent-pink text-xs font-bold rounded-full animate-pulse">
              AI-Powered
            </span>
          </div>
          <div className="space-y-3">
            {maintenance.filter(m => m.isPredictive).slice(0, 5).map((item, index) => (
              <div 
                key={item.id} 
                className="flex items-center justify-between p-4 bg-accent-pink/5 border border-accent-pink/20 rounded-xl hover:border-accent-pink/40 hover:bg-accent-pink/10 transition-all duration-300"
              >
                <div className="flex items-center gap-4">
                  <div className="w-10 h-10 rounded-lg bg-accent-pink/20 flex items-center justify-center">
                    <MaintenanceIcon size="sm" className="text-accent-pink" />
                  </div>
                  <div>
                    <p className="font-semibold text-white">Vehicle #{item.vehicle?.vehicleNumber || 'N/A'}</p>
                    <p className="text-sm text-white/50">{item.issueType}</p>
                  </div>
                </div>
                <span className={`status-badge ${getStatusStyle(item.priority)}`}>
                  <AlertIcon size="sm" />
                  {item.priority}
                </span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
