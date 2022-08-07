package ink.rayin.datarule;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class ConditionInterceptor {
    /**
     * 检查当前脚本执行是否超时
     * @param timeout 超时时间，单位秒
     * @return 超时返回false
     */
    static boolean checkTimeout(int timeout) {
        boolean flag = ThreadLocalUtils.getStartTime() + timeout * 1000 < System.currentTimeMillis();
        if (flag) {
            System.err.printf("[%s] Execution timed out after %s seconds. Start Time:%s%n",
                    ThreadLocalUtils.get("scriptName"),
                    timeout,
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(ThreadLocalUtils.getStartTime()), ZoneOffset.ofHours(8))
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        return flag;
    }
}
