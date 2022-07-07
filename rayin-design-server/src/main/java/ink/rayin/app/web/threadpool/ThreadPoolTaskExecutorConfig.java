package ink.rayin.app.web.threadpool;

import ink.rayin.app.web.exception.BaseGlobalAsyncExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置类
 * Created by tangyongmao on 2019-6-14 15:32:27.
 */
@Slf4j
@Configuration
@EnableAsync
public class ThreadPoolTaskExecutorConfig {

    /** 线程池核心线程数 */
    @Value(value = "${ecs.common.threadPool.corePoolSize:5}")
    int corePoolSize;

    /** 线程池最大线程数 */
    @Value(value = "${ecs.common.threadPool.maxPool:10}")
    int maxPool;

    /** 线程池队列大小 */
    @Value(value = "${ecs.common.threadPool.queueCapacity:5}")
    int queueCapacity;

    /** 线程名称（前缀） */
    @Value(value = "${ecs.common.threadPool.threadNamePrefix:default}")
    String threadNamePrefix;

    /**
     * 实例化线程池
     * @return
     */
    /**********优化自定义线程池 增加异步异常处理   tangyongmao   2019/6/21***********/
    @Bean
    public AsyncConfigurer getAsyncConfigurer() {
        AsyncConfigurer asyncConfigurer = new AsyncConfigurer() {
            public ThreadPoolTaskExecutorHelper executor;
            @Override
            public Executor getAsyncExecutor() {
                if(executor == null) {
                    log.debug("thread pool init");
                    executor = new ThreadPoolTaskExecutorHelper();
                    //配置核心线程数
                    executor.setCorePoolSize(corePoolSize);
                    //配置最大线程数
                    executor.setMaxPoolSize(maxPool);
                    //配置队列大小
                    executor.setQueueCapacity(queueCapacity);
                    //配置线程池中的线程的名称前缀
                    executor.setThreadNamePrefix(threadNamePrefix);

                    // rejection-policy：当pool已经达到max size的时候，如何处理新任务
                    // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
                    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
                    //执行初始化
                    executor.initialize();
                }
                return executor;
            }

            /**
             * 自定义异步异常处理器
             * The {@link AsyncUncaughtExceptionHandler} instance to be used
             * when an exception is thrown during an asynchronous method execution
             * with {@code void} return type.
             */
            @Override
            public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
                return new BaseGlobalAsyncExceptionHandler();
            }
        };

        return asyncConfigurer;
    }

}