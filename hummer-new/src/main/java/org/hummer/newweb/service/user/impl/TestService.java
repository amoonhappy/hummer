package org.hummer.newweb.service.user.impl;

import org.hummer.core.cache.annotation.CacheEvict;
import org.hummer.core.cache.intf.ICacheable;
import org.hummer.core.model.intf.ISingleLongPKModel;
import org.hummer.core.service.impl.BasicTestService;
import org.hummer.core.transaction.annotation.Transactional;
import org.hummer.newweb.dao.user.intf.ITestDAO;
import org.hummer.newweb.model.impl.User1;
import org.hummer.newweb.model.intf.IUser;
import org.hummer.newweb.service.user.intf.ITestService;

import java.util.Collection;

@CacheEvict(evictForMethod = "saveUser", evictOnClass = TestService.class, evictOnMethod = "getUserById")
@CacheEvict(evictForMethod = "deleteUser", evictOnClass = TestService.class, evictOnMethod = "getUserById")
@CacheEvict(evictForMethod = "updateUser", evictOnClass = TestService.class, evictOnMethod = "getUserById")
@CacheEvict(evictForMethod = "insertUser", evictOnClass = TestService.class, evictOnMethod = "getAllUsers")
@CacheEvict(evictForMethod = "updateUser", evictOnClass = TestService.class, evictOnMethod = "getAllUsers")
@CacheEvict(evictForMethod = "saveUser", evictOnClass = TestService.class, evictOnMethod = "getAllUsers")
@CacheEvict(evictForMethod = "deleteUser", evictOnClass = TestService.class, evictOnMethod = "getAllUsers")
public class TestService extends BasicTestService implements ITestService, ICacheable {
    private ITestDAO testDAO;

    @Override
    public Collection getAllUsers() {
        return testDAO.getAllModels();
    }

    //@Override
    @Transactional
    public void insertUser(IUser user) {
        testDAO.insert(user);
    }

    @Transactional
    public void updateUser(IUser user) {
        testDAO.updateModel(user);
    }

    @Transactional
    public void saveUser(IUser user) {
        testDAO.insert(user);
    }

    @Override
    public void deleteUser(String id) {
        ISingleLongPKModel singleLongPKModel = new User1();
        singleLongPKModel.setId(Long.valueOf(id));

        testDAO.deleteModel(singleLongPKModel);
    }

    @Override
    public IUser getUserById(Integer id) {
        return testDAO.getUserById(id);
    }

    public ITestDAO getTestDAO() {
        return testDAO;
    }

    public void setTestDAO(ITestDAO testDAO) {
        this.testDAO = testDAO;
    }
}
