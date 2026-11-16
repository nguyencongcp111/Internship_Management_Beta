import { useEffect, useState } from 'react';
import api from '../api/axios';
import styles from './shared.module.css';

/* ─── Create Modal ─── */
function CreateModal({ onClose, onCreated }) {
  const [form, setForm] = useState({
    studentCode: '',
    major: '',
    className: '',
    dateOfBirth: '',
    address: '',
    userId: '',
  });
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  const handle = (k) => (e) => setForm((f) => ({ ...f, [k]: e.target.value }));

  const submit = async () => {
    if (!form.studentCode || !form.userId) {
      setError('Student Code and User ID are required.');
      return;
    }
    setSaving(true);
    setError('');
    try {
      await api.post('/student', { ...form, userId: parseInt(form.userId, 10) });
      onCreated();
    } catch (err) {
      setError(err?.response?.data?.message || 'Failed to create student.');
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className={styles.overlay} onClick={(e) => e.target === e.currentTarget && onClose()}>
      <div className={styles.modal}>
        <h2 className={styles.modalTitle}>Add New Student</h2>
        {error && <div className={styles.error}>{error}</div>}

        {[
          { label: 'Student Code *', key: 'studentCode', type: 'text' },
          { label: 'Major', key: 'major', type: 'text' },
          { label: 'Class Name', key: 'className', type: 'text' },
          { label: 'Date of Birth', key: 'dateOfBirth', type: 'date' },
          { label: 'Address', key: 'address', type: 'text' },
          { label: 'User ID *', key: 'userId', type: 'number' },
        ].map(({ label, key, type }) => (
          <div className={styles.formField} key={key}>
            <label className={styles.formLabel}>{label}</label>
            <input
              className={styles.formInput}
              type={type}
              value={form[key]}
              onChange={handle(key)}
              min={type === 'number' ? 1 : undefined}
            />
          </div>
        ))}

        <div className={styles.modalFooter}>
          <button className={styles.cancelBtn} onClick={onClose}>Cancel</button>
          <button className={styles.saveBtn} onClick={submit} disabled={saving}>
            {saving ? 'Creating…' : 'Create Student'}
          </button>
        </div>
      </div>
    </div>
  );
}

/* ─── Edit Modal ─── */
function EditModal({ student, onClose, onSaved }) {
  const [form, setForm] = useState({
    studentCode: student.studentCode ?? '',
    major: student.major ?? '',
    className: student.className ?? '',
    dateOfBirth: student.dateOfBirth ? student.dateOfBirth.slice(0, 10) : '',
    address: student.address ?? '',
  });
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  const handle = (k) => (e) => setForm((f) => ({ ...f, [k]: e.target.value }));

  const submit = async () => {
    if (!form.studentCode) {
      setError('Student Code is required.');
      return;
    }
    setSaving(true);
    setError('');
    try {
      await api.put(`/student/${student.studentId}`, form);
      onSaved();
    } catch (err) {
      setError(err?.response?.data?.message || 'Failed to update student.');
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className={styles.overlay} onClick={(e) => e.target === e.currentTarget && onClose()}>
      <div className={styles.modal}>
        <h2 className={styles.modalTitle}>Edit Student — {student.user?.fullName || student.studentCode}</h2>
        {error && <div className={styles.error}>{error}</div>}

        {[
          { label: 'Student Code *', key: 'studentCode', type: 'text' },
          { label: 'Major', key: 'major', type: 'text' },
          { label: 'Class Name', key: 'className', type: 'text' },
          { label: 'Date of Birth', key: 'dateOfBirth', type: 'date' },
          { label: 'Address', key: 'address', type: 'text' },
        ].map(({ label, key, type }) => (
          <div className={styles.formField} key={key}>
            <label className={styles.formLabel}>{label}</label>
            <input
              className={styles.formInput}
              type={type}
              value={form[key]}
              onChange={handle(key)}
            />
          </div>
        ))}

        <div className={styles.modalFooter}>
          <button className={styles.cancelBtn} onClick={onClose}>Cancel</button>
          <button className={styles.saveBtn} onClick={submit} disabled={saving}>
            {saving ? 'Saving…' : 'Save Changes'}
          </button>
        </div>
      </div>
    </div>
  );
}

/* ─── Main Page ─── */
export default function StudentsPage() {
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const [showCreate, setShowCreate] = useState(false);
  const [editTarget, setEditTarget] = useState(null);

  const fetchStudents = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await api.get('/student');
      setStudents(res.data?.data ?? res.data ?? []);
    } catch (err) {
      setError(err?.response?.data?.message || 'Failed to load students.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchStudents(); }, []);

  const formatDate = (d) => {
    if (!d) return '—';
    return new Date(d).toLocaleDateString('en-GB');
  };

  return (
    <div className={styles.root}>
      <div className={styles.header}>
        <h1 className={styles.title}>Student Management</h1>
        <button className={styles.addBtn} onClick={() => setShowCreate(true)}>
          + Add Student
        </button>
      </div>

      {error && <div className={styles.error}>{error}</div>}

      {loading ? (
        <div className={styles.loading}>Loading students…</div>
      ) : (
        <div className={styles.tableWrap}>
          <table className={styles.table}>
            <thead>
              <tr>
                <th>ID</th>
                <th>Student Code</th>
                <th>Full Name</th>
                <th>Major</th>
                <th>Class</th>
                <th>Date of Birth</th>
                <th>Address</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {students.length === 0 ? (
                <tr><td colSpan={8} className={styles.empty}>No students found.</td></tr>
              ) : (
                students.map((s) => (
                  <tr key={s.studentId}>
                    <td>{s.studentId}</td>
                    <td>{s.studentCode}</td>
                    <td>{s.user?.fullName || '—'}</td>
                    <td>{s.major || '—'}</td>
                    <td>{s.className || '—'}</td>
                    <td>{formatDate(s.dateOfBirth)}</td>
                    <td>{s.address || '—'}</td>
                    <td>
                      <div className={styles.actions}>
                        <button className={styles.editBtn} onClick={() => setEditTarget(s)}>Edit</button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      )}

      {showCreate && (
        <CreateModal
          onClose={() => setShowCreate(false)}
          onCreated={() => { setShowCreate(false); fetchStudents(); }}
        />
      )}
      {editTarget && (
        <EditModal
          student={editTarget}
          onClose={() => setEditTarget(null)}
          onSaved={() => { setEditTarget(null); fetchStudents(); }}
        />
      )}
    </div>
  );
}
