package co.gov.defensajuridica.arbitramentos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * Class that overrides the scheduling configuration allowing us to schedule our own scheduled beans keeping
 * Spring boot's scheduler working normally. This configuration allow us to change the time for running the start
 * and end system services dynamically.
 */
@Configuration
@EnableScheduling
public class CashRegisterSchedulingConfigurer implements SchedulingConfigurer {

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(taskScheduler());
    }
}