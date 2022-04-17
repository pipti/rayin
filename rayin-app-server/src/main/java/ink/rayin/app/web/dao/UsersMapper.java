package ink.rayin.app.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.rayin.app.web.model.Users;
import org.springframework.stereotype.Repository;

/**
 * [MyBatis user Dao Mapper].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-04-01 WangZhu created<br>
 * <br>
 * @author Jonah Wang
 * @version 1.0
 * @since JDK 1.8
 */
@Repository
public interface UsersMapper extends BaseMapper<Users> {
}
