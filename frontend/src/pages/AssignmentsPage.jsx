import { useEffect, useState } from 'react';
import api from '../api/axios';
import styles from './shared.module.css';

const STATUS_OPTIONS = ['ACTIVE', 'INACTIVE', 'COMPLETED', 'PENDING'];

function getUser() {
  try {
    return JSON.parse(localStorage.getItem('user')) || {};
  } catch {
    return {};
  }
}

function statusBadgeClass(status) {
  switch ((status || '').toUpperCase()) {
    case 'ACTIVE':     return styles.badgeActive;
    case 'INACTIVE':   return styles.badgeInactive;
    case 'COMPLETED':  return styles.badgeAdmin;
    case 'PENDING':    return styles.badgeMentor;
    default:           return styles.badgeStudent;
  }
}

function fmtDate(d) {
  if (!d) return '—';
  try {
    return new Date(d).toLocaleDateString('vi-VN');
  } catch {
    return String(d);
  }
}

const EMPTY_FORM = {
  studentId: '',
  mentorId: '',
  phaseId: '',
  startDate: '',
  endDate: '',
  status: 'ACTIVE',
  notes: '',
};

export default function AssignmentsPage() {
  const [assignments, setAssignments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError]     = useState('');

  const [modalMode, setModalMode] = useState(null); // 'add' | 'edit'
  const [selected, setSelected]   = useState(null);
  const [form, setForm]           = useState(EMPTY_FORM);
  const [saving, setSaving]       = useState(false);
  const [formError, setFormError] = useState('');

  const currentUser = getUser();
  const isAdmin = currentUser?.role === 'ROLE_ADMIN' || currentUser?.role === 'ADMIN';

  /* ── fetch ── */
  const fetchData = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await api.get('/internship_assignment');
      const raw = res.data?.data ?? res.data;
      if (Array.isArray(raw)) {
        setAssignments(raw);
      } else {
        setAssignments([]);
      }
    } catch (e) {
      const msg =
        e.response?.data?.message ||
        e.message ||
        'Không thể tải dữ liệu phân công (lỗi dữ liệu máy chủ).';
      setError(msg);
      setAssignments([]);
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
      startDate: item.assignmentTime ? String(item.assignmentTime).slice(0, 10) : (item.startDate ? String(item.startDate).slice(0, 10) : ''),
      endDate:   item.endDate ? String(item.endDate).slice(0, 10) : '',
      status:    item.status || 'ACTIVE',
      notes:     item.notes || '',
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
      if (modalMode === 'add') {
        const payload = {
          studentId: parseInt(form.studentId, 10),
          mentorId:  parseInt(form.mentorId,  10),
          phaseId:   parseInt(form.phaseId,   10),
          startDate: form.startDate,
          endDate:   form.endDate,
          status:    form.status,
          notes:     form.notes,
        };
        await api.post('/internship_assignment', payload);
      } else {
        const payload = {
          startDate: form.startDate,
          endDate:   form.endDate,
          status:    form.status,
          notes:     form.notes,
        };
        const id = selected.assignmentId || selected.id;
        await api.put(`/internship_assignment/${id}`, payload);
      }
      closeModal();
      fetchData();
    } catch (e) {
      setFormError(e.response?.data?.message || 'Lưu thất bại. Vui lòng thử lại.');
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className={styles.root}>
      {/* Header */}
      <div className={styles.header}>
        <h1 className={styles.title}>Phân công thực tập</h1>
        {isAdmin && (
          <button className={styles.addBtn} onClick={openAdd}>
            + Thêm phân công
          </button>
        )}
      </div>

      {/* Error alert with retry button */}
      {error && (
        <div className={styles.error} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <span>⚠️ {error}</span>
          <button
            onClick={fetchData}
            style={{
              background: '#c62828',
              color: '#fff',
              border: 'none',
              borderRadius: 6,
              padding: '4px 10px',
              cursor: 'pointer',
              fontSize: 12,
            }}
          >
            Thử lại
          </button>
        </div>
      )}

      {/* Table */}
      {loading ? (
        <div className={styles.loading}>Đang tải dữ liệu phân công…</div>
      ) : (
        <div className={styles.tableWrap}>
          <table className={styles.table}>
            <thead>
              <tr>
                <th>ID</th>
                <th>Sinh viên</th>
                <th>Mentor</th>
                <th>Giai đoạn</th>
                <th>Thời gian phân công</th>
                <th>Trạng thái</th>
                {isAdmin && <th>Hành động</th>}
              </tr>
            </thead>
            <tbody>
              {(!assignments || assignments.length === 0) ? (
                <tr>
                  <td colSpan={isAdmin ? 7 : 6} className={styles.empty}>
                    Không có dữ liệu phân công nào.
                  </td>
                </tr>
              ) : (
                assignments.map((a, idx) => {
                  const id = a.assignmentId ?? a.id ?? (idx + 1);
                  const studentName = a.student?.user?.fullName || a.student?.studentCode || '—';
                  const studentCode = a.student?.studentCode || '';
                  const mentorName = a.mentor?.user?.fullName || a.mentor?.department || '—';
                  const phaseName = a.internshipPhase?.phaseName || a.phase?.phaseName || '—';
                  const time = a.assignmentTime || a.startDate;

                  return (
                    <tr key={id}>
                      <td>#{id}</td>
                      <td>
                        <div style={{ fontWeight: 600 }}>{studentName}</div>
                        {studentCode && (
                          <div style={{ fontSize: 11.5, color: '#666' }}>MSSV: {studentCode}</div>
                        )}
                      </td>
                      <td>{mentorName}</td>
                      <td>
                        <span className={styles.badge} style={{ background: '#e3f2fd', color: '#0d47a1' }}>
                          {phaseName}
                        </span>
                      </td>
                      <td>{fmtDate(time)}</td>
                      <td>
                        <span className={`${styles.badge} ${statusBadgeClass(a.status)}`}>
                          {a.status || '—'}
                        </span>
                      </td>
                      {isAdmin && (
                        <td>
                          <div className={styles.actions}>
                            <button className={styles.editBtn} onClick={() => openEdit(a)}>
                              Sửa
                            </button>
                          </div>
                        </td>
                      )}
                    </tr>
                  );
                })
              )}
            </tbody>
          </table>
        </div>
      )}

      {/* Modal */}
      {modalMode && (
        <div className={styles.overlay} onClick={(e) => e.target === e.currentTarget && closeModal()}>
          <div className={styles.modal}>
            <h2 className={styles.modalTitle}>
              {modalMode === 'add' ? 'Thêm phân công thực tập' : 'Sửa phân công thực tập'}
            </h2>

            {formError && <div className={styles.error}>{formError}</div>}

            <form onSubmit={handleSave}>
              {modalMode === 'add' && (
                <>
                  <div className={styles.formField}>
                    <label className={styles.formLabel}>ID Sinh viên *</label>
                    <input
                      className={styles.formInput}
                      type="number"
                      name="studentId"
                      value={form.studentId}
                      onChange={handleChange}
                      placeholder="VD: 1"
                      required
                    />
                  </div>

                  <div className={styles.formField}>
                    <label className={styles.formLabel}>ID Mentor *</label>
                    <input
                      className={styles.formInput}
                      type="number"
                      name="mentorId"
                      value={form.mentorId}
                      onChange={handleChange}
                      placeholder="VD: 1"
                      required
                    />
                  </div>

                  <div className={styles.formField}>
                    <label className={styles.formLabel}>ID Giai đoạn *</label>
                    <input
                      className={styles.formInput}
                      type="number"
                      name="phaseId"
                      value={form.phaseId}
                      onChange={handleChange}
                      placeholder="VD: 1"
                      required
                    />
                  </div>
                </>
              )}

              <div className={styles.formField}>
                <label className={styles.formLabel}>Thời gian bắt đầu</label>
                <input
                  className={styles.formInput}
                  type="date"
                  name="startDate"
                  value={form.startDate}
                  onChange={handleChange}
                />
              </div>

              <div className={styles.formField}>
                <label className={styles.formLabel}>Trạng thái *</label>
                <select
                  className={styles.formSelect}
                  name="status"
                  value={form.status}
                  onChange={handleChange}
                >
                  {STATUS_OPTIONS.map((st) => (
                    <option key={st} value={st}>
                      {st}
                    </option>
                  ))}
                </select>
              </div>

              <div className={styles.modalFooter}>
                <button type="button" className={styles.cancelBtn} onClick={closeModal} disabled={saving}>
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
    </div>
  );
}
