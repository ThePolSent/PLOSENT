import { useState, useEffect } from 'react';
import api from '../../api/axiosConfig';

const ReporteAuditoria = () => {
    const [logs, setLogs] = useState([]);

    useEffect(() => {
        const cargarLogs = async () => {
            try {
                const res = await api.get('/auditoria');
                setLogs(res.data);
            } catch (err) {
                console.error("Error cargando auditoría", err);
            }
        };
        cargarLogs();
    }, []);

    return (
        <div>
            <h3 className="fw-bold mb-4 text-uni-dark">Auditoría del Sistema</h3>
            
            <div className="card card-custom">
                <div className="card-body p-0">
                    <div className="table-responsive">
                        <table className="table table-hover align-middle mb-0 table-sm">
                            <thead className="bg-light">
                                <tr>
                                    <th className="ps-3 py-3">Fecha/Hora</th>
                                    <th>Usuario</th>
                                    <th>Acción</th>
                                    <th>Detalles</th>
                                </tr>
                            </thead>
                            <tbody>
                                {logs.map((log) => (
                                    <tr key={log.id}>
                                        <td className="ps-3 small text-muted">
                                            {new Date(log.fechaHora).toLocaleString()}
                                        </td>
                                        <td className="fw-bold text-primary">{log.usuario}</td>
                                        <td>
                                            <span className={`badge ${
                                                log.accion.includes('EXITO') ? 'bg-success' : 
                                                log.accion.includes('FALLIDO') ? 'bg-danger' : 
                                                'bg-secondary'
                                            }`}>
                                                {log.accion}
                                            </span>
                                        </td>
                                        <td className="small">{log.detalles}</td>
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

export default ReporteAuditoria;