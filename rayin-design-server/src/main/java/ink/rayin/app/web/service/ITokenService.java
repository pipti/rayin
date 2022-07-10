package ink.rayin.app.web.service;

import ink.rayin.app.web.model.UserModel;

/**
 * [PDF生成token获取接口].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-03-09 Eric Wang create<br>
 * <br>
 * @author Wang Zhu
 * @version 1.0
 * @since JDK 1.8
 */
public interface ITokenService {
    String getToken(String accKey, String secretKey);
    UserModel decodeToken(String token);
}
