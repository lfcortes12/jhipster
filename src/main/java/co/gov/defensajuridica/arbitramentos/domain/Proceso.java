package co.gov.defensajuridica.arbitramentos.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import co.gov.defensajuridica.arbitramentos.domain.util.CustomLocalDateSerializer;
import co.gov.defensajuridica.arbitramentos.domain.util.ISO8601LocalDateDeserializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Proceso.
 */
@Entity
@Table(name = "PROCESO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="proceso")
public class Proceso implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 3)        
    @Column(name = "descripcion_hechos", nullable = false)
    private String descripcionHechos;
    
    @Column(name = "estado_actual_proceso")
    private Integer estadoActualProceso;

    @NotNull        
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
    @Column(name = "fecha_hechos", nullable = false)
    private LocalDate fechaHechos;

    @NotNull
    @Size(min = 3)        
    @Column(name = "hechos", nullable = false)
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

        Proceso proceso = (Proceso) o;

        if ( ! Objects.equals(id, proceso.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Proceso{" +
                "id=" + id +
                ", descripcionHechos='" + descripcionHechos + "'" +
                ", estadoActualProceso='" + estadoActualProceso + "'" +
                ", fechaHechos='" + fechaHechos + "'" +
                ", hechos='" + hechos + "'" +
                '}';
    }
}
