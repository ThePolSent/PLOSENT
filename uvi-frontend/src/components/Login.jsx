import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom'; // <--- 1. Importar Link
import api from '../api/axiosConfig';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        setError('');
        try {
            const response = await api.post('/auth/login', { username, password });
            localStorage.setItem('token', response.data.accessToken);
            localStorage.setItem('rol', response.data.rol);
            localStorage.setItem('usuario', response.data.username);
            navigate('/dashboard');
        } catch (err) {
            console.error(err);
            setError('Credenciales incorrectas o error de conexión.');
        }
    };

    return (
        <div className="container-fluid vh-100 d-flex" style={{ backgroundColor: '#fff' }}>
            {/* Mitad Izquierda: Branding */}
            <div className="d-none d-md-flex col-md-6 bg-uni-red align-items-center justify-content-center text-white">
                <div className="text-center">
                    <h1 className="display-1 fw-bold">UVI <span className="fw-light">+portal</span></h1>
                    <p className="lead">Bienvenido a tu plataforma universitaria</p>
                </div>
            </div>

            {/* Mitad Derecha: Formulario */}
            <div className="col-12 col-md-6 d-flex align-items-center justify-content-center">
                <div className="w-75" style={{ maxWidth: '400px' }}>
                    <div className="mb-5 text-center text-md-start">
                        <h2 className="fw-bold text-uni-dark">Iniciar Sesión</h2>
                        <p className="text-muted">Ingresa tus credenciales institucionales</p>
                    </div>

                    {error && <div className="alert alert-danger">{error}</div>}

                    <form onSubmit={handleLogin}>
                        <div className="mb-3">
                            <label className="form-label fw-bold small text-muted">USUARIO / CÓDIGO</label>
                            <input 
                                type="text" 
                                className="form-control form-control-lg bg-light border-0"
                                placeholder="Ej: U123456"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                required 
                            />
                        </div>
                        <div className="mb-4">
                            <label className="form-label fw-bold small text-muted">CONTRASEÑA</label>
                            <input 
                                type="password" 
                                className="form-control form-control-lg bg-light border-0"
                                placeholder="••••••••"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required 
                            />
                        </div>
                        
                        <button type="submit" className="btn btn-uni-red w-100 btn-lg mb-4 shadow-sm">
                            Ingresar
                        </button>
                        
                        <div className="text-center border-top pt-3">
                            {/* --- 2. AQUÍ ESTÁ EL ENLACE CONECTADO --- */}
                            <Link to="/recuperar" className="text-decoration-none text-muted small fw-bold">
                                ¿Olvidaste tu contraseña?
                            </Link>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default Login;