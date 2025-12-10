import { useState } from 'react';
import api from '../../api/axiosConfig';

const RegistroUsuarios = () => {
    const [rol, setRol] = useState('ALUMNO'); 
    const [formData, setFormData] = useState({
        username: '', password: '', nombreCompleto: '', email: '',
        codigo: '', especialidad: '', carrera: '' 
    });
    const [mensaje, setMensaje] = useState(null);

    const handleInputChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const registrar = async (e) => {
        e.preventDefault();
        setMensaje(null);

        const usuarioBase = {
            username: formData.username,
            password: formData.password,
            nombreCompleto: formData.nombreCompleto,
            email: formData.email,
            rol: rol
        };

        let endpoint = '/auth/register'; 
        let payload = usuarioBase;

        if (rol === 'ALUMNO') {
            endpoint = '/admin/crear-alumno';
            payload = { 
                usuario: { ...usuarioBase, rol: 'ALUMNO' }, 
                codigo: formData.codigo, 
                carrera: formData.carrera 
            };
        } else if (rol === 'DOCENTE') {
            endpoint = '/admin/crear-docente';
            payload = { 
                usuario: { ...usuarioBase, rol: 'DOCENTE' }, 
                codigo: formData.codigo, 
                especialidad: formData.especialidad 
            };
        } else {
            endpoint = '/admin/crear-admin';
        }

        try {
            await api.post(endpoint, payload);
            setMensaje({ type: 'success', text: `Usuario ${rol} registrado con éxito.` });
            setFormData({ ...formData, username: '', password: '', nombreCompleto: '', email: '', codigo: '' });
        } catch (error) {
            setMensaje({ type: 'danger', text: 'Error al registrar. El usuario o código ya existe.' });
        }
    };

    return (
        <div className="row justify-content-center">
            <div className="col-md-8">
                <div className="card card-custom">
                    <div className="card-header bg-white border-bottom-0 pt-4 px-4">
                        <h4 className="fw-bold text-uni-dark">Registro de Usuarios</h4>
                        
                        <ul className="nav nav-pills mt-3">
                            {['ALUMNO', 'DOCENTE', 'ADMIN'].map((r) => (
                                <li className="nav-item" key={r}>
                                    <button 
                                        className={`nav-link ${rol === r ? 'active bg-uni-red' : 'text-secondary'}`} 
                                        onClick={() => setRol(r)}
                                    >
                                        {r.charAt(0) + r.slice(1).toLowerCase()}
                                    </button>
                                </li>
                            ))}
                        </ul>
                    </div>
                    
                    <div className="card-body p-4">
                        {mensaje && <div className={`alert alert-${mensaje.type}`}>{mensaje.text}</div>}
                        
                        <form onSubmit={registrar} className="row g-3">
                            <h6 className="text-muted text-uppercase small fw-bold mb-0 mt-4">Datos de Cuenta</h6>
                            <div className="col-md-6">
                                <label className="form-label">Usuario</label>
                                <input type="text" name="username" className="form-control" value={formData.username} onChange={handleInputChange} required />
                            </div>
                            <div className="col-md-6">
                                <label className="form-label">Contraseña</label>
                                <input type="password" name="password" className="form-control" value={formData.password} onChange={handleInputChange} required />
                            </div>

                            <h6 className="text-muted text-uppercase small fw-bold mb-0 mt-4">Datos Personales</h6>
                            <div className="col-12">
                                <label className="form-label">Nombre Completo</label>
                                <input type="text" name="nombreCompleto" className="form-control" value={formData.nombreCompleto} onChange={handleInputChange} required />
                            </div>
                            <div className="col-md-6">
                                <label className="form-label">Email</label>
                                <input type="email" name="email" className="form-control" value={formData.email} onChange={handleInputChange} required />
                            </div>

                            {rol !== 'ADMIN' && (
                                <div className="col-md-6">
                                    <label className="form-label">Código {rol === 'ALUMNO' ? 'Universitario' : 'Docente'}</label>
                                    <input type="text" name="codigo" className="form-control" value={formData.codigo} onChange={handleInputChange} required />
                                </div>
                            )}

                            {rol === 'ALUMNO' && (
                                <div className="col-md-12">
                                    <label className="form-label">Carrera</label>
                                    <input type="text" name="carrera" className="form-control" placeholder="Ej: Ingeniería de Sistemas" value={formData.carrera} onChange={handleInputChange} required />
                                </div>
                            )}

                            {rol === 'DOCENTE' && (
                                <div className="col-md-12">
                                    <label className="form-label">Especialidad</label>
                                    <input type="text" name="especialidad" className="form-control" placeholder="Ej: Matemáticas" value={formData.especialidad} onChange={handleInputChange} required />
                                </div>
                            )}

                            <div className="col-12 mt-4">
                                <button type="submit" className="btn btn-uni-red w-100 btn-lg">Registrar Usuario</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default RegistroUsuarios;