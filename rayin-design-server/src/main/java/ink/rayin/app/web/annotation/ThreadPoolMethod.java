package ink.rayin.app.web.annotation;

import java.lang.annotation.*;

/**
 * 标注交给线程池执行的方法，可用于手动提交线程池
 * Create by tangyongmao on 2019/7/8.
 */
@Documented
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ThreadPoolMethod {
}
