package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.DatosActualizarMedico;
import med.voll.api.domain.medico.DatosListadoMedico;
import med.voll.api.domain.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("pacientes")
public class PacienteController {

    @Autowired
    PacienteRepository pacienteRepository;

    @PostMapping
    @Transactional
    public ResponseEntity registrar(@RequestBody @Valid DatosRegistroPaciente datosRegistroPaciente, UriComponentsBuilder uriComponentsBuilder){
        var paciente = new Paciente(datosRegistroPaciente);
        pacienteRepository.save(paciente);

        URI url = uriComponentsBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(url).body(new DatosDetalladoPaciente(paciente));
    }

    @GetMapping
    public ResponseEntity<Page<DatosListaPaciente>> listar(@PageableDefault(page = 0,size = 10,sort = {"nombre"})
                                           Pageable paginacion){
        return ResponseEntity.ok(pacienteRepository.findByActivoTrue(paginacion).map(DatosListaPaciente::new));
    }

    @PutMapping
    @Transactional
    public ResponseEntity actualizar(@RequestBody @Valid DatosActualizarPaciente datosActualizarPaciente){
        var paciente = pacienteRepository.getReferenceById(datosActualizarPaciente.id());
        paciente.actualizarInformacion(datosActualizarPaciente);

        return ResponseEntity.ok(new DatosDetalladoPaciente(paciente));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity remover(@PathVariable Long id){
        var paciente = pacienteRepository.getReferenceById(id);
        paciente.inactivar();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detallar(@PathVariable Long id){
        var paciente = pacienteRepository.getReferenceById(id);
        return ResponseEntity.ok(new DatosDetalladoPaciente(paciente));
    }

}

