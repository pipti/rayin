package ink.rayin.app.web.annotation;

import java.lang.annotation.*;

@Documented
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface OrgId {

}