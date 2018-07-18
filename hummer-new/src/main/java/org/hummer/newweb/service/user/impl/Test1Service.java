package org.hummer.newweb.service.user.impl;

import org.hummer.core.ioc.annotation.Autowired;
import org.hummer.core.ioc.annotation.BeanType;
import org.hummer.core.service.impl.BasicTestService;
import org.hummer.core.transaction.annotation.Propagation;
import org.hummer.core.transaction.annotation.Transactional;
import org.hummer.newweb.dao.user.intf.ITestDAO;
import org.hummer.newweb.model.intf.IUser;
import org.hummer.newweb.service.user.intf.ITest1Service;
import org.hummer.newweb.service.user.intf.ITestService;

public class Test1Service extends BasicTestService implements ITest1Service {
    @Autowired(BeanType.HUMMER_BEAN)
    ITestDAO testDAO;
    @Autowired(BeanType.HUMMER_BEAN)
    ITestService testService;

    //@Override
    @Transactional
    public void insertUser(IUser user) {
        testDAO.insert(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(IUser user) {
        user.setLastName("updated by test1");
        testDAO.updateModel(user);
        testService.getAllUsers();
    }

    @Transactional
    public void saveUser(IUser user) {
        testDAO.insert(user);
    }
}