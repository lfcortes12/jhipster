package co.gov.defensajuridica.arbitramentos.web.rest;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.gov.defensajuridica.arbitramentos.web.rest.dto.ExecutionDTO;
import co.gov.defensajuridica.arbitramentos.web.rest.dto.TaskDTO;
import co.gov.defensajuridica.arbitramentos.web.rest.mapper.TaskMapper;

import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing Task.
 */
@RestController
@RequestMapping("/api")
public class TaskResource {

    private final Logger log = LoggerFactory.getLogger(TaskResource.class);
    

    /// Camunda dependencies
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private RuntimeService runtimeService;
    
    @RequestMapping(value = "/tasks",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TaskDTO> listarTareas() throws URISyntaxException {
    	
    	List<Task> list = taskService.createTaskQuery().list();
    	runtimeService.createExecutionQuery().list();
    	List<TaskDTO> results = new ArrayList<TaskDTO>();
    	
    	for (Task task : list) {
    		TaskDTO taskDTO = new TaskDTO();
			BeanUtils.copyProperties(task, taskDTO);
			results.add(taskDTO);
		}
    	log.info("CANTIDAD DE TAREAS {}", list.size());
    	return results;
    }
    
    @RequestMapping(value = "/processes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ExecutionDTO> listarProcesos() throws URISyntaxException {
    	List<Execution> list = runtimeService.createExecutionQuery().list();
    	List<ExecutionDTO> results = new ArrayList<ExecutionDTO>();
    	for (Execution execution : list) {
			ExecutionDTO executionDTO = new ExecutionDTO();
			BeanUtils.copyProperties(execution, executionDTO);
			results.add(executionDTO);
		}
    	log.info("CANTIDAD DE INSTANCIAS {}", list.size());
    	return results;
    }

}
