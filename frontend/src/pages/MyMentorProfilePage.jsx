import React, { useEffect, useState } from 'react';
import styles from './shared.module.css';
import api from '../api/axios';
import profileStyles from './MyMentorProfilePage.module.css';

const EMPTY_FORM = { department: '', academicRank: '' };

export default function MyMentorProfilePage() {
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [modalOpen, setModalOpen] = useState(false);
  const [form, setForm] = useState(EMPTY_FORM);
  const [formError, setFormError] = useState('');
  const [saving, setSaving] = useState(false);
  const [successMsg, setSuccessMsg] = useState('');

  const fetchProfile = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await api.get('/mentor/my-profile');
      const data = res.data?.data ?? res.data;
      setProfile(data);
    } catch (err) {
      setError(err.response?.data?.message || 'Không thể tải hồ sơ mentor.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchProfile(); }, []);

  const openEdit = () => {
    if (!profile) return;
    setForm({
      department: profile.department || '',
      academicRank: profile.academicRank || '',
    });
    setFormError('');
    setSuccessMsg('');
    setModalOpen(true);
  };

  const closeModal = () => { setModalOpen(false); setFormError(''); };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSave = async () => {
    setSaving(true);
    setFormError('');
    const payload = {
      department: form.department.trim(),
      academicRank: form.academicRank.trim(),
    };
    try {
      await api.put('/mentor/my-profile', payload);
      setSuccessMsg('Cập nhật hồ sơ mentor thành công!');
      closeModal();
      fetchProfile();
    } catch (err) {
      setFormError(err.response?.data?.message || 'Cập nhật thất bại. Vui lòng thử lại.');
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className={styles.root}>
      <div className={styles.header}>
        <h1 className={styles.title}>Hồ sơ Mentor của tôi</h1>
        {profile && (
          <button className={styles.addBtn} onClick={openEdit}>
            ✏️ Chỉnh sửa hồ sơ
          </button>
        )}
      </div>

      {successMsg && (
        <div style={{ background: '#e8f5e9', color: '#2e7d32', border: '1px solid #a5d6a7', borderRadius: 8, padding: '10px 16px', fontSize: 13.5 }}>
          {successMsg}
        </div>
      )}

      {error && <div className={styles.error}>{error}</div>}

      {loading ? (
        <div className={styles.loading}>Đang tải hồ sơ…</div>
      ) : !profile ? (
        <div className={styles.empty}>Không tìm thấy thông tin hồ sơ mentor.</div>
      ) : (
        <div className={profileStyles.card}>
          <div className={profileStyles.avatarSection}>
            <div className={profileStyles.avatar}>
              {profile.user?.fullName?.[0] || 'M'}
            </div>
            <div>
              <h2 className={profileStyles.fullName}>{profile.user?.fullName}</h2>
              <p className={profileStyles.subInfo}>Mentor ID: <strong>#{profile.mentorId}</strong></p>
            </div>
          </div>

          <div className={profileStyles.divider} />

          <div className={profileStyles.grid}>
            <div className={profileStyles.field}>
              <span className={profileStyles.label}>Phòng ban / Khoa</span>
              <span className={profileStyles.value}>{profile.department || '—'}</span>
            </div>
            <div className={profileStyles.field}>
              <span className={profileStyles.label}>Học hàm / Học vị / Chức vụ</span>
              <span className={profileStyles.value}>{profile.academicRank || '—'}</span>
            </div>
            <div className={profileStyles.field}>
              <span className={profileStyles.label}>Email liên hệ</span>
              <span className={profileStyles.value}>{profile.user?.email || '—'}</span>
            </div>
            <div className={profileStyles.field}>
              <span className={profileStyles.label}>Số điện thoại</span>
              <span className={profileStyles.value}>{profile.user?.phoneNumber || '—'}</span>
            </div>
            <div className={profileStyles.field}>
              <span className={profileStyles.label}>Tên tài khoản</span>
              <span className={profileStyles.value}>{profile.user?.username || '—'}</span>
            </div>
          </div>
        </div>
      )}

      {modalOpen && (
        <div className={styles.overlay} onClick={(e) => e.target === e.currentTarget && closeModal()}>
          <div className={styles.modal}>
            <h2 className={styles.modalTitle}>Chỉnh sửa hồ sơ Mentor</h2>

            {formError && <div className={styles.error}>{formError}</div>}

            <div className={styles.formField}>
              <label className={styles.formLabel}>Phòng ban / Khoa</label>
              <input
                className={styles.formInput}
                name="department"
                value={form.department}
                onChange={handleChange}
                placeholder="VD: Khoa Công nghệ thông tin"
              />
            </div>

            <div className={styles.formField}>
              <label className={styles.formLabel}>Học hàm / Học vị / Chức vụ</label>
              <input
                className={styles.formInput}
                name="academicRank"
                value={form.academicRank}
                onChange={handleChange}
                placeholder="VD: Thạc sĩ / Giảng viên chính"
              />
            </div>

            <div className={styles.modalFooter}>
              <button className={styles.cancelBtn} onClick={closeModal} disabled={saving}>Hủy</button>
              <button className={styles.saveBtn} onClick={handleSave} disabled={saving}>
                {saving ? 'Đang lưu…' : 'Cập nhật'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
