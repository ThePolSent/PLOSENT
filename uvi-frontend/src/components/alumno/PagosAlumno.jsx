import { useState, useEffect } from 'react';
import api from '../../api/axiosConfig';

const PagosAlumno = () => {
    const [pendientes, setPendientes] = useState([]);
    const [step, setStep] = useState(1); // 1: Lista, 2: Opciones, 3: Tarjeta, 4: Exito
    const [seleccionada, setSeleccionada] = useState(null);
    const [loading, setLoading] = useState(false);
    
    // Datos del formulario de pago
    const [cardData, setCardData] = useState({ numero: '', caducidad: '', cvv: '', titular: '', email: '' });

    useEffect(() => {
        cargarDeudas();
    }, []);

    const cargarDeudas = async () => {
        try {
            // Endpoint unificado que trae Derechos + Pensiones
            const res = await api.get('/matriculas/deudas');
            setPendientes(res.data);
        } catch (error) { 
            console.error("Error cargando deudas", error); 
        }
    };

    const iniciarPago = (deuda) => {
        setSeleccionada(deuda);
        setStep(2); // Pasar a opciones de pago
    };

    const procesarPago = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            // Usamos el endpoint unificado pagar-online
            await api.post('/matriculas/pagar-online', {
                matriculaId: seleccionada.id, // Puede ser ID de cuota o "DERECHO-X"
                ...cardData
            });
            setStep(4); // Pantalla de éxito
            cargarDeudas(); // Actualizar la lista (la deuda pagada desaparecerá)
        } catch (error) {
            console.error(error);
            alert("Error al procesar el pago. Verifique los datos.");
        } finally {
            setLoading(false);
        }
    };

    // --- VISTA 1: LISTA DE DEUDAS ---
    if (step === 1) return (
        <div>
            <h3 className="fw-bold mb-4 text-uni-dark">Mis Pagos Pendientes</h3>
            
            {pendientes.length === 0 ? (
                <div className="alert alert-success border-0 shadow-sm p-4 text-center">
                    <i className="bi bi-check-circle-fill display-4 mb-3 d-block text-success"></i>
                    <h5 className="fw-bold">¡Estás al día!</h5>
                    <p className="mb-0">No tienes pagos pendientes por el momento.</p>
                </div>
            ) : (
                <div className="row g-4">
                    {pendientes.map(p => (
                        <div key={p.id} className="col-md-6 col-lg-4">
                            <div className="card card-custom h-100 border-start border-4 border-danger">
                                <div className="card-body">
                                    <div className="d-flex justify-content-between mb-2">
                                        <span className={`badge ${p.tipo === 'DERECHO' ? 'bg-warning text-dark' : 'bg-secondary'}`}>
                                            {p.tipo === 'DERECHO' ? 'Trámite' : 'Mensualidad'}
                                        </span>
                                        <small className="text-danger fw-bold">Vence: {p.vencimiento}</small>
                                    </div>
                                    <h5 className="fw-bold text-uni-dark mb-3">{p.concepto}</h5>
                                    
                                    <div className="d-flex justify-content-between align-items-end mt-4">
                                        <div>
                                            <small className="text-muted d-block">Monto a pagar</small>
                                            <span className="fs-3 fw-bold text-uni-red">S/ {p.monto.toFixed(2)}</span>
                                        </div>
                                        <button onClick={() => iniciarPago(p)} className="btn btn-outline-danger">
                                            Pagar <i className="bi bi-chevron-right"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );

    // --- VISTA 2: OPCIONES DE PAGO ---
    if (step === 2) return (
        <div className="row justify-content-center">
            <div className="col-md-8 col-lg-6">
                <button onClick={() => setStep(1)} className="btn btn-link text-muted mb-3 ps-0 text-decoration-none">
                    <i className="bi bi-arrow-left me-1"></i> Volver a mis deudas
                </button>
                
                <div className="card card-custom">
                    <div className="card-body p-4">
                        <h4 className="fw-bold mb-1">Método de Pago</h4>
                        <p className="text-muted mb-4">Elige cómo deseas pagar <strong>{seleccionada.concepto}</strong></p>
                        
                        <div className="list-group">
                            <button className="list-group-item list-group-item-action p-3 d-flex align-items-center">
                                <div className="bg-light rounded p-2 me-3"><i className="bi bi-phone fs-4"></i></div>
                                <div><div className="fw-bold">Yape / Plin</div><small>Escanea el QR</small></div>
                            </button>
                            <button 
                                className="list-group-item list-group-item-action p-3 d-flex align-items-center active bg-uni-purple border-0"
                                onClick={() => setStep(3)}
                            >
                                <div className="bg-white bg-opacity-25 rounded p-2 me-3"><i className="bi bi-credit-card fs-4 text-white"></i></div>
                                <div><div className="fw-bold">Tarjeta de Crédito/Débito</div><small className="text-white-50">Visa, Mastercard</small></div>
                                <i className="bi bi-chevron-right ms-auto"></i>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );

    // --- VISTA 3: FORMULARIO DE TARJETA ---
    if (step === 3) return (
        <div className="row justify-content-center">
            <div className="col-md-6 col-lg-5">
                <div className="card card-custom shadow-lg">
                    <div className="card-header bg-white border-0 pt-4 px-4">
                        <button onClick={() => setStep(2)} className="btn btn-sm btn-light mb-2"><i className="bi bi-arrow-left"></i> Atrás</button>
                        <h5 className="fw-bold">Ingresa los datos de tu tarjeta</h5>
                    </div>
                    <div className="card-body p-4 pt-0">
                        <div className="bg-light p-3 rounded mb-4 mt-2 d-flex justify-content-between align-items-center">
                            <span>Total:</span>
                            <span className="fs-4 fw-bold text-dark">S/ {seleccionada.monto.toFixed(2)}</span>
                        </div>

                        <form onSubmit={procesarPago}>
                            <div className="mb-3">
                                <label className="form-label small fw-bold">Número</label>
                                <input type="text" className="form-control" placeholder="0000 0000 0000 0000" 
                                       value={cardData.numero} onChange={e => setCardData({...cardData, numero: e.target.value})} required maxLength="16" />
                            </div>
                            <div className="row g-3 mb-3">
                                <div className="col-6">
                                    <label className="form-label small fw-bold">Vencimiento</label>
                                    <input type="text" className="form-control" placeholder="MM/YY" 
                                           value={cardData.caducidad} onChange={e => setCardData({...cardData, caducidad: e.target.value})} required maxLength="5" />
                                </div>
                                <div className="col-6">
                                    <label className="form-label small fw-bold">CVV</label>
                                    <input type="password" className="form-control" placeholder="123" 
                                           value={cardData.cvv} onChange={e => setCardData({...cardData, cvv: e.target.value})} required maxLength="3" />
                                </div>
                            </div>
                            <div className="mb-3">
                                <label className="form-label small fw-bold">Titular</label>
                                <input type="text" className="form-control" placeholder="Nombre en la tarjeta" 
                                       value={cardData.titular} onChange={e => setCardData({...cardData, titular: e.target.value})} required />
                            </div>
                            <div className="mb-4">
                                <label className="form-label small fw-bold">Email</label>
                                <input type="email" className="form-control" placeholder="correo@ejemplo.com" 
                                       value={cardData.email} onChange={e => setCardData({...cardData, email: e.target.value})} required />
                            </div>

                            <button type="submit" className="btn btn-uni-purple w-100 btn-lg shadow-sm" disabled={loading}>
                                {loading ? 'Procesando...' : 'Pagar Ahora'}
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );

    // --- VISTA 4: ÉXITO ---
    if (step === 4) return (
        <div className="text-center py-5">
            <div className="mb-4">
                <div className="d-inline-flex align-items-center justify-content-center bg-success bg-opacity-10 rounded-circle" style={{width: '100px', height: '100px'}}>
                    <i className="bi bi-check-lg text-success display-3"></i>
                </div>
            </div>
            <h2 className="fw-bold text-uni-dark">¡Pago Exitoso!</h2>
            <p className="text-muted lead">La operación se realizó correctamente.</p>
            <button onClick={() => setStep(1)} className="btn btn-outline-primary mt-3 px-5">
                Volver a mis pagos
            </button>
        </div>
    );
};

export default PagosAlumno;