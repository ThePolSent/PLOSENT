import { useState, useEffect } from 'react';
import api from '../../api/axiosConfig';
import { getUsuarioId } from '../../utils/auth';

const DocenteDashboard = () => {
    const [secciones, setSecciones] = useState([]);
    const [alumnos, setAlumnos] = useState([]);
    const [seccionSeleccionada, setSeccionSeleccionada] = useState(null);
    const [loading, setLoading] = useState(false);

   
    const docenteId = 1;

    useEffect(() => {
        cargarSecciones();
    }, []);

    const cargarSecciones = async () => {
        try {
            const res = await api.get(`/docentes/${docenteId}/secciones-actuales`);
            setSecciones(res.data);
        } catch (error) {
            console.error("Error al cargar secciones", error);
        }
    };

    const verAlumnos = async (seccion) => {
        setLoading(true);
        setSeccionSeleccionada(seccion);
        try {
            const res = await api.get(`/docentes/secciones/${seccion.id}/alumnos`);
            setAlumnos(res.data);
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <h3 className="fw-bold mb-4 text-uni-dark">Portal Docente</h3>

            <div className="row">
                <div className="col-md-5">
                    <div className="card card-custom h-100">
                        <div className="card-header bg-white fw-bold text-uni-purple">
                            <i className="bi bi-person-workspace me-2"></i> Mis Asignaturas
                        </div>
                        <div className="list-group list-group-flush">
                            {secciones.map((sec) => (
                                <button
                                    key={sec.id}
                                    className={`list-group-item list-group-item-action py-3 ${seccionSeleccionada?.id === sec.id ? 'bg-light border-start border-4 border-primary' : ''}`}
                                    onClick={() => verAlumnos(sec)}
                                >
                                    <div className="d-flex w-100 justify-content-between">
                                        <h6 className="mb-1 fw-bold">{sec.cursoNombre}</h6>
                                        <small className="badge bg-uni-red">{sec.codigoSeccion}</small>
                                    </div>
                                    <small className="text-muted">Inscritos: {sec.inscritos}</small>
                                </button>
                            ))}
                            {secciones.length === 0 && <div className="p-3 text-muted text-center">No tienes cursos asignados este ciclo.</div>}
                        </div>
                    </div>
                </div>

                <div className="col-md-7">
                    <div className="card card-custom h-100">
                        <div className="card-header bg-white fw-bold">
                            {seccionSeleccionada 
                                ? `Alumnos de ${seccionSeleccionada.cursoNombre} (${seccionSeleccionada.codigoSeccion})`
                                : 'Selecciona un curso para ver detalles'}
                        </div>
                        <div className="card-body p-0">
                            {loading ? (
                                <div className="text-center p-5"><div className="spinner-border text-uni-purple"></div></div>
                            ) : seccionSeleccionada ? (
                                <div className="table-responsive">
                                    <table className="table table-hover align-middle mb-0">
                                        <thead className="bg-light">
                                            <tr>
                                                <th className="ps-4">Código</th>
                                                <th>Alumno</th>
                                                <th>Fecha Inscripción</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {alumnos.map((alum, idx) => (
                                                <tr key={idx}>
                                                    <td className="ps-4 font-monospace small">{alum.codigo || 'S/D'}</td>
                                                    <td className="fw-medium">{alum.nombre || 'Nombre No Registrado'}</td>
                                                    <td className="text-muted small">{alum.fechaInscripcion}</td>
                                                </tr>
                                            ))}
                                            {alumnos.length === 0 && (
                                                <tr><td colSpan="3" className="text-center py-4 text-muted">No hay alumnos inscritos aún.</td></tr>
                                            )}
                                        </tbody>
                                    </table>
                                </div>
                            ) : (
                                <div className="text-center p-5 text-muted">
                                    <i className="bi bi-people display-4 mb-3 d-block opacity-25"></i>
                                    Selecciona una asignatura a la izquierda
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default DocenteDashboard;