import { Link, useLocation } from 'react-router-dom';
import { useState } from 'react'; 

const Sidebar = ({ rol, isOpen, toggleSidebar }) => {
    const location = useLocation();
    const isActive = (path) => location.pathname === path ? 'active' : '';

    return (
        <>
            <div className={`bg-white vh-100 shadow-sm position-fixed sidebar-container ${isOpen ? 'show' : ''}`} 
                 style={{ width: '250px', zIndex: 1050, top: 0, left: 0, transition: 'transform 0.3s ease-in-out' }}>
                
                <div className="p-3 bg-uni-red text-white d-flex align-items-center justify-content-between" style={{ height: '60px' }}>
                    <h4 className="m-0 fw-bold">UVI <span className="fw-light small">+portal</span></h4>
                    <button className="btn btn-link text-white d-md-none" onClick={toggleSidebar}>
                        <i className="bi bi-x-lg"></i>
                    </button>
                </div>

                <div className="py-3 overflow-auto" style={{ height: 'calc(100vh - 60px)' }}>
                    <Link to="/dashboard" className={`sidebar-link ${isActive('/dashboard')}`} onClick={() => toggleSidebar(false)}>
                        <i className="bi bi-grid-1x2 sidebar-icon"></i> Inicio
                    </Link>
                    
                    {rol === 'ALUMNO' && (
                        <>
                            <div className="text-muted small fw-bold px-3 mt-3 mb-1">ACADÉMICO</div>
                            <Link to="/matricula" className={`sidebar-link ${isActive('/matricula')}`} onClick={() => toggleSidebar(false)}>
                                <i className="bi bi-journal-check sidebar-icon"></i> Matrícula
                            </Link>
                            <Link to="/horario" className={`sidebar-link ${isActive('/horario')}`} onClick={() => toggleSidebar(false)}>
                                <i className="bi bi-calendar-week sidebar-icon"></i> Mi Horario
                            </Link>
                            <Link to="/historial" className={`sidebar-link ${isActive('/historial')}`} onClick={() => toggleSidebar(false)}>
                                <i className="bi bi-clock-history sidebar-icon"></i> Historial / Notas
                            </Link>
                             <Link to="/pagos" className={`sidebar-link ${isActive('/pagos')}`} onClick={() => toggleSidebar(false)}>
                                <i className="bi bi-credit-card sidebar-icon"></i> Pagos
                            </Link>
                             <Link to="/perfil" className={`sidebar-link ${isActive('/perfil')}`} onClick={() => toggleSidebar(false)}>
                                <i className="bi bi-person-circle sidebar-icon"></i> Mi Perfil
                            </Link>
                        </>
                    )}

                    {rol === 'DOCENTE' && (
                        <>
                            <div className="text-muted small fw-bold px-3 mt-3 mb-1">DOCENCIA</div>
                            <Link to="/docente" className={`sidebar-link ${isActive('/docente')}`} onClick={() => toggleSidebar(false)}>
                                <i className="bi bi-person-workspace sidebar-icon"></i> Mis Cursos
                            </Link>
                            <Link to="/docente/horario" className={`sidebar-link ${isActive('/docente/horario')}`} onClick={() => toggleSidebar(false)}>
                                <i className="bi bi-calendar-event sidebar-icon"></i> Mi Horario
                            </Link>
                        </>
                    )}

                    {rol === 'ADMIN' && (
                        <>
                            <div className="text-muted small fw-bold px-3 mt-3 mb-1">GESTIÓN</div>
                            <Link to="/admin/usuarios" className={`sidebar-link ${isActive('/admin/usuarios')}`} onClick={() => toggleSidebar(false)}>
                                <i className="bi bi-people sidebar-icon"></i> Usuarios
                            </Link>
                            
                            <div className="text-muted small fw-bold px-3 mt-3 mb-1">ACADÉMICO</div>
                            <Link to="/admin/ciclos" className={`sidebar-link ${isActive('/admin/ciclos')}`} onClick={() => toggleSidebar(false)}>
                                <i className="bi bi-calendar3 sidebar-icon"></i> Ciclos
                            </Link>
                            <Link to="/admin/cursos" className={`sidebar-link ${isActive('/admin/cursos')}`} onClick={() => toggleSidebar(false)}>
                                <i className="bi bi-book sidebar-icon"></i> Cursos
                            </Link>
                            <Link to="/admin/secciones" className={`sidebar-link ${isActive('/admin/secciones')}`} onClick={() => toggleSidebar(false)}>
                                <i className="bi bi-layers sidebar-icon"></i> Secciones
                            </Link>

                            <div className="text-muted small fw-bold px-3 mt-3 mb-1">SEGURIDAD</div>
                            <Link to="/admin/auditoria" className={`sidebar-link ${isActive('/admin/auditoria')}`} onClick={() => toggleSidebar(false)}>
                                <i className="bi bi-shield-check sidebar-icon"></i> Auditoría
                            </Link>
                        </>
                    )}
                </div>
            </div>
            
            {isOpen && <div className="d-md-none position-fixed top-0 start-0 w-100 h-100 bg-dark opacity-50" style={{zIndex: 1040}} onClick={() => toggleSidebar(false)}></div>}
        </>
    );
};

