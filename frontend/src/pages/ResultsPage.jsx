import React, { useEffect, useState, useCallback } from 'react';
import styles from './shared.module.css';
import api from '../api/axios';

const EMPTY_CREATE = { assignmentId: '', roundCriteriaId: '', score: '', comments: '' };

export default function ResultsPage() {
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState(EMPTY_CREATE);
  const [formError, setFormError] = useState('');
  const [saving, setSaving] = useState(false);

  const [filterAssignmentId, setFilterAssignmentId] = useState('');
  const [filterUserId, setFilterUserId] = useState('');

  const storedUser = localStorage.getItem('user');
  const currentUser = storedUser ? JSON.parse(storedUser) : null;
  const isMentor = currentUser?.role === 'ROLE_MENTOR' || currentUser?.role === 'MENTOR';
  const isAdmin = currentUser?.role === 'ROLE_ADMIN' || currentUser?.role === 'ADMIN';
  const canAdd = isMentor;
  const canEdit = isMentor;

  const fetchResults = useCallback(async () => {
    setLoading(true);
    setError('');
    try {
      const params = {};
      if (filterAssignmentId) params.assignmentId = filterAssignmentId;
      if (filterUserId) params.userId = filterUserId;
      const res = await api.get('/assessment_results', { params });
      const list = res.data?.data ?? res.data ?? [];
      setResults(Array.isArray(list) ? list : []);
    } catch (err) {
      setError(err.response?.data?.message || 'Không thể tải kết quả đánh giá.');
    } finally {
      setLoading(false);
    }
  }, [filterAssignmentId, filterUserId]);

  useEffect(() => { fetchResults(); }, [fetchResults]);

  const openCreate = () => {
    setEditing(null);
    setForm(EMPTY_CREATE);
    setFormError('');
    setModalOpen(true);
  };

  const openEdit = (item) => {
    setEditing(item);
    setForm({ score: item.score ?? '', comments: item.comments || '' });
    setFormError('');
    setModalOpen(true);
  };

  const closeModal = () => { setModalOpen(false); setFormError(''); };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSave = async () => {
    if (!editing) {
      if (!form.assignmentId || isNaN(Number(form.assignmentId))) { setFormError('ID Phân công phải là số hợp lệ.'); return; }
      if (!form.roundCriteriaId || isNaN(Number(form.roundCriteriaId))) { setFormError('ID Tiêu chí vòng phải là số hợp lệ.'); return; }
    }
    if (form.score === '' || isNaN(Number(form.score))) { setFormError('Điểm phải là số hợp lệ.'); return; }

    setSaving(true);
    setFormError('');
    try {
      if (editing) {
        const payload = { score: Number(form.score), comments: form.comments };
        await api.put(`/assessment_results/${editing.id}`, payload);
      } else {
        const payload = {
          assignmentId: parseInt(form.assignmentId, 10),
          roundCriteriaId: parseInt(form.roundCriteriaId, 10),
          score: Number(form.score),
          comments: form.comments,
        };
        await api.post('/assessment_results', payload);
      }
      closeModal();
      fetchResults();
    } catch (err) {
      setFormError(err.response?.data?.message || 'Lưu thất bại. Vui lòng thử lại.');
    } finally {
      setSaving(false);
    }
  };

  const formatDate = (dateStr) => {
    if (!dateStr) return '—';
    try { return new Date(dateStr).toLocaleDateString('vi-VN'); } catch { return dateStr; }
  };

  return (
    <div className={styles.root}>
      <div className={styles.header}>
        <h1 className={styles.title}>Kết quả đánh giá thực tập</h1>
        {canAdd && (
          <button className={styles.addBtn} onClick={openCreate}>
            + Chấm điểm đánh giá
          </button>
        )}
      </div>

      {/* Filter bar */}
      <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap', alignItems: 'center' }}>
        <input
          className={styles.formInput}
          style={{ maxWidth: 190 }}
          placeholder="Lọc theo ID Phân công"
          value={filterAssignmentId}
          onChange={(e) => setFilterAssignmentId(e.target.value)}
          type="number"
          min={1}
        />
        <input
          className={styles.formInput}
          style={{ maxWidth: 180 }}
          placeholder="Lọc theo ID Người dùng"
          value={filterUserId}
          onChange={(e) => setFilterUserId(e.target.value)}
          type="number"
          min={1}
        />
        <button
          className={styles.editBtn}
          onClick={fetchResults}
          style={{ padding: '9px 18px' }}
        >
          🔍 Tìm kiếm
        </button>
        {(filterAssignmentId || filterUserId) && (
          <button
            className={styles.deleteBtn}
            onClick={() => { setFilterAssignmentId(''); setFilterUserId(''); }}
            style={{ padding: '9px 16px' }}
          >
            ✕ Xóa lọc
          </button>
        )}
      </div>

      {error && <div className={styles.error}>{error}</div>}

      {loading ? (
        <div className={styles.loading}>Đang tải kết quả…</div>
      ) : (
        <div className={styles.tableWrap}>
          <table className={styles.table}>
            <thead>
              <tr>
                <th>ID</th>
                <th>Phân công</th>
                <th>Tiêu chí</th>
                <th>Vòng đánh giá</th>
                <th>Điểm số</th>
                <th>Nhận xét</th>
                <th>Người đánh giá</th>
                <th>Ngày đánh giá</th>
                {canEdit && <th>Hành động</th>}
              </tr>
            </thead>
            <tbody>
              {results.length === 0 ? (
                <tr>
                  <td colSpan={canEdit ? 9 : 8} className={styles.empty}>
                    Không tìm thấy kết quả đánh giá nào.
                  </td>
                </tr>
              ) : (
                results.map((r) => (
                  <tr key={r.id}>
                    <td>{r.id}</td>
                    <td>
                      <span className={styles.badge} style={{ background: '#ede7f6', color: '#512da8' }}>
                        Phân công #{r.assignment?.id || r.assignmentId}
                      </span>
                    </td>
                    <td style={{ fontWeight: 600 }}>
                      {r.roundCriteria?.criteria?.criteriaName || `Tiêu chí #${r.roundCriteriaId || '—'}`}
                    </td>
                    <td>{r.roundCriteria?.round?.roundName || '—'}</td>
                    <td>
                      <span
                        className={styles.badge}
                        style={{
                          background: r.score >= 80 ? '#e8f5e9' : r.score >= 50 ? '#fff8e1' : '#ffebee',
                          color: r.score >= 80 ? '#2e7d32' : r.score >= 50 ? '#f57f17' : '#c62828',
                          fontWeight: 700,
                          fontSize: 13,
                        }}
                      >
                        {r.score}
                      </span>
                    </td>
                    <td style={{ color: '#444', maxWidth: 220 }}>{r.comments || '—'}</td>
                    <td>{r.evaluatedBy?.fullName || '—'}</td>
                    <td>{formatDate(r.evaluatedAt || r.createAt)}</td>
                    {canEdit && (
                      <td>
                        <button className={styles.editBtn} onClick={() => openEdit(r)}>
                          Sửa điểm
                        </button>
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
              {editing ? 'Cập nhật điểm đánh giá' : 'Thêm kết quả đánh giá'}
            </h2>

            {formError && <div className={styles.error}>{formError}</div>}

            {!editing && (
              <>
                <div className={styles.formField}>
                  <label className={styles.formLabel}>ID Phân công *</label>
                  <input
                    className={styles.formInput}
                    name="assignmentId"
                    type="number"
                    value={form.assignmentId}
                    onChange={handleChange}
                    placeholder="VD: 1"
                  />
                </div>
                <div className={styles.formField}>
                  <label className={styles.formLabel}>ID Tiêu chí vòng (RoundCriteria ID) *</label>
                  <input
                    className={styles.formInput}
                    name="roundCriteriaId"
                    type="number"
                    value={form.roundCriteriaId}
                    onChange={handleChange}
                    placeholder="VD: 1"
                  />
                </div>
              </>
            )}

            <div className={styles.formField}>
              <label className={styles.formLabel}>Điểm số *</label>
              <input
                className={styles.formInput}
                name="score"
                type="number"
                step="0.5"
                min="0"
                value={form.score}
                onChange={handleChange}
                placeholder="VD: 85"
              />
            </div>

            <div className={styles.formField}>
              <label className={styles.formLabel}>Nhận xét</label>
              <textarea
                className={styles.formInput}
                name="comments"
                value={form.comments}
                onChange={handleChange}
                rows={3}
                placeholder="Nhận xét chi tiết về kết quả của sinh viên…"
                style={{ resize: 'vertical' }}
              />
            </div>

            <div className={styles.modalFooter}>
              <button className={styles.cancelBtn} onClick={closeModal} disabled={saving}>Hủy</button>
              <button className={styles.saveBtn} onClick={handleSave} disabled={saving}>
                {saving ? 'Đang lưu…' : 'Lưu kết quả'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
