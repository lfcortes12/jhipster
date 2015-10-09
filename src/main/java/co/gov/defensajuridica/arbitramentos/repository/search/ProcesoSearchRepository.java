package co.gov.defensajuridica.arbitramentos.repository.search;

import co.gov.defensajuridica.arbitramentos.domain.Proceso;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Proceso entity.
 */
public interface ProcesoSearchRepository extends ElasticsearchRepository<Proceso, Long> {
}
