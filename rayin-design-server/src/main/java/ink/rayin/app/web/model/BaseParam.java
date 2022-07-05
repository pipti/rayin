package ink.rayin.app.web.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: tym
 * @create: 2020-10-14 09:38
 **/
public class BaseParam {
    private Map<String, List<Object>> param = new HashMap<>();

    public List<Object> getParam(String taskName) {
        return param.get(taskName);
    }

    public BaseParam appendParam(String taskName,Object o) {
        List list = param.get(taskName);
        if (list == null) {
            list = new ArrayList();
            param.put(taskName,list);
        }
        list.add(o);
        return this;
    }
}
