package ink.rayin.app.web.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: rayin-app-parent
 * @description: ImageModel
 * @author: tym
 * @create: 2020-01-16 15:15
 **/
@Data
public class ImageModel implements Serializable{
    /**
     * 随机数
     */
    private String code;
    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    public boolean isExpried(){
        //如果 过期时间 在 当前日期 之前，则验证码过期
        return LocalDateTime.now().isAfter(expireTime);
    }
}
