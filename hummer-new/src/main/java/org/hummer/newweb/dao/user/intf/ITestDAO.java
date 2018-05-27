package org.hummer.newweb.dao.user.intf;

import org.hummer.core.persistence.intf.IBasicTestDAO;
import org.hummer.newweb.model.intf.IUser;

public interface ITestDAO extends IBasicTestDAO {
    int insert(IUser a);
}
