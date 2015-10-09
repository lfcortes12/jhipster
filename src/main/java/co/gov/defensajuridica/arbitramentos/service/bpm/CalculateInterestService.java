package co.gov.defensajuridica.arbitramentos.service.bpm;

import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.gov.defensajuridica.arbitramentos.domain.Proceso;
import co.gov.defensajuridica.arbitramentos.repository.ProcesoRepository;

@Component
public class CalculateInterestService implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CalculateInterestService.class);
	
	@Autowired
	private ProcesoRepository procesoRepository;
	
	@Autowired
    private TaskService taskService;

	public void execute(DelegateExecution delegate) throws Exception {
		LOGGER.info("Spring Bean invoked PROCESSSSSSSSSSSSSSS. {} {} {}", delegate.getProcessInstance(), delegate.getCurrentActivityId(), delegate.getActivityInstanceId());
		Task task = taskService.createTaskQuery()
		          .processInstanceId(delegate.getProcessInstance().getId()).singleResult();
		
		if (null != task) {
			Proceso proceso = procesoRepository
					.findByEstadoActualProceso(new Integer(task.getId()));
			if (null != proceso) {
				proceso.setEstadoActualProceso(-1);
			}
		}

	}

}