const Topbar = ({ usuario, cerrarSesion, toggleSidebar }) => {
    return (
        <div className="bg-white shadow-sm px-4 d-flex justify-content-between align-items-center position-sticky top-0 w-100" 
             style={{ height: '60px', zIndex: 900 }}>
            
            <div className="d-flex align-items-center">
                <button className="btn btn-link text-dark d-md-none me-2" onClick={() => toggleSidebar(true)}>
                    <i className="bi bi-list fs-4"></i>
                </button>

                <span className="fw-medium text-secondary me-3 d-none d-sm-inline">Periodo Actual: <strong>2026-I</strong></span>
                <span className="badge bg-light text-success border border-success d-none d-sm-inline">
                    <i className="bi bi-circle-fill small me-1" style={{fontSize: '8px'}}></i> Sistema Activo
                </span>
            </div>

            <div className="d-flex align-items-center gap-3">
                <div className="text-end lh-1">
                    <div className="fw-bold small">{usuario}</div>
                    <small className="text-muted" style={{fontSize: '0.75rem'}}>Conectado</small>
                </div>
                <button 
                    onClick={cerrarSesion} 
                    className="btn btn-outline-danger btn-sm rounded-circle d-flex align-items-center justify-content-center" 
                    style={{width: '32px', height: '32px'}}
                    title="Cerrar Sesión"
                >
                    <i className="bi bi-box-arrow-right"></i>
                </button>
            </div>
        </div>
    );
};

const Layout = ({ children }) => {
    const usuario = localStorage.getItem('usuario');
    const rol = localStorage.getItem('rol');
    const [sidebarOpen, setSidebarOpen] = useState(false);

    const toggleSidebar = (state) => {
        setSidebarOpen(state === undefined ? !sidebarOpen : state);
    };

    const cerrarSesion = () => {
        localStorage.clear();
        window.location.href = '/';
    };

    return (
        <div className="d-flex bg-light min-vh-100">
            <Sidebar rol={rol} isOpen={sidebarOpen} toggleSidebar={toggleSidebar} />
            
            <div className="flex-grow-1 d-flex flex-column w-100 main-content">
                 <style>{`
                    @media (min-width: 768px) {
                        .main-content { margin-left: 250px; width: calc(100% - 250px) !important; }
                        .sidebar-container { transform: translateX(0) !important; }
                    }
                    @media (max-width: 767.98px) {
                        .main-content { margin-left: 0; width: 100% !important; }
                        .sidebar-container { transform: translateX(-100%); }
                        .sidebar-container.show { transform: translateX(0); }
                    }
                `}</style>
                
                <Topbar usuario={usuario} cerrarSesion={cerrarSesion} toggleSidebar={toggleSidebar} />
                
                <div className="container-fluid p-4">
                    {children}
                </div>
            </div>
        </div>
    );
};

export default Layout;