package org.hummer.newweb.service.user.impl;

import org.hummer.core.cache.annotation.CacheEvict;
import org.hummer.core.cache.intf.ICacheable;
import org.hummer.core.container.impl.HummerContainer;
import org.hummer.core.container.intf.IBusinessServiceManager;
import org.hummer.core.container.intf.IHummerContainer;
import org.hummer.core.service.impl.BasicTestService;
import org.hummer.core.transaction.annotation.Transactional;
import org.hummer.newweb.dao.user.intf.ITestDAO;
import org.hummer.newweb.model.intf.IUser;
import org.hummer.newweb.service.user.intf.ITest1Service;
import org.hummer.newweb.service.user.intf.ITestService;

import java.util.Collection;

@CacheEvict(evictForMethod = "insertUser", evictOnClass = TestService.class, evictOnMethod = "getAllUsers")
@CacheEvict(evictForMethod = "updateUser", evictOnClass = TestService.class, evictOnMethod = "getAllUsers")
@CacheEvict(evictForMethod = "saveUser", evictOnClass = TestService.class, evictOnMethod = "getAllUsers")
public class TestService extends BasicTestService implements ITestService, ICacheable {
    private ITestDAO testDAO;

    @Override
    public Collection getAllUsers() {
        return testDAO.getAllModels();
    }

    //@Override
    @Transactional
    public void insertUser(IUser user) {
        IHummerContainer hummerContainer = HummerContainer.getInstance();
        IBusinessServiceManager bsm = hummerContainer.getServiceManager();
        ITest1Service service = (ITest1Service) bsm.getService("test1Service");
        saveUser(user);
//        user.setFirstName("changed by test");
        service.updateUser(user);

        updateUser(user);
    }

    @Transactional
    public void updateUser(IUser user) {
        user.setFirstName("updated by test");
        testDAO.updateModel(user);
        //throw new RuntimeException("aaaa");
    }

    @Transactional
    public void saveUser(IUser user) {
        testDAO.insert(user);
    }

    public ITestDAO getTestDAO() {
        return testDAO;
    }

    public void setTestDAO(ITestDAO testDAO) {
        this.testDAO = testDAO;
    }
}
