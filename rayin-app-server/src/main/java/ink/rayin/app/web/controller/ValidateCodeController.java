package ink.rayin.app.web.controller;

import com.wf.captcha.SpecCaptcha;
import ink.rayin.app.web.cache.RedisTemplateUtil;
import ink.rayin.app.web.configuration.ValidateCodeGenerator;
import ink.rayin.app.web.model.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: rayin-parent
 * @description: ValidateCodeController
 * @author: tym
 * @create: 2020-01-16 11:02
 **/
@Slf4j
@RestController
public class ValidateCodeController {
    public static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";

    @Resource
    private ValidateCodeGenerator imageCodeGenerator;

    @Resource
    private RedisTemplateUtil redisTemplateUtil;

    @GetMapping("/users/code/image")
    public RestResponse createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        ImageCode imageCode = imageCodeGenerator.createCode(new ServletWebRequest(request));
        String sessionId = request.getSession().getId();
        String refreshType = request.getParameter("refreshType");


        SpecCaptcha specCaptcha = new SpecCaptcha(160, 48, 4);
        String verCode = specCaptcha.text().toLowerCase();
//        ImageModel imageModel = new ImageModel();
//        imageModel.setCode(verCode);
//        imageModel.setExpireTime(60);
        log.debug("验证码：" + verCode);
        if(StringUtils.isNoneBlank(refreshType)){
            redisTemplateUtil.save(sessionId + refreshType,verCode,Long.valueOf(60*5));
        }else{
            redisTemplateUtil.save(sessionId,verCode,Long.valueOf(60*5));
        }


        return RestResponse.success(specCaptcha.toBase64());
        //写给response 响应
//        ImageIO.write(imageCode.getImage(),"JPEG",response.getOutputStream());
    }
}
