package com.develop.backend.insfraestructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

@Configuration
public class AsyncExecutorConfig {
    @Bean(name = "emailExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // Hilos iniciales
        executor.setMaxPoolSize(10); // Máximo de hilos concurrentes
        executor.setQueueCapacity(50); // Capacidad de la cola
        executor.setThreadNamePrefix("AsyncEmail-");
        executor.initialize();
        return executor;
    }
}
