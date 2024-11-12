package org.jala.university.config;

import org.jala.university.application.mapper.FormEntityMapper;
import org.jala.university.application.mapper.LoanEntityMapper;
import org.jala.university.application.service.FormEntityServiceImpl;
import org.jala.university.application.service.LoanEntityServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@ComponentScan(basePackages = "org.jala.university")
public class AppConfig {

    @Bean
    public FormEntityMapper formEntityMapper() {
        // Retorna uma instância de FormEntityMapper
        return new FormEntityMapper();
    }

    @Bean
    public LoanEntityMapper loanEntityMapper() {
        // Retorna uma instância de LoanEntityMapper
        return new LoanEntityMapper();
    }

    @Bean
    public TaskScheduler taskScheduler() {
        // Configura o TaskScheduler para tarefas assíncronas
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        return scheduler;
    }

    @Bean
    public FormEntityServiceImpl formEntityServiceImpl(FormEntityMapper formEntityMapper) {
        // Injeta os beans RepositoryFactory e FormEntityMapper em FormEntityServiceImpl
        return new FormEntityServiceImpl(formEntityMapper);
    }

    @Bean
    public LoanEntityServiceImpl loanEntityServiceImpl(LoanEntityMapper loanEntityMapper, FormEntityMapper formEntityMapper,TaskScheduler taskScheduler) {
        // Injeta os beans RepositoryFactory, LoanEntityMapper e TaskScheduler em LoanEntityServiceImpl
        return new LoanEntityServiceImpl(loanEntityMapper, formEntityMapper, taskScheduler);
    }

}
