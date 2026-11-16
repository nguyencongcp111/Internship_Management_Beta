import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../api/axios';
import TrianglePattern from '../components/TrianglePattern';
import styles from './LoginPage.module.css';

export default function LoginPage() {
  const [form, setForm] = useState({ username: '', password: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  // Clear any existing session/token when visiting the login page
  useEffect(() => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  }, []);

  const handleChange = (e) =>
    setForm((f) => ({ ...f, [e.target.name]: e.target.value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const res = await api.post('/auth/login', form);
      const data = res.data?.data ?? res.data;
      const jwt = data?.accessToken || data?.token;

      if (!jwt || typeof jwt !== 'string') {
        throw new Error('Không nhận được mã xác thực (accessToken) từ máy chủ.');
      }

      // Decode JWT payload to get username and role
      let role = 'ROLE_ADMIN';
      let username = data?.username || form.username;

      try {
        const parts = jwt.split('.');
        if (parts.length === 3) {
          const payload = JSON.parse(
            decodeURIComponent(
              atob(parts[1].replace(/-/g, '+').replace(/_/g, '/'))
                .split('')
                .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
                .join('')
            )
          );
          if (payload.role) {
            role = payload.role.startsWith('ROLE_')
              ? payload.role
              : `ROLE_${payload.role}`;
          }
          if (payload.sub) {
            username = payload.sub;
          }
        }
      } catch (_) {}

      const userData = {
        username: username,
        fullName: username,
        role: role,
        active: true,
      };

      // Save credentials and transition directly to dashboard
      login(userData, jwt);
      navigate('/dashboard');
    } catch (err) {
      const msg =
        err.response?.data?.message ||
        err.message ||
        'Đăng nhập thất bại. Vui lòng kiểm tra lại tài khoản hoặc mật khẩu.';
      setError(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.root}>
      <TrianglePattern colorMode="white" />

      <div className={styles.card}>
        <div className={styles.logoArea}>
          <span className={styles.logoEmoji}>🎓</span>
          <h1 className={styles.title}>Internship Manager</h1>
          <p className={styles.subtitle}>Hệ thống quản lý thực tập</p>
        </div>

        <form className={styles.form} onSubmit={handleSubmit}>
          <div className={styles.field}>
            <label className={styles.label}>Tên đăng nhập</label>
            <input
              className={styles.input}
              type="text"
              name="username"
              value={form.username}
              onChange={handleChange}
              placeholder="Nhập tên đăng nhập..."
              required
              autoFocus
            />
          </div>

          <div className={styles.field}>
            <label className={styles.label}>Mật khẩu</label>
            <input
              className={styles.input}
              type="password"
              name="password"
              value={form.password}
              onChange={handleChange}
              placeholder="Nhập mật khẩu..."
              required
            />
          </div>

          {error && <div className={styles.error}>{error}</div>}

          <button className={styles.btn} type="submit" disabled={loading}>
            {loading ? 'Đang đăng nhập...' : 'Đăng nhập'}
          </button>
        </form>
      </div>
    </div>
  );
}
