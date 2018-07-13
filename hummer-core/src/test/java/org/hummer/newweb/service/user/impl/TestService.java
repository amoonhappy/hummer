package org.hummer.newweb.service.user.impl;

//import org.hummer.core.beans.annotation.AutoLinked;

import org.hummer.core.cache.annotation.CacheKey;
import org.hummer.core.cache.intf.ICacheable;
import org.hummer.core.service.impl.BasicTestService;
import org.hummer.core.transaction.annotation.Transactional;
import org.hummer.newweb.dao.user.intf.ITestDAO;
import org.hummer.newweb.model.intf.IUser;
import org.hummer.newweb.service.user.intf.ITestService;

import java.util.Collection;
import java.util.List;

public class TestService extends BasicTestService implements ITestService, ICacheable {
    //    @AutoLinked
    private ITestDAO testDAO;

    @Override
    public Collection getAllUsers() {
        return testDAO.getAllModels();
    }

    //@Override
    @Transactional
    public void insertUser(IUser user) {
        testDAO.insert(user);
    }

    @Transactional
    public void updateUser(IUser user) {
        testDAO.updateModel(user);
    }

    @Transactional
    public void saveUser(IUser user) {
        testDAO.insert(user);
    }

    @Override
    public void deleteUser(String id) {
//        ISingleLongPKModel singleLongPKModel = new User1();
//        singleLongPKModel.setId(Long.valueOf(id));
//
//        testDAO.deleteModel(singleLongPKModel);
    }

    @Override
    @CacheKey(cacheName = "user", key = "#p0")
    public IUser getUserById(Long id) {

        System.out.println("adsfasdf");
        return testDAO.getUserById(id);
    }


    @CacheKey(cacheName = "user", key = "#p0.concat(#p1.id).concat(#p2)")
    public IUser getLatestUser(String status, IUser user, Integer time) {
        return testDAO.getUserById(Long.valueOf("30000"));
    }

    @CacheKey(cacheName = "user", key = "#p0.id")
    public IUser getUserById(IUser user) {
        return testDAO.getUserById(Long.valueOf(user.getId()));
    }


    @Override
    public IUser getLatestUser(String status, IUser user, Integer time, List list) {
        return null;
    }

}
