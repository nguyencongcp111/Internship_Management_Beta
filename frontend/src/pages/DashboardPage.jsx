import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import api from '../api/axios';
import styles from './DashboardPage.module.css';

export default function DashboardPage() {
  const { user } = useAuth();
  const [stats, setStats] = useState(null);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const role = user?.role;
        if (role === 'ROLE_ADMIN') {
          const [users, students, mentors, assignments] = await Promise.all([
            api.get('/admin'),
            api.get('/student'),
            api.get('/mentor'),
            api.get('/internship_assignment'),
          ]);
          setStats({
            users: users.data.data?.length || 0,
            students: students.data.data?.length || 0,
            mentors: mentors.data.data?.length || 0,
            assignments: assignments.data.data?.length || 0,
          });
        }
      } catch (_) {}
    };
    fetchStats();
  }, [user]);

  const greeting = () => {
    const h = new Date().getHours();
    if (h < 12) return 'Chào buổi sáng';
    if (h < 18) return 'Chào buổi chiều';
    return 'Chào buổi tối';
  };

  const statCards = [
    { label: 'Người dùng', value: stats?.users ?? '-', icon: '👥', color: '#1565c0' },
    { label: 'Sinh viên', value: stats?.students ?? '-', icon: '🎓', color: '#00695c' },
    { label: 'Mentor', value: stats?.mentors ?? '-', icon: '👨‍🏫', color: '#6a1b9a' },
    { label: 'Phân công', value: stats?.assignments ?? '-', icon: '📋', color: '#e65100' },
  ];

  return (
    <div className={styles.root}>
      <div className={styles.header}>
        <div>
          <h1 className={styles.greeting}>{greeting()}, {user?.fullName || 'bạn'} 👋</h1>
          <p className={styles.sub}>Chào mừng đến với hệ thống quản lý thực tập</p>
        </div>
        <div className={styles.dateBox}>
          {new Date().toLocaleDateString('vi-VN', {
            weekday: 'long', day: '2-digit', month: '2-digit', year: 'numeric',
          })}
        </div>
      </div>

      {user?.role === 'ROLE_ADMIN' && stats && (
        <div className={styles.statsGrid}>
          {statCards.map((s) => (
            <div key={s.label} className={styles.statCard}>
              <div className={styles.statIcon} style={{ background: s.color + '15', color: s.color }}>
                {s.icon}
              </div>
              <div>
                <div className={styles.statValue}>{s.value}</div>
                <div className={styles.statLabel}>{s.label}</div>
              </div>
            </div>
          ))}
        </div>
      )}

      <div className={styles.infoCard}>
        <h2 className={styles.infoTitle}>📌 Thông tin tài khoản</h2>
        <div className={styles.infoGrid}>
          <div className={styles.infoItem}>
            <span className={styles.infoKey}>Tên đăng nhập</span>
            <span className={styles.infoVal}>{user?.username}</span>
          </div>
          <div className={styles.infoItem}>
            <span className={styles.infoKey}>Họ và tên</span>
            <span className={styles.infoVal}>{user?.fullName}</span>
          </div>
          <div className={styles.infoItem}>
            <span className={styles.infoKey}>Email</span>
            <span className={styles.infoVal}>{user?.email}</span>
          </div>
          <div className={styles.infoItem}>
            <span className={styles.infoKey}>Điện thoại</span>
            <span className={styles.infoVal}>{user?.phoneNumber || '—'}</span>
          </div>
          <div className={styles.infoItem}>
            <span className={styles.infoKey}>Vai trò</span>
            <span className={styles.infoVal}>{user?.role?.replace('ROLE_', '')}</span>
          </div>
          <div className={styles.infoItem}>
            <span className={styles.infoKey}>Trạng thái</span>
            <span className={`${styles.infoVal} ${user?.active ? styles.active : styles.inactive}`}>
              {user?.active ? '✅ Hoạt động' : '❌ Không hoạt động'}
            </span>
          </div>
        </div>
      </div>
    </div>
  );
}
