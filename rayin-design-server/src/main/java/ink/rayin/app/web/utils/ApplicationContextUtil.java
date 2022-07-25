package ink.rayin.app.web.utils;

import ink.rayin.app.web.threadpool.ThreadPoolTaskExecutorHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * ApplicationContext工具类
 * @author tangyongmao on 2019/7/8.
 */
@Slf4j
@Component
public class ApplicationContextUtil implements ApplicationContextAware,ApplicationListener<ApplicationEvent> {
    private static ApplicationContext context;

    @Resource
    private AsyncConfigurer asyncConfigurer;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        try {
            if(applicationEvent instanceof ContextClosedEvent){
                //容器停止销毁线程池
                if (asyncConfigurer == null) {
                    asyncConfigurer = context.getBean(AsyncConfigurer.class);
                }
                ThreadPoolTaskExecutorHelper threadPoolTaskExecutorHelper = null;
                if (asyncConfigurer != null) {
                    threadPoolTaskExecutorHelper = (ThreadPoolTaskExecutorHelper) asyncConfigurer.getAsyncExecutor();
                }
                if (threadPoolTaskExecutorHelper != null) {
                    //LogLevel.INSTANCE.info("threadPool shutdown");
                    threadPoolTaskExecutorHelper.shutdown();
                }
            }else if(applicationEvent instanceof ContextRefreshedEvent){
            }else if(applicationEvent instanceof ContextStartedEvent){
            }else if(applicationEvent instanceof ContextStoppedEvent){
            }else{
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
