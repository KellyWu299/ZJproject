package org.hnu.precomputation.common.model.Nebula;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
/*字段注解*/
public @interface FieldAutoMapping {

    String method() default "";

    String type();

}
