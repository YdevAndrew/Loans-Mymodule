package org.jala.university.config;

import org.jala.university.application.mapper.FormEntityMapper;
import org.jala.university.application.mapper.InstallmentEntityMapper;
import org.jala.university.application.mapper.LoanEntityMapper;
import org.jala.university.application.service.FormEntityService;
import org.jala.university.application.service.FormEntityServiceImpl;
import org.jala.university.application.service.LoanEntityService;
import org.jala.university.application.service.LoanEntityServiceImpl;
import org.jala.university.application.service.LoanResultsService;
import org.jala.university.application.service.LoanResultsServiceImpl;
import org.jala.university.domain.repository.AccountRepository;
import org.jala.university.domain.repository.CustomerRepository;
import org.jala.university.presentation.SpringFXMLLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Configuration
@ComponentScan(basePackages = "org.jala.university")
@EnableJpaRepositories(basePackages = "org.jala.university", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AccountRepository.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CustomerRepository.class)
})
public class LoansAppConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public FormEntityMapper formEntityMapper() {
        return new FormEntityMapper();
    }

    @Bean
    public InstallmentEntityMapper installmentEntityMapper() {
        return new InstallmentEntityMapper();
    }

    @Bean
    public LoanEntityMapper loanEntityMapper() {
        return new LoanEntityMapper();
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        return scheduler;
    }

    @Bean
    public FormEntityService formEntityService(FormEntityMapper formEntityMapper) {
        return new FormEntityServiceImpl(formEntityMapper);
    }

    @Bean
    public LoanEntityService loanEntityService(LoanEntityMapper loanEntityMapper, InstallmentEntityMapper installmentEntityMapper, FormEntityMapper formEntityMapper,
            TaskScheduler taskScheduler) {
        return new LoanEntityServiceImpl(loanEntityMapper,installmentEntityMapper, formEntityMapper, taskScheduler);
    }

    @Bean
    public SpringFXMLLoader springFXMLLoader(ApplicationContext context) {
        return new SpringFXMLLoader(context);
    }

    @Bean
    public LoanResultsService loanResultsService() {
        return new LoanResultsServiceImpl();
    }
}
