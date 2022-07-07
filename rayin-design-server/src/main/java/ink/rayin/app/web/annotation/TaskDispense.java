package ink.rayin.app.web.annotation;

import ink.rayin.app.web.utils.BaseConstant;

import java.lang.annotation.*;

@Documented
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TaskDispense {
    String name();
    String proxyType() default BaseConstant.JAVA;
    String cron() default "";
    String taskId() default "";
    String proxyName() default "";
}
