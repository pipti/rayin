package ink.rayin.app.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.rayin.app.web.model.UserInfo;
import ink.rayin.app.web.model.UserInfoModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * [MyBatis user_info Dao Mapper].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-11-29 WangZhu created<br>
 * <br>
 * @author Jonah Wang
 * @version 1.0
 * @since JDK 1.8
 */
@Repository
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    UserInfoModel getUserByInfoById(@Param("userId") String userId);

    int insert(UserInfoModel userInfoModel);
    int update(UserInfoModel userInfoModel);
}
