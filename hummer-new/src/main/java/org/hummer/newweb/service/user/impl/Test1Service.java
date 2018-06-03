package org.hummer.newweb.service.user.impl;

import org.hummer.core.service.impl.BasicTestService;
import org.hummer.newweb.dao.user.intf.ITestDAO;
import org.hummer.newweb.model.intf.IUser;
import org.hummer.newweb.service.user.intf.ITest1Service;

public class Test1Service extends BasicTestService implements ITest1Service {
    private ITestDAO testDAO;

    //@Override
    //@Transactional(propagation = Propagation.MANDATORY, timeout = 30000, isolation = Isolation.READ_COMMITTED)
    public void insertUser(IUser user) {
        testDAO.insert(user);
    }

    //@Transactional
    public void updateUser(IUser user) {
        user.setLastName("updated by test1");
        testDAO.updateModel(user);
    }

    //@Transactional
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