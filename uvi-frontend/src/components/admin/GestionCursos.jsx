import { useState, useEffect } from 'react';
import api from '../../api/axiosConfig';

const GestionCursos = () => {
    const [cursos, setCursos] = useState([]);
    const [nuevoCurso, setNuevoCurso] = useState({
        codigo: '', nombre: '', creditos: 0, horasTeoricas: 0, horasPracticas: 0, departamento: ''
    });
    const [mensaje, setMensaje] = useState(null);

    useEffect(() => {
        cargarCursos();
    }, []);

    const cargarCursos = async () => {
        try {
            const res = await api.get('/cursos');
            setCursos(res.data);
        } catch (error) {
            console.error("Error cargando cursos", error);
        }
    };

    const handleInputChange = (e) => {
        setNuevoCurso({ ...nuevoCurso, [e.target.name]: e.target.value });
    };

    const crearCurso = async (e) => {
        e.preventDefault();
        setMensaje(null); 

        try {
            await api.post('/cursos', nuevoCurso);
            setMensaje({ type: 'success', text: 'Curso creado correctamente' });
            setNuevoCurso({ 
                codigo: '', 
                nombre: '', 
                creditos: 0, 
                horasTeoricas: 0, 
                horasPracticas: 0, 
                departamento: '' 
            });
            cargarCursos();
        } catch (error) {
            const textoError = error.response?.data?.message || 'Error al procesar la solicitud.';
            setMensaje({ type: 'danger', text: textoError });
        }
    };

    return (
        <div>
            <h3 className="fw-bold mb-4 text-uni-dark">Gestión de Cursos</h3>

            <div className="card card-custom mb-4">
                <div className="card-header bg-white fw-bold text-uni-red">
                    <i className="bi bi-plus-circle me-2"></i> Nuevo Curso
                </div>
                <div className="card-body">
                    {mensaje && <div className={`alert alert-${mensaje.type}`}>{mensaje.text}</div>}
                    <form onSubmit={crearCurso} className="row g-3">
                        <div className="col-md-2">
                            <input type="text" name="codigo" className="form-control" placeholder="Código (Ej: MAT101)" value={nuevoCurso.codigo} onChange={handleInputChange} required />
                        </div>
                        <div className="col-md-4">
                            <input type="text" name="nombre" className="form-control" placeholder="Nombre del Curso" value={nuevoCurso.nombre} onChange={handleInputChange} required />
                        </div>
                        <div className="col-md-2">
                            <input type="number" name="creditos" className="form-control" placeholder="Créditos" value={nuevoCurso.creditos} onChange={handleInputChange} required />
                        </div>
                        <div className="col-md-4">
                            <input type="text" name="departamento" className="form-control" placeholder="Departamento" value={nuevoCurso.departamento} onChange={handleInputChange} />
                        </div>
                        <div className="col-12 text-end">
                            <button type="submit" className="btn btn-uni-purple">Guardar Curso</button>
                        </div>
                    </form>
                </div>
            </div>

            <div className="card card-custom">
                <div className="card-body p-0">
                    <div className="table-responsive">
                        <table className="table table-hover align-middle mb-0">
                            <thead className="bg-light">
                                <tr>
                                    <th className="ps-4">Código</th>
                                    <th>Nombre</th>
                                    <th>Créditos</th>
                                    <th>Dpto.</th>
                                    <th>Estado</th>
                                </tr>
                            </thead>
                            <tbody>
                                {cursos.map((curso) => (
                                    <tr key={curso.id}>
                                        <td className="ps-4 fw-bold">{curso.codigo}</td>
                                        <td>{curso.nombre}</td>
                                        <td><span className="badge bg-secondary">{curso.creditos} CR</span></td>
                                        <td>{curso.departamento}</td>
                                        <td>
                                            <span className={`badge ${curso.activo ? 'bg-success' : 'bg-danger'}`}>
                                                {curso.activo ? 'Activo' : 'Inactivo'}
                                            </span>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default GestionCursos;