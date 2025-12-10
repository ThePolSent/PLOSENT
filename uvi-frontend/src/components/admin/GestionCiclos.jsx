import { useState, useEffect } from 'react';
import api from '../../api/axiosConfig';

const GestionCiclos = () => {
    const [ciclos, setCiclos] = useState([]);
    const [form, setForm] = useState({
        codigo: '', nombre: '', fechaInicio: '', fechaFin: '', 
        fechaInicioMatricula: '', fechaFinMatricula: ''
    });
    const [mensaje, setMensaje] = useState(null);

    useEffect(() => {
        cargarCiclos();
    }, []);

    const cargarCiclos = async () => {
        try {
            const res = await api.get('/ciclos');
            setCiclos(res.data);
        } catch (error) {
            console.error(error);
        }
    };

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const guardarCiclo = async (e) => {
        e.preventDefault();
        try {
            await api.post('/ciclos', { ...form, activo: true });
            setMensaje({ type: 'success', text: 'Ciclo académico creado correctamente' });
            setForm({ codigo: '', nombre: '', fechaInicio: '', fechaFin: '', fechaInicioMatricula: '', fechaFinMatricula: '' });
            cargarCiclos();
        } catch (error) {
            setMensaje({ type: 'danger', text: 'Error al crear el ciclo. Verifica las fechas.' });
        }
    };

    return (
        <div>
            <h3 className="fw-bold mb-4 text-uni-dark">Gestión de Ciclos Académicos</h3>

            <div className="card card-custom mb-4">
                <div className="card-header bg-white fw-bold text-uni-purple">
                    <i className="bi bi-calendar-plus me-2"></i> Nuevo Ciclo
                </div>
                <div className="card-body">
                    {mensaje && <div className={`alert alert-${mensaje.type}`}>{mensaje.text}</div>}
                    <form onSubmit={guardarCiclo}>
                        <div className="row g-3">
                            <div className="col-md-3">
                                <label className="form-label small text-muted">Código (Ej: 2025-I)</label>
                                <input type="text" name="codigo" className="form-control" value={form.codigo} onChange={handleChange} required />
                            </div>
                            <div className="col-md-9">
                                <label className="form-label small text-muted">Nombre del Ciclo</label>
                                <input type="text" name="nombre" className="form-control" value={form.nombre} onChange={handleChange} required />
                            </div>
                            
                            <div className="col-md-3">
                                <label className="form-label small text-muted">Inicio Ciclo</label>
                                <input type="date" name="fechaInicio" className="form-control" value={form.fechaInicio} onChange={handleChange} required />
                            </div>
                            <div className="col-md-3">
                                <label className="form-label small text-muted">Fin Ciclo</label>
                                <input type="date" name="fechaFin" className="form-control" value={form.fechaFin} onChange={handleChange} required />
                            </div>
                            <div className="col-md-3">
                                <label className="form-label small text-muted text-uni-red fw-bold">Inicio Matrícula</label>
                                <input type="date" name="fechaInicioMatricula" className="form-control border-danger" value={form.fechaInicioMatricula} onChange={handleChange} required />
                            </div>
                            <div className="col-md-3">
                                <label className="form-label small text-muted text-uni-red fw-bold">Fin Matrícula</label>
                                <input type="date" name="fechaFinMatricula" className="form-control border-danger" value={form.fechaFinMatricula} onChange={handleChange} required />
                            </div>
                            
                            <div className="col-12 text-end">
                                <button type="submit" className="btn btn-uni-red">Crear Ciclo</button>
                            </div>
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
                                    <th>Periodo Académico</th>
                                    <th>Periodo Matrícula</th>
                                    <th>Estado</th>
                                </tr>
                            </thead>
                            <tbody>
                                {ciclos.map((c) => (
                                    <tr key={c.id}>
                                        <td className="ps-4 fw-bold">{c.codigo}</td>
                                        <td>{c.nombre}</td>
                                        <td className="small">{c.fechaInicio} al {c.fechaFin}</td>
                                        <td className="small text-danger">{c.fechaInicioMatricula} al {c.fechaFinMatricula}</td>
                                        <td>
                                            <span className={`badge ${c.activo ? 'bg-success' : 'bg-secondary'}`}>
                                                {c.activo ? 'Activo' : 'Cerrado'}
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

export default GestionCiclos;