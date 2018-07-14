package org.hummer.newweb.service.user.impl;

import org.hummer.core.cache.annotation.CacheEvict;
import org.hummer.core.cache.annotation.CacheKey;
import org.hummer.core.cache.annotation.CacheModelEvict;
import org.hummer.core.cache.impl.RedisService;
import org.hummer.core.cache.intf.ICacheable;
import org.hummer.core.ioc.annotation.Autowired;
import org.hummer.core.ioc.annotation.BeanType;
import org.hummer.core.model.intf.IModel;
import org.hummer.core.service.impl.BasicTestService;
import org.hummer.core.transaction.annotation.Transactional;
import org.hummer.core.util.Log4jUtils;
import org.hummer.newweb.dao.user.intf.ITestDAO;
import org.hummer.newweb.model.impl.User;
import org.hummer.newweb.model.impl.User1;
import org.hummer.newweb.model.intf.IUser;
import org.hummer.newweb.service.user.intf.ITestService;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.List;

/**
 * Service 示例类，在HummerFramework中注册
 *
 * @author Jeff Zhou
 * @date
 */
public class TestService extends BasicTestService implements ITestService, ICacheable {
    private static Logger log = Log4jUtils.getLogger(TestService.class);
    //init by Hummer，Hummer容器注入的容器對象
    @Autowired(value = BeanType.HUMMER_BEAN)
    ITestDAO testDAO;
    //init by Spring，Hummer容器通過Spring注入的Spring對象
    @Autowired(value = BeanType.SPRING_BEAN)
    RedisService redisService;

    @Override
    @CacheKey(cacheName = "userList", key = "'test'", evictOnAll = true)
    public Collection getAllUsers() {
        return testDAO.getAllModels();
    }

    //@Override
    @Transactional
    @CacheEvict(cacheName = "userList", key = "'test'")
    public void insertUser(IUser user) {
        testDAO.insert(user);
    }

    @Transactional
    @CacheModelEvict(modelClass = User.class, key = "#p0.id")
    @CacheEvict(cacheName = "userList", key = "'test'")
    public void updateUser(IUser user) {
        testDAO.updateModel(user);
    }

    @Transactional
    @CacheModelEvict(modelClass = User.class, key = "#p0.id")
    @CacheEvict(cacheName = "userList", key = "'test'")
    public void saveUser(IUser user) {
        testDAO.insert(user);
    }

    @Transactional
    @CacheEvict(cacheName = "userList", key = "'test'")
    @CacheModelEvict(modelClass = User.class, key = "#p0")
    public void deleteUser(String id) {
        IModel singleLongPKModel = new User1();
        singleLongPKModel.setId(Long.valueOf(id));
        testDAO.deleteModel(singleLongPKModel);
    }

    @Transactional
    @CacheEvict(cacheName = "userList", key = "'test'")
    @CacheModelEvict(modelClass = User.class, key = "#p0.id")
    public void deleteUser(IUser user) {
        testDAO.deleteModel(user);
    }

    @Override
    @CacheKey(cacheName = "userCacheById", key = "#p0")
    public IUser getUserById(Long id) {
        return testDAO.getUserById(id);
    }

    @Override
    @CacheKey(cacheName = "userCacheById", key = "#p0.id")
    public IUser getUserById(IUser user) {
        return testDAO.getUserById(user.getId());
    }

    @Override
    @CacheKey(cacheName = "userList", key = "#p0.firstName.concat(#p0.role)")
    public List<IUser> getUserByFirstNameAndStatus(IUser user) {
        String firstName = user.getFirstName();
        String role = user.getRole();
        return testDAO.getActiveUserByName(firstName, role);
    }
}
