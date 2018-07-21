package org.hummer.core.cache.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CacheModelEvicts.class)
@Inherited
/**
 * 通过实现IModel和IStringPKModel，对应的Id组合成的key，找到相应的缓存的redis key
 * 并删除，达到更新缓存的目的
 *
 */
public @interface CacheModelEvict {
    Class modelClass() default Object.class;

    String key() default "";
}