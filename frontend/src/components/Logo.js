import React from 'react';

const Logo = ({ size = 'md', animate = true, showText = true }) => {
  const sizes = {
    sm: { icon: 'w-10 h-10', text: 'text-lg' },
    md: { icon: 'w-14 h-14', text: 'text-2xl' },
    medium: { icon: 'w-14 h-14', text: 'text-2xl' },
    lg: { icon: 'w-20 h-20', text: 'text-3xl' },
    large: { icon: 'w-20 h-20', text: 'text-3xl' },
    xl: { icon: 'w-28 h-28', text: 'text-5xl' },
  };

  const { icon, text } = sizes[size] || sizes.md;

  return (
    <div className="flex items-center gap-4">
      <div className={`${icon} relative group`}>
        {/* Outer glow effect */}
        <div className={`absolute inset-0 rounded-full bg-black opacity-10 blur-lg ${animate ? 'group-hover:opacity-20 transition-opacity duration-500' : ''}`}></div>
        
        {/* Main logo container */}
        <div className="relative w-full h-full rounded-full border-2 border-black bg-white flex items-center justify-center overflow-hidden">
          {/* Inner subtle gradient background */}
          <div className="absolute inset-0 bg-gradient-to-br from-black/5 to-transparent"></div>
          
          {/* Logo SVG */}
          <svg viewBox="0 0 100 100" className="w-3/5 h-3/5 relative z-10">
            <defs>
              <linearGradient id="logoGradient" x1="0%" y1="0%" x2="100%" y2="100%">
                <stop offset="0%" stopColor="#000000" />
                <stop offset="100%" stopColor="#404040" />
              </linearGradient>
              <filter id="glow">
                <feGaussianBlur stdDeviation="2" result="coloredBlur"/>
                <feMerge>
                  <feMergeNode in="coloredBlur"/>
                  <feMergeNode in="SourceGraphic"/>
                </feMerge>
              </filter>
            </defs>
            
            {/* Stylized "N" and "F" combination */}
            <g filter="url(#glow)">
              {/* N shape - Neural network inspired */}
              <path
                d="M 25 30 L 25 70 M 25 30 L 45 70 M 45 30 L 45 70"
                stroke="url(#logoGradient)"
                strokeWidth="5"
                strokeLinecap="round"
                strokeLinejoin="round"
                fill="none"
                className={animate ? 'animate-pulse' : ''}
                style={{ animationDuration: '3s' }}
              />
              
              {/* F shape - Fleet inspired */}
              <path
                d="M 55 30 L 55 70 M 55 30 L 75 30 M 55 50 L 70 50"
                stroke="url(#logoGradient)"
                strokeWidth="5"
                strokeLinecap="round"
                strokeLinejoin="round"
                fill="none"
                className={animate ? 'animate-pulse' : ''}
                style={{ animationDuration: '3s', animationDelay: '0.2s' }}
              />
              
              {/* Connection nodes - Neural network theme */}
              <circle cx="25" cy="30" r="3" fill="#000000" className={animate ? 'animate-pulse' : ''} />
              <circle cx="45" cy="70" r="3" fill="#000000" className={animate ? 'animate-pulse' : ''} style={{ animationDelay: '0.4s' }} />
              <circle cx="55" cy="50" r="3" fill="#000000" className={animate ? 'animate-pulse' : ''} style={{ animationDelay: '0.6s' }} />
            </g>
          </svg>
        </div>
      </div>
      
      {showText && (
        <div className="flex flex-col">
          <h1 className={`${text} font-bold leading-tight tracking-tight`}>
            <span className="text-black">Neuro</span>
            <span className="text-gray-600">Fleet</span>
            <span className="text-black">X</span>
          </h1>
        </div>
      )}
    </div>
  );
};

export default Logo;
