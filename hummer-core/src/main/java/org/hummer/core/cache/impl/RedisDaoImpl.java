package org.hummer.core.cache.impl;

import org.hummer.core.cache.intf.RedisDo;
import org.hummer.core.cache.redis.RedisPool;
import org.hummer.core.util.SerializableUtil;
import redis.clients.jedis.Jedis;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


public class RedisDaoImpl implements RedisDo {

    /**
     * 从连接池中获取实例
     *
     * @return
     */
    public static Jedis GetJedis() {
        return RedisPool.getJedis();
    }

    /**
     * 序列化
     *
     * @param object
     * @return
     */
    public byte[] serializeObj(Object object) {
        return SerializableUtil.toByte(object);
    }

    /**
     * 反序列化
     *
     * @param bt
     * @return
     */
    public Object unSerializeByte(byte[] bt) {
        if (bt == null) {
            return null;
        }
        return SerializableUtil.toObject(bt);
    }

    /**
     * 判断键是否存在
     *
     * @param key
     * @return
     */
    public boolean RedisExist(Object key) {
        return GetJedis().exists(serializeObj(key));
    }

    /**
     * set
     *
     * @param key
     * @param value
     */
    public void RedisSet(Object key, Object value) {
        Jedis redis = null;
        try {
            redis = GetJedis();

            redis.set(serializeObj(key), serializeObj(value));
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
    }

    /**
     * get
     *
     * @param key
     * @return
     */
    public Object RedisGet(Object key) {
        Jedis redis = null;
        Object ret;
        try {
            redis = GetJedis();
            ret = unSerializeByte(redis.get(serializeObj(key)));
        } finally {
            if (redis != null) {
                redis.close();
            }
        }
        return ret;
    }

    /**
     * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。
     *
     * @param key
     * @param value
     * @return
     */
    public Object RedisGetSet(Object key, Object value) {
        Jedis redis = GetJedis();
        Object ret = unSerializeByte(redis.getSet(serializeObj(key), serializeObj(value)));
        redis.close();
        return ret;
    }

    /**
     * 存入多个键值对 自己封装
     *
     * @param keysvalues
     */
    public void RedisMset(Object... keysvalues) {
        List<Object> list = new LinkedList<>();
        Jedis redis = GetJedis();

        int i = 0;
        if (keysvalues != null) {
            for (Object object : keysvalues) {
                list.add(i, object);
                i++;
            }
        }
        for (i = 0; i < list.size(); i++) {
            redis.set(serializeObj(list.get(i)), serializeObj(list.get(i + 1)));
            i = i + 1;
        }
        redis.close();
    }

    /**
     * 输入多个键得到List<>值
     *
     * @param keys
     * @return
     */
    public List<Object> RedisMget(Object... keys) {
        List<byte[]> keyslist = new LinkedList<>();
        int i = 0;
        if (keys != null) {
            for (Object object : keys) {
                keyslist.add(i, serializeObj(object));
                i++;
            }
        }
        List<Object> listObj = new LinkedList<>();
        for (i = 0; i < keyslist.size(); i++) {
            listObj.add(i, unSerializeByte(GetJedis().get(keyslist.get(i))));
        }
        return listObj;
    }

    /**
     * 设置一个有过期时间的key-value对
     *
     * @param key
     * @param seconds
     * @param value
     */
    public void RedisSetex(Object key, int seconds, Object value) {
        GetJedis().setex(serializeObj(key), seconds, serializeObj(value));
    }

    /**
     * 单独设置键的过期时间
     *
     * @param key
     * @param seconds
     */
    public void RedisExpire(Object key, int seconds) {
        GetJedis().expire(serializeObj(key), seconds);
    }

    /**
     * 移除键的过期时间，键保持持久
     *
     * @param key
     */
    public void RedisPersist(Object key) {
        GetJedis().persist(serializeObj(key));
    }

    /**
     * 查看键的剩余过期时间
     *
     * @param key
     * @return
     */
    public Long RedisTTL(Object key) {
        Long ret;
        Jedis redis = GetJedis();
        ret = redis.ttl(serializeObj(key));
        redis.close();
        return ret;
    }

    /**
     * 删除一个或者多个键
     *
     * @param keys
     */
    public void RedisDel(Collection keys) {
        if (keys != null && keys.size() > 0) {
            Jedis redis = GetJedis();
            Object[] oKeys = keys.toArray();
            for (Object key : oKeys) {
                redis.del(serializeObj(key));
            }
            redis.close();
        }
    }

    /**
     * 删除一个或者多个键
     *
     * @param keys
     */
    public void RedisDel(Object... keys) {
        List<Object> list = new LinkedList<Object>();
        Jedis redis = GetJedis();

        int i = 0;
        if (keys != null) {
            for (Object object : keys) {
                list.add(i, object);
                i++;
            }
        }
        for (i = 0; i < list.size(); i++) {
            redis.del(serializeObj(list.get(i)));
        }
        redis.close();
    }

    /**
     * 重命名key
     *
     * @param key
     * @param newkey
     */
    public void RedisRename(Object key, Object newkey) {
        Jedis redis = GetJedis();
        redis.rename(serializeObj(key), serializeObj(newkey));
        redis.close();
    }
}