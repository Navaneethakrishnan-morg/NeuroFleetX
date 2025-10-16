import React from 'react';
import { useNavigate } from 'react-router-dom';
import Logo from '../components/Logo';

const PortalSelectionPage = () => {
  const navigate = useNavigate();

  const portals = [
    {
      title: 'Admin Portal',
      icon: '🧠',
      description: 'Complete system control & analytics',
      role: 'admin',
    },
    {
      title: 'Manager Portal',
      icon: '🧩',
      description: 'Fleet operations & management',
      role: 'manager',
    },
    {
      title: 'Driver Portal',
      icon: '🚗',
      description: 'Trips & route management',
      role: 'driver',
    },
    {
      title: 'Customer Portal',
      icon: '👤',
      description: 'Vehicle booking & preferences',
      role: 'customer',
    },
  ];

  return (
    <div className="min-h-screen bg-gradient-to-b from-emerald-950 via-green-950 to-emerald-900">
      {/* Subtle background pattern */}
      <div className="absolute inset-0 opacity-10" style={{
        backgroundImage: `radial-gradient(circle at 2px 2px, rgba(16, 185, 129, 0.15) 1px, transparent 0)`,
        backgroundSize: '40px 40px'
      }}></div>

      {/* Navigation */}
      <nav className="fixed top-0 w-full bg-emerald-800/50 backdrop-blur-sm border-b border-emerald-600/30 shadow-lg z-50">
        <div className="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between">
          <Logo size="medium" />
          <button
            onClick={() => navigate('/')}
            className="text-emerald-200 hover:text-emerald-100 transition duration-300 font-medium"
          >
            ← Back to Home
          </button>
        </div>
      </nav>

      <div className="relative pt-32 pb-20 px-6">
        <div className="max-w-7xl mx-auto">
          <div className="text-center mb-16">
            <h1 className="text-5xl font-bold mb-4 text-emerald-100">
              Select Your Portal
            </h1>
            <p className="text-xl text-emerald-200">
              Choose your role to access the platform
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 max-w-6xl mx-auto mb-12">
            {portals.map((portal, index) => (
              <div
                key={index}
                className="relative group cursor-pointer"
                onClick={() => navigate(`/login/${portal.role}`)}
              >
                <div className="p-8 bg-emerald-900/60 backdrop-blur-sm border border-emerald-500/40 rounded-2xl transition-all duration-300 transform hover:-translate-y-2 hover:border-emerald-400/60 hover:shadow-xl hover:shadow-emerald-400/30">
                  <div className="text-7xl mb-6 text-center transform group-hover:scale-110 transition duration-300">
                    {portal.icon}
                  </div>
                  <h3 className="text-2xl font-bold text-center mb-3 text-emerald-100">
                    {portal.title}
                  </h3>
                  <p className="text-center mb-6 text-emerald-200/70">
                    {portal.description}
                  </p>
                  <button className="w-full py-3 rounded-lg font-semibold bg-emerald-600 hover:bg-emerald-500 text-white transition-all duration-300 hover:shadow-lg hover:shadow-emerald-400/50">
                    Access Portal
                  </button>
                </div>
              </div>
            ))}
          </div>

          <div className="text-center">
            <p className="text-lg mb-4 text-emerald-200">
              Don't have an account?{' '}
              <button
                onClick={() => navigate('/signup')}
                className="font-semibold text-emerald-400 hover:text-emerald-300 transition duration-300 underline"
              >
                Sign up now
              </button>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PortalSelectionPage;
