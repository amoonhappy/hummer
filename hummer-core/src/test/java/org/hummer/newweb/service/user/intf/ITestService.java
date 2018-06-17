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

    IUser getUserById(Integer id);

    IUser getLatestUser(String status, IUser user, Integer time, List list);
}
