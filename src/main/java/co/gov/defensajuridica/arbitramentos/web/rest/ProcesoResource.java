package co.gov.defensajuridica.arbitramentos.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryString;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.validation.Valid;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.gov.defensajuridica.arbitramentos.domain.Proceso;
import co.gov.defensajuridica.arbitramentos.repository.ProcesoRepository;
import co.gov.defensajuridica.arbitramentos.repository.search.ProcesoSearchRepository;
import co.gov.defensajuridica.arbitramentos.web.rest.dto.ProcesoDTO;
import co.gov.defensajuridica.arbitramentos.web.rest.mapper.ProcesoMapper;
import co.gov.defensajuridica.arbitramentos.web.rest.util.HeaderUtil;
import co.gov.defensajuridica.arbitramentos.web.rest.util.PaginationUtil;

import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing Proceso.
 */
@RestController
@RequestMapping("/api")
public class ProcesoResource {

    private final Logger log = LoggerFactory.getLogger(ProcesoResource.class);

    @Inject
    private ProcesoRepository procesoRepository;

    @Inject
    private ProcesoMapper procesoMapper;

    @Inject
    private ProcesoSearchRepository procesoSearchRepository;
    
    // Camunda dependencies
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private RuntimeService runtimeService;

    /**
     * POST  /procesos -> Create a new proceso.
     */
    @RequestMapping(value = "/procesos",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProcesoDTO> createProceso(@Valid @RequestBody ProcesoDTO procesoDTO) throws URISyntaxException {
        log.debug("REST request to save Proceso : {}", procesoDTO);
        if (procesoDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new proceso cannot already have an ID").body(null);
        }
        Proceso proceso = procesoMapper.procesoDTOToProceso(procesoDTO);
        Proceso result = procesoRepository.save(proceso);
        
      //starts a Sample process
        ProcessInstance processInstance = runtimeService
        	      .startProcessInstanceByKey("Sample");
        log.info("started {}", processInstance);
       

        Task task = taskService.createTaskQuery()
          .processInstanceId(processInstance.getId()).singleResult();
        
        result.setEstadoActualProceso(new Integer(task.getId()));
        
        log.info("signaled {}", task.getId());
        
        procesoRepository.save(result);
        
        return ResponseEntity.created(new URI("/api/procesos/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("proceso", result.getId().toString()))
                .body(procesoMapper.procesoToProcesoDTO(result));
    }

    /**
     * PUT  /procesos -> Updates an existing proceso.
     */
    @RequestMapping(value = "/procesos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProcesoDTO> updateProceso(@Valid @RequestBody ProcesoDTO procesoDTO) throws URISyntaxException {
        log.debug("REST request to update Proceso : {}", procesoDTO);
        if (procesoDTO.getId() == null) {
            return createProceso(procesoDTO);
        }
        Proceso proceso = procesoMapper.procesoDTOToProceso(procesoDTO);
        Proceso result = procesoRepository.save(proceso);
        procesoSearchRepository.save(proceso);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("proceso", procesoDTO.getId().toString()))
                .body(procesoMapper.procesoToProcesoDTO(result));
    }

    /**
     * GET  /procesos -> get all the procesos.
     */
    @RequestMapping(value = "/procesos",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ProcesoDTO>> getAllProcesos(Pageable pageable)
        throws URISyntaxException {
        Page<Proceso> page = procesoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/procesos");
        return new ResponseEntity<>(page.getContent().stream()
            .map(procesoMapper::procesoToProcesoDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /procesos/:id -> get the "id" proceso.
     */
    @RequestMapping(value = "/procesos/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProcesoDTO> getProceso(@PathVariable Long id) {
        log.debug("REST request to get Proceso : {}", id);
        return Optional.ofNullable(procesoRepository.findOne(id))
            .map(procesoMapper::procesoToProcesoDTO)
            .map(procesoDTO -> new ResponseEntity<>(
                procesoDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /procesos/:id -> delete the "id" proceso.
     */
    @RequestMapping(value = "/procesos/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProceso(@PathVariable Long id) {
        log.debug("REST request to delete Proceso : {}", id);
        procesoRepository.delete(id);
        procesoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("proceso", id.toString())).build();
    }

    /**
     * SEARCH  /_search/procesos/:query -> search for the proceso corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/procesos/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Proceso> searchProcesos(@PathVariable String query) {
        return StreamSupport
            .stream(procesoSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
    
    @RequestMapping(value = "/procesos/aprobar/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Proceso> aprobarProceso(@PathVariable Long id) {
    	log.info("APROBARRRRR INICIOOOO {}", id);
    	Proceso proceso = procesoRepository.findOne(id);
    	
    	if(null != proceso) {
    		Task task = taskService.createTaskQuery().taskId(proceso.getEstadoActualProceso()+"").singleResult();
    		runtimeService.signal(task.getExecutionId());
    	}
    	
    	
        return StreamSupport
            .stream(procesoSearchRepository.search(queryString("")).spliterator(), false)
            .collect(Collectors.toList());
    }
}
