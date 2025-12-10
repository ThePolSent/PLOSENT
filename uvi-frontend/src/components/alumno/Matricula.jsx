import { useState, useEffect } from 'react';
import api from '../../api/axiosConfig';
import { getUsuarioId } from '../../utils/auth';

const Matricula = () => {
    const [ciclo, setCiclo] = useState(null);
    const [alumno, setAlumno] = useState(null);
    const [cursosDisponibles, setCursosDisponibles] = useState({});
    const [seleccionadas, setSeleccionadas] = useState([]);
    const [mensaje, setMensaje] = useState(null);
    const [loading, setLoading] = useState(false);
    const [matriculaId, setMatriculaId] = useState(null); // ID para PDF inmediato
    
    // --- ESTADO PARA CONTROLAR EL ACORDEÓN ---
    // Guardamos el nombre del curso que está "abierto"
    const [openAccordion, setOpenAccordion] = useState(null);

    const PRECIO_CREDITO = 40.00; 

    useEffect(() => {
        cargarDatosCompletos();
    }, []);

    const cargarDatosCompletos = async () => {
        try {
            const [resCiclo, resPerfil, resHistorial] = await Promise.all([
                api.get('/ciclos/actual'),
                api.get('/alumnos/perfil'),
                api.get('/historial/mi-historial')
            ]);

            setCiclo(resCiclo.data);
            setAlumno(resPerfil.data);
            const historial = resHistorial.data;
            const cicloAlumno = resPerfil.data.cicloActual || 1;

            const resSecciones = await api.get(`/secciones/ciclo/${resCiclo.data.id}/vacantes`);
            const todasLasSecciones = resSecciones.data;

            // Filtrado (Lógica original)
            const cursosAprobadosIds = historial.filter(h => h.estado === 'APROBADO').map(h => h.codigoCurso);
            
            const seccionesFiltradas = todasLasSecciones.filter(sec => {
                const esAprobado = cursosAprobadosIds.includes(sec.cursoCodigo);
                const esDeMiCiclo = sec.cursoCiclo === cicloAlumno;
                const esDeCicloAnterior = sec.cursoCiclo < cicloAlumno;
                
                if (esAprobado) return false;
                if (esDeMiCiclo) return true;
                if (esDeCicloAnterior && !esAprobado) return true;
                return false;
            });

            // Agrupamiento
            const agrupados = seccionesFiltradas.reduce((acc, sec) => {
                if (!acc[sec.cursoNombre]) {
                    acc[sec.cursoNombre] = { info: sec, secciones: [] };
                }
                acc[sec.cursoNombre].secciones.push(sec);
                return acc;
            }, {});

            setCursosDisponibles(agrupados);
            
            // Abrir el primer curso por defecto para que se vea bonito
            if (Object.keys(agrupados).length > 0) {
                setOpenAccordion(Object.keys(agrupados)[0]);
            }

        } catch (err) {
            console.error(err);
            setMensaje({ type: 'danger', texto: 'Error cargando carga académica.' });
        }
    };

    const toggleAccordion = (nombreCurso) => {
        if (openAccordion === nombreCurso) {
            setOpenAccordion(null); // Cerrar si ya está abierto
        } else {
            setOpenAccordion(nombreCurso); // Abrir el nuevo
        }
    };

    const handleCheck = (seccionId, cursoNombre) => {
        const seccionesDelCurso = cursosDisponibles[cursoNombre].secciones.map(s => s.id);
        const yaSeleccionado = seleccionadas.find(id => seccionesDelCurso.includes(id));

        let nuevaSeleccion = [...seleccionadas];
        if (yaSeleccionado && yaSeleccionado !== seccionId) {
            nuevaSeleccion = nuevaSeleccion.filter(id => id !== yaSeleccionado);
        }

        if (nuevaSeleccion.includes(seccionId)) {
            nuevaSeleccion = nuevaSeleccion.filter(id => id !== seccionId);
        } else {
            nuevaSeleccion.push(seccionId);
        }
        setSeleccionadas(nuevaSeleccion);
    };

    const matricularse = async () => {
        setLoading(true);
        const alumnoId = getUsuarioId();
        try {
            const respuesta = await api.post('/matriculas', {
                alumnoId,
                cicloId: ciclo.id,
                seccionesIds: seleccionadas
            });
            
            setMatriculaId(respuesta.data.id); // Guardar ID para descarga inmediata
            setMensaje({ 
                tipo: 'success', 
                texto: `¡Matrícula Exitosa! Código: ${respuesta.data.codigoComprobante}` 
            });
            // NOTA: Quitamos el redirect automático para que le de tiempo a descargar
        } catch (err) {
            setMensaje({ type: 'danger', texto: err.response?.data?.message || 'Error.' });
        } finally {
            setLoading(false);
        }
    };

    const descargarPDF = async () => {
        if (!matriculaId) return;
        try {
            const response = await api.get(`/matriculas/${matriculaId}/comprobante`, { responseType: 'blob' });
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', 'Constancia_Matricula.pdf');
            document.body.appendChild(link);
            link.click();
            link.parentNode.removeChild(link);
            
            // Redirigir a pagos después de descargar
            setTimeout(() => window.location.href = '/pagos', 1000);
        } catch (e) { alert("Error descargando PDF"); }
    };

    // Totales
    const seccionesFlat = Object.values(cursosDisponibles).flatMap(c => c.secciones);
    const seccionesSeleccionadas = seccionesFlat.filter(s => seleccionadas.includes(s.id));
    const totalCreditos = seccionesSeleccionadas.reduce((acc, s) => acc + (s.creditos || 0), 0);
    const mensualidad = totalCreditos * PRECIO_CREDITO;

    if (!ciclo || !alumno) return <div className="text-center p-5"><div className="spinner-border text-uni-red"></div></div>;

    return (
        <div>
            <h3 className="fw-bold mb-3 text-uni-dark">Selección de Cursos <span className="text-muted fs-5">| {ciclo.nombre}</span></h3>
            
            {/* ALERTAS Y BOTÓN DE DESCARGA ÉXITO */}
            {mensaje && (
                <div className={`alert alert-${mensaje.type} d-flex justify-content-between align-items-center`}>
                    <span>{mensaje.texto}</span>
                    {mensaje.type === 'success' && (
                        <button onClick={descargarPDF} className="btn btn-sm btn-success fw-bold">
                            <i className="bi bi-download me-1"></i> Descargar Constancia y Continuar
                        </button>
                    )}
                </div>
            )}

            {/* --- ACORDEÓN CORREGIDO (React State) --- */}
            <div className="accordion mb-5" id="accordionCursos">
                {Object.keys(cursosDisponibles).map((nombreCurso, index) => {
                    const grupo = cursosDisponibles[nombreCurso];
                    const isSelected = grupo.secciones.some(s => seleccionadas.includes(s.id));
                    const isOpen = openAccordion === nombreCurso; // Controlado por React
                    
                    return (
                        <div className={`card card-custom mb-3 border-0 shadow-sm ${isSelected ? 'border-start border-4 border-success' : ''}`} key={index}>
                            {/* HEADER DEL ITEM */}
                            <div 
                                className="card-header bg-white p-3 d-flex justify-content-between align-items-center cursor-pointer"
                                onClick={() => toggleAccordion(nombreCurso)}
                                style={{cursor: 'pointer'}}
                            >
                                <div className="d-flex align-items-center">
                                    <i className={`bi bi-chevron-${isOpen ? 'up' : 'down'} me-3 text-muted`}></i>
                                    <div>
                                        <h6 className="mb-0 fw-bold">{nombreCurso}</h6>
                                        <small className="text-muted">{grupo.info.cursoCodigo}</small>
                                        {grupo.info.cursoCiclo < alumno.cicloActual && 
                                            <span className="badge bg-warning text-dark ms-2" style={{fontSize:'0.6rem'}}>Repitencia</span>
                                        }
                                    </div>
                                </div>
                                <span className="badge bg-light text-dark border">{grupo.info.creditos} Créditos</span>
                            </div>

                            {/* BODY DEL ITEM (COLLAPSE) */}
                            {isOpen && (
                                <div className="card-body p-0 border-top">
                                    <table className="table table-hover mb-0">
                                        <thead className="bg-light text-secondary small">
                                            <tr>
                                                <th className="ps-4">Sec.</th>
                                                <th>Docente</th>
                                                <th>Horario</th>
                                                <th>Vacantes</th>
                                                <th className="text-center">Seleccionar</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {grupo.secciones.map(sec => (
                                                <tr key={sec.id} className={seleccionadas.includes(sec.id) ? "table-success" : ""}>
                                                    <td className="ps-4 fw-bold text-uni-purple">{sec.codigo}</td>
                                                    <td>{sec.docenteNombre}</td>
                                                    <td>
                                                        {sec.horarios.map((h, i) => (
                                                            <div key={i} className="small text-muted">
                                                                {h.diaSemana.substring(0,3)} {h.horaInicio?.substring(0,5)}-{h.horaFin?.substring(0,5)}
                                                            </div>
                                                        ))}
                                                    </td>
                                                    <td>
                                                        <span className={`badge ${sec.vacantesDisponibles > 0 ? 'bg-success' : 'bg-danger'}`}>
                                                            {sec.vacantesDisponibles}
                                                        </span>
                                                    </td>
                                                    <td className="text-center">
                                                        <input 
                                                            className="form-check-input" 
                                                            type="checkbox" 
                                                            style={{transform: 'scale(1.3)', cursor: 'pointer'}}
                                                            checked={seleccionadas.includes(sec.id)}
                                                            onChange={() => handleCheck(sec.id, nombreCurso)}
                                                            disabled={sec.vacantesDisponibles <= 0}
                                                        />
                                                    </td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    </table>
                                </div>
                            )}
                        </div>
                    );
                })}
            </div>

            {/* --- BARRA TOTALES FLOTANTE --- */}
            <div className="card card-custom border-top border-4 border-danger shadow-lg fixed-bottom m-4 position-sticky bottom-0">
                <div className="card-body p-3 d-flex justify-content-between align-items-center">
                    <div>
                        <div className="small text-uppercase fw-bold text-muted">Mensualidad Estimada</div>
                        <div className="fs-2 fw-bold text-uni-red">S/ {mensualidad.toFixed(2)}</div>
                    </div>
                    <div className="text-end">
                         <div className="small text-muted mb-1">{totalCreditos} Créditos seleccionados</div>
                         <button 
                            className="btn btn-uni-red px-5 btn-lg" 
                            onClick={matricularse}
                            disabled={seleccionadas.length === 0 || loading || mensaje?.type === 'success'}
                        >
                            {loading ? 'Procesando...' : 'Confirmar Matrícula'}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Matricula;