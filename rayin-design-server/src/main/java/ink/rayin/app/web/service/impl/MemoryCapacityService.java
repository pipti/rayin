package ink.rayin.app.web.service.impl;

import ink.rayin.app.web.cache.KeyUtil;
import ink.rayin.app.web.cache.RedisTemplateUtil;
import ink.rayin.app.web.cache.lock.RedisLock;
import ink.rayin.app.web.dao.UserOrganizationMapper;
import ink.rayin.app.web.model.UserOrganization;
import ink.rayin.app.web.service.IMemoryCapacityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: tym
 * @create: 2020-10-26 10:29
 **/
@Slf4j
@Service
public class MemoryCapacityService implements IMemoryCapacityService {

    @Resource
    @Qualifier("synchronizedRedisLockSupport")
    private RedisLock redisLock;

    @Resource
    private UserOrganizationMapper userOrganizationMapper;

    @Resource
    private RedisTemplateUtil redisTemplateUtil;

    @Override
    public boolean checkAndAdd(String organizationId, Long size) {
        //单位B
        Long max = 1024 * 1024 * 1024L;
        UserOrganization param = new UserOrganization();
        param.setOrganizationId(organizationId);
        param.setOwner(true);
        UserOrganization ret = userOrganizationMapper.userOrganizationQueryOne(param);
        String key = KeyUtil.makeKey("Rayin","-","Mem",ret.getUserId());
        Long current = redisLock.get(key,Long.class);
        if (current == null) {
            current = 0L;
        }
        if (size + current > max) {
            return false;
        } else {
            return redisLock.add(key,size);
        }
    }

    @Override
    public void add(Long size) {

    }

    @Override
    public void remove(String organizationId, Long size) {

    }

    @Override
    public boolean check(String organizationId) {
        //TODO 配置加载用户存储容量
        Long max = 1024 * 1024 * 1024L;
        UserOrganization param = new UserOrganization();
        param.setOrganizationId(organizationId);
        param.setOwner(true);
        UserOrganization ret = userOrganizationMapper.userOrganizationQueryOne(param);
        String key = KeyUtil.makeKey("Rayin","-","Mem",ret.getUserId());
        Long current = redisLock.get(key,Long.class);
        //TODO 容量告警
        if (current == null) {
            current=0L;
            redisTemplateUtil.save(key,current);
        }
        if (current >= max) {
            return false;
        }
        return true;
    }
}
