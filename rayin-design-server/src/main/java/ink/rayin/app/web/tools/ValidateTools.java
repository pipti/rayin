package ink.rayin.app.web.tools;

import ink.rayin.app.web.cache.RedisTemplateUtil;
import ink.rayin.app.web.exception.UsersServerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Carol Tang
 * @version 1.0.0
 * @date 2020/7/14 18:30
 * @description 校验工具类
 */
@Slf4j
public class ValidateTools {
    /**
     * 校验图片验证码
     * @param codeInRequest
     * @param sessionId
     * @param redisTemplate
     */
    public static void validateCode(String codeInRequest, String sessionId, String validType, RedisTemplateUtil redisTemplate) {
        String key = sessionId;
        if(StringUtils.isNotBlank(validType)){
            key = sessionId + validType;
        }
        //从session中取出 验证码
        String codeInSession = redisTemplate.get(key,String.class);

        //从request 请求中 取出 验证码
        if (StringUtils.isBlank(codeInRequest)){
            log.info("1001 - 图片验证码不能为空");
            throw new UsersServerException("1001 - 图片验证码不能为空");
        }
//        if (codeInSession == null){
//            log.info("1001 - 图片验证码不存在");
//            throw new UsersServerException("1001 - 图片验证码不存在");
//        }
        if (codeInSession == null){
            log.info("1001 - 图片验证码已过期");
            redisTemplate.delete(key);
            throw new UsersServerException("1001 - 图片验证码已过期");
        }
        if (!StringUtils.equals(codeInSession,codeInRequest.toLowerCase())){
            log.info("验证码不匹配"+"codeInSession:"+codeInSession +", codeInRequest:"+codeInRequest);
            throw new UsersServerException("验证码不匹配");
        }
        redisTemplate.delete(key);
        //把对应 的 session信息  删掉
//        sessionStrategy.removeAttribute(request,ValidateCodeController.SESSION_KEY);
    }
}
