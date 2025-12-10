import { useState, useEffect } from 'react';
import api from '../../api/axiosConfig';

const PerfilAlumno = () => {
    const [perfil, setPerfil] = useState(null);

    useEffect(() => {
        api.get('/alumnos/perfil').then(res => setPerfil(res.data)).catch(console.error);
    }, []);

    if (!perfil) return <div className="text-center p-5"><div className="spinner-border text-uni-red"></div></div>;

    return (
        <div className="container py-4">
            <h3 className="fw-bold mb-4 text-uni-dark">Mi Perfil</h3>
            <div className="card card-custom border-top border-4 border-danger">
                <div className="card-body p-4">
                    <div className="row align-items-center">
                        <div className="col-md-2 text-center">
                            <div className="bg-light rounded-circle d-inline-flex align-items-center justify-content-center" style={{width: '100px', height: '100px'}}>
                                <i className="bi bi-person text-secondary display-4"></i>
                            </div>
                        </div>
                        <div className="col-md-10">
                            <h2 className="fw-bold text-uni-dark mb-1">{perfil.nombreCompleto}</h2>
                            <p className="text-muted mb-0"><i className="bi bi-envelope me-2"></i>{perfil.emailPersonal}</p>
                        </div>
                    </div>
                    <hr className="my-4" />
                    <div className="row g-4">
                        <div className="col-md-4">
                            <div className="p-3 bg-light rounded">
                                <small className="text-muted text-uppercase fw-bold">Código</small>
                                <div className="fs-5 fw-bold">{perfil.codigoUniversitario}</div>
                            </div>
                        </div>
                        <div className="col-md-4">
                            <div className="p-3 bg-light rounded">
                                <small className="text-muted text-uppercase fw-bold">Carrera</small>
                                <div className="fs-5 fw-bold text-uni-purple">{perfil.carrera}</div>
                            </div>
                        </div>
                        <div className="col-md-4">
                            <div className="p-3 bg-light rounded">
                                <small className="text-muted text-uppercase fw-bold">Ciclo Actual</small>
                                <div className="fs-5 fw-bold">{perfil.cicloActual || 1}° Ciclo</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default PerfilAlumno;