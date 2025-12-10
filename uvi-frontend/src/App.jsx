import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';

// --- COMPONENTES PÚBLICOS ---
import Login from './components/Login';

// --- COMPONENTES ESTRUCTURALES ---
import Layout from './components/shared/Layout';
import DashboardHome from './components/dashboard/DashboardHome';

// --- MÓDULO ALUMNO ---
import Matricula from './components/alumno/Matricula';
import HistorialAlumno from './components/alumno/HistorialAlumno';
import HorarioAlumno from './components/alumno/HorarioAlumno';
import ProcesoMatricula from './components/alumno/ProcesoMatricula';
import PerfilAlumno from './components/alumno/PerfilAlumno';
import PagosAlumno from './components/alumno/PagosAlumno';

// --- MÓDULO DOCENTE ---
import DocenteDashboard from './components/docente/DocenteDashboard';
import HorarioDocente from './components/docente/HorarioDocente';

// --- MÓDULO ADMINISTRADOR ---
import GestionCursos from './components/admin/GestionCursos';
import RegistroUsuarios from './components/admin/RegistroUsuarios';
import GestionCiclos from './components/admin/GestionCiclos';
import GestionSecciones from './components/admin/GestionSecciones';
import ReporteAuditoria from './components/admin/ReporteAuditoria';


// --- RECUPERAR CONTRA ---
import Recuperar from './components/public/Recuperar';
import ResetPassword from './components/public/ResetPassword';

const RutaPrivada = ({ children, rolRequerido }) => {
    const token = localStorage.getItem('token');
    const rolUsuario = localStorage.getItem('rol');

    if (!token) {
        return <Navigate to="/" />;
    }

    if (rolRequerido && rolUsuario !== rolRequerido) {
        return <Navigate to="/dashboard" />;
    }

    return children;
};

function App() {
    return (
        <Router>
            <Routes>
                {/* --- RUTA PÚBLICA (LOGIN) --- */}
                <Route path="/" element={<Login />} />
                <Route path="/recuperar" element={<Recuperar />} />
                <Route path="/reset-password" element={<ResetPassword />} />

                {/* --- DASHBOARD (ACCESIBLE POR TODOS LOS ROLES) --- */}
                <Route path="/dashboard" element={
                    <RutaPrivada>
                        <Layout>
                            <DashboardHome />
                        </Layout>
                    </RutaPrivada>
                } />

                {/* --- RUTAS DE ALUMNO --- */}
                <Route path="/matricula" element={
                    <RutaPrivada rolRequerido="ALUMNO">
                        <Layout>
                            <ProcesoMatricula /> 
                        </Layout>
                    </RutaPrivada>
                } />
                
                <Route path="/historial" element={
                    <RutaPrivada rolRequerido="ALUMNO">
                        <Layout>
                            <HistorialAlumno />
                        </Layout>
                    </RutaPrivada>
                } />

                <Route path="/horario" element={
                    <RutaPrivada rolRequerido="ALUMNO">
                        <Layout>
                            <HorarioAlumno />
                        </Layout>
                    </RutaPrivada>
                } />

                <Route path="/perfil" element={<RutaPrivada rolRequerido="ALUMNO"><Layout><PerfilAlumno /></Layout></RutaPrivada>} />
                <Route path="/pagos" element={<RutaPrivada rolRequerido="ALUMNO"><Layout><PagosAlumno /></Layout></RutaPrivada>} /> 

                {/* --- RUTAS DE DOCENTE --- */}
                <Route path="/docente" element={
                    <RutaPrivada rolRequerido="DOCENTE">
                        <Layout>
                            <DocenteDashboard />
                        </Layout>
                    </RutaPrivada>
                } />

                <Route path="/docente/horario" element={
                    <RutaPrivada rolRequerido="DOCENTE">
                        <Layout>
                            <HorarioDocente />
                        </Layout>
                    </RutaPrivada>
                } />

                {/* --- RUTAS DE ADMINISTRADOR --- */}
                <Route path="/admin/usuarios" element={
                    <RutaPrivada rolRequerido="ADMIN">
                        <Layout>
                            <RegistroUsuarios />
                        </Layout>
                    </RutaPrivada>
                } />

                <Route path="/admin/cursos" element={
                    <RutaPrivada rolRequerido="ADMIN">
                        <Layout>
                            <GestionCursos />
                        </Layout>
                    </RutaPrivada>
                } />

                <Route path="/admin/ciclos" element={
                    <RutaPrivada rolRequerido="ADMIN">
                        <Layout>
                            <GestionCiclos />
                        </Layout>
                    </RutaPrivada>
                } />

                <Route path="/admin/secciones" element={
                    <RutaPrivada rolRequerido="ADMIN">
                        <Layout>
                            <GestionSecciones />
                        </Layout>
                    </RutaPrivada>
                } />

                <Route path="/admin/auditoria" element={
                    <RutaPrivada rolRequerido="ADMIN">
                        <Layout>
                            <ReporteAuditoria />
                        </Layout>
                    </RutaPrivada>
                } />

                <Route path="*" element={<Navigate to="/" />} />
            </Routes>
        </Router>
    );
}

export default App;