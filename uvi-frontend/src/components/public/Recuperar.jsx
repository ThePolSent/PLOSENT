import { useState } from 'react';
import api from '../../api/axiosConfig';
import { Link } from 'react-router-dom';

const Recuperar = () => {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState(''); 
    const [mensaje, setMensaje] = useState(null);
    const [loading, setLoading] = useState(false);

    const enviar = async (e) => {
        e.preventDefault();
        setLoading(true);
        setMensaje(null);
        try {
            await api.post('/public/password/solicitar', { username, email });
            setMensaje({ type: 'success', text: `¡Enviado! Revisa la bandeja de entrada de ${email}` });
        } catch (error) {
            setMensaje({ type: 'danger', text: 'Error: Usuario no encontrado o fallo en el envío.' });
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container d-flex justify-content-center align-items-center vh-100">
            <div className="card card-custom p-4 shadow-sm border-top border-4 border-danger" style={{maxWidth: '400px', width: '100%'}}>
                <h3 className="fw-bold mb-2">Recuperar Cuenta</h3>
                <p className="text-muted small mb-4">Te enviaremos un enlace temporal para restablecer tu acceso.</p>
                
                {mensaje && <div className={`alert alert-${mensaje.type} small`}>{mensaje.text}</div>}
                
                <form onSubmit={enviar}>
                    <div className="mb-3">
                        <label className="form-label small fw-bold">Usuario / Código</label>
                        <input 
                            type="text" 
                            className="form-control" 
                            placeholder="Ej: cachimbo"
                            value={username} 
                            onChange={e => setUsername(e.target.value)} 
                            required 
                        />
                    </div>

                    <div className="mb-4">
                        <label className="form-label small fw-bold">Enviar enlace a:</label>
                        <input 
                            type="email" 
                            className="form-control" 
                            placeholder="tucorreo@gmail.com"
                            value={email} 
                            onChange={e => setEmail(e.target.value)} 
                            required 
                        />
                        <div className="form-text x-small fst-italic">
                            * Escribe tu correo real para recibir el link.
                        </div>
                    </div>

                    <button type="submit" className="btn btn-uni-red w-100" disabled={loading}>
                        {loading ? 'Enviando...' : 'Enviar Correo'}
                    </button>
                </form>
                <div className="text-center mt-3">
                    <Link to="/" className="text-decoration-none small text-secondary">
                        <i className="bi bi-arrow-left me-1"></i> Volver al Login
                    </Link>
                </div>
            </div>
        </div>
    );
};

export default Recuperar;