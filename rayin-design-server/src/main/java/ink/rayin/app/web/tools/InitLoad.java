package ink.rayin.app.web.tools;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ink.rayin.app.web.cache.KeyUtil;
import ink.rayin.app.web.cache.RedisTemplateUtil;
import ink.rayin.app.web.model.Role;
import ink.rayin.app.web.model.RoleAuth;
import ink.rayin.app.web.model.UserOrganization;
import ink.rayin.app.web.dao.RoleAuthMapper;
import ink.rayin.app.web.dao.RoleMapper;
import ink.rayin.app.web.dao.UserOrganizationMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: tym
 * @create: 2020-11-12 16:46
 **/
@Component
public class InitLoad implements InitializingBean {

    @Resource
    private UserOrganizationMapper userOrganizationMapper;

    @Resource
    private RoleAuthMapper roleAuthMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RedisTemplateUtil redisTemplateUtil;

    @Override
    public void afterPropertiesSet() throws Exception {
        loadOrgRole();
        loadAuth();
    }

    private void loadOrgRole() {
        List<UserOrganization> list = userOrganizationMapper.selectList(new QueryWrapper<>());
        if (list != null && list.size() > 0) {
            list.stream().forEach(userOrganization -> {
                String key = KeyUtil.makeKey("Rayin", "-","Auth", userOrganization.getUserId(), userOrganization.getOrganizationId());
                redisTemplateUtil.save(key,userOrganization);
            });
        }
    }

    private void loadAuth() {
        List<Role> roles = roleMapper.selectList(new QueryWrapper<>());
        roles.stream().forEach(role -> {
            List<RoleAuth> roleAuths = roleAuthMapper.selectList(new QueryWrapper<RoleAuth>().lambda().eq(RoleAuth::getRoleId,role.getRoleId()));
            List<String> paths = new ArrayList<>();
            roleAuths.stream().forEach(roleAuth -> paths.add(roleAuth.getUrl()));
            String key = KeyUtil.makeKey("Rayin","","Role","Auth",String.valueOf(role.getRoleId()));
            redisTemplateUtil.save(key,paths);
        });
    }
}
