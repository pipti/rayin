package ink.rayin.app.web.cache;

import java.util.List;
import java.util.UUID;

/**
 * 生成键工具类
 * Created by tangyongmao on 2019-6-3 11:07:49.
 */
public class KeyUtil {
    /**
     * 生成键方法
     * @param head 键头　key的前缀
     * @param connectionTag 连接符号 每一个参数中间的连接符号
     * @param param 参数
     * @return
     */
    public static String makeKey (String head,String connectionTag,String ... param) {
        StringBuffer key = new StringBuffer();
        if (param == null || param.length == 0) {
            return head;
        }
        key.append(head);
        for (String str : param) {
            if (str != null && !"".equals(str)) {
                key.append(connectionTag);
                key.append(str);
            }
        }
        return key.toString();
    }

    /**
     * 生成键方法
     * @param head 键头
     * @param connectionTag 连接符号
     * @param list 参数
     * @return
     */
    public static String makeKeyByList (String head,String connectionTag,List<String> list) {
        StringBuffer key = new StringBuffer();
        if (list == null || list.size() == 0) {
            return head;
        }
        key.append(head);
        list.stream().filter(str -> str != null&&!"".equals(str)).forEach(str -> {
            key.append(connectionTag);
            key.append(str);
        });
        return key.toString();
    }

    /**
     * 生成随机码的方法
     * @return
     */
    public static String getUUID(){
        UUID uuid = UUID.randomUUID();
        String key = uuid.toString().replace("-", "");
        return key;
    }
}
