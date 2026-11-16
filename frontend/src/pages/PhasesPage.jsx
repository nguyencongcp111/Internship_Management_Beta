import { useEffect, useState } from 'react';
import api from '../api/axios';
import styles from './shared.module.css';

function fmtDate(d) {
  if (!d) return '—';
  return new Date(d).toLocaleDateString('vi-VN');
}

const EMPTY_FORM = { phaseName: '', description: '', startDate: '', endDate: '' };

export default function PhasesPage() {
  const [phases, setPhases]   = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError]     = useState('');

  const [modalMode, setModalMode] = useState(null); // 'add' | 'edit'
  const [selected, setSelected]   = useState(null);
  const [form, setForm]           = useState(EMPTY_FORM);
  const [saving, setSaving]       = useState(false);
  const [formError, setFormError] = useState('');

  const [confirmDelete, setConfirmDelete] = useState(null);
  const [deleting, setDeleting]           = useState(false);

  /* ── fetch ── */
  const fetchData = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await api.get('/internship_phases');
      setPhases(res.data?.data || res.data || []);
    } catch (e) {
      setError(e.response?.data?.message || 'Không thể tải danh sách giai đoạn.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchData(); }, []);

  /* ── open modals ── */
  const openAdd = () => {
    setForm(EMPTY_FORM);
    setFormError('');
    setSelected(null);
    setModalMode('add');
  };

  const openEdit = (item) => {
    setSelected(item);
    setForm({
      phaseName:   item.phaseName   || '',
      description: item.description || '',
      startDate:   item.startDate   ? item.startDate.slice(0, 10) : '',
      endDate:     item.endDate     ? item.endDate.slice(0, 10)   : '',
    });
    setFormError('');
    setModalMode('edit');
  };

  const closeModal = () => { setModalMode(null); setSelected(null); };

  /* ── field change ── */
  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  /* ── save ── */
  const handleSave = async (e) => {
    e.preventDefault();
    setSaving(true);
    setFormError('');
    try {
      const payload = {
        phaseName:   form.phaseName,
        description: form.description,
        startDate:   form.startDate,
        endDate:     form.endDate,
      };
      if (modalMode === 'add') {
        await api.post('/internship_phases', payload);
      } else {
        await api.put(`/internship_phases/${selected.phaseId}`, payload);
      }
      closeModal();
      fetchData();
    } catch (e) {
      setFormError(e.response?.data?.message || 'Lưu thất bại. Vui lòng thử lại.');
    } finally {
      setSaving(false);
    }
  };

  /* ── delete ── */
  const handleDelete = async () => {
    if (!confirmDelete) return;
    setDeleting(true);
    try {
      await api.delete(`/internship_phases/${confirmDelete.phaseId}`);
      setConfirmDelete(null);
      fetchData();
    } catch (e) {
      setError(e.response?.data?.message || 'Xóa thất bại.');
      setConfirmDelete(null);
    } finally {
      setDeleting(false);
    }
  };

  /* ── render ── */
  return (
    <div className={styles.root}>
      {/* Header */}
      <div className={styles.header}>
        <h1 className={styles.title}>Giai đoạn thực tập</h1>
        <button className={styles.addBtn} onClick={openAdd}>
          + Thêm giai đoạn
        </button>
      </div>

      {/* Error */}
      {error && <div className={styles.error}>{error}</div>}

      {/* Table */}
      {loading ? (
        <div className={styles.loading}>Đang tải dữ liệu…</div>
      ) : (
        <div className={styles.tableWrap}>
          <table className={styles.table}>
            <thead>
              <tr>
                <th>ID</th>
                <th>Tên giai đoạn</th>
                <th>Mô tả</th>
                <th>Ngày bắt đầu</th>
                <th>Ngày kết thúc</th>
                <th>Hành động</th>
              </tr>
            </thead>
            <tbody>
              {phases.length === 0 ? (
                <tr>
                  <td colSpan={6} className={styles.empty}>
                    Không có giai đoạn nào.
                  </td>
                </tr>
              ) : (
                phases.map((p) => (
                  <tr key={p.phaseId}>
                    <td>{p.phaseId}</td>
                    <td style={{ fontWeight: 600 }}>{p.phaseName}</td>
                    <td style={{ maxWidth: 220, whiteSpace: 'pre-wrap', wordBreak: 'break-word' }}>
                      {p.description || '—'}
                    </td>
                    <td>{fmtDate(p.startDate)}</td>
                    <td>{fmtDate(p.endDate)}</td>
                    <td>
                      <div className={styles.actions}>
                        <button className={styles.editBtn} onClick={() => openEdit(p)}>
                          Sửa
                        </button>
                        <button className={styles.deleteBtn} onClick={() => setConfirmDelete(p)}>
                          Xóa
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      )}

      {/* Add / Edit Modal */}
      {modalMode && (
        <div className={styles.overlay} onClick={(e) => e.target === e.currentTarget && closeModal()}>
          <div className={styles.modal}>
            <h2 className={styles.modalTitle}>
              {modalMode === 'add' ? 'Thêm giai đoạn' : 'Chỉnh sửa giai đoạn'}
            </h2>

            {formError && <div className={styles.error}>{formError}</div>}

            <form onSubmit={handleSave}>
              <div className={styles.formField}>
                <label className={styles.formLabel}>Tên giai đoạn *</label>
                <input
                  className={styles.formInput}
                  type="text"
                  name="phaseName"
                  value={form.phaseName}
                  onChange={handleChange}
                  required
                  placeholder="Nhập tên giai đoạn"
                />
              </div>

              <div className={styles.formField}>
                <label className={styles.formLabel}>Mô tả</label>
                <input
                  className={styles.formInput}
                  type="text"
                  name="description"
                  value={form.description}
                  onChange={handleChange}
                  placeholder="Mô tả (tuỳ chọn)"
                />
              </div>

              <div className={styles.formField}>
                <label className={styles.formLabel}>Ngày bắt đầu *</label>
                <input
                  className={styles.formInput}
                  type="date"
                  name="startDate"
                  value={form.startDate}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className={styles.formField}>
                <label className={styles.formLabel}>Ngày kết thúc *</label>
                <input
                  className={styles.formInput}
                  type="date"
                  name="endDate"
                  value={form.endDate}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className={styles.modalFooter}>
                <button type="button" className={styles.cancelBtn} onClick={closeModal}>
                  Hủy
                </button>
                <button type="submit" className={styles.saveBtn} disabled={saving}>
                  {saving ? 'Đang lưu…' : 'Lưu'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Delete Confirm Modal */}
      {confirmDelete && (
        <div className={styles.overlay} onClick={(e) => e.target === e.currentTarget && setConfirmDelete(null)}>
          <div className={styles.modal} style={{ maxWidth: 400 }}>
            <h2 className={styles.modalTitle}>Xác nhận xóa</h2>
            <p style={{ fontSize: 14, color: '#444', marginBottom: 24, fontFamily: "'Helvetica Neue', Helvetica, Arial, sans-serif" }}>
              Bạn có chắc muốn xóa giai đoạn <strong>"{confirmDelete.phaseName}"</strong>? Hành động này không thể hoàn tác.
            </p>
            <div className={styles.modalFooter}>
              <button className={styles.cancelBtn} onClick={() => setConfirmDelete(null)}>
                Hủy
              </button>
              <button
                className={styles.deleteBtn}
                style={{ padding: '10px 24px', fontSize: 13.5 }}
                onClick={handleDelete}
                disabled={deleting}
              >
                {deleting ? 'Đang xóa…' : 'Xóa'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
