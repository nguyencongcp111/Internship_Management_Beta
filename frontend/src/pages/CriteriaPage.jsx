import React, { useEffect, useState, useCallback } from 'react';
import styles from './shared.module.css';
import api from '../api/axios';

const EMPTY_FORM = { criteriaName: '', criteriaDescription: '', maxScore: '' };

export default function CriteriaPage() {
  const [criteria, setCriteria] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState(EMPTY_FORM);
  const [formError, setFormError] = useState('');
  const [saving, setSaving] = useState(false);

  const storedUser = localStorage.getItem('user');
  const currentUser = storedUser ? JSON.parse(storedUser) : null;
  const isAdmin = currentUser?.role === 'ROLE_ADMIN' || currentUser?.role === 'ADMIN';

  const fetchCriteria = useCallback(async () => {
    setLoading(true);
    setError('');
    try {
      const res = await api.get('/evaluation_criteria');
      const list = res.data?.data ?? res.data ?? [];
      setCriteria(Array.isArray(list) ? list : []);
    } catch (err) {
      setError(err.response?.data?.message || 'Không thể tải danh sách tiêu chí.');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => { fetchCriteria(); }, [fetchCriteria]);

  const openCreate = () => {
    setEditing(null);
    setForm(EMPTY_FORM);
    setFormError('');
    setModalOpen(true);
  };

  const openEdit = (item) => {
    setEditing(item);
    setForm({
      criteriaName: item.criteriaName || '',
      criteriaDescription: item.criteriaDescription || '',
      maxScore: item.maxScore ?? '',
    });
    setFormError('');
    setModalOpen(true);
  };

  const closeModal = () => { setModalOpen(false); setFormError(''); };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSave = async () => {
    if (!form.criteriaName.trim()) { setFormError('Tên tiêu chí không được để trống.'); return; }
    if (form.maxScore === '' || isNaN(Number(form.maxScore))) { setFormError('Điểm tối đa phải là số.'); return; }

    setSaving(true);
    setFormError('');
    const payload = {
      criteriaName: form.criteriaName.trim(),
      criteriaDescription: form.criteriaDescription.trim(),
      maxScore: Number(form.maxScore),
    };
    try {
      if (editing) {
        await api.put(`/evaluation_criteria/${editing.criteriaId}`, payload);
      } else {
        await api.post('/evaluation_criteria', payload);
      }
      closeModal();
      fetchCriteria();
    } catch (err) {
      setFormError(err.response?.data?.message || 'Lưu thất bại. Vui lòng thử lại.');
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Bạn có chắc muốn xóa tiêu chí này?')) return;
    try {
      await api.delete(`/evaluation_criteria/${id}`);
      fetchCriteria();
    } catch (err) {
      alert(err.response?.data?.message || 'Xóa thất bại.');
    }
  };

  return (
    <div className={styles.root}>
      <div className={styles.header}>
        <h1 className={styles.title}>Tiêu chí đánh giá</h1>
        {isAdmin && (
          <button className={styles.addBtn} onClick={openCreate}>
            + Thêm tiêu chí
          </button>
        )}
      </div>

      {error && <div className={styles.error}>{error}</div>}

      {loading ? (
        <div className={styles.loading}>Đang tải tiêu chí…</div>
      ) : (
        <div className={styles.tableWrap}>
          <table className={styles.table}>
            <thead>
              <tr>
                <th>ID</th>
                <th>Tên tiêu chí</th>
                <th>Mô tả</th>
                <th>Điểm tối đa</th>
                {isAdmin && <th>Hành động</th>}
              </tr>
            </thead>
            <tbody>
              {criteria.length === 0 ? (
                <tr>
                  <td colSpan={isAdmin ? 5 : 4} className={styles.empty}>
                    Chưa có tiêu chí đánh giá nào.
                  </td>
                </tr>
              ) : (
                criteria.map((item) => (
                  <tr key={item.criteriaId}>
                    <td>{item.criteriaId}</td>
                    <td style={{ fontWeight: 600 }}>{item.criteriaName}</td>
                    <td style={{ color: '#444', maxWidth: 320 }}>{item.criteriaDescription || '—'}</td>
                    <td>
                      <span className={styles.badge} style={{ background: '#e8f0fe', color: '#1565c0' }}>
                        {item.maxScore}
                      </span>
                    </td>
                    {isAdmin && (
                      <td>
                        <div className={styles.actions}>
                          <button className={styles.editBtn} onClick={() => openEdit(item)}>Sửa</button>
                          <button className={styles.deleteBtn} onClick={() => handleDelete(item.criteriaId)}>Xóa</button>
                        </div>
                      </td>
                    )}
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      )}

      {modalOpen && (
        <div className={styles.overlay} onClick={(e) => e.target === e.currentTarget && closeModal()}>
          <div className={styles.modal}>
            <h2 className={styles.modalTitle}>
              {editing ? 'Sửa tiêu chí' : 'Thêm tiêu chí mới'}
            </h2>

            {formError && <div className={styles.error}>{formError}</div>}

            <div className={styles.formField}>
              <label className={styles.formLabel}>Tên tiêu chí *</label>
              <input
                className={styles.formInput}
                name="criteriaName"
                value={form.criteriaName}
                onChange={handleChange}
                placeholder="VD: Kỹ năng chuyên môn"
              />
            </div>

            <div className={styles.formField}>
              <label className={styles.formLabel}>Mô tả</label>
              <textarea
                className={styles.formInput}
                name="criteriaDescription"
                value={form.criteriaDescription}
                onChange={handleChange}
                rows={3}
                placeholder="Mô tả nội dung tiêu chí…"
                style={{ resize: 'vertical' }}
              />
            </div>

            <div className={styles.formField}>
              <label className={styles.formLabel}>Điểm tối đa *</label>
              <input
                className={styles.formInput}
                name="maxScore"
                type="number"
                min={0}
                value={form.maxScore}
                onChange={handleChange}
                placeholder="VD: 100"
              />
            </div>

            <div className={styles.modalFooter}>
              <button className={styles.cancelBtn} onClick={closeModal} disabled={saving}>Hủy</button>
              <button className={styles.saveBtn} onClick={handleSave} disabled={saving}>
                {saving ? 'Đang lưu…' : 'Lưu'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
