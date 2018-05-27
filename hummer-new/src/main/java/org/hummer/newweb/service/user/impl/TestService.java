package org.hummer.newweb.service.user.impl;

import org.hummer.core.service.impl.BasicTestService;
import org.hummer.newweb.dao.user.intf.ITestDAO;
import org.hummer.newweb.model.intf.IUser;
import org.hummer.newweb.service.user.intf.ITestService;

public class TestService extends BasicTestService implements ITestService {
    private ITestDAO testDAO;


    @Override
    public IUser getAllUsers() {
        return null;
    }

    @Override
    public void insertUser(IUser user) {
        int i = testDAO.insert(user);
        user.setFirstName("changed");
        i = testDAO.updateModel(user);
    }

    public ITestDAO getTestDAO() {
        return testDAO;
    }

    public void setTestDAO(ITestDAO testDAO) {
        this.testDAO = testDAO;
    }
}
