package org.hummer.core.cache.intf;

import java.util.List;

public interface RedisDo {

    public boolean RedisExist(Object key);

    public void RedisSet(Object key, Object value);

    public Object RedisGet(Object key);

    public Object RedisGetSet(Object key, Object value);

    public void RedisMset(Object... keysvalues);

    public List<Object> RedisMget(Object... keys);

    public void RedisSetex(Object key, int seconds, Object value);

    public void RedisExpire(Object key, int seconds);

    public void RedisPersist(Object key);

    public Long RedisTTL(Object key);

    public void RedisDel(Object... keys);

    public void RedisRename(Object key, Object newkey);

}