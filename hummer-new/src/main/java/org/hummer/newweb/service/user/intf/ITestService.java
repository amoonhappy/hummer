package org.hummer.newweb.service.user.intf;

import org.hummer.newweb.model.intf.IUser;

public interface ITestService {
    IUser getAllUsers();

    void insertUser(IUser user);
}
