package org.hummer.newweb.dao.user.impl;

import org.hummer.core.persistence.impl.BasicMybatisDAO;
import org.hummer.newweb.dao.user.intf.ITestDAO;
import org.hummer.newweb.model.intf.IUser;

public class TestDAO extends BasicMybatisDAO implements ITestDAO {

    protected String getModelName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int insert(IUser a) {
        return super.insertModel(a);
    }

}
