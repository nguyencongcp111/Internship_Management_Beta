import { useEffect, useState } from 'react';
import api from '../api/axios';
import styles from './shared.module.css';

const ROLES = ['ROLE_ADMIN', 'ROLE_STUDENT', 'ROLE_MENTOR'];

const roleBadgeClass = (role) => {
  if (role === 'ROLE_ADMIN') return styles.badgeAdmin;
  if (role === 'ROLE_STUDENT') return styles.badgeStudent;
  if (role === 'ROLE_MENTOR') return styles.badgeMentor;
  return styles.badge;
};

const roleLabel = (role) => role?.replace('ROLE_', '') ?? '—';

/* ─── Small inline button ─── */
function SmallBtn({ children, color = '#1565c0', onClick }) {
  return (
    <button
      onClick={onClick}
      style={{
        padding: '4px 10px',
        fontSize: '11px',
        fontWeight: 600,
        border: `1.5px solid ${color}`,
        borderRadius: '6px',
        background: 'transparent',
        color,
        cursor: 'pointer',
        fontFamily: "'Helvetica Neue', Helvetica, Arial, sans-serif",
        transition: 'all 0.15s',
        whiteSpace: 'nowrap',
      }}
      onMouseEnter={(e) => {
        e.currentTarget.style.background = color;
        e.currentTarget.style.color = '#fff';
      }}
      onMouseLeave={(e) => {
        e.currentTarget.style.background = 'transparent';
        e.currentTarget.style.color = color;
      }}
    >
      {children}
    </button>
  );
}

