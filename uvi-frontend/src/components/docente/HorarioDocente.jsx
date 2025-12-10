import { useState, useEffect } from 'react';
import api from '../../api/axiosConfig';

const HorarioDocente = () => {
    const [clases, setClases] = useState([]);
    const [loading, setLoading] = useState(true);
    
    const docenteId = 1; 

    useEffect(() => {
        const cargarHorario = async () => {
            try {
                const res = await api.get(`/docentes/${docenteId}/horario`);
                setClases(res.data);
            } catch (error) {
                console.error("Error cargando horario", error);
            } finally {
                setLoading(false);
            }
        };
        cargarHorario();
    }, []);

    const diasOrden = ["LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO"];
    const clasesPorDia = diasOrden.reduce((acc, dia) => {
        acc[dia] = clases.filter(c => c.dia.toUpperCase() === dia);
        return acc;
    }, {});

    if (loading) return <div className="text-center p-5"><div className="spinner-border text-uni-purple"></div></div>;

    return (
        <div>
            <h3 className="fw-bold mb-4 text-uni-dark">Mi Horario de Clases</h3>
            
            <div className="row g-4">
                {diasOrden.map(dia => {
                    const clasesDia = clasesPorDia[dia];
                    if (clasesDia.length === 0) return null; 

                    return (
                        <div className="col-md-6 col-xl-4" key={dia}>
                            <div className="card card-custom h-100 border-0 shadow-sm">
                                <div className="card-header bg-uni-purple text-white fw-bold d-flex justify-content-between">
                                    <span>{dia}</span>
                                    <span className="badge bg-white text-uni-purple">{clasesDia.length} Clases</span>
                                </div>
                                <div className="list-group list-group-flush">
                                    {clasesDia.sort((a,b) => a.horaInicio.localeCompare(b.horaInicio)).map((clase, idx) => (
                                        <div key={idx} className="list-group-item py-3">
                                            <div className="d-flex justify-content-between mb-1">
                                                <span className="fw-bold text-dark">{clase.horaInicio} - {clase.horaFin}</span>
                                                <span className="badge bg-light text-dark border">{clase.seccion}</span>
                                            </div>
                                            <h6 className="mb-1 text-primary">{clase.curso}</h6>
                                            <small className="text-muted">
                                                <i className="bi bi-geo-alt-fill me-1"></i> {clase.aula}
                                            </small>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        </div>
                    );
                })}
            </div>

            {clases.length === 0 && (
                <div className="alert alert-light text-center border p-5">
                    <i className="bi bi-calendar-x display-4 text-muted mb-3 d-block"></i>
                    No tienes horarios programados para este ciclo.
                </div>
            )}
        </div>
    );
};

export default HorarioDocente;