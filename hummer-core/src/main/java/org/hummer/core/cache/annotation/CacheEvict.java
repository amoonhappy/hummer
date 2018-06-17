package org.hummer.core.cache.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CacheEvicts.class)
@Inherited
public @interface CacheEvict {
    String cacheName() default "";

    Class modelClass() default Object.class;

    String key() default "";
}