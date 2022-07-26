package ink.rayin.springboot;


import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({RayinPDFAdapterAutoConfiguration.class})
public @interface EnableRayinPdfAdpter {
}
