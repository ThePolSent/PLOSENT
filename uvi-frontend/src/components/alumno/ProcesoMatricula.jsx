import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../../api/axiosConfig';
import Matricula from './Matricula';

const ProcesoMatricula = () => {
    // Estados del flujo: CARGANDO | PAGO_PENDIENTE | MATRICULA_PENDIENTE | COMPLETADO
    const [estado, setEstado] = useState('CARGANDO'); 
    const [vistaPago, setVistaPago] = useState('OPCIONES'); // 'OPCIONES' o 'FORMULARIO'
    const [procesando, setProcesando] = useState(false);
    
    // Estado del formulario de tarjeta
    const [cardData, setCardData] = useState({ 
        numero: '', caducidad: '', cvv: '', titular: '', email: '' 
    });

    useEffect(() => {
        verificarEstadoGeneral();
    }, []);

    const verificarEstadoGeneral = async () => {
        try {
            // 1. Verificar si ya tiene matrícula académica registrada (Fin del proceso)
            const resMatriculado = await api.get('/matriculas/ya-matriculado');
            if (resMatriculado.data === true) {
                setEstado('COMPLETADO');
                return;
            }

            // 2. Verificar si ya pagó el derecho de matrícula (Paso previo)
            const resPago = await api.get('/matriculas/estado-pago');
            if (resPago.data === true) {
                setEstado('MATRICULA_PENDIENTE'); // Puede elegir cursos
            } else {
                setEstado('PAGO_PENDIENTE'); // Debe pagar derecho
            }
        } catch (error) {
            console.error("Error verificando estado del alumno", error);
            alert("Hubo un error al cargar tu estado. Intenta recargar la página.");
        } finally {
            // Pequeña espera para que no parpadee
            setTimeout(() => setEstado(prev => prev === 'CARGANDO' ? 'PAGO_PENDIENTE' : prev), 500); 
        }
    };

    const handlePagoDerecho = async (e) => {
        e.preventDefault();
        setProcesando(true);
        try {
            // Llamamos al endpoint para pagar el derecho de S/ 350.00
            await api.post('/matriculas/pagar-derecho');
            
            // Si tiene éxito, actualizamos el estado para dejarlo pasar
            alert("¡Pago de derecho de matrícula exitoso! Ahora puedes seleccionar tus cursos.");
            setEstado('MATRICULA_PENDIENTE'); 
        } catch (error) {
            console.error(error);
            alert(error.response?.data?.message || "Error al procesar el pago. Verifica los datos.");
        } finally {
            setProcesando(false);
        }
    };

    const descargarConstanciaActual = async () => {
        try {
            // 1. Obtener la matrícula actual para sacar el ID
            const res = await api.get('/matriculas/actual');
            const matriculaId = res.data.id;
            
            // 2. Descargar el PDF
            const response = await api.get(`/matriculas/${matriculaId}/comprobante`, { responseType: 'blob' });
            
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `Constancia_Matricula_2026-I.pdf`);
            document.body.appendChild(link);
            link.click();
            link.parentNode.removeChild(link);
        } catch (e) {
            alert("No se pudo descargar la constancia. Intenta más tarde.");
        }
    };

    // --- RENDERIZADO CONDICIONAL ---

    if (estado === 'CARGANDO') {
        return (
            <div className="d-flex justify-content-center align-items-center" style={{minHeight: '400px'}}>
                <div className="spinner-border text-uni-red" role="status">
                    <span className="visually-hidden">Cargando...</span>
                </div>
            </div>
        );
    }

    // ESCENARIO 3: YA ESTÁ MATRICULADO
    if (estado === 'COMPLETADO') {
        return (
            <div className="container py-5 text-center">
                <div className="card card-custom mx-auto shadow-sm p-5 border-top border-4 border-success" style={{maxWidth: '650px'}}>
                    <div className="mb-3 text-success">
                        <i className="bi bi-patch-check-fill" style={{fontSize: '5rem'}}></i>
                    </div>
                    <h2 className="fw-bold text-uni-dark mb-3">¡Ya estás matriculado!</h2>
                    <p className="text-muted lead mb-4">
                        Has completado tu inscripción para el ciclo <strong>2026-I</strong> exitosamente.
                    </p>
                    
                    <div className="alert alert-light border d-inline-block text-start mb-4 p-3">
                        <small className="d-block text-muted fw-bold text-uppercase mb-2"><i className="bi bi-info-circle me-1"></i> Siguientes Pasos:</small>
                        <ul className="mb-0 ps-3 small text-secondary">
                            <li>Descarga tu constancia para cualquier trámite.</li>
                            <li>Revisa tu horario en la sección "Mi Horario".</li>
                            <li>Recuerda pagar tu primera pensión antes del vencimiento.</li>
                        </ul>
                    </div>

                    <div className="d-grid gap-3 d-sm-flex justify-content-center">
                        <Link to="/horario" className="btn btn-uni-purple btn-lg px-4 shadow-sm">
                            <i className="bi bi-calendar-week me-2"></i> Ver Mi Horario
                        </Link>
                        <button onClick={descargarConstanciaActual} className="btn btn-outline-success btn-lg px-4">
                            <i className="bi bi-file-earmark-pdf me-2"></i> Descargar Constancia
                        </button>
                    </div>
                </div>
            </div>
        );
    }

    // ESCENARIO 2: SELECCIÓN DE CURSOS (Componente Hijo)
    if (estado === 'MATRICULA_PENDIENTE') {
        return <Matricula />;
    }

    // ESCENARIO 1: DEBE PAGAR EL DERECHO (Renderizado interno)
    
    // Sub-componente: Opciones de Pago
    const renderOpciones = () => (
        <div className="card card-custom mx-auto border-top border-4 border-danger shadow-sm" style={{ maxWidth: '600px' }}>
            <div className="card-body p-5">
                <div className="text-center mb-4">
                    <div className="bg-danger bg-opacity-10 rounded-circle d-inline-flex p-3 mb-3">
                        <i className="bi bi-shield-lock text-uni-red" style={{ fontSize: '3rem' }}></i>
                    </div>
                    <h3 className="fw-bold text-uni-dark">Habilitación de Matrícula</h3>
                    <p className="text-muted">
                        Para poder seleccionar tus cursos y armar tu horario, primero debes abonar el derecho de matrícula del ciclo actual.
                    </p>
                </div>

                <div className="bg-light p-3 rounded mb-4 d-flex justify-content-between align-items-center border">
                    <div>
                        <span className="d-block small text-muted text-uppercase fw-bold">Concepto</span>
                        <span className="fw-bold">Derecho Matrícula 2026-I</span>
                    </div>
                    <div className="text-end">
                        <span className="d-block small text-muted text-uppercase fw-bold">Total a Pagar</span>
                        <span className="fs-3 fw-bold text-uni-dark">S/ 350.00</span>
                    </div>
                </div>

                <div className="list-group mb-3">
                    <button className="list-group-item list-group-item-action p-3 d-flex align-items-center" onClick={() => alert("Acércate al banco con tu código de alumno.")}>
                        <div className="bg-light p-2 rounded me-3"><i className="bi bi-bank fs-4 text-secondary"></i></div>
                        <div><div className="fw-bold">Banca Móvil / Agentes</div><small className="text-muted">BCP, BBVA, Interbank, Scotiabank</small></div>
                    </button>
                    
                    <button 
                        className="list-group-item list-group-item-action p-3 d-flex align-items-center active bg-uni-red border-danger mt-2 shadow-sm"
                        onClick={() => setVistaPago('FORMULARIO')}
                    >
                        <div className="bg-white bg-opacity-25 p-2 rounded me-3"><i className="bi bi-credit-card fs-4 text-white"></i></div>
                        <div><div className="fw-bold">Pagar con Tarjeta</div><small className="text-white-50">Visa, Mastercard, Amex</small></div>
                        <i className="bi bi-chevron-right ms-auto"></i>
                    </button>
                </div>
            </div>
        </div>
    );

    // Sub-componente: Formulario de Tarjeta
    const renderFormulario = () => (
        <div className="card card-custom mx-auto shadow-lg" style={{ maxWidth: '500px' }}>
            <div className="card-header bg-white border-0 pt-4 px-4 d-flex align-items-center">
                <button onClick={() => setVistaPago('OPCIONES')} className="btn btn-sm btn-light me-3 rounded-circle" style={{width:'35px', height:'35px'}}>
                    <i className="bi bi-arrow-left"></i>
                </button>
                <h5 className="mb-0 fw-bold">Pago Seguro en Línea</h5>
            </div>
            
            <div className="card-body p-4 pt-2">
                <div className="alert alert-light border d-flex align-items-center py-2 px-3 mb-4 mt-2">
                    <i className="bi bi-cart-check text-success me-2 fs-5"></i>
                    <div>
                        <small className="d-block text-muted lh-1">Estás pagando:</small>
                        <strong className="text-dark">S/ 350.00</strong>
                    </div>
                </div>

                <form onSubmit={handlePagoDerecho}>
                    <div className="mb-3">
                        <label className="form-label small fw-bold text-muted text-uppercase">Número de Tarjeta</label>
                        <div className="input-group">
                            <span className="input-group-text bg-white text-muted"><i className="bi bi-credit-card"></i></span>
                            <input 
                                type="text" className="form-control border-start-0 ps-1" placeholder="0000 0000 0000 0000" 
                                value={cardData.numero} onChange={e => setCardData({...cardData, numero: e.target.value})} 
                                required maxLength="19" 
                            />
                        </div>
                    </div>
                    
                    <div className="row g-3 mb-3">
                        <div className="col-6">
                            <label className="form-label small fw-bold text-muted text-uppercase">Vencimiento</label>
                            <input type="text" className="form-control text-center" placeholder="MM/YY" 
                                   value={cardData.caducidad} onChange={e => setCardData({...cardData, caducidad: e.target.value})} required maxLength="5" />
                        </div>
                        <div className="col-6">
                            <label className="form-label small fw-bold text-muted text-uppercase">CVV / CVC</label>
                            <div className="input-group">
                                <input type="password" className="form-control text-center" placeholder="123" 
                                       value={cardData.cvv} onChange={e => setCardData({...cardData, cvv: e.target.value})} required maxLength="4" />
                                <span className="input-group-text bg-white text-muted"><i className="bi bi-question-circle"></i></span>
                            </div>
                        </div>
                    </div>
                    
                    <div className="mb-3">
                        <label className="form-label small fw-bold text-muted text-uppercase">Titular de la Tarjeta</label>
                        <input type="text" className="form-control" placeholder="Como aparece en el plástico" 
                               value={cardData.titular} onChange={e => setCardData({...cardData, titular: e.target.value.toUpperCase()})} required />
                    </div>
                    
                    <div className="mb-4">
                        <label className="form-label small fw-bold text-muted text-uppercase">Email</label>
                        <input type="email" className="form-control" placeholder="Para enviar el comprobante" 
                               value={cardData.email} onChange={e => setCardData({...cardData, email: e.target.value})} required />
                    </div>

                    <button type="submit" className="btn btn-uni-red w-100 btn-lg shadow-sm" disabled={procesando}>
                        {procesando ? (
                            <>
                                <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                                Procesando...
                            </>
                        ) : (
                            <>Pagar <span className="fw-bold">S/ 350.00</span></>
                        )}
                    </button>
                    
                    <div className="text-center mt-3">
                        <small className="text-muted"><i className="bi bi-lock-fill me-1"></i> Transacción encriptada de extremo a extremo</small>
                    </div>
                </form>
            </div>
        </div>
    );

    return (
        <div className="container py-5">
            {vistaPago === 'OPCIONES' ? renderOpciones() : renderFormulario()}
        </div>
    );
};

export default ProcesoMatricula;