package ProyUniversidad.UVI.models.usuario.services;

import ProyUniversidad.UVI.dtos.usuario.RegistroUsuarioDTO;
import ProyUniversidad.UVI.models.academico.models.Docente;
import ProyUniversidad.UVI.models.academico.repository.DocenteRepository;
import ProyUniversidad.UVI.models.usuario.models.Alumno;
import ProyUniversidad.UVI.models.usuario.models.DocenteUsuario;
import ProyUniversidad.UVI.models.usuario.models.Usuario;
import ProyUniversidad.UVI.models.usuario.repositorys.AlumnoRepository;
import ProyUniversidad.UVI.models.usuario.repositorys.DocenteUsuarioRepository;
import ProyUniversidad.UVI.models.usuario.repositorys.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UsuarioRepository usuarioRepository;
    private final AlumnoRepository alumnoRepository;
    private final DocenteUsuarioRepository docenteUsuarioRepository;
    private final DocenteRepository docenteAcademicoRepository; 
    private final PasswordEncoder passwordEncoder;


    @Transactional(transactionManager = "mysqlTransactionManager")
    public Usuario crearAdmin(RegistroUsuarioDTO datos) {
        if (usuarioRepository.existsByUsername(datos.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }

        Usuario admin = Usuario.builder()
            .username(datos.getUsername())
            .password(passwordEncoder.encode(datos.getPassword()))
            .rol(Usuario.Rol.ADMIN)
            .nombreCompleto(datos.getNombreCompleto()) 
            .activo(true)
            .build();

        return usuarioRepository.save(admin);
    }

    @Transactional(transactionManager = "mysqlTransactionManager")
    public Alumno crearAlumno(RegistroUsuarioDTO datos, String codigoUniversitario, String carrera) {
        if (usuarioRepository.existsByUsername(datos.getUsername())) {
            throw new RuntimeException("El usuario ya existe");
        }

        Alumno alumno = new Alumno();
        alumno.setUsername(datos.getUsername());
        alumno.setPassword(passwordEncoder.encode(datos.getPassword()));
        alumno.setRol(Usuario.Rol.ALUMNO);
        alumno.setNombreCompleto(datos.getNombreCompleto());
        alumno.setEmailPersonal(datos.getEmail());
        alumno.setCodigoUniversitario(codigoUniversitario);
        alumno.setCarrera(carrera);
        alumno.setCicloActual(1); 
        alumno.setActivo(true);

        return alumnoRepository.save(alumno);
    }


    @Transactional(transactionManager = "mysqlTransactionManager") 
    public DocenteUsuario crearDocente(RegistroUsuarioDTO datos, String codigoDocente, String especialidad) {
        if (usuarioRepository.existsByUsername(datos.getUsername())) {
            throw new RuntimeException("El usuario ya existe");
        }

        DocenteUsuario docenteUser = new DocenteUsuario();
        docenteUser.setUsername(datos.getUsername());
        docenteUser.setPassword(passwordEncoder.encode(datos.getPassword()));
        docenteUser.setRol(Usuario.Rol.DOCENTE);
        docenteUser.setNombreCompleto(datos.getNombreCompleto());
        docenteUser.setEmailPersonal(datos.getEmail());
        docenteUser.setCodigoDocente(codigoDocente);
        docenteUser.setActivo(true);
        
        DocenteUsuario usuarioGuardado = docenteUsuarioRepository.save(docenteUser);

        crearPerfilDocentePostgres(usuarioGuardado, especialidad);

        return usuarioGuardado;
    }

    @Transactional(transactionManager = "postgresqlTransactionManager")
    protected void crearPerfilDocentePostgres(DocenteUsuario usuario, String especialidad) {
        Docente docenteAcademico = Docente.builder()
                .usuarioId(usuario.getId()) 
                .codigoDocente(usuario.getCodigoDocente())
                .nombreCompleto(usuario.getNombreCompleto())
                .especialidad(especialidad)
                .activo(true)
                .build();
        
        docenteAcademicoRepository.save(docenteAcademico);
    }
}