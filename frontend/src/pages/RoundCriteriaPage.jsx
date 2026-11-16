import React, { useEffect, useState, useCallback } from 'react';
import styles from './shared.module.css';
import api from '../api/axios';

const EMPTY_FORM = { roundId: '', criteriaId: '', weight: '', isRequired: false };

export default function RoundCriteriaPage() {
  const [roundCriteria, setRoundCriteria] = useState([]);
  const [rounds, setRounds] = useState([]);
  const [criteriaList, setCriteriaList] = useState([]);
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

  const fetchData = useCallback(async () => {
    setLoading(true);
    setError('');
    try {
      const [rcRes, rRes, cRes] = await Promise.allSettled([
        api.get('/round_criteria'),
        api.get('/assessment_rounds'),
        api.get('/evaluation_criteria'),
      ]);

      if (rcRes.status === 'fulfilled') {
        const list = rcRes.value.data?.data ?? rcRes.value.data ?? [];
        setRoundCriteria(Array.isArray(list) ? list : []);
      }
      if (rRes.status === 'fulfilled') {
        const rList = rRes.value.data?.data ?? rRes.value.data ?? [];
        setRounds(Array.isArray(rList) ? rList : []);
      }
      if (cRes.status === 'fulfilled') {
        const cList = cRes.value.data?.data ?? cRes.value.data ?? [];
        setCriteriaList(Array.isArray(cList) ? cList : []);
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Không thể tải dữ liệu tiêu chí vòng.');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => { fetchData(); }, [fetchData]);

  const openCreate = () => {
    setEditing(null);
    setForm(EMPTY_FORM);
    setFormError('');
    setModalOpen(true);
  };

  const openEdit = (item) => {
    setEditing(item);
    setForm({
      roundId: item.round?.roundId ?? '',
      criteriaId: item.criteria?.criteriaId ?? '',
      weight: item.weight ?? '',
      isRequired: !!item.isRequired,
    });
    setFormError('');
    setModalOpen(true);
  };

  const closeModal = () => { setModalOpen(false); setFormError(''); };

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm((prev) => ({ ...prev, [name]: type === 'checkbox' ? checked : value }));
  };

  const validate = () => {
    if (!form.roundId || isNaN(Number(form.roundId))) return 'Vui lòng chọn hoặc nhập ID Vòng đánh giá hợp lệ.';
    if (!form.criteriaId || isNaN(Number(form.criteriaId))) return 'Vui lòng chọn hoặc nhập ID Tiêu chí hợp lệ.';
    const w = Number(form.weight);
    if (form.weight === '' || isNaN(w) || w < 0 || w > 100) return 'Trọng số phải là số từ 0 đến 100.';
    return '';
  };

  const handleSave = async () => {
    const err = validate();
    if (err) { setFormError(err); return; }

    setSaving(true);
    setFormError('');
    const payload = {
      roundId: parseInt(form.roundId, 10),
      criteriaId: parseInt(form.criteriaId, 10),
      weight: Number(form.weight),
      isRequired: form.isRequired,
    };
    try {
      if (editing) {
        await api.put(`/round_criteria/${editing.id}`, payload);
      } else {
        await api.post('/round_criteria', payload);
      }
      closeModal();
      fetchData();
    } catch (err) {
      setFormError(err.response?.data?.message || 'Lưu thất bại. Vui lòng thử lại.');
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Bạn có chắc muốn xóa cấu hình tiêu chí vòng này?')) return;
    try {
      await api.delete(`/round_criteria/${id}`);
      fetchData();
    } catch (err) {
      alert(err.response?.data?.message || 'Xóa thất bại.');
    }
  };

  return (
    <div className={styles.root}>
      <div className={styles.header}>
        <h1 className={styles.title}>Tiêu chí theo vòng đánh giá</h1>
        {isAdmin && (
          <button className={styles.addBtn} onClick={openCreate}>
            + Thêm phân bổ tiêu chí
          </button>
        )}
      </div>

      {error && <div className={styles.error}>{error}</div>}

      {loading ? (
        <div className={styles.loading}>Đang tải danh sách…</div>
      ) : (
        <div className={styles.tableWrap}>
          <table className={styles.table}>
            <thead>
              <tr>
                <th>ID</th>
                <th>Vòng đánh giá</th>
                <th>Tiêu chí</th>
                <th>Trọng số (%)</th>
                <th>Bắt buộc</th>
                {isAdmin && <th>Hành động</th>}
              </tr>
            </thead>
            <tbody>
              {roundCriteria.length === 0 ? (
                <tr>
                  <td colSpan={isAdmin ? 6 : 5} className={styles.empty}>
                    Chưa có cấu hình tiêu chí vòng nào.
                  </td>
                </tr>
              ) : (
                roundCriteria.map((item) => (
                  <tr key={item.id}>
                    <td>{item.id}</td>
                    <td style={{ fontWeight: 600 }}>
                      {item.round?.roundName || `Vòng #${item.round?.roundId}`}
                    </td>
                    <td>{item.criteria?.criteriaName || `Tiêu chí #${item.criteria?.criteriaId}`}</td>
                    <td>
                      <span className={styles.badge} style={{ background: '#e3f2fd', color: '#0d47a1' }}>
                        {item.weight}%
                      </span>
                    </td>
                    <td>
                      <span className={`${styles.badge} ${item.isRequired ? styles.badgeActive : styles.badgeInactive}`}>
                        {item.isRequired ? 'Bắt buộc' : 'Tùy chọn'}
                      </span>
                    </td>
                    {isAdmin && (
                      <td>
                        <div className={styles.actions}>
                          <button className={styles.editBtn} onClick={() => openEdit(item)}>Sửa</button>
                          <button className={styles.deleteBtn} onClick={() => handleDelete(item.id)}>Xóa</button>
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
              {editing ? 'Sửa tiêu chí vòng' : 'Thêm tiêu chí vòng'}
            </h2>

            {formError && <div className={styles.error}>{formError}</div>}

            <div className={styles.formField}>
              <label className={styles.formLabel}>Vòng đánh giá *</label>
              {rounds.length > 0 ? (
                <select
                  className={styles.formSelect}
                  name="roundId"
                  value={form.roundId}
                  onChange={handleChange}
                >
                  <option value="">-- Chọn vòng đánh giá --</option>
                  {rounds.map((r) => (
                    <option key={r.roundId} value={r.roundId}>
                      #{r.roundId} - {r.roundName}
                    </option>
                  ))}
                </select>
              ) : (
                <input
                  className={styles.formInput}
                  name="roundId"
                  type="number"
                  placeholder="ID Vòng đánh giá"
                  value={form.roundId}
                  onChange={handleChange}
                />
              )}
            </div>

            <div className={styles.formField}>
              <label className={styles.formLabel}>Tiêu chí đánh giá *</label>
              {criteriaList.length > 0 ? (
                <select
                  className={styles.formSelect}
                  name="criteriaId"
                  value={form.criteriaId}
                  onChange={handleChange}
                >
                  <option value="">-- Chọn tiêu chí --</option>
                  {criteriaList.map((c) => (
                    <option key={c.criteriaId} value={c.criteriaId}>
                      #{c.criteriaId} - {c.criteriaName}
                    </option>
                  ))}
                </select>
              ) : (
                <input
                  className={styles.formInput}
                  name="criteriaId"
                  type="number"
                  placeholder="ID Tiêu chí"
                  value={form.criteriaId}
                  onChange={handleChange}
                />
              )}
            </div>

            <div className={styles.formField}>
              <label className={styles.formLabel}>Trọng số (%) *</label>
              <input
                className={styles.formInput}
                name="weight"
                type="number"
                min={0}
                max={100}
                placeholder="VD: 30"
                value={form.weight}
                onChange={handleChange}
              />
            </div>

            <div className={styles.formField} style={{ flexDirection: 'row', alignItems: 'center', gap: 10 }}>
              <input
                type="checkbox"
                id="isRequiredCheck"
                name="isRequired"
                checked={form.isRequired}
                onChange={handleChange}
                style={{ width: 18, height: 18, cursor: 'pointer' }}
              />
              <label htmlFor="isRequiredCheck" className={styles.formLabel} style={{ cursor: 'pointer', margin: 0 }}>
                Tiêu chí bắt buộc
              </label>
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
