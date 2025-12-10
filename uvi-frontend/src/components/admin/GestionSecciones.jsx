import { useState, useEffect } from 'react';
import api from '../../api/axiosConfig';

const GestionSecciones = () => {
  const [secciones, setSecciones] = useState([]);
  const [cursos, setCursos] = useState([]);
  const [ciclos, setCiclos] = useState([]);
  const [docentes, setDocentes] = useState([]);

  const [form, setForm] = useState({
    codigo: "",
    cursoId: "",
    cicloId: "",
    docenteId: "",
    vacantesTotales: 30,
    modalidad: "Presencial",
  });
  const [mensaje, setMensaje] = useState(null);

  useEffect(() => {
    cargarDatos();
  }, []);

  const cargarDatos = async () => {
    try {
      const [resSecciones, resCursos, resCiclos, resDocentes] =
        await Promise.all([
          api.get("/secciones"),
          api.get("/cursos"),
          api.get("/ciclos"),
          api.get("/docentes"),
        ]);
      setSecciones(resSecciones.data);
      setCursos(resCursos.data);
      setCiclos(resCiclos.data);
      setDocentes(resDocentes.data);
    } catch (error) {
      console.error("Error cargando datos", error);
      setMensaje({
        type: "danger",
        text: "Error de conexión al cargar las listas.",
      });
    }
  };

  const handleChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const crearSeccion = async (e) => {
    e.preventDefault();
    try {
      const payload = {
        codigo: form.codigo,
        curso: { id: form.cursoId },
        cicloAcademico: { id: form.cicloId },
        docente: { id: form.docenteId },
        vacantesTotales: parseInt(form.vacantesTotales),
        modalidad: form.modalidad,
        activo: true,
      };

      await api.post("/secciones", payload);
      setMensaje({ type: "success", text: "Sección creada exitosamente." });
      cargarDatos();

      setForm({ ...form, codigo: "" });
    } catch (error) {
      setMensaje({
        type: "danger",
        text: "Error al crear. Verifica que no exista una sección con ese código en este ciclo.",
      });
    }
  };

  return (
    <div>
      <h3 className="fw-bold mb-4 text-uni-dark">Programación de Secciones</h3>

      <div className="card card-custom mb-4">
        <div className="card-body">
          {mensaje && (
            <div className={`alert alert-${mensaje.type}`}>{mensaje.text}</div>
          )}

          <h6 className="card-title text-muted mb-3 fw-bold">
            <i className="bi bi-plus-square-dotted me-2"></i> Nueva Sección
          </h6>

          <form onSubmit={crearSeccion} className="row g-3">
            <div className="col-md-2">
              <label className="form-label small fw-bold">Cód. Sección</label>
              <input
                type="text"
                name="codigo"
                className="form-control"
                placeholder="A"
                value={form.codigo}
                onChange={handleChange}
                required
              />
            </div>

            <div className="col-md-5">
              <label className="form-label small fw-bold">Curso</label>
              <select
                name="cursoId"
                className="form-select"
                value={form.cursoId}
                onChange={handleChange}
                required
              >
                <option value="">Seleccionar Curso...</option>
                {cursos.map((c) => (
                  <option key={c.id} value={c.id}>
                    {c.codigo} - {c.nombre}
                  </option>
                ))}
              </select>
            </div>

            <div className="col-md-5">
              <label className="form-label small fw-bold">
                Ciclo Académico
              </label>
              <select
                name="cicloId"
                className="form-select"
                value={form.cicloId}
                onChange={handleChange}
                required
              >
                <option value="">Seleccionar Ciclo...</option>
                {ciclos.map((c) => (
                  <option key={c.id} value={c.id}>
                    {c.codigo} ({c.nombre})
                  </option>
                ))}
              </select>
            </div>

            {/* --- SELECT DE DOCENTES (MEJORADO) --- */}
            <div className="col-md-6">
              <label className="form-label small fw-bold">
                Docente Asignado
              </label>
              <select
                name="docenteId"
                className="form-select"
                value={form.docenteId}
                onChange={handleChange}
                required
              >
                <option value="">Seleccionar Docente...</option>
                {docentes.map((d) => (
                  <option key={d.id} value={d.id}>
                    {d.nombreCompleto} ({d.especialidad})
                  </option>
                ))}
              </select>
            </div>

            <div className="col-md-3">
              <label className="form-label small fw-bold">Vacantes</label>
              <input
                type="number"
                name="vacantesTotales"
                className="form-control"
                value={form.vacantesTotales}
                onChange={handleChange}
                required
              />
            </div>

            <div className="col-md-3">
              <label className="form-label small fw-bold">Modalidad</label>
              <select
                name="modalidad"
                className="form-select"
                value={form.modalidad}
                onChange={handleChange}
              >
                <option value="Presencial">Presencial</option>
                <option value="Virtual">Virtual</option>
                <option value="Hibrido">Híbrido</option>
              </select>
            </div>

            <div className="col-12 text-end mt-4">
              <button type="submit" className="btn btn-uni-purple px-4">
                <i className="bi bi-save me-2"></i> Guardar Programación
              </button>
            </div>
          </form>
        </div>
      </div>

      <div className="card card-custom border-0 shadow-sm">
        <div className="card-body p-0">
          <div className="table-responsive">
            <table className="table table-hover align-middle mb-0">
              <thead className="bg-light text-secondary">
                <tr>
                  <th className="ps-4">Código</th>
                  <th>Curso</th>
                  <th>Ciclo</th>
                  <th>Docente</th>
                  <th>Vacantes</th>
                  <th>Modalidad</th>
                </tr>
              </thead>
              <tbody>
                {secciones.map((s) => (
                  <tr key={s.id}>
                    <td className="ps-4 fw-bold text-uni-red">{s.codigo}</td>

                    <td>
                      <div className="fw-bold">{s.cursoNombre}</div>
                      <small className="text-muted">{s.cursoCodigo}</small>
                    </td>

                    <td>
                      <span className="badge bg-light text-dark border">
                        {s.cicloNombre || s.cicloAcademicoId}
                      </span>
                    </td>

                    <td>{s.docenteNombre}</td>

                    <td>
                      <div className="d-flex align-items-center">
                        <div
                          className="progress flex-grow-1 me-2"
                          style={{ height: "6px" }}
                        >
                          <div
                            className={`progress-bar ${
                              s.vacantesOcupadas >= s.vacantesTotales
                                ? "bg-danger"
                                : "bg-success"
                            }`}
                            role="progressbar"
                            style={{
                              width: `${
                                (s.vacantesOcupadas / s.vacantesTotales) * 100
                              }%`,
                            }}
                          ></div>
                        </div>
                        <small className="text-muted">
                          {s.vacantesOcupadas}/{s.vacantesTotales}
                        </small>
                      </div>
                    </td>

                    <td>
                      <small className="text-muted">{s.modalidad}</small>
                    </td>
                  </tr>
                ))}
                {secciones.length === 0 && (
                  <tr>
                    <td colSpan="6" className="text-center py-4 text-muted">
                      No hay secciones programadas.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

export default GestionSecciones;