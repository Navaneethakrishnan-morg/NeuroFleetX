import React, { useState, useEffect } from 'react';
import { VehicleIcon, CloseIcon } from './Icons';

const VehicleModal = ({ isOpen, onClose, onSave, vehicle }) => {
  const [formData, setFormData] = useState({
    vehicleNumber: '',
    model: '',
    manufacturer: '',
    type: 'SEDAN',
    capacity: 4,
    isElectric: false,
    status: 'AVAILABLE',
    batteryLevel: 100,
    fuelLevel: 100,
    latitude: 0,
    longitude: 0,
  });

  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (vehicle) {
      setFormData({
        vehicleNumber: vehicle.vehicleNumber || '',
        model: vehicle.model || '',
        manufacturer: vehicle.manufacturer || '',
        type: vehicle.type || 'SEDAN',
        capacity: vehicle.capacity || 4,
        isElectric: vehicle.isElectric || false,
        status: vehicle.status || 'AVAILABLE',
        batteryLevel: vehicle.batteryLevel || 100,
        fuelLevel: vehicle.fuelLevel || 100,
        latitude: vehicle.latitude || 0,
        longitude: vehicle.longitude || 0,
      });
    } else {
      setFormData({
        vehicleNumber: '',
        model: '',
        manufacturer: '',
        type: 'SEDAN',
        capacity: 4,
        isElectric: false,
        status: 'AVAILABLE',
        batteryLevel: 100,
        fuelLevel: 100,
        latitude: 0,
        longitude: 0,
      });
    }
    setErrors({});
  }, [vehicle, isOpen]);

  const validate = () => {
    const newErrors = {};
    if (!formData.vehicleNumber.trim()) newErrors.vehicleNumber = 'Vehicle number is required';
    if (!formData.model.trim()) newErrors.model = 'Model is required';
    if (!formData.manufacturer.trim()) newErrors.manufacturer = 'Manufacturer is required';
    if (formData.capacity < 1) newErrors.capacity = 'Capacity must be at least 1';
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;

    setLoading(true);
    try {
      await onSave(formData);
      onClose();
    } catch (error) {
      console.error('Error saving vehicle:', error);
      setErrors({ submit: 'Failed to save vehicle. Please try again.' });
    } finally {
      setLoading(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black/70 backdrop-blur-sm flex items-center justify-center z-50 p-4 animate-fade-in">
      <div className="glass-card max-w-2xl w-full max-h-[90vh] overflow-y-auto animate-fade-in-up">
        <div className="sticky top-0 bg-dark-800/95 backdrop-blur-md border-b border-white/10 p-6 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="p-2 rounded-lg bg-gradient-to-br from-accent-cyan to-accent-blue">
              <VehicleIcon size="md" className="text-white" />
            </div>
            <h2 className="text-2xl font-bold text-white">
              {vehicle ? 'Edit Vehicle' : 'Add New Vehicle'}
            </h2>
          </div>
          <button
            onClick={onClose}
            className="btn-icon hover:bg-dark-700"
            disabled={loading}
          >
            <CloseIcon size="sm" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-6 space-y-6">
          {errors.submit && (
            <div className="px-4 py-3 rounded-lg bg-red-500/15 border border-red-500 text-red-400">
              {errors.submit}
            </div>
          )}

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className="block text-sm font-semibold mb-2 text-white/90">
                Vehicle Number <span className="text-red-400">*</span>
              </label>
              <input
                type="text"
                className={`input-field ${errors.vehicleNumber ? 'border-red-500' : ''}`}
                placeholder="e.g., NF-001"
                value={formData.vehicleNumber}
                onChange={(e) => setFormData({ ...formData, vehicleNumber: e.target.value })}
                disabled={loading}
              />
              {errors.vehicleNumber && (
                <p className="text-red-400 text-xs mt-1">{errors.vehicleNumber}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-semibold mb-2 text-white/90">
                Model <span className="text-red-400">*</span>
              </label>
              <input
                type="text"
                className={`input-field ${errors.model ? 'border-red-500' : ''}`}
                placeholder="e.g., Tesla Model S"
                value={formData.model}
                onChange={(e) => setFormData({ ...formData, model: e.target.value })}
                disabled={loading}
              />
              {errors.model && (
                <p className="text-red-400 text-xs mt-1">{errors.model}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-semibold mb-2 text-white/90">
                Manufacturer <span className="text-red-400">*</span>
              </label>
              <input
                type="text"
                className={`input-field ${errors.manufacturer ? 'border-red-500' : ''}`}
                placeholder="e.g., Tesla"
                value={formData.manufacturer}
                onChange={(e) => setFormData({ ...formData, manufacturer: e.target.value })}
                disabled={loading}
              />
              {errors.manufacturer && (
                <p className="text-red-400 text-xs mt-1">{errors.manufacturer}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-semibold mb-2 text-white/90">
                Vehicle Type
              </label>
              <select
                className="input-field"
                value={formData.type}
                onChange={(e) => setFormData({ ...formData, type: e.target.value })}
                disabled={loading}
              >
                <option value="SEDAN">Sedan</option>
                <option value="SUV">SUV</option>
                <option value="VAN">Van</option>
                <option value="TRUCK">Truck</option>
                <option value="BUS">Bus</option>
                <option value="BIKE">Bike</option>
              </select>
            </div>

            <div>
              <label className="block text-sm font-semibold mb-2 text-white/90">
                Capacity (Seats)
              </label>
              <input
                type="number"
                className={`input-field ${errors.capacity ? 'border-red-500' : ''}`}
                min="1"
                value={formData.capacity}
                onChange={(e) => setFormData({ ...formData, capacity: parseInt(e.target.value) || 0 })}
                disabled={loading}
              />
              {errors.capacity && (
                <p className="text-red-400 text-xs mt-1">{errors.capacity}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-semibold mb-2 text-white/90">
                Status
              </label>
              <select
                className="input-field"
                value={formData.status}
                onChange={(e) => setFormData({ ...formData, status: e.target.value })}
                disabled={loading}
              >
                <option value="AVAILABLE">Available</option>
                <option value="IN_USE">In Use</option>
                <option value="MAINTENANCE">Maintenance</option>
                <option value="OUT_OF_SERVICE">Out of Service</option>
              </select>
            </div>

            <div>
              <label className="block text-sm font-semibold mb-2 text-white/90">
                Battery Level (%)
              </label>
              <input
                type="number"
                className="input-field"
                min="0"
                max="100"
                value={formData.batteryLevel}
                onChange={(e) => setFormData({ ...formData, batteryLevel: parseInt(e.target.value) || 0 })}
                disabled={loading}
              />
            </div>

            <div>
              <label className="block text-sm font-semibold mb-2 text-white/90">
                Fuel Level (%)
              </label>
              <input
                type="number"
                className="input-field"
                min="0"
                max="100"
                value={formData.fuelLevel}
                onChange={(e) => setFormData({ ...formData, fuelLevel: parseInt(e.target.value) || 0 })}
                disabled={loading}
              />
            </div>
          </div>

          <div className="flex items-center gap-3 p-4 bg-dark-700/40 rounded-xl border border-white/5">
            <input
              type="checkbox"
              id="isElectric"
              className="w-5 h-5 accent-accent-cyan"
              checked={formData.isElectric}
              onChange={(e) => setFormData({ ...formData, isElectric: e.target.checked })}
              disabled={loading}
            />
            <label htmlFor="isElectric" className="text-sm font-semibold text-white/90 cursor-pointer">
              ⚡ Electric Vehicle
            </label>
          </div>

          <div className="flex gap-4 pt-4">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 btn-secondary"
              disabled={loading}
            >
              Cancel
            </button>
            <button
              type="submit"
              className="flex-1 btn-primary"
              disabled={loading}
            >
              {loading ? 'Saving...' : vehicle ? 'Update Vehicle' : 'Add Vehicle'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default VehicleModal;
