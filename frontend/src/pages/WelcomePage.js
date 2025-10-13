import React from 'react';
import { useNavigate } from 'react-router-dom';
import Logo from '../components/Logo';
import { DashboardIcon, VehicleIcon, RouteIcon, UserIcon } from '../components/Icons';

const WelcomePage = () => {
  const navigate = useNavigate();

  const roles = [
    {
      title: 'Admin Portal',
      icon: DashboardIcon,
      description: 'Full system access and control',
      gradient: 'from-accent-purple via-accent-pink to-accent-purple',
      glow: 'shadow-neon-purple',
      role: 'admin',
    },
    {
      title: 'Manager Portal',
      icon: VehicleIcon,
      description: 'Fleet operations management',
      gradient: 'from-accent-cyan via-accent-blue to-accent-cyan',
      glow: 'shadow-neon-cyan',
      role: 'manager',
    },
    {
      title: 'Driver Portal',
      icon: RouteIcon,
      description: 'Trips and route updates',
      gradient: 'from-accent-blue via-accent-purple to-accent-blue',
      glow: 'shadow-neon-blue',
      role: 'driver',
    },
    {
      title: 'Customer Portal',
      icon: UserIcon,
      description: 'Bookings and preferences',
      gradient: 'from-accent-green via-accent-cyan to-accent-green',
      glow: 'shadow-neon-cyan',
      role: 'customer',
    },
  ];

  return (
    <div className="min-h-screen bg-gradient-dark relative overflow-hidden">
      <div className="absolute inset-0 bg-gradient-mesh opacity-30"></div>
      
      <div className="absolute inset-0">
        <div className="absolute top-1/4 left-1/4 w-96 h-96 bg-accent-cyan/10 rounded-full blur-3xl animate-float"></div>
        <div className="absolute bottom-1/4 right-1/4 w-96 h-96 bg-accent-purple/10 rounded-full blur-3xl animate-float" style={{ animationDelay: '1s' }}></div>
      </div>

      <div className="relative min-h-screen flex items-center justify-center p-6">
        <div className="max-w-7xl w-full">
          <div className="text-center mb-16 animate-fade-in-down">
            <div className="flex justify-center mb-6">
              <Logo size="xl" animate={true} showText={true} />
            </div>
            <p className="text-xl text-white/80 mb-2">
              AI-Powered Urban Fleet & Traffic Intelligence
            </p>
            <p className="text-sm text-white/50">
              Next-Generation Fleet Management Platform
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-12">
            {roles.map((role, index) => {
              const IconComponent = role.icon;
              return (
                <div
                  key={index}
                  className="neon-card cursor-pointer group animate-fade-in-up"
                  style={{ animationDelay: `${index * 0.1}s` }}
                  onClick={() => navigate(`/login/${role.role}`)}
                >
                  <div className="p-6 relative z-10">
                    <div className={`w-20 h-20 mx-auto mb-4 rounded-2xl bg-gradient-to-br ${role.gradient} p-0.5 group-hover:scale-110 transition-transform duration-300`}>
                      <div className="w-full h-full bg-dark-900 rounded-2xl flex items-center justify-center">
                        <IconComponent size="lg" className="text-white" />
                      </div>
                    </div>
                    
                    <h3 className="text-xl font-bold text-white mb-2 text-center">
                      {role.title}
                    </h3>
                    <p className="text-white/60 text-center text-sm mb-6">
                      {role.description}
                    </p>
                    
                    <button className={`w-full btn-primary bg-gradient-to-r ${role.gradient}`}>
                      Access Portal
                    </button>
                  </div>
                </div>
              );
            })}
          </div>

          <div className="text-center space-y-6 animate-fade-in-up" style={{ animationDelay: '0.5s' }}>
            <div className="glass-card inline-block px-8 py-4">
              <p className="text-white/70 mb-3 text-sm">
                <span className="text-accent-yellow">⚡</span> Backend Not Connected?
              </p>
              <button
                onClick={() => navigate('/demo')}
                className="btn-secondary"
              >
                Try Demo Mode
              </button>
            </div>
            
            <div>
              <p className="text-white/70">
                Don't have an account?{' '}
                <button
                  onClick={() => navigate('/signup')}
                  className="text-accent-cyan font-semibold hover:text-accent-blue transition-colors duration-300 underline"
                >
                  Sign up now
                </button>
              </p>
            </div>
          </div>

          <div className="mt-16 text-center">
            <div className="flex items-center justify-center gap-8 text-white/40 text-sm">
              <span className="hover:text-white/60 transition-colors cursor-pointer">About</span>
              <span className="w-1 h-1 bg-white/40 rounded-full"></span>
              <span className="hover:text-white/60 transition-colors cursor-pointer">Features</span>
              <span className="w-1 h-1 bg-white/40 rounded-full"></span>
              <span className="hover:text-white/60 transition-colors cursor-pointer">Contact</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default WelcomePage;
