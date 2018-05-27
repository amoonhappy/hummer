package org.hummer.core.persistence.impl;


import org.hummer.core.model.intf.IModel;
import org.hummer.core.model.intf.ISingleLongPKModel;
import org.hummer.core.pagination.Pager;
import org.hummer.core.persistence.intf.IBasicTestDAO;
import org.hummer.core.util.Log4jUtils;
import org.hummer.core.util.MybatisUtil;
import org.slf4j.Logger;


public abstract class BasicMybatisDAO implements IBasicTestDAO {
    protected static final String SQL_ID_INSERT = "_insert";
    protected static final String SQL_ID_DELETE = "_delete";
    protected static final String SQL_ID_GET = "_get";
    protected static final String SQL_ID_UPDATE = "_update";
    private final static Logger log = Log4jUtils.getLogger(BasicMybatisDAO.class);

    protected abstract String getModelName();

    public void insertModel(IModel iModel) {

        try {
            MybatisUtil.getSession().insert(getModelName() + SQL_ID_INSERT, iModel);
            MybatisUtil.getSession().commit();
        } catch (Exception e) {
            log.error("add object {} failed!", iModel.toString(), e);
            e.printStackTrace();
            MybatisUtil.getSession().rollback();
        } finally {
            MybatisUtil.closeSession();
        }

    }

    public void deleteModel(IModel iModel) {
        try {
            MybatisUtil.getSession().delete(getModelName() + SQL_ID_DELETE, iModel);
            MybatisUtil.getSession().commit();
        } catch (Exception e) {
            log.error("add object {} failed!", iModel.toString(), e);
            e.printStackTrace();
            MybatisUtil.getSession().rollback();
        } finally {
            MybatisUtil.closeSession();
        }
    }

    public IModel getModel(ISingleLongPKModel iModel) {
        IModel ret;
        try {
            ret = MybatisUtil.getSession().selectOne(getModelName() + SQL_ID_GET, iModel.getId());
        } finally {
            MybatisUtil.closeSession();
        }
        return ret;
    }

    public void updateModel(ISingleLongPKModel iModel) {
        try {
            MybatisUtil.getSession().update(getModelName() + SQL_ID_UPDATE, iModel);
            MybatisUtil.getSession().commit();
        } catch (Exception e) {
            log.error("update object {} failed! id: [{}]", iModel.getId(), iModel.toString(), e);
            e.printStackTrace();
            MybatisUtil.getSession().rollback();
        } finally {
            MybatisUtil.closeSession();
        }
    }

    public Pager getAllModels() {
        return new Pager();
    }
}
