package org.hummer.core.service.impl;

import org.hummer.core.model.intf.IModel;
import org.hummer.core.persistence.intf.IBasicTestDAO;
import org.hummer.core.service.intf.IBasicTestService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class BasicTestService implements IBasicTestService {
    IBasicTestDAO dao;

    public IBasicTestDAO getDao() {
        return dao;
    }

    public void setDao(IBasicTestDAO dao) {
        this.dao = dao;
    }

    @Override
    public void insertModel(IModel iModel) throws SQLException {
        dao.insertModel(iModel);
    }

    @Override
    public void deleteMOdel(IModel iModel) {

    }

    @Override
    public IModel getModel(IModel iModel) {
        return null;
    }

    @Override
    public Collection getAllModels() {
        return new ArrayList();
    }
}
