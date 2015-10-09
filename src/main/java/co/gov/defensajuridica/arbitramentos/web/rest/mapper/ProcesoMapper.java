package co.gov.defensajuridica.arbitramentos.web.rest.mapper;

import co.gov.defensajuridica.arbitramentos.domain.*;
import co.gov.defensajuridica.arbitramentos.web.rest.dto.ProcesoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Proceso and its DTO ProcesoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProcesoMapper {

    ProcesoDTO procesoToProcesoDTO(Proceso proceso);

    Proceso procesoDTOToProceso(ProcesoDTO procesoDTO);
}
