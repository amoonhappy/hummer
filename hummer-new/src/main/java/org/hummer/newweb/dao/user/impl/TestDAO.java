package org.hummer.newweb.dao.user.impl;

import org.hummer.core.persistence.impl.BasicMybatisDAO;
import org.hummer.core.util.MybatisUtil;
import org.hummer.newweb.dao.user.intf.ITestDAO;
import org.hummer.newweb.model.impl.User;
import org.hummer.newweb.model.intf.IUser;

import java.util.List;

public class TestDAO extends BasicMybatisDAO implements ITestDAO {

    protected String getModelName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int insert(IUser a) {
        return super.insertModel(a);
    }

    @Override
    public IUser getUserById(Integer id) {
        return (IUser) super.getModel(id);
    }

    @Override
    public List<IUser> getActiveUserByName(String firstName, String role) {
        IUser user = new User();
        user.setFirstName(firstName);
        user.setRole(role);
        return MybatisUtil.getSession().selectList("TestDAO_Selective", user);
    }

}
