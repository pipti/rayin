package ink.rayin.app.web.cache.lock;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: tym
 * @create: 2020-10-26 17:49
 **/
public interface RedisLock {
    boolean add(String key, Long value);
    <T>T get(final String key, Class<T> elementType);
}
