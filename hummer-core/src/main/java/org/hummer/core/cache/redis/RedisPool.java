package org.hummer.core.cache.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.util.Pool;

import java.util.ResourceBundle;


public class RedisPool {
    private static Pool<Jedis> pool = null;

    /*使用静态代码块，优先加载顺序在static方法之前
     * 初始化redis连接配置
     */
    static {
        //得到redis.properties 中的配置信息
        ResourceBundle bundle = ResourceBundle.getBundle("redis");
        if (bundle == null) {
            throw new IllegalArgumentException("[redis.properties] is not found");
        }
        //配置信息对象config
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.valueOf(bundle.getString("redis.pool.maxActive")));
        config.setMaxIdle(Integer.valueOf(bundle.getString("redis.pool.maxIdle")));
        config.setMaxWaitMillis(Long.valueOf(bundle.getString("redis.pool.maxWait")));
        config.setTestOnBorrow(Boolean.valueOf(bundle.getString("redis.pool.testOnBorrow")));
        config.setTestOnReturn(Boolean.valueOf(bundle.getString("redis.pool.testOnReturn")));
        //创建redis连接池
        pool = new JedisPool(config, bundle.getString("redis.ip"), Integer.valueOf(bundle.getString("redis.port")), 2000);
    }

    public static Pool<Jedis> getPool() {
        if (pool == null) {
            synchronized (RedisPool.class) {
//                // 分为单机和master-slave的两种池获取。
//                if (redisMsgQueueConfig.getSentinelMasterName() != null) {
//                    Set<String> sentinelSet = getSentinelSet(redisMsgQueueConfig.getSentinels());
//                    if (sentinelSet != null && sentinelSet.size() > 0) {
//                        pool = new JedisSentinelPool(redisMsgQueueConfig.getSentinelMasterName(), sentinelSet, createPoolConfig(), 2000, redisMsgQueueConfig.getPassword());
//                    } else {
//                        throw new RuntimeException("Error configuring Redis Sentinel connection pool: expected both `sentinelMasterName` and `sentiels` to be configured");
//                    }
//                } else {
//                    pool = new JedisPool(new RedisDriver().createPoolConfig(), redisMsgQueueConfig.getRedisHost(), redisMsgQueueConfig.getRedisPort(), 2000, redisMsgQueueConfig.getPassword());
//                }

            }
        }


        return pool;
    }


    /**
     * 获取jedis实例
     * 在获取jedis实例方法前，加上序列化
     *
     * @return
     */
    public static Jedis getJedis() {
        return pool == null ? null : pool.getResource();
    }


    /**
     * 释放jedis实例资源
     */
    public static void returnResource(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}
