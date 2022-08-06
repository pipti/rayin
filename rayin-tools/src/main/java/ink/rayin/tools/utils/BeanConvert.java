package ink.rayin.tools.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.io.IOException;

/**
 * @author Jonah Wang/ 王柱
 */
public class BeanConvert {
    private static Logger logger = LoggerFactory.getLogger(BeanConvert.class);

    /**
     * 将from对象中相同的变量值转换为to对象，并赋值to对象中
     * @param from 源对象实体
     * @param to 目标对象类型
     * @return 目标对象实体
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> T convert(Object from, Class<T> to) throws IllegalAccessException, InstantiationException {
        T toObject = to.newInstance();
        BeanUtils.copyProperties(from,toObject);
        return toObject;
    }
}
