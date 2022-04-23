package ink.rayin.app.web.annotation;

import java.lang.annotation.*;

/**
 * @author Jonah Wang
 */
@Documented
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface OrgId {
}