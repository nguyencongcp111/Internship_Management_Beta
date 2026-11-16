import axios from 'axios';

const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
});

api.interceptors.request.use((config) => {
  // If request explicitly sets an Authorization header, respect it
  if (config.headers?.Authorization) {
    return config;
  }

  // Never send Authorization header on login or public endpoints
  if (config.url && config.url.includes('/auth/login')) {
    delete config.headers.Authorization;
    return config;
  }

  const token = localStorage.getItem('token');
  if (token && token !== 'undefined' && token !== 'null' && token.trim() !== '') {
    config.headers.Authorization = `Bearer ${token.trim()}`;
  } else {
    delete config.headers.Authorization;
  }
  return config;
});

api.interceptors.response.use(
  (res) => res,
  (err) => {
    // Only redirect to /login if 401 occurs while user is not already on /login
    if (
      err.response?.status === 401 &&
      !err.config?.url?.includes('/auth/login') &&
      window.location.pathname !== '/login'
    ) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(err);
  }
);

export default api;
