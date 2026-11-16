import { useState, useEffect } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../api/axios';
import styles from './Sidebar.module.css';

const adminLinks = [
  { to: '/dashboard', label: 'Dashboard', icon: '🏠' },
  { to: '/users', label: 'Người dùng', icon: '👥' },
  { to: '/students', label: 'Sinh viên', icon: '🎓' },
  { to: '/mentors', label: 'Mentor', icon: '👨‍🏫' },
  { to: '/assignments', label: 'Phân công', icon: '📋' },
  { to: '/phases', label: 'Giai đoạn', icon: '📅' },
  { to: '/rounds', label: 'Vòng đánh giá', icon: '🔄' },
  { to: '/criteria', label: 'Tiêu chí', icon: '✅' },
  { to: '/round-criteria', label: 'Tiêu chí vòng', icon: '🔗' },
  { to: '/results', label: 'Kết quả', icon: '📊' },
];

const studentLinks = [
  { to: '/dashboard', label: 'Dashboard', icon: '🏠' },
  { to: '/my-student-profile', label: 'Hồ sơ của tôi', icon: '👤' },
  { to: '/assignments', label: 'Phân công', icon: '📋' },
  { to: '/results', label: 'Kết quả', icon: '📊' },
];

const mentorLinks = [
  { to: '/dashboard', label: 'Dashboard', icon: '🏠' },
  { to: '/my-mentor-profile', label: 'Hồ sơ của tôi', icon: '👤' },
  { to: '/assignments', label: 'Phân công', icon: '📋' },
  { to: '/results', label: 'Kết quả', icon: '📊' },
];

const fontOptions = [
  { label: 'Helvetica Neue', value: "'Helvetica Neue', Helvetica, Arial, sans-serif" },
  { label: 'Segoe UI', value: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif" },
  { label: 'Arial', value: 'Arial, sans-serif' },
  { label: 'Roboto', value: "'Roboto', sans-serif" },
  { label: 'Inter', value: "'Inter', sans-serif" },
];

export default function Sidebar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const [selectedFont, setSelectedFont] = useState(() => {
    return localStorage.getItem('sidebarFont') || "'Helvetica Neue', Helvetica, Arial, sans-serif";
  });

  const handleFontChange = (e) => {
    const val = e.target.value;
    setSelectedFont(val);
    localStorage.setItem('sidebarFont', val);
  };

  const role = user?.role;
  const links =
    role === 'ROLE_ADMIN' ? adminLinks
    : role === 'ROLE_STUDENT' ? studentLinks
    : role === 'ROLE_MENTOR' ? mentorLinks
    : adminLinks;

  const handleLogout = async () => {
    try {
      const token = localStorage.getItem('token');
      await api.post('/auth/logout', null, {
        headers: { Authorization: `Bearer ${token}` },
      });
    } catch (_) {}
    logout();
    navigate('/login');
  };

  return (
    <aside className={styles.sidebar} style={{ fontFamily: selectedFont }}>
      <div className={styles.logo}>
        <span className={styles.logoIcon}>🎓</span>
        <div>
          <div className={styles.logoTitle}>Internship</div>
          <div className={styles.logoSub}>Manager</div>
        </div>
      </div>

      <div className={styles.userInfo}>
        <div className={styles.userAvatar}>{user?.fullName?.[0] || '?'}</div>
        <div>
          <div className={styles.userName}>{user?.fullName || 'Người dùng'}</div>
          <div className={styles.userRole}>{role?.replace('ROLE_', '') || ''}</div>
        </div>
      </div>

      <nav className={styles.nav}>
        {links.map((link) => (
          <NavLink
            key={link.to}
            to={link.to}
            className={({ isActive }) =>
              `${styles.navLink} ${isActive ? styles.active : ''}`
            }
          >
            <span className={styles.navIcon}>{link.icon}</span>
            <span>{link.label}</span>
          </NavLink>
        ))}
      </nav>

      {/* Font selector allows user to set or change menu font to Helvetica Neue or other fonts */}
      <div className={styles.fontSelectorBox}>
        <label className={styles.fontLabel}>🔤 Font chữ Menu:</label>
        <select
          className={styles.fontSelect}
          value={selectedFont}
          onChange={handleFontChange}
        >
          {fontOptions.map((opt) => (
            <option key={opt.label} value={opt.value}>
              {opt.label}
            </option>
          ))}
        </select>
      </div>

      <button className={styles.logoutBtn} onClick={handleLogout}>
        <span>🚪</span> Đăng xuất
      </button>
    </aside>
  );
}
