package org.jala.university.config;

import org.jala.university.application.mapper.FormEntityMapper;
import org.jala.university.application.mapper.LoanEntityMapper;
import org.jala.university.application.service.FormEntityService;
import org.jala.university.application.service.FormEntityServiceImpl;
import org.jala.university.application.service.LoanEntityService;
import org.jala.university.application.service.LoanEntityServiceImpl;
import org.jala.university.application.service.LoanResultsService;
import org.jala.university.application.service.LoanResultsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.jala.university.presentation.SpringFXMLLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Configuration
@ComponentScan(basePackages = "org.jala.university")
public class AppConfig {

    @PersistenceContext
    private EntityManager entityManager;

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
    public FormEntityService formEntityService(FormEntityMapper formEntityMapper) {
        // Injeta os beans RepositoryFactory e FormEntityMapper em FormEntityServiceImpl
        return new FormEntityServiceImpl(formEntityMapper);
    }

    @Bean
    public LoanEntityService loanEntityService(LoanEntityMapper loanEntityMapper, FormEntityMapper formEntityMapper,TaskScheduler taskScheduler) {
        // Injeta os beans RepositoryFactory, LoanEntityMapper e TaskScheduler em LoanEntityServiceImpl
        return new LoanEntityServiceImpl(loanEntityMapper, formEntityMapper, taskScheduler);
    }

     @Bean
    public SpringFXMLLoader springFXMLLoader(ApplicationContext context) {
        return new SpringFXMLLoader(context);
    }


    // @Bean
    // public JpaRepositoryFactory jpaRepositoryFactory() {
    //     return new JpaRepositoryFactory(entityManager);
    // }

    // @Bean
    // public AccountRepository accountRepository(JpaRepositoryFactory factory) {
    //     return factory.getRepository(AccountRepository.class);
    // }

    // @Bean
    // public PaymentHistoryMapper paymentHistoryMapper() {
    //     return new PaymentHistoryMapper();
    // }

    // @Bean
    // public PaymentHistoryService paymentHistoryService(PaymentHistoryRepository paymentHistoryRepository, AccountRepository accountRepository, PaymentHistoryMapper paymentHistoryMapper, AccountMapper accountMapper) {
    //     return new PaymentHistoryServiceImpl(paymentHistoryRepository, accountRepository, paymentHistoryMapper, accountMapper);
    // }

    @Bean
    public LoanResultsService loanResultsService() {
        return new LoanResultsServiceImpl();
    }
}
