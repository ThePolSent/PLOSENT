package ProyUniversidad.UVI.config;

import ProyUniversidad.UVI.dtos.usuario.RegistroUsuarioDTO;
import ProyUniversidad.UVI.models.academico.models.*;
import ProyUniversidad.UVI.models.academico.repository.*;
import ProyUniversidad.UVI.models.historial.models.HistorialAcademico;
import ProyUniversidad.UVI.models.historial.repositorys.HistorialAcademicoRepository;
import ProyUniversidad.UVI.models.usuario.models.Alumno;
import ProyUniversidad.UVI.models.usuario.repositorys.UsuarioRepository;
import ProyUniversidad.UVI.models.usuario.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final AdminService adminService;
    private final UsuarioRepository usuarioRepository;
    private final CicloAcademicoRepository cicloRepository;
    private final CursoRepository cursoRepository;
    private final AulaRepository aulaRepository;
    private final DocenteRepository docenteRepository;
    private final SeccionRepository seccionRepository;
    private final HistorialAcademicoRepository historialRepository;

    private List<CicloAcademico> ciclosCreados = new ArrayList<>();
    private List<Curso> cursosCreados = new ArrayList<>();
    private List<Docente> docentesCreados = new ArrayList<>();
    private List<Aula> aulasCreadas = new ArrayList<>();

    @Override
    @Transactional(transactionManager = "postgresqlTransactionManager")
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() > 0) {
            System.out.println(">> DATA SEEDER: Datos detectados. Saltando carga.");
            return;
        }

        System.out.println(">> DATA SEEDER: 🚀 Iniciando Carga Masiva (Corregida)...");

        crearAdmin();
        crearAulas();
        crearCiclos();
        
        crearDocentesClasificados(); 

        crearMallaSistemas(); 
        crearMallaDerecho(); 

        crearSeccionesInteligentes(); 
        crearAlumnosConHistorial(); 

        System.out.println(">> DATA SEEDER: ¡Carga completada sin duplicados!");
    }

    private void crearAdmin() {
        adminService.crearAdmin(RegistroUsuarioDTO.builder()
            .username("admin").password("admin").nombreCompleto("Super Admin").rol("ADMIN").email("admin@uvi.edu.pe").build());
    }

    private void crearDocentesClasificados() {
        crearDocente("DOC-S01", "Luz Teresa Morales Vega", "Sistemas", "Programación");
        crearDocente("DOC-S02", "Luis Antonio Chafloque Avellaneda", "Sistemas", "Ingeniería de Software");
        crearDocente("DOC-S03", "Anaximandro Fernandez Guerrero", "Sistemas", "Gestión de TI");
        crearDocente("DOC-S04", "Roger Niño Morante", "Sistemas", "Inteligencia Artificial");
        crearDocente("DOC-S05", "Alan Turing", "Sistemas", "Algoritmos");

        crearDocente("DOC-C01", "Isaac Newton", "Ciencias", "Física Pura");
        crearDocente("DOC-C02", "Gottfried Leibniz", "Ciencias", "Cálculo");
        crearDocente("DOC-C03", "Marie Curie", "Ciencias", "Química");

        crearDocente("DOC-D01", "Dra. Ana Maria Polo", "Derecho", "Derecho Penal");
        crearDocente("DOC-D02", "Harvey Specter", "Derecho", "Derecho Corporativo");
        
        crearDocente("DOC-H01", "Cesar Vallejo", "Humanidades", "Literatura");
    }

    private void crearDocente(String codigo, String nombre, String departamento, String especialidad) {
        adminService.crearDocente(RegistroUsuarioDTO.builder()
            .username(codigo).password("123456").nombreCompleto(nombre).rol("DOCENTE")
            .email(codigo.toLowerCase() + "@uvi.edu.pe").build(), 
            codigo, especialidad);
        
        Docente d = docenteRepository.findByCodigoDocente(codigo).orElseThrow();
        d.setDepartamento(departamento);
        docentesCreados.add(docenteRepository.save(d));
    }

    private void crearMallaSistemas() {
        // Ciclo 1
        crearCurso(1, "MAT101", "Matemática Básica", 4, "Ciencias", null);
        crearCurso(1, "ING101", "Introducción a Ingeniería", 2, "Sistemas", null);
        crearCurso(1, "QUI101", "Química General", 4, "Ciencias", null);
        crearCurso(1, "ALG100", "Algoritmos Básicos", 4, "Sistemas", null);
        crearCurso(1, "FIS100", "Física General", 4, "Ciencias", null);

        // Ciclo 2
        crearCurso(2, "MAT102", "Cálculo I", 4, "Ciencias", "MAT101");
        crearCurso(2, "PRO101", "Fundamentos de Programación", 4, "Sistemas", "ALG100");
        crearCurso(2, "FIS101", "Física I", 4, "Ciencias", "FIS100");
        crearCurso(2, "EST101", "Estadística General", 3, "Ciencias", "MAT101");
        crearCurso(2, "ORG101", "Organización y Métodos", 3, "Sistemas", "ING101");

        // Ciclo 3
        crearCurso(3, "MAT103", "Cálculo II", 4, "Ciencias", "MAT102");
        crearCurso(3, "FIS102", "Física II", 4, "Ciencias", "FIS101");
        crearCurso(3, "PRO102", "POO", 4, "Sistemas", "PRO101");
        crearCurso(3, "ALG101", "Estructura de Datos", 4, "Sistemas", "PRO101");
        crearCurso(3, "BD101", "Modelado de Datos", 3, "Sistemas", "ING101");

        // Ciclo 4
        crearCurso(4, "PRO103", "Desarrollo Web I", 4, "Sistemas", "PRO102");
        crearCurso(4, "BD102", "Base de Datos I", 4, "Sistemas", "BD101");
        crearCurso(4, "CIR101", "Sistemas Digitales", 3, "Sistemas", "FIS102");
        crearCurso(4, "EST102", "Probabilidad y Estadística", 3, "Ciencias", "EST101");
        crearCurso(4, "REQ101", "Ingeniería de Requisitos", 3, "Sistemas", "ORG101");

        // Ciclo 5
        crearCurso(5, "PRO104", "Desarrollo Web II", 4, "Sistemas", "PRO103");
        crearCurso(5, "BD103", "Base de Datos II", 4, "Sistemas", "BD102");
        crearCurso(5, "RED101", "Redes y Comunicaciones I", 4, "Sistemas", "CIR101");
        crearCurso(5, "ANA101", "Análisis y Diseño", 4, "Sistemas", "REQ101");
        crearCurso(5, "SOP101", "Sistemas Operativos", 3, "Sistemas", "CIR101");

        // Ciclo 6
        crearCurso(6, "ING201", "Ingeniería de Software", 4, "Sistemas", "ANA101");
        crearCurso(6, "INT101", "Inteligencia Artificial", 4, "Sistemas", "PRO104");
        crearCurso(6, "RED102", "Seguridad Informática", 4, "Sistemas", "RED101");
        crearCurso(6, "MOV101", "Desarrollo Móvil", 4, "Sistemas", "PRO104");
        crearCurso(6, "TES101", "Calidad de Software", 3, "Sistemas", "ING201");
    }

    private void crearMallaDerecho() {
        // Ciclo 1
        crearCurso(1, "DER101", "Introducción al Derecho", 4, "Derecho", null);
        crearCurso(1, "HIS101", "Historia del Derecho", 3, "Derecho", null);
        crearCurso(1, "SOC102", "Sociología Jurídica", 3, "Derecho", null);
        // Curso compartido con Sistemas? No, creemos códigos únicos para evitar líos
        crearCurso(1, "LEN101", "Lenguaje y Comunicación", 3, "Humanidades", null); 
        crearCurso(1, "INV100", "Metodología del Estudio", 2, "Humanidades", null);

        // Ciclo 2
        crearCurso(2, "DER102", "Derecho Romano", 4, "Derecho", "DER101");
        crearCurso(2, "CIV101", "Derecho Civil I", 4, "Derecho", "DER101");
        crearCurso(2, "POL101", "Ciencia Política", 3, "Derecho", null);
        crearCurso(2, "ECO102", "Economía y Derecho", 3, "Humanidades", null);
        crearCurso(2, "LEN102", "Argumentación Jurídica", 3, "Humanidades", "LEN101");

        // Ciclo 3 (CÓDIGOS CORREGIDOS PARA NO CHOCAR CON SISTEMAS)
        crearCurso(3, "CIV102", "Derecho Civil II", 4, "Derecho", "CIV101");
        crearCurso(3, "PEN101", "Derecho Penal I", 4, "Derecho", "DER102");
        // Antes era CON101 (choque con Contabilidad), ahora es DCT101
        crearCurso(3, "DCT101", "Derecho Constitucional I", 4, "Derecho", "POL101"); 
        crearCurso(3, "FAM101", "Derecho de Familia", 3, "Derecho", "CIV101");
        crearCurso(3, "ADM101", "Derecho Administrativo I", 3, "Derecho", null);

        // Ciclo 4
        crearCurso(4, "CIV103", "Derecho Civil III", 4, "Derecho", "CIV102");
        crearCurso(4, "PEN102", "Derecho Penal II", 4, "Derecho", "PEN101");
        crearCurso(4, "DCT102", "Derecho Constitucional II", 4, "Derecho", "DCT101");
        // Antes PRO101 (choque), ahora TGP101
        crearCurso(4, "TGP101", "Teoría General del Proceso", 3, "Derecho", "DER101"); 
        crearCurso(4, "ADM102", "Derecho Administrativo II", 3, "Derecho", "ADM101");

        // Ciclo 5
        crearCurso(5, "CIV104", "Derecho Civil IV", 4, "Derecho", "CIV103");
        crearCurso(5, "LAB101", "Derecho Laboral Individual", 4, "Derecho", "DCT102");
        // Antes PRO102 (choque), ahora DPC101
        crearCurso(5, "DPC101", "Derecho Procesal Civil I", 4, "Derecho", "TGP101"); 
        crearCurso(5, "MER101", "Derecho Mercantil I", 3, "Derecho", "CIV102");
        crearCurso(5, "TRIB101", "Derecho Tributario I", 3, "Derecho", "ADM102");

        // Ciclo 6
        crearCurso(6, "CIV105", "Derecho Civil V", 4, "Derecho", "CIV104");
        crearCurso(6, "LAB102", "Derecho Laboral Colectivo", 4, "Derecho", "LAB101");
        // Antes PRO103 (choque), ahora DPP101
        crearCurso(6, "DPP101", "Derecho Procesal Penal I", 4, "Derecho", "PEN102"); 
        crearCurso(6, "MER102", "Derecho Mercantil II", 3, "Derecho", "MER101");
        // Antes INT101 (choque con IA), ahora DIP101
        crearCurso(6, "DIP101", "Derecho Internacional Público", 3, "Derecho", "DCT102"); 
    }

    private void crearCurso(int ciclo, String codigo, String nombre, int creditos, String departamento, String codRequisito) {
        Optional<Curso> existente = cursoRepository.findByCodigo(codigo);
        if (existente.isPresent()) {
            if (!cursosCreados.contains(existente.get())) {
                cursosCreados.add(existente.get());
            }
            return; 
        }

        Curso.CursoBuilder builder = Curso.builder()
            .ciclo(ciclo).codigo(codigo).nombre(nombre).creditos(creditos)
            .horasTeoricas(2).horasPracticas(2).departamento(departamento).activo(true);

        if (codRequisito != null) {
            Curso req = cursosCreados.stream().filter(c -> c.getCodigo().equals(codRequisito)).findFirst()
                        .orElse(cursoRepository.findByCodigo(codRequisito).orElse(null));
            
            if (req != null) builder.prerrequisitos(List.of(req));
        }
        cursosCreados.add(cursoRepository.save(builder.build()));
    }

    private void crearSeccionesInteligentes() {
        CicloAcademico cicloActual = ciclosCreados.stream().filter(CicloAcademico::getActivo).findFirst().orElseThrow();
        Random rand = new Random();

        for (Curso curso : cursosCreados) {
            List<Docente> docentesAptos = docentesCreados.stream()
                .filter(d -> d.getDepartamento().equals(curso.getDepartamento()))
                .collect(Collectors.toList());
            
            if (docentesAptos.isEmpty()) docentesAptos = docentesCreados; // Fallback

            Docente docente = docentesAptos.get(rand.nextInt(docentesAptos.size()));
            Aula aula = aulasCreadas.get(rand.nextInt(aulasCreadas.size()));

            Seccion seccion = Seccion.builder()
                .codigo("A").curso(curso).cicloAcademico(cicloActual).docente(docente)
                .vacantesTotales(30).vacantesOcupadas(0).modalidad("Presencial").activo(true).build();
            
            int horaInicio = 8 + rand.nextInt(10); 
            boolean esLunesMiercoles = rand.nextBoolean();
            
            seccion.setHorarios(List.of(
                Horario.builder().diaSemana(esLunesMiercoles ? "LUNES" : "MARTES").horaInicio(LocalTime.of(horaInicio,0)).horaFin(LocalTime.of(horaInicio+2,0)).aula(aula).seccion(seccion).build(),
                Horario.builder().diaSemana(esLunesMiercoles ? "MIERCOLES" : "JUEVES").horaInicio(LocalTime.of(horaInicio,0)).horaFin(LocalTime.of(horaInicio+2,0)).aula(aula).seccion(seccion).build()
            ));
            seccionRepository.save(seccion);
        }
    }

    private void crearAlumnosConHistorial() {
        crearUnAlumno("U23232323", "Omar Aquino Teppo", 5, "Ingeniería de Sistemas");
        crearUnAlumno("U20210001", "Marcelo Alarcon Manay", 6, "Ingeniería de Sistemas");
        crearUnAlumno("U20220002", "Noemi Cusquisiban Salazar", 4, "Ingeniería de Sistemas");
        crearUnAlumno("U20230003", "Guillermo Vallejos Maradiegue", 3, "Ingeniería de Sistemas");
        crearUnAlumno("U20230004", "Nayeli Castillo", 2, "Derecho");
        crearUnAlumno("U20240005", "Juan Carranza Solano", 1, "Ingeniería de Sistemas");
    }

    private void crearUnAlumno(String codigo, String nombre, int cicloActual, String carrera) {
        Alumno alumno = adminService.crearAlumno(RegistroUsuarioDTO.builder()
            .username(codigo).password("123456").nombreCompleto(nombre).rol("ALUMNO")
            .email(codigo.toLowerCase() + "@uvi.edu.pe").build(), 
            codigo, carrera);
        
        alumno.setCicloActual(cicloActual);
        alumno.setFechaTurnoMatricula(LocalDateTime.now().minusHours(1)); 
        
        if (cicloActual > 1) {
            String depto = carrera.contains("Derecho") ? "Derecho" : "Sistemas";
            generarHistorialAprobado(alumno, cicloActual - 1, depto);
        } else {
            alumno.setPromedioGeneral(0.00);
            usuarioRepository.save(alumno);
        }
    }

    private void generarHistorialAprobado(Alumno alumno, int hastaCiclo, String departamentoBase) {
        CicloAcademico cicloPasado = ciclosCreados.stream().filter(c -> c.getCodigo().equals("2025-II")).findFirst().orElse(null);
        if (cicloPasado == null) return;

        List<HistorialAcademico> historial = new ArrayList<>();
        double sumaPonderada = 0;
        int totalCreditos = 0;
        
        for (Curso c : cursosCreados) {
            boolean esComun = c.getDepartamento().equals("Ciencias") || c.getDepartamento().equals("Humanidades");
            boolean esCarrera = c.getDepartamento().equals(departamentoBase);

            if (c.getCiclo() <= hastaCiclo && (esComun || esCarrera)) {
                double notaRaw = 13.0 + (Math.random() * 7);
                double notaFinal = Math.round(notaRaw * 100.0) / 100.0;
                
                historial.add(HistorialAcademico.builder()
                    .alumnoId(alumno.getId()).cursoId(c.getId()).cicloId(cicloPasado.getId())
                    .nota(notaFinal).estado(HistorialAcademico.EstadoCurso.APROBADO)
                    .fechaRegistro(LocalDateTime.now().minusMonths(6)).build());
                
                sumaPonderada += notaFinal * c.getCreditos();
                totalCreditos += c.getCreditos();
            }
        }
        historialRepository.saveAll(historial);

        if (totalCreditos > 0) {
            double promedio = sumaPonderada / totalCreditos;
            alumno.setPromedioGeneral(Math.round(promedio * 100.0) / 100.0);
            usuarioRepository.save(alumno);
        }
    }

    private void crearAulas() {
        for (int i = 1; i <= 10; i++) {
            Aula a = Aula.builder().codigo("A-" + (100 + i)).nombre("Aula " + i).capacidad(40).piso("1").tipo("Aula").activo(true).build();
            aulasCreadas.add(aulaRepository.save(a));
        }
    }

    private void crearCiclos() {
        crearCiclo("2024-I", false, LocalDate.of(2024, 3, 1));
        crearCiclo("2024-II", false, LocalDate.of(2024, 8, 1));
        crearCiclo("2025-I", false, LocalDate.of(2025, 3, 1));
        crearCiclo("2025-II", false, LocalDate.of(2025, 8, 1)); 
        CicloAcademico actual = CicloAcademico.builder().codigo("2026-I").nombre("Ciclo Regular 2026-I").fechaInicio(LocalDate.now().plusWeeks(2)).fechaFin(LocalDate.now().plusMonths(4)).fechaInicioMatricula(LocalDate.now().minusDays(10)).fechaFinMatricula(LocalDate.now().plusDays(20)).activo(true).build();
        ciclosCreados.add(cicloRepository.save(actual));
    }

    private void crearCiclo(String codigo, boolean activo, LocalDate inicio) {
        CicloAcademico c = CicloAcademico.builder().codigo(codigo).nombre("Ciclo " + codigo).fechaInicio(inicio).fechaFin(inicio.plusMonths(4)).activo(activo).build();
        ciclosCreados.add(cicloRepository.save(c));
    }
}