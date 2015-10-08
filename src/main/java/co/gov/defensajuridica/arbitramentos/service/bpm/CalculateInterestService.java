package co.gov.defensajuridica.arbitramentos.service.bpm;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CalculateInterestService implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CalculateInterestService.class);

	public void execute(DelegateExecution delegate) throws Exception {
		LOGGER.info("Spring Bean invoked PROCESSSSSSSSSSSSSSS.", delegate);

	}

}
