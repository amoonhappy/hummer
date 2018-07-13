package org.hummer.core.service.impl;

import org.hummer.core.model.intf.IModel;
import org.hummer.core.persistence.intf.IBasicTestDAO;
import org.hummer.core.service.intf.IBasicTestService;

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
    public void insertModel(IModel iModel) {
        dao.insertModel(iModel);
    }

    @Override
    public void deleteModel(IModel iModel) {
        dao.deleteModel(iModel);
    }

    @Override
    public IModel getModel(IModel iModel) {
        return dao.getModel(iModel);
    }

    @Override
    public Collection getAllModels() {
        return dao.getAllModels();
    }
}
