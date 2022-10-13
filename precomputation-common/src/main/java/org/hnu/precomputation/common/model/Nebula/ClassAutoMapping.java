package org.hnu.precomputation.common.model.Nebula;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
/*类注解*/
public @interface ClassAutoMapping {

    String value() default "";

}
