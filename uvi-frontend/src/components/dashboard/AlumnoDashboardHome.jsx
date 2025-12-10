import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../../api/axiosConfig';

const AlumnoDashboardHome = () => {
    const [resumen, setResumen] = useState({
        estadoAcademico: 'Cargando...',
        promedioPonderado: 0.0,
        deudaTotal: 0.0
    });
    const [alumnoNombre, setAlumnoNombre] = useState('Alumno');

    useEffect(() => {
        cargarDatos();
    }, []);

    const cargarDatos = async () => {
        try {
            const res = await api.get('/alumnos/dashboard-resumen');
            setResumen(res.data);
            
            const perfil = await api.get('/alumnos/perfil');
            setAlumnoNombre(perfil.data.nombreCompleto.split(' ')[0]); 
        } catch (error) {
            console.error("Error cargando dashboard", error);
            setResumen({ estadoAcademico: '---', promedioPonderado: 0, deudaTotal: 0 });
        }
    };

    return (
        <div className="fade-in">
            <div className="d-flex justify-content-between align-items-end mb-4">
                <div>
                    <h2 className="fw-bold text-uni-dark mb-0">¡Hola, {alumnoNombre}! </h2>
                    <p className="text-muted mb-0">Bienvenido a tu portal del estudiante.</p>
                </div>
                <div className="text-end d-none d-md-block">
                    <span className="badge bg-light text-secondary border px-3 py-2">Ciclo 2026-I</span>
                </div>
            </div>
            
            <div className="card card-custom mb-5 border-0 shadow-sm overflow-hidden position-relative">
                <div className="row g-0">
                    {/* Contenido (Izquierda) */}
                    <div className="col-md-8 p-5 d-flex flex-column justify-content-center position-relative z-1">
                        <span className="badge bg-uni-red mb-3 w-auto align-self-start">AVISO IMPORTANTE</span>
                        <h1 className="fw-bold mb-3 display-6 text-uni-dark">
                            Matrícula Online <span className="text-danger">2026-I</span>
                        </h1>
                        <p className="text-muted mb-4 lead">
                            El proceso de inscripción ya está disponible. Revisa tus cursos habilitados y asegura tu vacante.
                        </p>
                        
                        <div className="d-flex gap-3 flex-wrap">
                            <Link to="/matricula" className="btn btn-uni-red btn-lg px-5 shadow-sm rounded-pill fw-bold">
                                Iniciar Matrícula <i className="bi bi-arrow-right ms-2"></i>
                            </Link>
                            <Link to="/horario" className="btn btn-outline-secondary btn-lg px-4 rounded-pill">
                                Ver Horario
                            </Link>
                        </div>
                    </div>

                    <div className="col-md-4 d-none d-md-block position-relative" 
                         style={{
                             background: '#E62B4D', // <--- CAMBIO: Color rojo sólido (sin morado)
                             clipPath: 'polygon(20% 0, 100% 0, 100% 100%, 0% 100%)'
                         }}>
                        <div className="position-absolute top-50 start-50 translate-middle text-white text-center w-100">
                            {/* Icono del Birrete */}
                            <i className="bi bi-mortarboard-fill" style={{fontSize: '8rem', opacity: 0.25}}></i>
                        </div>
                    </div>
                </div>
            </div>

            <h5 className="fw-bold text-uni-dark mb-3">Tu Resumen Académico</h5>
            <div className="row g-4 mb-5">
                {/* 1. Estado Académico */}
                <div className="col-md-4">
                    <div className="card card-custom h-100 border-0 shadow-sm p-3">
                        <div className="d-flex align-items-center">
                            <div className={`p-3 rounded-circle me-3 ${resumen.estadoAcademico === 'En Riesgo' ? 'bg-danger bg-opacity-10 text-danger' : 'bg-success bg-opacity-10 text-success'}`}>
                                <i className={`bi ${resumen.estadoAcademico === 'En Riesgo' ? 'bi-exclamation-triangle-fill' : 'bi-check-circle-fill'} fs-3`}></i>
                            </div>
                            <div>
                                <small className="text-muted text-uppercase fw-bold" style={{fontSize: '0.7rem'}}>Estado Académico</small>
                                <h4 className="fw-bold mb-0">{resumen.estadoAcademico}</h4>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="col-md-4">
                    <div className="card card-custom h-100 border-0 shadow-sm p-3">
                        <div className="d-flex align-items-center">
                            <div className="p-3 rounded-circle me-3 bg-warning bg-opacity-10 text-warning">
                                <i className="bi bi-graph-up-arrow fs-3"></i>
                            </div>
                            <div>
                                <small className="text-muted text-uppercase fw-bold" style={{fontSize: '0.7rem'}}>Promedio Ponderado</small>
                                <h4 className="fw-bold mb-0">{resumen.promedioPonderado.toFixed(2)}</h4>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="col-md-4">
                    <div className="card card-custom h-100 border-0 shadow-sm p-3 position-relative overflow-hidden">
                        <div className="d-flex align-items-center">
                            <div className={`p-3 rounded-circle me-3 ${resumen.deudaTotal > 0 ? 'bg-danger bg-opacity-10 text-danger' : 'bg-primary bg-opacity-10 text-primary'}`}>
                                <i className="bi bi-wallet2 fs-3"></i>
                            </div>
                            <div>
                                <small className="text-muted text-uppercase fw-bold" style={{fontSize: '0.7rem'}}>Deuda Pendiente</small>
                                <h4 className="fw-bold mb-0">S/ {resumen.deudaTotal.toFixed(2)}</h4>
                            </div>
                        </div>
                        {resumen.deudaTotal > 0 && (
                            <Link to="/pagos" className="stretched-link" title="Ir a pagar"></Link>
                        )}
                    </div>
                </div>
            </div>

            <div className="row g-4">
                <div className="col-md-6">
                    <Link to="/historial" className="text-decoration-none">
                        <div className="card card-custom h-100 border-0 shadow-sm hover-scale">
                            <div className="card-body p-4 d-flex justify-content-between align-items-center">
                                <div>
                                    <h5 className="fw-bold text-uni-dark mb-1">Historial de Notas</h5>
                                    <small className="text-muted">Consulta tus calificaciones de ciclos anteriores.</small>
                                </div>
                                <i className="bi bi-clock-history fs-2 text-danger"></i> {/* Icono rojo */}
                            </div>
                        </div>
                    </Link>
                </div>
                <div className="col-md-6">
                    <Link to="/pagos" className="text-decoration-none">
                        <div className="card card-custom h-100 border-0 shadow-sm hover-scale">
                            <div className="card-body p-4 d-flex justify-content-between align-items-center">
                                <div>
                                    <h5 className="fw-bold text-uni-dark mb-1">Estado de Cuenta</h5>
                                    <small className="text-muted">Revisa tus cronogramas de pago y boletas.</small>
                                </div>
                                <i className="bi bi-receipt fs-2 text-danger"></i> {/* Icono rojo */}
                            </div>
                        </div>
                    </Link>
                </div>
            </div>
            
            <style>{`
                .hover-scale { transition: transform 0.2s ease; }
                .hover-scale:hover { transform: translateY(-3px); }
                .fade-in { animation: fadeIn 0.5s ease-in-out; }
                @keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }
            `}</style>
        </div>
    );
};

export default AlumnoDashboardHome;