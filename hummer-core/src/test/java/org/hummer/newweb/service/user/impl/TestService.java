package org.hummer.newweb.service.user.impl;

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

public class TestService extends BasicTestService implements ITestService, ICacheable {
    private ITestDAO testDAO;


    @Override
    public Collection getAllUsers() {
//        IUser[] ret = new IUser[10];
//        ret[0] = new User();
//        ret[1] = new User();
//        ret[2] = new User();
//        ret[3] = new User();
//        ret[4] = new User();
//        ret[5] = new User();
//        ret[6] = new User();
//        ret[7] = new User();
//        ret[8] = new User();
//        ret[9] = new User();
//
//        for (IUser iUser : ret) {
//            int i = 0;
//            iUser.setFirstName(i++ + "");
//        }
//        return ret;
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
