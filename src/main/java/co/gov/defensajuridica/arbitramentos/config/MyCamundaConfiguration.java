package co.gov.defensajuridica.arbitramentos.config;

import org.camunda.bpm.spring.boot.starter.configuration.CamundaConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class MyCamundaConfiguration {

        @Bean
        public ThreadPoolTaskScheduler taskExecutor() {
                return new ThreadPoolTaskScheduler();
        }

        @Bean
        @Order(CamundaConfiguration.DEFAULT_ORDER + 1)
        public static CamundaConfiguration myCustomConfiguration() {
                return new MyCustomConfiguration();
        }

}