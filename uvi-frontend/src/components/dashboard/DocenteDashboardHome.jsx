import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../../api/axiosConfig';

const DocenteDashboardHome = () => {
    const [clasesHoy, setClasesHoy] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const cargarHorarioHoy = async () => {
            try {
                const res = await api.get('/docentes/1/horario'); 
                
                const dias = ["DOMINGO", "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO"];
                const hoy = dias[new Date().getDay()];
                
                const clases = res.data.filter(c => c.dia.toUpperCase() === hoy);
                setClasesHoy(clases);
            } catch (error) {
                console.error("Error cargando horario", error);
            } finally {
                setLoading(false);
            }
        };
        cargarHorarioHoy();
    }, []);

    return (
        <div>
            <div className="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2 className="fw-bold text-uni-dark mb-0">Portal Docente</h2>
                    <p className="text-muted">Bienvenido a tu espacio académico</p>
                </div>
                <div className="text-end d-none d-md-block">
                    <span className="badge bg-uni-purple p-2 fs-6">Periodo 2026-I</span>
                </div>
            </div>

            <div className="row g-4">
                <div className="col-lg-8">
                    <div className="card card-custom h-100 border-0 shadow-sm">
                        <div className="card-header bg-white border-bottom-0 pt-4 pb-0">
                            <h5 className="fw-bold mb-0"><i className="bi bi-calendar-day me-2 text-uni-red"></i> Clases de Hoy</h5>
                        </div>
                        <div className="card-body">
                            {loading ? (
                                <div className="text-center py-4"><span className="spinner-border spinner-border-sm"></span></div>
                            ) : clasesHoy.length > 0 ? (
                                <div className="list-group list-group-flush mt-2">
                                    {clasesHoy.map((clase, i) => (
                                        <div key={i} className="list-group-item px-0 py-3 border-bottom-0">
                                            <div className="d-flex align-items-center">
                                                <div className="bg-light p-3 rounded text-center me-3" style={{minWidth: '80px'}}>
                                                    <div className="fw-bold">{clase.horaInicio}</div>
                                                    <small className="text-muted">Inicio</small>
                                                </div>
                                                <div className="flex-grow-1">
                                                    <h6 className="fw-bold mb-1">{clase.curso}</h6>
                                                    <span className="badge bg-uni-purple me-2">{clase.seccion}</span>
                                                    <span className="text-muted small"><i className="bi bi-geo-alt"></i> {clase.aula}</span>
                                                </div>
                                                <Link to="/docente" className="btn btn-sm btn-outline-primary">Ver Lista</Link>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            ) : (
                                <div className="text-center py-5 text-muted">
                                    <i className="bi bi-cup-hot display-4 mb-3 d-block opacity-50"></i>
                                    <p>No tienes clases programadas para hoy.</p>
                                </div>
                            )}
                        </div>
                    </div>
                </div>

                <div className="col-lg-4">
                    <div className="card card-custom bg-uni-red text-white h-100 border-0 mb-4">
                        <div className="card-body p-4 d-flex flex-column justify-content-between">
                            <div>
                                <i className="bi bi-people-fill display-4 mb-3"></i>
                                <h4>Mis Estudiantes</h4>
                                <p className="opacity-75">Consulta las listas de asistencia y datos de tus alumnos.</p>
                            </div>
                            <Link to="/docente" className="btn btn-light text-uni-red fw-bold w-100">Ir a Mis Cursos</Link>
                        </div>
                    </div>
                </div>
            </div>

            <div className="row mt-4">
                 <div className="col-12">
                     <div className="alert alert-info border-0 shadow-sm d-flex align-items-center">
                         <i className="bi bi-info-circle-fill fs-4 me-3"></i>
                         <div>
                             <strong>Recordatorio:</strong> El registro de notas parciales estará habilitado a partir de la semana 8.
                         </div>
                     </div>
                 </div>
            </div>
        </div>
    );
};

export default DocenteDashboardHome;