package org.jala.university.config;

import org.jala.university.application.mapper.FormEntityMapper;
import org.jala.university.application.mapper.LoanEntityMapper;
import org.jala.university.application.service.FormEntityServiceImpl;
import org.jala.university.application.service.LoanEntityServiceImpl;
import org.jala.university.infrastructure.persistance.RepositoryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@ComponentScan(basePackages = "org.jala.university")
public class AppConfig {



    @Bean
    public RepositoryFactory repositoryFactory() {
        // Constrói o RepositoryFactory com os argumentos necessários
        return new RepositoryFactory();
    }

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
    public FormEntityServiceImpl formEntityServiceImpl(RepositoryFactory repositoryFactory, FormEntityMapper formEntityMapper) {
        // Injeta os beans RepositoryFactory e FormEntityMapper em FormEntityServiceImpl
        return new FormEntityServiceImpl(repositoryFactory, formEntityMapper);
    }

    @Bean
    public LoanEntityServiceImpl loanEntityServiceImpl(RepositoryFactory repositoryFactory, LoanEntityMapper loanEntityMapper, TaskScheduler taskScheduler) {
        // Injeta os beans RepositoryFactory, LoanEntityMapper e TaskScheduler em LoanEntityServiceImpl
        return new LoanEntityServiceImpl(repositoryFactory, loanEntityMapper, taskScheduler);
    }


}
