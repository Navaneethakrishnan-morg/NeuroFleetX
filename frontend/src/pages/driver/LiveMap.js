import React from 'react';
import { LocationIcon, RouteIcon } from '../../components/Icons';

const LiveMap = () => {
  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-3xl font-bold text-white mb-2 flex items-center gap-3">
          <LocationIcon size="lg" className="text-accent-green" />
          Live Map View
        </h2>
        <p className="text-white/50">Real-time route tracking and navigation</p>
      </div>

      <div className="glass-card p-6">
        <div className="bg-dark-700/40 rounded-xl border border-white/10 p-8 text-center aspect-video flex flex-col items-center justify-center">
          <div className="w-24 h-24 rounded-full bg-gradient-to-br from-accent-green to-accent-cyan flex items-center justify-center mb-6 animate-pulse">
            <LocationIcon size="xl" className="text-white" />
          </div>
          <p className="text-white/70 text-lg mb-4">🗺️ Interactive Map View</p>
          <p className="text-white/50 text-sm max-w-md">
            Real-time GPS tracking would be displayed here with highlighted route lines in emerald green, 
            showing pickup location, current position, and destination.
          </p>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="glass-card p-6">
          <h3 className="text-xl font-bold text-white mb-4 flex items-center gap-2">
            <RouteIcon size="md" className="text-accent-cyan" />
            Current Route
          </h3>
          <div className="space-y-4">
            <div className="flex items-center gap-4 p-4 bg-accent-green/10 border border-accent-green/30 rounded-xl">
              <div className="w-12 h-12 rounded-full bg-gradient-to-br from-accent-green to-accent-cyan flex items-center justify-center">
                <span className="text-2xl">📍</span>
              </div>
              <div>
                <p className="text-accent-green text-sm font-semibold mb-1">Pickup Location</p>
                <p className="text-white font-bold">123 Main Street, New York</p>
                <p className="text-white/50 text-sm">ETA: 5 minutes</p>
              </div>
            </div>

            <div className="flex justify-center">
              <div className="flex flex-col items-center gap-2">
                <div className="w-px h-8 bg-gradient-to-b from-accent-cyan to-accent-blue"></div>
                <div className="w-8 h-8 rounded-full border-4 border-accent-cyan bg-dark-800 flex items-center justify-center animate-pulse">
                  <div className="w-3 h-3 rounded-full bg-accent-cyan"></div>
                </div>
                <div className="w-px h-8 bg-gradient-to-b from-accent-cyan to-accent-pink"></div>
              </div>
            </div>

            <div className="flex items-center gap-4 p-4 bg-accent-pink/10 border border-accent-pink/30 rounded-xl">
              <div className="w-12 h-12 rounded-full bg-gradient-to-br from-accent-pink to-accent-purple flex items-center justify-center">
                <span className="text-2xl">🏁</span>
              </div>
              <div>
                <p className="text-accent-pink text-sm font-semibold mb-1">Destination</p>
                <p className="text-white font-bold">456 Oak Avenue, New York</p>
                <p className="text-white/50 text-sm">Distance: 8.5 km</p>
              </div>
            </div>
          </div>
        </div>

        <div className="glass-card p-6">
          <h3 className="text-xl font-bold text-white mb-4">Trip Information</h3>
          <div className="space-y-3">
            <div className="flex items-center justify-between p-4 bg-dark-700/40 rounded-xl border border-white/5">
              <span className="text-white/60">Total Distance</span>
              <span className="text-white font-bold">8.5 km</span>
            </div>
            <div className="flex items-center justify-between p-4 bg-dark-700/40 rounded-xl border border-white/5">
              <span className="text-white/60">Estimated Time</span>
              <span className="text-white font-bold">25 minutes</span>
            </div>
            <div className="flex items-center justify-between p-4 bg-dark-700/40 rounded-xl border border-white/5">
              <span className="text-white/60">Current Speed</span>
              <span className="text-white font-bold">45 km/h</span>
            </div>
            <div className="flex items-center justify-between p-4 bg-dark-700/40 rounded-xl border border-white/5">
              <span className="text-white/60">Traffic Status</span>
              <span className="text-accent-green font-bold flex items-center gap-2">
                <span className="w-2 h-2 rounded-full bg-accent-green animate-pulse"></span>
                Light Traffic
              </span>
            </div>
          </div>

          <div className="mt-6 p-4 bg-gradient-to-r from-accent-cyan/10 to-accent-blue/10 border border-accent-cyan/30 rounded-xl">
            <p className="text-accent-cyan text-sm font-semibold mb-2">🚨 Navigation Tip</p>
            <p className="text-white/70 text-sm">
              Turn right at the next intersection. Continue straight for 2.3 km.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LiveMap;
