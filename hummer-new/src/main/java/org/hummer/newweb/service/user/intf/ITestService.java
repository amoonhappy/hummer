package org.hummer.newweb.service.user.intf;

import org.hummer.newweb.model.intf.IUser;

import java.util.Collection;
import java.util.List;

public interface ITestService {
    Collection getAllUsers();

    void insertUser(IUser user);

    void updateUser(IUser user);

    void saveUser(IUser user);

    void deleteUser(String id);

    void deleteUser(IUser user);

    IUser getUserById(Integer id);

    IUser getUserById(IUser user);

    List<IUser> getUserByFirstNameAndStatus(IUser user);
}
