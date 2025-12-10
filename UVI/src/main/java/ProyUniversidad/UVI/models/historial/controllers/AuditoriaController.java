package ProyUniversidad.UVI.models.historial.controllers;

import ProyUniversidad.UVI.models.historial.models.LogAuditoria;
import ProyUniversidad.UVI.models.historial.repositorys.LogAuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") 
public class AuditoriaController {

    private final LogAuditoriaRepository logRepository;

    @GetMapping
    public ResponseEntity<List<LogAuditoria>> listarLogs() {
        return ResponseEntity.ok(logRepository.findAll(Sort.by(Sort.Direction.DESC, "fechaHora")));
    }
}