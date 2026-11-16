import React, { useEffect, useState } from 'react';
import styles from './shared.module.css';
import api from '../api/axios';
import profileStyles from './MyStudentProfilePage.module.css';

const EMPTY_FORM = { major: '', className: '', dateOfBirth: '', address: '' };

export default function MyStudentProfilePage() {
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
      const res = await api.get('/student/my-profile');
      const data = res.data?.data ?? res.data;
      setProfile(data);
    } catch (err) {
      setError(err.response?.data?.message || 'Không thể tải hồ sơ sinh viên.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchProfile(); }, []);

  const openEdit = () => {
    if (!profile) return;
    setForm({
      major: profile.major || '',
      className: profile.className || '',
      dateOfBirth: profile.dateOfBirth ? String(profile.dateOfBirth).slice(0, 10) : '',
      address: profile.address || '',
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
      major: form.major.trim(),
      className: form.className.trim(),
      dateOfBirth: form.dateOfBirth || null,
      address: form.address.trim(),
    };
    try {
      await api.put('/student/my-profile', payload);
      setSuccessMsg('Cập nhật hồ sơ sinh viên thành công!');
      closeModal();
      fetchProfile();
    } catch (err) {
      setFormError(err.response?.data?.message || 'Cập nhật thất bại. Vui lòng thử lại.');
    } finally {
      setSaving(false);
    }
  };

  const formatDate = (dateStr) => {
    if (!dateStr) return '—';
    try { return new Date(dateStr).toLocaleDateString('vi-VN'); } catch { return String(dateStr); }
  };

  return (
    <div className={styles.root}>
      <div className={styles.header}>
        <h1 className={styles.title}>Hồ sơ sinh viên của tôi</h1>
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
        <div className={styles.empty}>Không tìm thấy thông tin hồ sơ sinh viên.</div>
      ) : (
        <div className={profileStyles.card}>
          <div className={profileStyles.avatarSection}>
            <div className={profileStyles.avatar}>
              {profile.user?.fullName?.[0] || 'S'}
            </div>
            <div>
              <h2 className={profileStyles.fullName}>{profile.user?.fullName}</h2>
              <p className={profileStyles.subInfo}>MSSV: <strong>{profile.studentCode}</strong></p>
            </div>
          </div>

          <div className={profileStyles.divider} />

          <div className={profileStyles.grid}>
            <div className={profileStyles.field}>
              <span className={profileStyles.label}>Chuyên ngành</span>
              <span className={profileStyles.value}>{profile.major || '—'}</span>
            </div>
            <div className={profileStyles.field}>
              <span className={profileStyles.label}>Lớp</span>
              <span className={profileStyles.value}>{profile.className || '—'}</span>
            </div>
            <div className={profileStyles.field}>
              <span className={profileStyles.label}>Ngày sinh</span>
              <span className={profileStyles.value}>{formatDate(profile.dateOfBirth)}</span>
            </div>
            <div className={profileStyles.field}>
              <span className={profileStyles.label}>Địa chỉ</span>
              <span className={profileStyles.value}>{profile.address || '—'}</span>
            </div>
            <div className={profileStyles.field}>
              <span className={profileStyles.label}>Email liên hệ</span>
              <span className={profileStyles.value}>{profile.user?.email || '—'}</span>
            </div>
            <div className={profileStyles.field}>
              <span className={profileStyles.label}>Số điện thoại</span>
              <span className={profileStyles.value}>{profile.user?.phoneNumber || '—'}</span>
            </div>
          </div>
        </div>
      )}

      {modalOpen && (
        <div className={styles.overlay} onClick={(e) => e.target === e.currentTarget && closeModal()}>
          <div className={styles.modal}>
            <h2 className={styles.modalTitle}>Chỉnh sửa hồ sơ sinh viên</h2>

            {formError && <div className={styles.error}>{formError}</div>}

            <div className={styles.formField}>
              <label className={styles.formLabel}>Chuyên ngành</label>
              <input
                className={styles.formInput}
                name="major"
                value={form.major}
                onChange={handleChange}
                placeholder="VD: Kỹ thuật phần mềm"
              />
            </div>

            <div className={styles.formField}>
              <label className={styles.formLabel}>Lớp</label>
              <input
                className={styles.formInput}
                name="className"
                value={form.className}
                onChange={handleChange}
                placeholder="VD: KTPM-K45"
              />
            </div>

            <div className={styles.formField}>
              <label className={styles.formLabel}>Ngày sinh</label>
              <input
                className={styles.formInput}
                name="dateOfBirth"
                type="date"
                value={form.dateOfBirth}
                onChange={handleChange}
              />
            </div>

            <div className={styles.formField}>
              <label className={styles.formLabel}>Địa chỉ</label>
              <input
                className={styles.formInput}
                name="address"
                value={form.address}
                onChange={handleChange}
                placeholder="VD: Hà Nội, Việt Nam"
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
