package ink.rayin.datarule;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ThreadLocalUtils {
    // 存储脚本开始执行时间
    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = ThreadLocal.withInitial(HashMap::new);

    private ThreadLocalUtils() {
    }

    public static long getStartTime() {
        return (long) THREAD_LOCAL.get().get("__startTime");
    }

    public static void setStartTime() {
        THREAD_LOCAL.get().put("__startTime", System.currentTimeMillis());
    }

    public static void set(String key, Object value) {
        THREAD_LOCAL.get().put(key, value);
    }

    public static Object get(String key) {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (Objects.isNull(map)) {
            return null;
        }
        return map.get(key);
    }

    public static void setAll(Map<String, Object> map) {
        THREAD_LOCAL.get().putAll(map);
    }
}
