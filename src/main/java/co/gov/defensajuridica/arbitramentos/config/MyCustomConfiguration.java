package co.gov.defensajuridica.arbitramentos.config;

import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.CamundaConfiguration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(CamundaConfiguration.DEFAULT_ORDER + 1)
public class MyCustomConfiguration implements CamundaConfiguration {

        @Override
        public void apply(SpringProcessEngineConfiguration configuration) {
                //...
        }

}