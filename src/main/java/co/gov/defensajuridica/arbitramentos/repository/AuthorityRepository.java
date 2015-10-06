package co.gov.defensajuridica.arbitramentos.repository;

import co.gov.defensajuridica.arbitramentos.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
