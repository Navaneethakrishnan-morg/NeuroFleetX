import React from 'react';
import { useNavigate } from 'react-router-dom';
import Logo from '../components/Logo';

const LandingPage = () => {
  const navigate = useNavigate();

  const features = [
    {
      icon: '🤖',
      title: 'AI-Powered Analytics',
      description: 'Smart fleet management with predictive insights',
    },
    {
      icon: '📍',
      title: 'Real-Time Tracking',
      description: 'Monitor your fleet location and status instantly',
    },
    {
      icon: '🔧',
      title: 'Predictive Maintenance',
      description: 'Prevent breakdowns before they happen',
    },
    {
      icon: '📊',
      title: 'Advanced Analytics',
      description: 'Data-driven decisions for optimal performance',
    },
  ];

  return (
    <div className="min-h-screen bg-white text-black">
      {/* Navigation */}
      <nav className="fixed top-0 w-full bg-white bg-opacity-90 backdrop-blur-lg border-b border-gray-200 z-50 shadow-sm">
        <div className="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between">
          <Logo size="medium" />
          <button
            onClick={() => navigate('/portals')}
            className="px-6 py-2 bg-white text-black rounded-lg font-semibold hover:shadow-lg transition duration-300 transform hover:scale-105"
          >
            Get Started
          </button>
        </div>
      </nav>

      {/* Hero Section */}
      <section className="pt-32 pb-20 px-6">
        <div className="max-w-7xl mx-auto text-center">
          <div className="mb-8 animate-fade-in">
            <Logo size="large" className="justify-center mb-8" />
          </div>
          
          <h1 className="text-6xl md:text-7xl font-bold mb-6 leading-tight">
            <span className="text-black">
              AI-Powered Urban Fleet
            </span>
            <br />
            <span className="text-black">Management System</span>
          </h1>
          
          <p className="text-xl md:text-2xl text-gray-600 mb-12 max-w-3xl mx-auto">
            Transform your fleet operations with intelligent automation, real-time tracking, 
            and predictive analytics for maximum efficiency.
          </p>
          
          <div className="flex gap-4 justify-center">
            <button
              onClick={() => navigate('/portals')}
              className="px-8 py-4 bg-white text-black rounded-lg font-semibold text-lg hover:shadow-2xl transition duration-300 transform hover:scale-105"
            >
              Access Portal
            </button>
            <button
              className="px-8 py-4 bg-white border-2 border-black text-black rounded-lg font-semibold text-lg hover:bg-gray-100 transition duration-300"
            >
              Learn More
            </button>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-20 px-6 bg-gray-50">
        <div className="max-w-7xl mx-auto">
          <h2 className="text-4xl font-bold text-center mb-16">
            <span className="text-black">
              Powerful Features
            </span>
          </h2>
          
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
            {features.map((feature, index) => (
              <div
                key={index}
                className="p-6 bg-white backdrop-blur-lg rounded-xl border-2 border-gray-200 hover:border-black transition duration-300 transform hover:-translate-y-2 shadow-sm hover:shadow-md"
              >
                <div className="text-5xl mb-4">{feature.icon}</div>
                <h3 className="text-xl font-bold mb-2 text-black">{feature.title}</h3>
                <p className="text-gray-600">{feature.description}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Stats Section */}
      <section className="py-20 px-6">
        <div className="max-w-7xl mx-auto">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-8 text-center">
            {[
              { value: '99.9%', label: 'Uptime' },
              { value: '500+', label: 'Active Fleets' },
              { value: '50K+', label: 'Vehicles Tracked' },
              { value: '24/7', label: 'Support' },
            ].map((stat, index) => (
              <div key={index} className="p-6">
                <div className="text-5xl font-bold text-black mb-2">
                  {stat.value}
                </div>
                <div className="text-gray-600 text-lg">{stat.label}</div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-20 px-6 bg-gray-100">
        <div className="max-w-4xl mx-auto text-center">
          <h2 className="text-4xl font-bold mb-6">Ready to Transform Your Fleet?</h2>
          <p className="text-xl text-gray-600 mb-8">
            Join hundreds of companies optimizing their fleet operations with NeuroFleetX
          </p>
          <button
            onClick={() => navigate('/portals')}
            className="px-12 py-4 bg-white text-black rounded-lg font-semibold text-lg hover:shadow-2xl transition duration-300 transform hover:scale-105"
          >
            Get Started Today
          </button>
        </div>
      </section>

      {/* Footer */}
      <footer className="py-8 px-6 bg-white border-t border-gray-200">
        <div className="max-w-7xl mx-auto text-center text-gray-600">
          <p>&copy; 2025 NeuroFleetX. All rights reserved.</p>
        </div>
      </footer>
    </div>
  );
};

export default LandingPage;
