package ink.rayin.app.web.service;

import ink.rayin.app.web.model.UserModel;
import ink.rayin.app.web.vo.UserVo;


public interface UserService {
    UserVo getUserInfo(String userName);
    String createUser(UserModel userModel) throws Exception;
    boolean checkSignature(String signature,String timestamp,String nonce) throws Exception;
    UserModel getUserByUserName(String username);
    String changePassword(String username);
}
