package org.hummer.newweb.dao.user.intf;

import org.hummer.core.persistence.intf.IBasicTestDAO;
import org.hummer.newweb.model.intf.IUser;

import java.util.List;

public interface ITestDAO extends IBasicTestDAO {
    int insert(IUser a);

    IUser getUserById(Long id);

    List<IUser> getActiveUserByName(String firstName, String status);
}
