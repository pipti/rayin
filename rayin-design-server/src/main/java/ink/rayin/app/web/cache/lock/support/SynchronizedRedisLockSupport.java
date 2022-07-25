package ink.rayin.app.web.cache.lock.support;

import ink.rayin.app.web.cache.lock.RedisLock;
import ink.rayin.app.web.cache.KeyUtil;
import ink.rayin.app.web.cache.RedisTemplateUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: tym
 * @create: 2020-10-27 10:13
 **/
@Component("synchronizedRedisLockSupport")
public class SynchronizedRedisLockSupport implements RedisLock {
    public static String lockKey = "lockKey";
    private ThreadLocal<String> threadLocal = new ThreadLocal<>();
    @Resource
    private RedisTemplateUtil redisTemplateUtil;
    @Resource
    private RedisTemplate redisTemplate;
    @Override
    public boolean add(String key, Long value){
        String lk = KeyUtil.makeKey(key,"-",lockKey);
        tryLock(lk);
        Long v1 = redisTemplateUtil.get(key,Long.class);
        v1 += value;
        redisTemplateUtil.save(key,v1);
        unlock(lk);
        return true;
    }

    @Override
    public <T> T get(String key, Class<T> elementType) {
        return redisTemplateUtil.get(key,elementType);
    }

    public void tryLock(String key){
        if (threadLocal.get() == null) {
            String val = Thread.currentThread().getId()+ "-" + UUID.randomUUID();
            threadLocal.set(val);
        } else {
            return;
        }
        try {
            do {
                if (redisTemplate.opsForValue().setIfAbsent(key,threadLocal.get())) {
                    redisTemplate.expire(key,10L,TimeUnit.SECONDS);
                    return;
                }else {
                    System.err.println(Thread.currentThread().getName() + " wait");
                    Thread.sleep(100);
                }
            } while (true);
        }catch (Exception e) {
            e.printStackTrace();
            unlock(key);
        }
    }

    public void unlock(String key) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object res = execute(script,key,threadLocal.get());
        threadLocal.remove();
    }

    private Object execute(String script,String key,Object... args){
        DefaultRedisScript redisScript = new DefaultRedisScript(script);
        redisScript.setResultType(Long.class);
        Object result = redisTemplate.execute(redisScript, Collections.singletonList(key),args);
        return result;
    }
}