/* ─── Create Modal ─── */
function CreateModal({ onClose, onCreated }) {
  const [form, setForm] = useState({
    username: '', password: '', fullName: '', email: '', phoneNumber: '', role: 'ROLE_STUDENT',
  });
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  const handle = (k) => (e) => setForm((f) => ({ ...f, [k]: e.target.value }));

  const submit = async () => {
    if (!form.username || !form.password || !form.fullName || !form.email) {
      setError('Please fill in all required fields.');
      return;
    }
    setSaving(true);
    setError('');
    try {
      await api.post('/admin', form);
      onCreated();
    } catch (err) {
      setError(err?.response?.data?.message || 'Failed to create user.');
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className={styles.overlay} onClick={(e) => e.target === e.currentTarget && onClose()}>
      <div className={styles.modal}>
        <h2 className={styles.modalTitle}>Add New User</h2>
        {error && <div className={styles.error}>{error}</div>}

        {[
          { label: 'Username *', key: 'username', type: 'text' },
          { label: 'Password *', key: 'password', type: 'password' },
          { label: 'Full Name *', key: 'fullName', type: 'text' },
          { label: 'Email *', key: 'email', type: 'email' },
          { label: 'Phone Number', key: 'phoneNumber', type: 'text' },
        ].map(({ label, key, type }) => (
          <div className={styles.formField} key={key}>
            <label className={styles.formLabel}>{label}</label>
            <input
              className={styles.formInput}
              type={type}
              value={form[key]}
              onChange={handle(key)}
              autoComplete="off"
            />
          </div>
        ))}

        <div className={styles.formField}>
          <label className={styles.formLabel}>Role</label>
          <select className={styles.formSelect} value={form.role} onChange={handle('role')}>
            {ROLES.map((r) => <option key={r} value={r}>{roleLabel(r)}</option>)}
          </select>
        </div>

        <div className={styles.modalFooter}>
          <button className={styles.cancelBtn} onClick={onClose}>Cancel</button>
          <button className={styles.saveBtn} onClick={submit} disabled={saving}>
            {saving ? 'Creating…' : 'Create User'}
          </button>
        </div>
      </div>
    </div>
  );
}

/* ─── Edit Modal (fullName, email, phoneNumber) ─── */
function EditModal({ user, onClose, onSaved }) {
  const [form, setForm] = useState({
    fullName: user.fullName ?? '',
    email: user.email ?? '',
    phoneNumber: user.phoneNumber ?? '',
  });
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  const handle = (k) => (e) => setForm((f) => ({ ...f, [k]: e.target.value }));

  const submit = async () => {
    if (!form.fullName || !form.email) {
      setError('Full Name and Email are required.');
      return;
    }
    setSaving(true);
    setError('');
    try {
      await api.put(`/admin/${user.id}`, form);
      onSaved();
    } catch (err) {
      setError(err?.response?.data?.message || 'Failed to update user.');
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className={styles.overlay} onClick={(e) => e.target === e.currentTarget && onClose()}>
      <div className={styles.modal}>
        <h2 className={styles.modalTitle}>Edit User — {user.username}</h2>
        {error && <div className={styles.error}>{error}</div>}

        {[
          { label: 'Full Name *', key: 'fullName' },
          { label: 'Email *', key: 'email' },
          { label: 'Phone Number', key: 'phoneNumber' },
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

/* ─── Role Modal ─── */
function RoleModal({ user, onClose, onSaved }) {
  const [role, setRole] = useState(user.role ?? 'ROLE_STUDENT');
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  const submit = async () => {
    setSaving(true);
    setError('');
    try {
      await api.put(`/admin/${user.id}/role`, { role });
      onSaved();
    } catch (err) {
      setError(err?.response?.data?.message || 'Failed to change role.');
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className={styles.overlay} onClick={(e) => e.target === e.currentTarget && onClose()}>
      <div className={styles.modal}>
        <h2 className={styles.modalTitle}>Change Role — {user.username}</h2>
        {error && <div className={styles.error}>{error}</div>}
        <div className={styles.formField}>
          <label className={styles.formLabel}>Role</label>
          <select className={styles.formSelect} value={role} onChange={(e) => setRole(e.target.value)}>
            {ROLES.map((r) => <option key={r} value={r}>{roleLabel(r)}</option>)}
          </select>
        </div>
        <div className={styles.modalFooter}>
          <button className={styles.cancelBtn} onClick={onClose}>Cancel</button>
          <button className={styles.saveBtn} onClick={submit} disabled={saving}>
            {saving ? 'Saving…' : 'Update Role'}
          </button>
        </div>
      </div>
    </div>
  );
}

/* ─── Delete Confirm Modal ─── */
function DeleteModal({ user, onClose, onDeleted }) {
  const [error, setError] = useState('');
  const [deleting, setDeleting] = useState(false);

  const confirm = async () => {
    setDeleting(true);
    setError('');
    try {
      await api.delete(`/admin/${user.id}`);
      onDeleted();
    } catch (err) {
      setError(err?.response?.data?.message || 'Failed to delete user.');
    } finally {
      setDeleting(false);
    }
  };

  return (
    <div className={styles.overlay} onClick={(e) => e.target === e.currentTarget && onClose()}>
      <div className={styles.modal}>
        <h2 className={styles.modalTitle}>Delete User</h2>
        {error && <div className={styles.error}>{error}</div>}
        <p style={{ fontSize: 14, color: '#444', margin: '0 0 20px', fontFamily: "'Helvetica Neue', Helvetica, Arial, sans-serif" }}>
          Are you sure you want to delete <strong>{user.username}</strong>? This action cannot be undone.
        </p>
        <div className={styles.modalFooter}>
          <button className={styles.cancelBtn} onClick={onClose}>Cancel</button>
          <button
            className={styles.deleteBtn}
            onClick={confirm}
            disabled={deleting}
            style={{ padding: '10px 24px', fontSize: '13.5px', fontWeight: 600 }}
          >
            {deleting ? 'Deleting…' : 'Delete'}
          </button>
        </div>
      </div>
    </div>
  );
}

/* ─── Main Page ─── */
export default function UsersPage() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const [showCreate, setShowCreate] = useState(false);
  const [editTarget, setEditTarget] = useState(null);
  const [roleTarget, setRoleTarget] = useState(null);
  const [deleteTarget, setDeleteTarget] = useState(null);

  const fetchUsers = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await api.get('/admin');
      setUsers(res.data?.data ?? res.data ?? []);
    } catch (err) {
      setError(err?.response?.data?.message || 'Failed to load users.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchUsers(); }, []);

  const handleStatusToggle = async (u) => {
    try {
      await api.put(`/admin/${u.id}/status`, { active: !u.active });
      fetchUsers();
    } catch (err) {
      setError(err?.response?.data?.message || 'Failed to update status.');
    }
  };

  return (
    <div className={styles.root}>
      <div className={styles.header}>
        <h1 className={styles.title}>User Management</h1>
        <button className={styles.addBtn} onClick={() => setShowCreate(true)}>
          + Add User
        </button>
      </div>

      {error && <div className={styles.error}>{error}</div>}

      {loading ? (
        <div className={styles.loading}>Loading users…</div>
      ) : (
        <div className={styles.tableWrap}>
          <table className={styles.table}>
            <thead>
              <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Full Name</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Role</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {users.length === 0 ? (
                <tr><td colSpan={8} className={styles.empty}>No users found.</td></tr>
              ) : (
                users.map((u) => (
                  <tr key={u.id}>
                    <td>{u.id}</td>
                    <td>{u.username}</td>
                    <td>{u.fullName}</td>
                    <td>{u.email}</td>
                    <td>{u.phoneNumber || '—'}</td>
                    <td>
                      <span className={`${styles.badge} ${roleBadgeClass(u.role)}`}>
                        {roleLabel(u.role)}
                      </span>
                    </td>
                    <td>
                      <span className={`${styles.badge} ${u.active ? styles.badgeActive : styles.badgeInactive}`}>
                        {u.active ? 'Active' : 'Inactive'}
                      </span>
                    </td>
                    <td>
                      <div className={styles.actions}>
                        <button className={styles.editBtn} onClick={() => setEditTarget(u)}>Edit</button>
                        <SmallBtn color="#6a1b9a" onClick={() => setRoleTarget(u)}>Role</SmallBtn>
                        <SmallBtn
                          color={u.active ? '#e65100' : '#2e7d32'}
                          onClick={() => handleStatusToggle(u)}
                        >
                          {u.active ? 'Deactivate' : 'Activate'}
                        </SmallBtn>
                        <button className={styles.deleteBtn} onClick={() => setDeleteTarget(u)}>Delete</button>
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
          onCreated={() => { setShowCreate(false); fetchUsers(); }}
        />
      )}
      {editTarget && (
        <EditModal
          user={editTarget}
          onClose={() => setEditTarget(null)}
          onSaved={() => { setEditTarget(null); fetchUsers(); }}
        />
      )}
      {roleTarget && (
        <RoleModal
          user={roleTarget}
          onClose={() => setRoleTarget(null)}
          onSaved={() => { setRoleTarget(null); fetchUsers(); }}
        />
      )}
      {deleteTarget && (
        <DeleteModal
          user={deleteTarget}
          onClose={() => setDeleteTarget(null)}
          onDeleted={() => { setDeleteTarget(null); fetchUsers(); }}
        />
      )}
    </div>
  );
}
