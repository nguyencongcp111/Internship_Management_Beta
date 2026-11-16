import React, { createContext, useContext, useState } from 'react';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try {
      const stored = localStorage.getItem('user');
      return (stored && stored !== 'undefined' && stored !== 'null') ? JSON.parse(stored) : null;
    } catch (_) {
      return null;
    }
  });

  const [token, setToken] = useState(() => {
    const stored = localStorage.getItem('token');
    if (stored && stored !== 'undefined' && stored !== 'null' && stored.trim() !== '') {
      return stored.trim();
    }
    localStorage.removeItem('token');
    return null;
  });

  const login = (userData, jwt) => {
    if (jwt && jwt !== 'undefined' && jwt !== 'null') {
      const cleanJwt = jwt.trim();
      setToken(cleanJwt);
      localStorage.setItem('token', cleanJwt);
    }
    if (userData) {
      setUser(userData);
      localStorage.setItem('user', JSON.stringify(userData));
    }
  };

  const logout = () => {
    setUser(null);
    setToken(null);
    localStorage.removeItem('user');
    localStorage.removeItem('token');
  };

  return (
    <AuthContext.Provider value={{ user, token, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
