package ink.rayin.app.web.exception;

import ink.rayin.app.web.utils.ApplicationContextUtil;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 统一异步异常处理handler
 * Created by tangyongmao on 2019-6-21.
 */
@Component
public class BaseGlobalAsyncExceptionHandler implements AsyncUncaughtExceptionHandler,InitializingBean {
    private AsyncExceptionHandlerApi asyncExceptionHandlerApi;
    /**
     * exception handle
     * @param e
     * @param method
     * @param params
     */
    @Override
    public void handleUncaughtException(Throwable e, Method method, Object... params) {
        if (asyncExceptionHandlerApi == null) {
            ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
            //asyncExceptionHandlerApi = applicationContext.getBean(AsyncExceptionHandlerApi.class);
        }
        if (asyncExceptionHandlerApi != null) {
            asyncExceptionHandlerApi.handleUncaughtException(e,method,params);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
