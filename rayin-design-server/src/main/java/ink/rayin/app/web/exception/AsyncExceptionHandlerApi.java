package ink.rayin.app.web.exception;

import java.lang.reflect.Method;

/**
 * @author tangyongmao on 2019-6-21.
 */
public interface AsyncExceptionHandlerApi{
    void handleUncaughtException(Throwable e, Method method, Object... params);
}
