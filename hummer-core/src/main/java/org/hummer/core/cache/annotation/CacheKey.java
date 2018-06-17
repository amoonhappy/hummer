package org.hummer.core.cache.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheKey {
    String cacheName() default "";

    String key() default "";

    boolean evictOnAll() default false;
}