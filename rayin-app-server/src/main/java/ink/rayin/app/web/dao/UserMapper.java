package ink.rayin.app.web.dao;

import ink.rayin.app.web.model.UserModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @program: rayin-app-parent
 * @description: userMapper
 * @author: tangyongmao
 * @create: 2020-02-04 09:27
 **/
@Repository
public interface UserMapper {
    UserModel getUserByUsername(String userName);
    UserModel getUserByMobile(String moblie);
    int insert(UserModel userModel);
    UserModel getUserByThirdId(@Param("thirdType") String thirdType, @Param("thirdId")String thirdId);
    UserModel getUserByUserId(String userId);
    boolean updateUserByUserId(UserModel userModel);
}
