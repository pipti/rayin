package ink.rayin.app.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import ink.rayin.app.web.model.UserIndexes;
import ink.rayin.app.web.model.UserIndexesUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserIndexesMapper extends BaseMapper<UserIndexes> {
    @Select("select * from user_indexes u right join organization_indexes o on u.index_id = o.index_id " +
            "where u.user_id = #{userIndexes.userId} and u.organization_id = #{userIndexes.organizationId}")
    IPage<UserIndexesUser> selectPageWithUser(IPage page, @Param("userIndexes") UserIndexes userIndexes);
}
