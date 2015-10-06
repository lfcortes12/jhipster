package co.gov.defensajuridica.arbitramentos.repository;

import co.gov.defensajuridica.arbitramentos.domain.Usuario;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Usuario entity.
 */
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

}
