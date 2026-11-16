import { useEffect, useState } from 'react';
import api from '../api/axios';
import styles from './shared.module.css';

/* ─── Create Modal ─── */
function CreateModal({ onClose, onCreated }) {
  const [form, setForm] = useState({
    department: '',
    academicRank: '',
    userId: '',
  });
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  const handle = (k) => (e) => setForm((f) => ({ ...f, [k]: e.target.value }));

  const submit = async () => {
    if (!form.userId) {
      setError('User ID is required.');
      return;
    }
    setSaving(true);
    setError('');
    try {
      await api.post('/mentor', { ...form, userId: parseInt(form.userId, 10) });
      onCreated();
    } catch (err) {
      setError(err?.response?.data?.message || 'Failed to create mentor.');
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className={styles.overlay} onClick={(e) => e.target === e.currentTarget && onClose()}>
      <div className={styles.modal}>
        <h2 className={styles.modalTitle}>Add New Mentor</h2>
        {error && <div className={styles.error}>{error}</div>}

        {[
          { label: 'Department', key: 'department', type: 'text' },
          { label: 'Academic Rank', key: 'academicRank', type: 'text' },
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
            {saving ? 'Creating…' : 'Create Mentor'}
          </button>
        </div>
      </div>
    </div>
  );
}

/* ─── Edit Modal ─── */
function EditModal({ mentor, onClose, onSaved }) {
  const [form, setForm] = useState({
    department: mentor.department ?? '',
    academicRank: mentor.academicRank ?? '',
  });
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  const handle = (k) => (e) => setForm((f) => ({ ...f, [k]: e.target.value }));

  const submit = async () => {
    setSaving(true);
    setError('');
    try {
      await api.put(`/mentor/${mentor.mentorId}`, form);
      onSaved();
    } catch (err) {
      setError(err?.response?.data?.message || 'Failed to update mentor.');
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className={styles.overlay} onClick={(e) => e.target === e.currentTarget && onClose()}>
      <div className={styles.modal}>
        <h2 className={styles.modalTitle}>Edit Mentor — {mentor.user?.fullName || `#${mentor.mentorId}`}</h2>
        {error && <div className={styles.error}>{error}</div>}

        {[
          { label: 'Department', key: 'department' },
          { label: 'Academic Rank', key: 'academicRank' },
        ].map(({ label, key }) => (
          <div className={styles.formField} key={key}>
            <label className={styles.formLabel}>{label}</label>
            <input
              className={styles.formInput}
              type="text"
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
export default function MentorsPage() {
  const [mentors, setMentors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const [showCreate, setShowCreate] = useState(false);
  const [editTarget, setEditTarget] = useState(null);

  const fetchMentors = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await api.get('/mentor');
      setMentors(res.data?.data ?? res.data ?? []);
    } catch (err) {
      setError(err?.response?.data?.message || 'Failed to load mentors.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchMentors(); }, []);

  return (
    <div className={styles.root}>
      <div className={styles.header}>
        <h1 className={styles.title}>Mentor Management</h1>
        <button className={styles.addBtn} onClick={() => setShowCreate(true)}>
          + Add Mentor
        </button>
      </div>

      {error && <div className={styles.error}>{error}</div>}

      {loading ? (
        <div className={styles.loading}>Loading mentors…</div>
      ) : (
        <div className={styles.tableWrap}>
          <table className={styles.table}>
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Department</th>
                <th>Academic Rank</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {mentors.length === 0 ? (
                <tr><td colSpan={6} className={styles.empty}>No mentors found.</td></tr>
              ) : (
                mentors.map((m) => (
                  <tr key={m.mentorId}>
                    <td>{m.mentorId}</td>
                    <td>{m.user?.fullName || '—'}</td>
                    <td>{m.user?.email || '—'}</td>
                    <td>{m.department || '—'}</td>
                    <td>{m.academicRank || '—'}</td>
                    <td>
                      <div className={styles.actions}>
                        <button className={styles.editBtn} onClick={() => setEditTarget(m)}>Edit</button>
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
          onCreated={() => { setShowCreate(false); fetchMentors(); }}
        />
      )}
      {editTarget && (
        <EditModal
          mentor={editTarget}
          onClose={() => setEditTarget(null)}
          onSaved={() => { setEditTarget(null); fetchMentors(); }}
        />
      )}
    </div>
  );
}
