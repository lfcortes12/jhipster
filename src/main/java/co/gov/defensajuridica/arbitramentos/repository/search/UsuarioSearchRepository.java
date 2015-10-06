package co.gov.defensajuridica.arbitramentos.repository.search;

import co.gov.defensajuridica.arbitramentos.domain.Usuario;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Usuario entity.
 */
public interface UsuarioSearchRepository extends ElasticsearchRepository<Usuario, Long> {
}
