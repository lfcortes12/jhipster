package co.gov.defensajuridica.arbitramentos.web.rest.dto;

import org.joda.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Proceso entity.
 */
public class ProcesoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3)
    private String descripcionHechos;

    private Integer estadoActualProceso;

    @NotNull
    private LocalDate fechaHechos;

    @NotNull
    @Size(min = 3)
    private String hechos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcionHechos() {
        return descripcionHechos;
    }

    public void setDescripcionHechos(String descripcionHechos) {
        this.descripcionHechos = descripcionHechos;
    }

    public Integer getEstadoActualProceso() {
        return estadoActualProceso;
    }

    public void setEstadoActualProceso(Integer estadoActualProceso) {
        this.estadoActualProceso = estadoActualProceso;
    }

    public LocalDate getFechaHechos() {
        return fechaHechos;
    }

    public void setFechaHechos(LocalDate fechaHechos) {
        this.fechaHechos = fechaHechos;
    }

    public String getHechos() {
        return hechos;
    }

    public void setHechos(String hechos) {
        this.hechos = hechos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProcesoDTO procesoDTO = (ProcesoDTO) o;

        if ( ! Objects.equals(id, procesoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProcesoDTO{" +
                "id=" + id +
                ", descripcionHechos='" + descripcionHechos + "'" +
                ", estadoActualProceso='" + estadoActualProceso + "'" +
                ", fechaHechos='" + fechaHechos + "'" +
                ", hechos='" + hechos + "'" +
                '}';
    }
}
