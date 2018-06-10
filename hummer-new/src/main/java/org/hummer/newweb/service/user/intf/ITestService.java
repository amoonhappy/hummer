package org.hummer.newweb.service.user.intf;

import org.hummer.newweb.model.intf.IUser;

import java.util.Collection;

public interface ITestService {
    Collection getAllUsers();

    void insertUser(IUser user);

    void updateUser(IUser user);

    void saveUser(IUser user);

    void deleteUser(String id);

    IUser getUserById(Integer id);
}
