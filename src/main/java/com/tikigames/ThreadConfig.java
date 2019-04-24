package com.tikigames;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class ThreadConfig {

	@Value("${config.thread.maxpoolsize:4}")
	private Integer MAX_POOL_SIZE;

	@Value("${config.thread.corepoolsize:4}")
	private Integer CORE_POOL_SIZE;

	@Value("${config.thread.queuecapacity:25}")
	private Integer QUEUE_CAPACITY;

	@Value("${config.thread.threadnameprefix:background_task}")
	private String THREAD_NAME_PREFIX;

	/**
	 * initialize and configure the task executer.
	 * 
	 * @Bean(name) parameter : to specify a name for the pool in order to be able to run tasks in seperate pools.
	 * In our case it is not that useful. because wee need only one pool.
	 * 
	 * @return the task executer after initializing and configuring it.
	 */
	@Bean(name = "taskExecuter")
	public TaskExecutor threadPoolTaskExecutor() {

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

		executor.setMaxPoolSize(MAX_POOL_SIZE);
		executor.setCorePoolSize(CORE_POOL_SIZE);
		executor.setQueueCapacity(QUEUE_CAPACITY);
		executor.setThreadNamePrefix(THREAD_NAME_PREFIX);

		executor.initialize();

		return executor;
	}
}
