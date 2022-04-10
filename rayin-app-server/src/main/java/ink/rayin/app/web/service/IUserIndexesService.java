package ink.rayin.app.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.rayin.app.web.model.UserIndexes;
import ink.rayin.app.web.model.UserIndexesUser;

public interface IUserIndexesService {
    IPage<UserIndexesUser> userIndexQueryWithOrgIndexes(Page page, UserIndexes userIndexes);
    IPage<UserIndexesUser> userIndexQuery(Page page, UserIndexes userIndexes);
    int userIndexSave(UserIndexes userIndexes);
    int userIndexDel(UserIndexes userIndexes);
}
