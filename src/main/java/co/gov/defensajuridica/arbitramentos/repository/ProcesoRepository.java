package co.gov.defensajuridica.arbitramentos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.gov.defensajuridica.arbitramentos.domain.Proceso;

/**
 * Spring Data JPA repository for the Proceso entity.
 */
public interface ProcesoRepository extends JpaRepository<Proceso,Long> {
	
	Proceso findByEstadoActualProceso(Integer estadoActualProceso);

}
