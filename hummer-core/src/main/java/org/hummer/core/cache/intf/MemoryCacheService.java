package org.hummer.core.cache.intf;

import java.util.Collection;

public interface MemoryCacheService {
    public void deleteByPrex(String prex);

    public boolean isExist(Object key);

    public void set(Object key, Object value);

    public Object get(Object key);

    public Object getSet(Object key, Object value);

    public void setWithExp(Object key, int seconds, Object value);

    public void expire(Object key, int seconds);

    public void removeExp(Object key);

    public Long getExp(Object key);

    public void delete(Object keys);

    public void delete(Collection keys);

    public void rename(Object key, Object newkey);

}