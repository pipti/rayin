package ink.rayin.app.web.threadpool;

import ink.rayin.app.web.annotation.ThreadPoolMethod;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 手动提交线程池工具类
 * Create by tangyongmao on 2019/7/8.
 */
@Component
public class ThreadPoolUtil {

    @Resource
    AsyncConfigurer asyncConfigurer;

    /**
     * 将待执行方法提交给线程池
     * 只执行无参方法
     * @param object 待交线程池类实例
     */
    public void asyncAll(Object object){
        Executor executor = asyncConfigurer.getAsyncExecutor();
        Class clazz = object.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            String name = method.getName();
            ThreadPoolMethod threadPoolMethod = method.getAnnotation(ThreadPoolMethod.class);
            if (threadPoolMethod != null) {
                Thread thread = new Thread(() -> {
                    try {
                        method.invoke(object,null);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
                executor.execute(thread);
            }

        }
    }

    /**
     * 将待执行方法提交给线程池
     * 只执行无参方法
     * @param object 待交线程池类实例
     * @param method 待执行实例
     * @param objects 参数
     */
    public void async(Object object,Method method,Object[] objects){
        Executor executor = asyncConfigurer.getAsyncExecutor();
        Thread thread = new Thread(() -> {
            try {
                method.invoke(object,objects);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        executor.execute(thread);
    }

    /**
     * 将待执行方法提交给线程池
     * 只执行无参方法
     * @param object 待交线程池类实例
     * @param methodName 待执行方法名
     * @param objects 参数
     */
    public void async(Object object,String methodName,Object[] objects) throws Exception {
        Method method;
        try {
            method = object.getClass().getMethod(methodName);
        } catch (NoSuchMethodException e) {
            throw new Exception("method not found:"+methodName,e);
        }
        async(object,method,objects);
    }

    public ThreadPoolTaskExecutor getThreadPoolTaskExecutorHelper(int corePoolSize,int maxPool,int queueCapacity,String threadNamePrefix){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutorHelper();
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
        return executor;
    }
}
