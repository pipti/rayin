package ink.rayin.app.web.exception;

import java.lang.reflect.Method;

/**
 * Created by tangyongmao on 2019-6-21.
 */
public interface AsyncExceptionHandlerApi{
    void handleUncaughtException(Throwable e, Method method, Object... params);
}
