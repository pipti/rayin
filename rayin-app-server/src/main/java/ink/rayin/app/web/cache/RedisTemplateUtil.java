package ink.rayin.app.web.cache;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

import static org.springframework.data.redis.connection.ReturnType.BOOLEAN;
import static org.springframework.data.redis.connection.ReturnType.INTEGER;

/**
 * 自定义redis操作工具类包含存取方法
 * Created by tangyongmao on 2019-6-4 14:35:34.
 */
@Component("redisTemplateUtil")
public class RedisTemplateUtil{
    //注入StringRedisTemplate
    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
     * 向redis中存序列化后的数据
     * @param key
     * @param value
     */
    public void save(final String key, Object value) {
        save(key,value,null);
    }
    /**
     * 向redis中存序列化后的数据
     * @param key
     * @param value
     */
    public void save(final String key, Object value,Long time) {
        //序列化Object对象
        final byte[] vbytes = SerializeUtil.serialize(value);
        //系列化key值
        final byte[] keyBytes = stringRedisTemplate.getStringSerializer().serialize(key);
        stringRedisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                //判断key值是否存在，存在重新加载
                if (connection.exists(keyBytes)) {
                    connection.del(keyBytes);
                }

                if (time != null) {
                    connection.setEx(stringRedisTemplate.getStringSerializer().serialize(key),time, vbytes);
                } else {
                    connection.set(stringRedisTemplate.getStringSerializer().serialize(key), vbytes);
                }
                return null;
            }
        });
        RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
    }

    public void updateExpire(final String key,Long time) {
        //序列化Object对象
        //系列化key值
        final byte[] keyBytes = stringRedisTemplate.getStringSerializer().serialize(key);
        stringRedisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                if (time != null) {
                    connection.expire(keyBytes,time);
                }
                return null;
            }
        });
        RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
    }

    public Long getIncrVal(String key) {
        String str = stringRedisTemplate.opsForValue().get(key);
        return str == null ? null : Long.valueOf(str);
    }
    /**
     * 从redis中取出反序列化的javaBean
     * @param key
     * @param elementType
     * @return T
     */
    public <T>T get(final String key, Class<T> elementType) {

        T res = stringRedisTemplate.execute(new RedisCallback<T>() {
            @Override
            public T doInRedis(RedisConnection connection)
                    throws DataAccessException {
                //序列化key值
                byte[] keyBytes = stringRedisTemplate.getStringSerializer().serialize(key);
                if (connection.exists(keyBytes)) {
                    byte[] valueBytes = connection.get(keyBytes);
                    if (valueBytes == null) {
                        return null;
                    }
                    T value = (T) SerializeUtil.deserializeClass(valueBytes,elementType);
                    return value;
                }
                return null;
            }
        });
        RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
        return res;

    }
    /**
     * key是否存在
     * @param key
     * @return T
     */
    public Boolean hasKey(final String key) {
        Boolean res = stringRedisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                //序列化key值
                byte[] keyBytes = stringRedisTemplate.getStringSerializer().serialize(key);
                return connection.exists(keyBytes);
            }
        });
        //RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
        return res;
    }

    /**
     * 从redis中取出反序列化的javaBean
     * @param key
     * @return T
     */
    public Object getII(final String key,ClassLoader classLoader) {
        Object res = stringRedisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                //序列化key值
                byte[] keyBytes = stringRedisTemplate.getStringSerializer().serialize(key);

                if (connection.exists(keyBytes)) {
                    byte[] valueBytes = connection.get(keyBytes);
                    Object value = SerializeUtil.deserializeNoClass(valueBytes,classLoader);
                    return value;
                }
                return null;
            }
        });
        RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
        return res;
    }

    public void deleteByKeyLike(String likeKey) {
        Set<String> keys = stringRedisTemplate.keys(likeKey + "*");
        stringRedisTemplate.delete(keys);
        RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
    }

    /**
     * 删除
     * @param key
     */
    public void delete(String key) {
        stringRedisTemplate.delete(key);
        RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
    }

    /**
     * 计数 (原子操作)
     * @param key
     * @param time 过期时间 如果是空不设置过期时间
     * @param fromNumber 起始数值
     * @return
     */
    public long incr(String key,Long time,Long fromNumber) {
        Long res = stringRedisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                //序列化key值
                byte[] keyBytes = stringRedisTemplate.getStringSerializer().serialize(key);
                /**  优化原子计数优化 tangyongmao  2019/7/1  */
                if (redisConnection.exists(keyBytes)) {
                    long value = redisConnection.incr(keyBytes);
                    return value;
                } else {
                    if (time != null) {
                        redisConnection.expire(keyBytes,time);
                    }
                    redisConnection.set(keyBytes, stringRedisTemplate.getStringSerializer().serialize(String.valueOf(fromNumber)));
                    return fromNumber;
                }
            }
        });
        RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
        return res;
    }

    /**
     * 计数 (原子操作) 从0开始
     * @param key
     * @param time 过期时间 如果是空不设置过期时间
     * @return
     */
    public long incrFromZero(String key,Long time) {
        return this.incr(key,time,Long.valueOf(0));
    }

    /**
     * 计数 (原子操作) 从1开始
     * @param key
     * @param time 过期时间 如果是空不设置过期时间
     * @return
     */
    public long incrFromOne(String key,Long time) {
        return this.incr(key,time,Long.valueOf(1));
    }

//    /**
//     * 保存(原子操作)
//     * @param key
//     * @param value
//     * @return
//     */
//    public boolean set(String key,String value) {
//        boolean res = stringRedisTemplate.execute(new RedisCallback<Boolean>() {
//            @Override
//            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
//                byte[] keyBytes = stringRedisTemplate.getStringSerializer().serialize(key);
//                byte[] valueBytes = stringRedisTemplate.getStringSerializer().serialize(value);
//                boolean b = redisConnection.setNX(keyBytes,valueBytes);
//                redisConnection.expire(keyBytes,10000);
//                return b;
//            }
//        });
//        return res;
//    }
    /**
     * 保存(原子操作)
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key,Object value) {
        return set(key, value, null);
    }

    public boolean set(String key,Object value, Long time) {
        boolean res = stringRedisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte[] keyBytes = stringRedisTemplate.getStringSerializer().serialize(key);
                byte[] valueBytes = SerializeUtil.serialize(value);
                boolean b = redisConnection.setNX(keyBytes,valueBytes);
                if (b && time != null) {
                    redisConnection.expire(keyBytes,time);
                }
                return b;
            }
        });
        return res;
    }

    /**
     * 执行脚本
     * @param script 脚本字符串
     * @param key
     * @param value
     * @return
     */
    public boolean eval(String script,String key,String value) {
        boolean res = stringRedisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte[] keyBytes = stringRedisTemplate.getStringSerializer().serialize(key);
                byte[] valueBytes = stringRedisTemplate.getStringSerializer().serialize(value);
                byte[] scriptBytes = stringRedisTemplate.getStringSerializer().serialize(script);
                return redisConnection.eval(scriptBytes, BOOLEAN,1, keyBytes, valueBytes);
            }
        });
        return res;
    }

    public Long evalForInt(String script,String key,String value) {
        Long res = stringRedisTemplate.execute((RedisCallback<Long>) redisConnection -> {
            byte[] keyBytes = stringRedisTemplate.getStringSerializer().serialize(key);
            byte[] valueBytes = stringRedisTemplate.getStringSerializer().serialize(value);
            byte[] scriptBytes = stringRedisTemplate.getStringSerializer().serialize(script);
            return redisConnection.eval(scriptBytes, INTEGER,1, keyBytes, valueBytes);
        });
        return res;
    }

}
