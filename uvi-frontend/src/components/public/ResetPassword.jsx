import { useState } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import api from '../../api/axiosConfig';

const ResetPassword = () => {
    const [searchParams] = useSearchParams();
    const token = searchParams.get('token');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const enviar = async (e) => {
        e.preventDefault();
        try {
            await api.post('/public/password/reset', { token, nuevaPassword: password });
            alert("¡Contraseña cambiada! Ingresa con tu nueva clave.");
            navigate('/');
        } catch (error) {
            alert("El enlace ha expirado o es inválido.");
        }
    };

    if (!token) return <div className="alert alert-danger m-5">Enlace inválido.</div>;

    return (
        <div className="container d-flex justify-content-center align-items-center vh-100">
            <div className="card card-custom p-4" style={{maxWidth: '400px'}}>
                <h3 className="fw-bold mb-3">Nueva Contraseña</h3>
                <form onSubmit={enviar}>
                    <div className="mb-3">
                        <label className="form-label">Ingresa tu nueva clave</label>
                        <input type="password" className="form-control" value={password} onChange={e => setPassword(e.target.value)} required minLength="6" />
                    </div>
                    <button type="submit" className="btn btn-uni-purple w-100">Confirmar Cambio</button>
                </form>
            </div>
        </div>
    );
};

export default ResetPassword;