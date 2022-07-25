package ink.rayin.app.web.cache.lock.support;

import ink.rayin.app.web.cache.lock.RedisLock;
import ink.rayin.app.web.cache.KeyUtil;
import ink.rayin.app.web.cache.RedisTemplateUtil;
import ink.rayin.app.web.cache.SerializeUtil;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: tym
 * @create: 2020-10-26 18:07
 **/
@Component("optimisticRedisLockSupport")
public class OptimisticRedisLockSupport implements RedisLock {
    @Resource
    private RedisTemplateUtil redisTemplateUtil;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    public static String versionKey = "versionKey";
    private static int defaultTimes = 30;
    @Override
    public boolean add(String key, Long num){
        int i = 0;
        //TODO 异常导致version小于key
        do {
            Long version = redisTemplateUtil.getIncrVal(versionKey);
            if (version == null) {
                version = redisTemplateUtil.incr(versionKey,null,0L);
            }
            String vKold = KeyUtil.makeKey(key,"-",String.valueOf(version));
            version++;
            String vKnew = KeyUtil.makeKey(key,"-",String.valueOf(version));
//            Long value = redisTemplateUtil.get(vKold,Long.class);



//                Long finalValue = value;
            boolean res = stringRedisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
                byte[] vKoldB = stringRedisTemplate.getStringSerializer().serialize(vKold);
                byte[] vKnewB = stringRedisTemplate.getStringSerializer().serialize(vKnew);

                byte[] valueb = redisConnection.get(vKoldB);
                if (valueb == null) {
                    return false;
                }
                Long value = SerializeUtil.deserializeClass(valueb, Long.class);
                value += num;
                byte[] valueBytes = SerializeUtil.serialize(value);
                boolean b = redisConnection.setNX(vKnewB,valueBytes);
                if (b) {
                    redisConnection.set(stringRedisTemplate.getStringSerializer().serialize(key),
                            valueBytes);
                    redisConnection.del(vKoldB);
                    redisConnection.incr(stringRedisTemplate.getStringSerializer().serialize(versionKey));
                    return true;
                } else {
                    return false;
                }
            });
            if (res) {
                RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
                return true;
            } else {
                System.err.println(Thread.currentThread().getName() + " wait for " + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }

            if (i == defaultTimes) {
                return false;
            }
        } while (true);
    }

    @Override
    public <T> T get(String key, Class<T> elementType) {
        Long version = redisTemplateUtil.getIncrVal(versionKey);
        if (version == null) {
            return null;
        }
        String vK = KeyUtil.makeKey(key,"-",String.valueOf(version));
        return redisTemplateUtil.get(vK,elementType);
    }
}
