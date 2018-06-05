package org.hummer.core.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CacheEvicts.class)
public @interface CacheEvict {
    String evictForMethod() default "";

    String evictOnMethod() default "";

    Class evictOnClass();
}