package co.gov.defensajuridica.arbitramentos.web.rest.mapper;

import co.gov.defensajuridica.arbitramentos.domain.*;
import co.gov.defensajuridica.arbitramentos.web.rest.dto.UsuarioDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Usuario and its DTO UsuarioDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UsuarioMapper {

    UsuarioDTO usuarioToUsuarioDTO(Usuario usuario);

    Usuario usuarioDTOToUsuario(UsuarioDTO usuarioDTO);
}
