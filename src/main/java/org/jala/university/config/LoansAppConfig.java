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

/**
 * This class serves as the configuration class for the Loans
 * application. It defines bean configurations and component
 * scanning rules.
 */
@Configuration
@ComponentScan(basePackages = "org.jala.university")
@EnableJpaRepositories(basePackages = "org.jala.university",
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                        classes = AccountRepository.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                        classes = CustomerRepository.class)
        })
public class LoansAppConfig {

    /**
     * The entity manager for handling persistent entities.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Creates a bean for the {@link FormEntityMapper}.
     *
     * @return A new instance of {@link FormEntityMapper}.
     */
    @Bean
    public FormEntityMapper formEntityMapper() {
        return new FormEntityMapper();
    }

    /**
     * Creates a bean for the {@link InstallmentEntityMapper}.
     *
     * @return A new instance of {@link InstallmentEntityMapper}.
     */
    @Bean
    public InstallmentEntityMapper installmentEntityMapper() {
        return new InstallmentEntityMapper();
    }

    /**
     * Creates a bean for the {@link LoanEntityMapper}.
     *
     * @return A new instance of {@link LoanEntityMapper}.
     */
    @Bean
    public LoanEntityMapper loanEntityMapper() {
        return new LoanEntityMapper();
    }

    /**
     * Creates a bean for the {@link TaskScheduler}.
     *
     * @return A new instance of {@link ThreadPoolTaskScheduler}.
     */
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        // MagicNumber: Replace with constant
        scheduler.setPoolSize(10);
        return scheduler;
    }

    /**
     * Creates a bean for the {@link FormEntityService}.
     *
     * @param formEntityMapper The {@link FormEntityMapper}
     *                         to be used by the service.
     * @return A new instance of {@link FormEntityServiceImpl}.
     */
    @Bean
    public FormEntityService formEntityService(
            final FormEntityMapper formEntityMapper) {
        return new FormEntityServiceImpl(formEntityMapper);
    }

    /**
     * Creates a bean for the {@link LoanEntityService}.
     *
     * @param loanEntityMapper        The {@link LoanEntityMapper}
     *                                to be used by the service.
     * @param installmentEntityMapper The {@link InstallmentEntityMapper}
     *                                to be used by the service.
     * @param formEntityMapper        The {@link FormEntityMapper}
     *                                to be used by the service.
     * @param taskScheduler           The {@link TaskScheduler}
     *                                to be used by the service.
     * @return A new instance of {@link LoanEntityServiceImpl}.
     */
    @Bean
    public LoanEntityService loanEntityService(
            final LoanEntityMapper loanEntityMapper,
            final InstallmentEntityMapper installmentEntityMapper,
            final FormEntityMapper formEntityMapper,
            final TaskScheduler taskScheduler) {
        return new LoanEntityServiceImpl(loanEntityMapper,
                installmentEntityMapper, formEntityMapper, taskScheduler);
    }

    /**
     * Creates a bean for the {@link SpringFXMLLoader}.
     *
     * @param context The {@link ApplicationContext} to be used
     *                by the loader.
     * @return A new instance of {@link SpringFXMLLoader}.
     */
    @Bean
    public SpringFXMLLoader springFXMLLoader(
            final ApplicationContext context) {
        return new SpringFXMLLoader(context);
    }

    /**
     * Creates a bean for the {@link LoanResultsService}.
     *
     * @return A new instance of {@link LoanResultsServiceImpl}.
     */
    @Bean
    public LoanResultsService loanResultsService() {
        return new LoanResultsServiceImpl();
    }
}