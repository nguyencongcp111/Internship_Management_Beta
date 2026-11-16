import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Layout from './components/Layout';

import LoginPage from './pages/LoginPage';
import DashboardPage from './pages/DashboardPage';
import UsersPage from './pages/UsersPage';
import StudentsPage from './pages/StudentsPage';
import MentorsPage from './pages/MentorsPage';
import AssignmentsPage from './pages/AssignmentsPage';
import PhasesPage from './pages/PhasesPage';
import RoundsPage from './pages/RoundsPage';
import CriteriaPage from './pages/CriteriaPage';
import RoundCriteriaPage from './pages/RoundCriteriaPage';
import ResultsPage from './pages/ResultsPage';
import MyStudentProfilePage from './pages/MyStudentProfilePage';
import MyMentorProfilePage from './pages/MyMentorProfilePage';

function ProtectedLayout({ children }) {
  return (
    <ProtectedRoute>
      <Layout>{children}</Layout>
    </ProtectedRoute>
  );
}

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<LoginPage />} />

          <Route
            path="/dashboard"
            element={<ProtectedLayout><DashboardPage /></ProtectedLayout>}
          />
          <Route
            path="/users"
            element={<ProtectedLayout><UsersPage /></ProtectedLayout>}
          />
          <Route
            path="/students"
            element={<ProtectedLayout><StudentsPage /></ProtectedLayout>}
          />
          <Route
            path="/mentors"
            element={<ProtectedLayout><MentorsPage /></ProtectedLayout>}
          />
          <Route
            path="/assignments"
            element={<ProtectedLayout><AssignmentsPage /></ProtectedLayout>}
          />
          <Route
            path="/phases"
            element={<ProtectedLayout><PhasesPage /></ProtectedLayout>}
          />
          <Route
            path="/rounds"
            element={<ProtectedLayout><RoundsPage /></ProtectedLayout>}
          />
          <Route
            path="/criteria"
            element={<ProtectedLayout><CriteriaPage /></ProtectedLayout>}
          />
          <Route
            path="/round-criteria"
            element={<ProtectedLayout><RoundCriteriaPage /></ProtectedLayout>}
          />
          <Route
            path="/results"
            element={<ProtectedLayout><ResultsPage /></ProtectedLayout>}
          />
          <Route
            path="/my-student-profile"
            element={<ProtectedLayout><MyStudentProfilePage /></ProtectedLayout>}
          />
          <Route
            path="/my-mentor-profile"
            element={<ProtectedLayout><MyMentorProfilePage /></ProtectedLayout>}
          />

          <Route path="/" element={<Navigate to="/dashboard" replace />} />
          <Route path="*" element={<Navigate to="/dashboard" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}
