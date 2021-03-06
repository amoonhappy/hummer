package org.hummer.core.persistence.impl;


import org.hummer.core.model.intf.IModel;
import org.hummer.core.persistence.intf.IBasicTestDAO;
import org.hummer.core.util.MybatisUtil;

import java.util.List;


public abstract class BasicMybatisDAO implements IBasicTestDAO {
    private static final String SQL_ID_INSERT = "_insert";
    private static final String SQL_ID_DELETE = "_delete";
    private static final String SQL_ID_GET = "_get";
    private static final String SQL_ID_UPDATE = "_update";
    private static final String SQL_ID_SELECTALL = "_selectAll";

    //private final static Logger log = Log4jUtils.getLogger(BasicMybatisDAO.class);

    protected abstract String getModelName();

    public int insertModel(IModel iModel) {
        return MybatisUtil.getSession().insert(getModelName() + SQL_ID_INSERT, iModel);
    }

    public void deleteModel(IModel iModel) {
        MybatisUtil.getSession().delete(getModelName() + SQL_ID_DELETE, iModel);
    }

    public IModel getModel(IModel iModel) {
        IModel ret;
        ret = MybatisUtil.getSession().selectOne(getModelName() + SQL_ID_GET, iModel.getId());
        return ret;
    }

    public IModel getModel(Long id) {
        IModel ret;
        ret = MybatisUtil.getSession().selectOne(getModelName() + SQL_ID_GET, id);
        return ret;
    }

    public int updateModel(IModel iModel) {
        return MybatisUtil.getSession().update(getModelName() + SQL_ID_UPDATE, iModel);
    }

    //TODO: pagination search
    public List getAllModels() {
        return MybatisUtil.getSession().selectList(getModelName() + SQL_ID_SELECTALL);
    }
}
