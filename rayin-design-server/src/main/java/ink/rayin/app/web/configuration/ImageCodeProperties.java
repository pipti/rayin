package ink.rayin.app.web.configuration;

import lombok.Data;

/**
 * @program: rayin-parent
 * @description: ImageCodeProperties
 * @author: tym
 * @create: 2020-01-16 11:05
 **/
@Data
public class ImageCodeProperties {
    /**
     * 验证码宽度
     */
    private int width = 67;
    /**
     * 高度
     */
    private int height = 23;
    /**
     * 长度（几个数字）
     */
    private int length = 4;
    /**
     * 过期时间
     */
    private int expireIn = 60;
}
