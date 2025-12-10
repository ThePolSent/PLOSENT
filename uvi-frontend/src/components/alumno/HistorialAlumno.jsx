import { useState, useEffect } from 'react';
import api from '../../api/axiosConfig';

const HistorialAlumno = () => {
    const [historial, setHistorial] = useState([]);

    useEffect(() => {
        const cargarHistorial = async () => {
            try {
                const res = await api.get('/historial/mi-historial');
                setHistorial(res.data);
            } catch (err) {
                console.error("Error cargando historial", err);
            }
        };
        cargarHistorial();
    }, []);

    const formatearNota = (nota) => {
        return nota ? Number(nota).toFixed(2) : "0.00";
    };

    return (
        <div>
            <h3 className="fw-bold mb-4 text-uni-dark">Mi Historial Académico</h3>

            <div className="card card-custom">
                <div className="card-body p-0">
                    <div className="table-responsive">
                        <table className="table table-hover align-middle mb-0">
                            <thead className="bg-light">
                                <tr>
                                    <th className="ps-4">Ciclo</th>
                                    <th>Código</th>
                                    <th>Curso</th>
                                    <th className="text-center">Nota</th>
                                    <th>Estado</th>
                                </tr>
                            </thead>
                            <tbody>
                                {historial.map((h, i) => (
                                    <tr key={i}>
                                        <td className="ps-4 text-muted small">2024-II</td> 
                                        <td className="fw-bold">{h.codigoCurso}</td>
                                        <td>{h.curso}</td>
                                        <td className="text-center">
                                            {/* CORRECCIÓN: Usar toFixed(2) */}
                                            <span className={`fw-bold ${h.nota >= 10.5 ? 'text-success' : 'text-danger'}`}>
                                                {formatearNota(h.nota)}
                                            </span>
                                        </td>
                                        <td>
                                            <span className={`badge ${
                                                h.estado === 'APROBADO' ? 'bg-success' : 
                                                h.estado === 'MATRICULADO' ? 'bg-primary' : 'bg-danger'
                                            }`}>
                                                {h.estado}
                                            </span>
                                        </td>
                                    </tr>
                                ))}
                                {historial.length === 0 && (
                                    <tr><td colSpan="5" className="text-center py-4 text-muted">Aún no tienes cursos registrados en tu historial.</td></tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default HistorialAlumno;