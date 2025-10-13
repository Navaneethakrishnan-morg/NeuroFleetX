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
    <div className="min-h-screen bg-white text-black">
      {/* Navigation */}
      <nav className="fixed top-0 w-full bg-white bg-opacity-90 backdrop-blur-lg border-b border-gray-200 z-50 shadow-sm">
        <div className="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between">
          <Logo size="medium" />
          <button
            onClick={() => navigate('/')}
            className="text-gray-600 hover:text-black transition duration-300"
          >
            ← Back to Home
          </button>
        </div>
      </nav>

      <div className="pt-32 pb-20 px-6">
        <div className="max-w-7xl mx-auto">
          <div className="text-center mb-16">
            <h1 className="text-5xl font-bold mb-4 text-black">
              Select Your Portal
            </h1>
            <p className="text-xl text-gray-600">
              Choose your role to access the platform
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 max-w-6xl mx-auto mb-12">
            {portals.map((portal, index) => (
              <div
                key={index}
                className={`relative group cursor-pointer`}
                onClick={() => navigate(`/login/${portal.role}`)}
              >
                <div className="p-8 bg-white backdrop-blur-lg rounded-2xl border-2 border-gray-200 hover:border-black transition-all duration-300 transform hover:-translate-y-2 shadow-sm hover:shadow-lg">
                  <div className={`text-7xl mb-6 text-center transform group-hover:scale-110 transition duration-300`}>
                    {portal.icon}
                  </div>
                  <h3 className="text-2xl font-bold text-center mb-3 text-black">
                    {portal.title}
                  </h3>
                  <p className="text-gray-600 text-center mb-6">
                    {portal.description}
                  </p>
                  <button className="w-full py-3 bg-black text-white rounded-lg font-semibold hover:bg-gray-800 transition-all duration-300 hover:scale-105">
                    Access Portal
                  </button>
                </div>
              </div>
            ))}
          </div>

          <div className="text-center">
            <p className="text-gray-600 text-lg mb-4">
              Don't have an account?{' '}
              <button
                onClick={() => navigate('/signup')}
                className="text-black font-semibold hover:text-gray-700 transition duration-300 underline"
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
