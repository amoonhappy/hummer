package org.hummer.core.cache.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CacheModelEvicts.class)
@Inherited
public @interface CacheModelEvict {
    Class modelClass() default Object.class;

    String key() default "";
}