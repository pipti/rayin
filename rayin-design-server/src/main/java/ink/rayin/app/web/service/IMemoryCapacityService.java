package ink.rayin.app.web.service;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: tym
 * @create: 2020-10-26 10:26
 **/
public interface IMemoryCapacityService {
    boolean checkAndAdd(String organizationId, Long size);
    void add(Long size);
    void remove(String organizationId, Long size);
    boolean check(String organizationId);
}
