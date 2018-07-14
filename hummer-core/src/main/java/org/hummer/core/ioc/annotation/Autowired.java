package org.hummer.core.ioc.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Autowired {
    BeanType value() default BeanType.HUMMER_BEAN;
}
