import { useState, useEffect } from 'react';
import api from '../../api/axiosConfig';

const HorarioAlumno = () => {
    const [cursos, setCursos] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        const cargarHorario = async () => {
            try {
                const res = await api.get('/matriculas/mi-horario');
                setCursos(res.data);
            } catch (err) {
                setError("No se encontró matrícula para el ciclo actual. Asegúrate de matricularte primero.");
            }
        };
        cargarHorario();
    }, []);

    if (error) return <div className="alert alert-warning m-4">{error}</div>;

    return (
        <div>
            <h3 className="fw-bold mb-4 text-uni-dark">Mi Horario de Clases</h3>
            
            <div className="row g-4">
                {cursos.map((curso, index) => (
                    <div className="col-md-6 col-lg-4" key={index}>
                        <div className="card card-custom h-100 border-top border-4 border-primary">
                            <div className="card-body">
                                <div className="d-flex justify-content-between mb-2">
                                    <span className="badge bg-light text-dark">{curso.cursoCodigo}</span>
                                    <span className="badge bg-uni-purple">{curso.seccionCodigo}</span>
                                </div>
                                <h5 className="card-title fw-bold mb-3">{curso.cursoNombre}</h5>
                                
                                <div className="bg-light p-3 rounded">
                                    <h6 className="small text-muted fw-bold text-uppercase mb-2">Horario:</h6>
                                    {curso.horariosTexto && curso.horariosTexto.map((h, i) => (
                                        <div key={i} className="d-flex align-items-center mb-1 text-secondary">
                                            <i className="bi bi-clock me-2"></i>
                                            <span>{h}</span>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        </div>
                    </div>
                ))}
            </div>

            {cursos.length === 0 && !error && (
                <div className="text-center py-5 text-muted">
                    <i className="bi bi-calendar-x display-1"></i>
                    <p className="mt-3">No tienes cursos registrados para este ciclo.</p>
                </div>
            )}
        </div>
    );
};

export default HorarioAlumno;