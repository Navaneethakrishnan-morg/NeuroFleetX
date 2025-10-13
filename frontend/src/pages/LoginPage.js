import React, { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { authService } from '../services/api';
import Logo from '../components/Logo';
import { DashboardIcon, VehicleIcon, RouteIcon, UserIcon } from '../components/Icons';

const LoginPageNew = () => {
  const navigate = useNavigate();
  const { role } = useParams();
  const [formData, setFormData] = useState({ username: '', password: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const roleConfig = {
    admin: { 
      title: 'Admin Portal', 
      icon: DashboardIcon, 
      gradient: 'from-accent-purple via-accent-pink to-accent-purple',
      glow: 'shadow-neon-purple' 
    },
    manager: { 
      title: 'Manager Portal', 
      icon: VehicleIcon, 
      gradient: 'from-accent-cyan via-accent-blue to-accent-cyan',
      glow: 'shadow-neon-cyan' 
    },
    driver: { 
      title: 'Driver Portal', 
      icon: RouteIcon, 
      gradient: 'from-accent-blue via-accent-purple to-accent-blue',
      glow: 'shadow-neon-blue' 
    },
    customer: { 
      title: 'Customer Portal', 
      icon: UserIcon, 
      gradient: 'from-accent-green via-accent-cyan to-accent-green',
      glow: 'shadow-neon-cyan' 
    },
  };

  const config = roleConfig[role] || roleConfig.customer;
  const IconComponent = config.icon;

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    
    try {
      const response = await authService.login(formData.username, formData.password);
      const { token, role: userRole, username, fullName } = response.data;
      
      localStorage.setItem('token', token);
      localStorage.setItem('role', userRole);
      localStorage.setItem('username', username);
      localStorage.setItem('fullName', fullName);

      navigate(`/${userRole.toLowerCase()}/dashboard`);
    } catch (err) {
      setError('Invalid username or password. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 relative overflow-hidden">

      <div className="relative min-h-screen flex items-center justify-center p-6">
        <div className="max-w-md w-full animate-fade-in-up">
          <div className="glass-card p-8">
            <div className="flex justify-center mb-8">
              <Logo size="md" animate={false} showText={true} />
            </div>

            <div className="text-center mb-8">
              <div className="w-20 h-20 mx-auto mb-4 rounded-2xl bg-gray-200 border-2 border-gray-300 flex items-center justify-center">
                <IconComponent size="lg" className="text-black" />
              </div>
              <h2 className="text-2xl font-bold mb-2 text-black">{config.title}</h2>
              <p className="text-gray-600 text-sm">Sign in to your account</p>
            </div>

            {error && (
              <div className="bg-red-50 border border-red-300 text-red-700 px-4 py-3 rounded-xl mb-6 animate-fade-in">
                {error}
              </div>
            )}

            <form onSubmit={handleSubmit} className="space-y-6">
              <div>
                <label className="block text-sm font-semibold mb-2 text-gray-700">
                  Username
                </label>
                <input
                  type="text"
                  className="input-field"
                  placeholder="Enter your username"
                  value={formData.username}
                  onChange={(e) => setFormData({ ...formData, username: e.target.value })}
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-semibold mb-2 text-gray-700">
                  Password
                </label>
                <input
                  type="password"
                  className="input-field"
                  placeholder="Enter your password"
                  value={formData.password}
                  onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                  required
                />
              </div>

              <button
                type="submit"
                disabled={loading}
                className="w-full py-4 bg-black text-white rounded-xl font-semibold text-lg transition-all duration-300 hover:bg-gray-800 hover:scale-105 active:scale-95 disabled:opacity-50 disabled:cursor-not-allowed disabled:transform-none"
              >
                {loading ? 'Signing in...' : 'Sign In'}
              </button>
            </form>

            <div className="mt-8 space-y-4">
              <div className="divider"></div>
              <div className="flex items-center justify-between text-sm">
                <button
                  onClick={() => navigate('/portals')}
                  className="text-gray-600 hover:text-black transition-colors duration-300"
                >
                  ← Back to Portals
                </button>
                <button
                  onClick={() => navigate('/signup')}
                  className="text-black hover:text-gray-700 font-semibold transition-colors duration-300"
                >
                  Sign up
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPageNew;
