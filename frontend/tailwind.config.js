module.exports = {
  purge: ['./src/**/*.{js,jsx,ts,tsx}', './public/index.html'],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        dark: {
          950: '#000000',
          900: '#0a0a0a',
          800: '#121212',
          700: '#1a1a1a',
          600: '#242424',
          500: '#2e2e2e',
        },
        accent: {
          cyan: '#ffffff',
          blue: '#e5e5e5',
          purple: '#cccccc',
          pink: '#b3b3b3',
          green: '#999999',
          yellow: '#808080',
        },
      },
      backgroundImage: {
        'gradient-dark': 'linear-gradient(135deg, #000000 0%, #1a1a1a 100%)',
        'gradient-dark-radial': 'radial-gradient(circle at top right, #0a0a0a, #000000)',
        'gradient-accent': 'linear-gradient(135deg, #ffffff 0%, #e5e5e5 50%, #cccccc 100%)',
        'gradient-accent-hover': 'linear-gradient(135deg, #ffffff 0%, #cccccc 100%)',
        'gradient-mesh': 'radial-gradient(at 40% 20%, rgba(255, 255, 255, 0.05) 0px, transparent 50%), radial-gradient(at 80% 0%, rgba(255, 255, 255, 0.05) 0px, transparent 50%), radial-gradient(at 0% 50%, rgba(255, 255, 255, 0.05) 0px, transparent 50%)',
      },
      boxShadow: {
        'neon-cyan': '0 0 20px rgba(255, 255, 255, 0.3), 0 0 40px rgba(255, 255, 255, 0.2)',
        'neon-blue': '0 0 20px rgba(255, 255, 255, 0.3), 0 0 40px rgba(255, 255, 255, 0.2)',
        'neon-purple': '0 0 20px rgba(255, 255, 255, 0.3), 0 0 40px rgba(255, 255, 255, 0.2)',
        'neon-pink': '0 0 20px rgba(255, 255, 255, 0.3), 0 0 40px rgba(255, 255, 255, 0.2)',
        'glass': '0 8px 32px 0 rgba(255, 255, 255, 0.1)',
        'glass-lg': '0 12px 48px 0 rgba(255, 255, 255, 0.15)',
      },
      backdropBlur: {
        'glass': '16px',
        'glass-lg': '24px',
      },
      animation: {
        'fade-in': 'fadeIn 0.8s ease-out forwards',
        'fade-in-up': 'fadeInUp 0.8s ease-out forwards',
        'fade-in-down': 'fadeInDown 0.8s ease-out forwards',
        'slide-in-left': 'slideInLeft 0.6s ease-out forwards',
        'slide-in-right': 'slideInRight 0.6s ease-out forwards',
        'scale-in': 'scaleIn 0.5s ease-out forwards',
        'pulse-glow': 'pulseGlow 2s ease-in-out infinite',
        'float': 'float 3s ease-in-out infinite',
        'shimmer': 'shimmer 2s linear infinite',
        'spin-slow': 'spin 3s linear infinite',
      },
      keyframes: {
        fadeIn: {
          '0%': { opacity: '0' },
          '100%': { opacity: '1' },
        },
        fadeInUp: {
          '0%': { opacity: '0', transform: 'translateY(30px)' },
          '100%': { opacity: '1', transform: 'translateY(0)' },
        },
        fadeInDown: {
          '0%': { opacity: '0', transform: 'translateY(-30px)' },
          '100%': { opacity: '1', transform: 'translateY(0)' },
        },
        slideInLeft: {
          '0%': { opacity: '0', transform: 'translateX(-50px)' },
          '100%': { opacity: '1', transform: 'translateX(0)' },
        },
        slideInRight: {
          '0%': { opacity: '0', transform: 'translateX(50px)' },
          '100%': { opacity: '1', transform: 'translateX(0)' },
        },
        scaleIn: {
          '0%': { opacity: '0', transform: 'scale(0.8)' },
          '100%': { opacity: '1', transform: 'scale(1)' },
        },
        pulseGlow: {
          '0%, 100%': { boxShadow: '0 0 20px rgba(255, 255, 255, 0.2)' },
          '50%': { boxShadow: '0 0 40px rgba(255, 255, 255, 0.4), 0 0 60px rgba(255, 255, 255, 0.3)' },
        },
        float: {
          '0%, 100%': { transform: 'translateY(0px)' },
          '50%': { transform: 'translateY(-10px)' },
        },
        shimmer: {
          '0%': { backgroundPosition: '-1000px 0' },
          '100%': { backgroundPosition: '1000px 0' },
        },
      },
    },
  },
  variants: {
    extend: {
      opacity: ['disabled'],
      cursor: ['disabled'],
      scale: ['group-hover'],
      transform: ['group-hover'],
    },
  },
  plugins: [],
}
