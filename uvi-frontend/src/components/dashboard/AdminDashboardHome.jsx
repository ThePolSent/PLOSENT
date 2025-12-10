import { Link } from 'react-router-dom';

const AdminDashboardHome = () => {
    return (
        <div>
            <h2 className="fw-bold mb-4 text-uni-dark">Panel de Administración</h2>
            
            <div className="row g-4">
                <div className="col-12">
                    <h6 className="text-muted text-uppercase fw-bold mb-3 border-bottom pb-2">Gestión de Usuarios</h6>
                </div>
                <div className="col-md-6 col-xl-4">
                    <div className="card card-custom h-100 border-0 shadow-sm">
                        <div className="card-body d-flex align-items-center">
                            <div className="bg-primary bg-opacity-10 p-3 rounded me-3">
                                <i className="bi bi-people-fill fs-3 text-primary"></i>
                            </div>
                            <div>
                                <h5 className="fw-bold mb-1">Usuarios</h5>
                                <p className="text-muted small mb-2">Crear alumnos y docentes</p>
                                <Link to="/admin/usuarios" className="btn btn-sm btn-outline-primary stretched-link">Gestionar</Link>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="col-12 mt-4">
                    <h6 className="text-muted text-uppercase fw-bold mb-3 border-bottom pb-2">Gestión Académica</h6>
                </div>
                <div className="col-md-6 col-xl-3">
                    <div className="card card-custom h-100 border-0 shadow-sm">
                        <div className="card-body text-center">
                            <i className="bi bi-calendar3 fs-1 text-uni-purple mb-3"></i>
                            <h6 className="fw-bold">Ciclos</h6>
                            <Link to="/admin/ciclos" className="btn btn-sm btn-light w-100 mt-2 stretched-link">Abrir/Cerrar</Link>
                        </div>
                    </div>
                </div>
                <div className="col-md-6 col-xl-3">
                    <div className="card card-custom h-100 border-0 shadow-sm">
                        <div className="card-body text-center">
                            <i className="bi bi-book fs-1 text-uni-purple mb-3"></i>
                            <h6 className="fw-bold">Cursos</h6>
                            <Link to="/admin/cursos" className="btn btn-sm btn-light w-100 mt-2 stretched-link">Catálogo</Link>
                        </div>
                    </div>
                </div>
                <div className="col-md-6 col-xl-3">
                    <div className="card card-custom h-100 border-0 shadow-sm">
                        <div className="card-body text-center">
                            <i className="bi bi-layers fs-1 text-uni-purple mb-3"></i>
                            <h6 className="fw-bold">Secciones</h6>
                            <Link to="/admin/secciones" className="btn btn-sm btn-light w-100 mt-2 stretched-link">Programación</Link>
                        </div>
                    </div>
                </div>

                <div className="col-12 mt-4">
                    <h6 className="text-muted text-uppercase fw-bold mb-3 border-bottom pb-2">Seguridad y Control</h6>
                </div>
                <div className="col-md-12">
                    <div className="card card-custom bg-dark text-white border-0">
                        <div className="card-body d-flex justify-content-between align-items-center">
                            <div className="d-flex align-items-center">
                                <i className="bi bi-shield-check fs-2 me-3"></i>
                                <div>
                                    <h5 className="mb-0">Auditoría del Sistema</h5>
                                    <small className="text-white-50">Monitorea accesos y movimientos en tiempo real</small>
                                </div>
                            </div>
                            <Link to="/admin/auditoria" className="btn btn-outline-light">Ver Logs</Link>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AdminDashboardHome;