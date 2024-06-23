package med.voll.api.domain.paciente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Getter;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.direccion.Direccion;

@Table(name = "pacientes")
@Entity(name = "Paciente")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String email;
    private String documentoIdentidad;
    private String telefono;

    private Boolean activo;
    @Embedded
    private Direccion direccion;

    public Paciente(DatosRegistroPaciente datosRegistroPaciente) {
        this.activo = true;
        this.nombre = datosRegistroPaciente.nombre();
        this.email = datosRegistroPaciente.email();
        this.telefono = datosRegistroPaciente.telefono();
        this.documentoIdentidad = datosRegistroPaciente.documentoIdentidad();
        this.direccion = new Direccion(datosRegistroPaciente.direccion());

    }

    public void actualizarInformacion(DatosActualizarPaciente datosActualizarPaciente){
        if (datosActualizarPaciente.nombre() != null) {
            this.nombre = datosActualizarPaciente.nombre();
        }
        if (datosActualizarPaciente.telefono() != null){
            this.telefono = datosActualizarPaciente.telefono();
        }
        if (datosActualizarPaciente.direccion() != null){
            direccion.actualizarDatos(datosActualizarPaciente.direccion());
        }
    }

    public void inactivar(){
        this.activo = false;
    }
}
