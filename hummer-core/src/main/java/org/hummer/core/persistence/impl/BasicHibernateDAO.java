/**
 * <p>Open Source Architecture Project -- Hummer            </p>
 * <p>Class Description                                     </p>
 * <p>                                                      </p>
 * <p>                                                      </p>
 * <p>Change History                                        </p>
 * <p>Author    Date      Description                       </p>
 * <p>                                                      </p>
 * <p>                                                      </p>
 *
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a> Date: 2005-12-7
 * @version 1.0
 */
package org.hummer.core.persistence.impl;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hummer.core.constants.ExceptionConstant;
import org.hummer.core.constants.SearchConstant;
import org.hummer.core.exception.BusinessException;
import org.hummer.core.model.ModelFactory;
import org.hummer.core.model.intf.*;
import org.hummer.core.pagination.Pager;
import org.hummer.core.persistence.intf.HibernateCallback;
import org.hummer.core.persistence.intf.IBasicDAO;
import org.hummer.core.persistence.intf.IPagerHandler;
import org.hummer.core.util.*;
import org.slf4j.Logger;

import javax.sql.RowSet;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
//import org.springframework.orm.hibernate3.HibernateCallback;
//import org.springframework.orm.hibernate3.SessionFactoryUtils;

//import sun.jdbc.rowset.CachedRowSet;

/**
 * @author jeff.zhou
 */
public class BasicHibernateDAO implements IBasicDAO {
    private static final Logger log = Log4jUtils.getLogger(BasicHibernateDAO.class);
    protected IPagerHandler pagerHandler;
    private IBasicDAO proxy = null;

    public IBasicDAO getProxy() {
        return proxy;
    }

    public void setProxy(IBasicDAO proxy) {
        this.proxy = proxy;
    }

    /**
     * the entry of pager handler Type 2 Setter injection
     *
     * @param pagerHandler
     */
    public void setPagerHandler(IPagerHandler pagerHandler) {
        this.pagerHandler = pagerHandler;
    }

    /**
     * this method is suitable for increase one column's value of sequence table
     *
     * @param o         the model
     * @param getMethod the get method name of the increase column
     * @param setMethod the set method name of the increase column
     * @return
     * @throws Exception
     */
    public IModel increaseSeq(IModel o, String getMethod, String setMethod) throws Exception {
        Serializable id = null;

        if (o == null) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_GET_CONDITION_ERROR);
        }

        Method getMethods = null;
        Method setMethods = null;
        Method[] methods = ModelFactory.getInstance().getPGMethod(o.getClass());

        for (int i = 0; i < methods.length; i++) {
            Method temp = methods[i];

            if (StringUtils.equalsIgnoreCase(getMethod, temp.getName())) {
                getMethods = methods[i];
            }

            if (StringUtils.equalsIgnoreCase(setMethod, temp.getName())) {
                setMethods = methods[i];
            }
        }

        // use reflection to get the id or composite id and fetch the object
        // Session session = null;
        // try {
        // session = HibernateUtil.getSession();
        id = ModelUtil.getIdValue(o);
        o = (IModel) HibernateUtil.getSession().get(o.getClass(), id, LockMode.UPGRADE);
        // o = (IModel) HibernateUtil.getSession().get(o.getClass(), id,
        // LockMode.UPGRADE);

        if ((getMethods != null) && (setMethods != null)) {
            Object orgValue = getMethods.invoke(o, BlankObjectUtil.ObjectArray);

            if (orgValue instanceof Long) {
                Long aLong = (Long) orgValue;
                Object[] newValues = new Object[]{new Long(aLong.longValue() + 1)};

                // increase the value
                setMethods.invoke(o, newValues);
            }
        }

        // HibernateUtil.getSession().flush();
        HibernateUtil.getSession().flush();

        return o;
    }

    /**
     * this method is suitable for simple object query that return one
     * instance,pay attention that the Model MUST have the getId or getComp_id
     * method is locked is true dao will select for update
     *
     * @param o
     * @param locked
     * @return a populated object (or null if id doesn't exist)
     */
    public IModel getObject(IModel o, boolean locked) throws Exception {
        Serializable id = null;

        if (o == null) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_GET_CONDITION_ERROR);
        }

        id = ModelUtil.getIdValue(o);

        if (id == null) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_NO_IDENTIFIER);
        }

        if (locked) {
            o = (IModel) HibernateUtil.getSession().get(o.getClass(), id, LockMode.UPGRADE);
        } else {
            o = (IModel) HibernateUtil.getSession().get(o.getClass(), id);
        }

        o = LobDisBoundWrap(o);

        return o;
    }

    /**
     * this method is suitable for simple object query that return one
     * instance,pay attention that the Model MUST have the getId or getComp_id
     * method
     *
     * @param o
     * @return
     * @throws Exception
     */
    public IModel getObject(IModel o) throws Exception {
        return getObject(o, false);
    }

    /**
     * this method is suitable for simple object update and insert that return
     * one instance,pay attention that the Model MUST have the getId or
     * getComp_id method
     *
     * @param o
     * @param isInsert if false when no id throw exception if true when id exist
     *                 throw exception
     * @return
     * @throws Exception
     */
    public IModel saveObject(IModel o, boolean isInsert) throws Exception {
        Serializable ret = null;

        if (o == null) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_SAVE_CONDITION_ERROR);
        }

        Session session = null;

        // try {
        session = HibernateUtil.getSession();

        // if true when no id throw exception
        if (isInsert) {
            IModel model = null;

            try {
                model = (IModel) session.load(o.getClass(), ModelUtil.getIdValue(o));
            } catch (Exception e) {
                // nothing to do
            }

            if ((model != null) && (ModelUtil.getIdValue(model) != null)) {
                throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_DUPLICATE_ERROR);
            }
        } else {
            // check the id or comp_id????
            Serializable id = ModelUtil.getIdValue(o);

            if (id == null) {
                throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_NO_IDENTIFIER);
            }
        }

        if (o instanceof IAuditableModel) {
            IAuditableModel changeLogModel = (IAuditableModel) o;
            changeLogModel.setCrtDate(DateUtil.getDate());
            // changeLogModel.setCrtUser(SecurityUtil.getUserId());
            changeLogModel.setMdfDate(DateUtil.getDate());
            // changeLogModel.setUpdUser(SecurityUtil.getUserId());
            o = changeLogModel;
        }

        session.evict(o);
        // ret = (IModel) HibernateUtil.getSession().saveOrUpdateCopy(o);
        ret = (IModel) HibernateUtil.getSession().merge(o);

        // session.flush();
        // } finally {
        // SessionFactoryUtils.closeSessionIfNecessary(session,
        // getSessionFactory());
        // }
        return (IModel) ret;
    }

    /**
     * this method is suitable for simple object update and insert that return
     * one instance,pay attention that the Model MUST have the getId or
     * getComp_id method
     * <p/>
     * first select the object from the database if the model does not exist in
     * the database, then insert if the model exist in the database, then update
     *
     * @param o
     * @return
     * @throws Exception
     */
    public IModel saveObject(IModel o) throws Exception {
        // HibernateUtil.beginTransaction();
        Serializable ret = null;

        if (o == null) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_SAVE_CONDITION_ERROR);
        }

        // Session session = null;
        // try {
        // session = HibernateUtil.getSession();
        // check the id or comp_id???
        Serializable id = ModelUtil.getIdValue(o);

        // if (id == null) {
        // throw
        // ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_NO_IDENTIFIER);
        // }

        if (o instanceof IAuditableModel) {
            IAuditableModel changeLogModel = (IAuditableModel) o;
            if (id == null) {
                changeLogModel.setCrtDate(DateUtil.getDate());
                // changeLogModel.setCrtUser(SecurityUtil.getUserId());
            }
            changeLogModel.setMdfDate(DateUtil.getDate());
            // changeLogModel.setUpdUser(SecurityUtil.getUserId());
            o = changeLogModel;
        }

        HibernateUtil.getSession().evict(o);
        // ret = (IModel) HibernateUtil.getSession().saveOrUpdateCopy(o);
        ret = (IModel) HibernateUtil.getSession().merge(o);
        HibernateUtil.getSession().flush();
        // HibernateUtil.commitTransaction();

        // } finally {
        // SessionFactoryUtils.closeSessionIfNecessary(session,
        // getSessionFactory());
        // }
        return (IModel) ret;
    }

    /**
     * this method is suitable for simple object update that return one
     * instance,pay attention that the Model MUST have the getId or getComp_id
     * method
     * <p/>
     * if the id or comp_id is null, a BIZ_NO_IDENTIFIER BusinessException will
     * occur.
     *
     * @param c
     * @throws Exception
     */
    public void updateObject(Collection<IModel> c) throws Exception {
        // Session session = null;
        // try {
        // session = HibernateUtil.getSession();
        for (Iterator<IModel> iterator = c.iterator(); iterator.hasNext(); ) {
            IModel model = iterator.next();

            if (model == null) {
                throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_SAVE_CONDITION_ERROR);
            }

            // check the id or comp_id
            Serializable id = ModelUtil.getIdValue(model);

            if (id == null) {
                throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_NO_IDENTIFIER);
            }

            if (model instanceof IAuditableModel) {
                IAuditableModel changeLogModel = (IAuditableModel) model;
                changeLogModel.setMdfDate(DateUtil.getDate());
                // changeLogModel.setUpdUser(SecurityUtil.getUserId());
                model = changeLogModel;
            }

            HibernateUtil.getSession().evict(model);

            LobWrap(model);

            HibernateUtil.getSession().update(model);
        }

        // session.flush();
        // } finally {
        // SessionFactoryUtils.closeSessionIfNecessary(session,
        // getSessionFactory());
        // }
    }

    /**
     * this method is suitable for simple object update that return one
     * instance,pay attention that the Model MUST have the getId or getComp_id
     * method
     * <p/>
     * if the id or comp_id is null, a BIZ_NO_IDENTIFIER BusinessException will
     * occur.
     *
     * @param o
     * @return
     * @throws Exception
     */
    public IModel updateObject(IModel o) throws Exception {
        if (o == null) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_SAVE_CONDITION_ERROR);
        }

        // Session session = null;
        // try {
        // session = HibernateUtil.getSession();
        // check the id or comp_id
        Serializable id = ModelUtil.getIdValue(o);

        if (id == null) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_NO_IDENTIFIER);
        }

        if (o instanceof IAuditableModel) {
            IAuditableModel changeLogModel = (IAuditableModel) o;
            changeLogModel.setMdfDate(DateUtil.getDate());
            // changeLogModel.setUpdUser(SecurityUtil.getUserId());
            o = changeLogModel;
        }

        HibernateUtil.getSession().evict(o);

        LobWrap(o);

        HibernateUtil.getSession().update(o);
        HibernateUtil.getSession().flush();

        // } finally {
        // SessionFactoryUtils.closeSessionIfNecessary(session,
        // getSessionFactory());
        // }
        // becase of the blob and clob does not
        // implement the Serializable interface
        // so I must disbound the blob and clob
        // from the Entity Bean
        o = LobDisBoundWrap(o);

        return o;
    }

    /**
     * this method is suitable for a collection simple objects
     * <p>
     * insert that return one instance,pay attention that the
     * <p>
     * Model MUST have the getId or getComp_id method
     * <p>
     * If the Model is already in the database, will throw a
     * <p>
     * BIZ_DUPLICATE_ERROR exception
     * <p>
     *
     * @param c
     * @throws Exception
     */
    public void addObject(Collection<IModel> c) throws Exception {
        Serializable identify = null;

        // Session session = null;
        // try {
        // session = HibernateUtil.getSession();
        for (Iterator<IModel> iterator = c.iterator(); iterator.hasNext(); ) {
            IModel model = iterator.next();
            IModel ret = null;

            if (model == null) {
                throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_SAVE_CONDITION_ERROR);
            }

            if (model instanceof IAuditableModel) {
                IAuditableModel changeLogModel = (IAuditableModel) model;
                changeLogModel.setCrtDate(DateUtil.getDate());
                // changeLogModel.setCrtUser(SecurityUtil.getUserId());
                model = changeLogModel;
            }

            // load the record first to check the duplication
            try {
                ret = (IModel) HibernateUtil.getSession().load(model.getClass(), ModelUtil.getIdValue(model));
            } catch (Exception e) {
                // nothing to do
            }

            if ((ret != null) && (ModelUtil.getIdValue(ret) != null)) {
                throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_DUPLICATE_ERROR);
            }

            LobWrap(model);
            HibernateUtil.getSession().evict(model);
            identify = HibernateUtil.getSession().save(model);

            if (model instanceof ISingleStringPKModel) {
                ISingleStringPKModel singleStringPKModel = (ISingleStringPKModel) model;
                singleStringPKModel.setId((String) identify);
                ret = singleStringPKModel;
            } else if (model instanceof ISingleLongPKModel) {
                ISingleLongPKModel singleStringPKModel = (ISingleLongPKModel) model;
                singleStringPKModel.setId((Long) identify);
                ret = singleStringPKModel;
            } else if (model instanceof ICompositePKModel) {
                ICompositePKModel compPKModel = (ICompositePKModel) model;
                compPKModel.setComp_id(identify);
                ret = compPKModel;
            } else {
                ret = model;
            }
        }

        HibernateUtil.getSession().flush();

        // } finally {
        // SessionFactoryUtils.closeSessionIfNecessary(session,
        // getSessionFactory());
        // }
    }

    /**
     * this method is suitable for simple object insert that return one
     * instance,pay attention that the Model MUST have the getId or getComp_id
     * method If the Model is already in the database, will throw a
     * BIZ_DUPLICATE_ERROR exception
     *
     * @param o
     * @return
     * @throws Exception
     */
    public IModel addObject(IModel o) throws Exception {
        Serializable identify = null;
        IModel ret = null;

        if (o == null) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_SAVE_CONDITION_ERROR);
        }

        if (o instanceof IAuditableModel) {
            IAuditableModel changeLogModel = (IAuditableModel) o;
            changeLogModel.setCrtDate(DateUtil.getDate());
            // changeLogModel.setCrtUser(SecurityUtil.getUserId());
            o = changeLogModel;
        }

        // Session session = null;
        // try {
        // session = HibernateUtil.getSession();
        // load the record first to check the duplication
        try {
            ret = (IModel) HibernateUtil.getSession().load(o.getClass(), ModelUtil.getIdValue(o));
        } catch (Exception e) {
            // nothing to do
        }

        if ((ret != null) && (ModelUtil.getIdValue(ret) != null)) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_DUPLICATE_ERROR);
        }

        LobWrap(o);
        HibernateUtil.getSession().evict(o);
        identify = HibernateUtil.getSession().save(o);

        if (o instanceof ISingleStringPKModel) {
            ISingleStringPKModel singleStringPKModel = (ISingleStringPKModel) o;
            singleStringPKModel.setId((String) identify);
            ret = singleStringPKModel;
        } else if (o instanceof ISingleLongPKModel) {
            ISingleLongPKModel singleStringPKModel = (ISingleLongPKModel) o;
            singleStringPKModel.setId((Long) identify);
            ret = singleStringPKModel;
        } else if (o instanceof ICompositePKModel) {
            ICompositePKModel compPKModel = (ICompositePKModel) o;
            compPKModel.setComp_id(identify);
            ret = compPKModel;
        } else {
            ret = o;
        }

        HibernateUtil.getSession().flush();

        // } finally {
        // SessionFactoryUtils.closeSessionIfNecessary(session,
        // getSessionFactory());
        // }
        ret = LobDisBoundWrap(ret);

        return ret;
    }

    /**
     * this method is used to initialize the blob or clob
     *
     * @param o
     */
    public void LobWrap(IModel o) {
        byte[] buffer = new byte[1];
        buffer[0] = 1;

        if (o instanceof IBlobModel) {
            IBlobModel blobModel = (IBlobModel) o;
            //blobModel.setBlob(HibernateUtil.getSession().getLobHelper().createBlob(buffer));
            // blobModel.setBlob(Hibernate.createBlob(buffer));
        } else if (o instanceof IClobModel) {
            IClobModel clobModel = (IClobModel) o;
            //clobModel.setClob(HibernateUtil.getSession().getLobHelper().createClob(" "));
            // clobModel.setClob(Hibernate.createClob(" "));
        }
    }

    /**
     * this method is used to dis-bound the blob or clob before the entity
     * return to the persistence tier
     *
     * @param o
     * @return
     */
    public IModel LobDisBoundWrap(IModel o) {
        if (o instanceof IBlobModel) {
            IBlobModel blobModel = (IBlobModel) o;
            blobModel.setBlob(null);
            o = blobModel;
        } else if (o instanceof IClobModel) {
            IClobModel clobModel = (IClobModel) o;
            clobModel.setClob(null);
            o = clobModel;
        }

        return o;
    }

    /**
     * this method is suitable for simple object delete that return one
     * instance,pay attention that the Model MUST have the getId or getComp_id
     * method
     * <p>
     * <p/>
     * if the id or comp_id is null, then gen the delete where clause from the
     * model and exec delete method
     * <p>
     * if the id or comp_id is not null, then remove the object directly by
     * hibernate
     * <p>
     * <p/>
     * <B>Notice:</B> if you want to batch delete some rows please use
     * dao.execUpdateSQL instead of this method!
     *
     * @param o
     * @return
     * @throws Exception
     */
    public IModel deleteObject(IModel o) throws Exception {
        IModel ret = null;

        if (o == null) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_DELETE_CONDITION_ERROR);
        }

        Serializable id = ModelUtil.getIdValue(o);

        if (id == null) {
            SearchCondition searchCondition = new SearchCondition();
            searchCondition.setDeleteModel(o);
            deleteObject(searchCondition);
        } else {
            o = (IModel) HibernateUtil.getSession().get(o.getClass(), id, LockMode.UPGRADE);

            if (o != null) {
                HibernateUtil.getSession().delete(o);
            }
        }

        ret = o;

        HibernateUtil.getSession().flush();

        return ret;
    }

    /**
     * Generic method to delete some objects, the where clause is generated
     * according to the inputted searchCondition
     * <p/>
     * s *
     *
     * @param searchCondition
     */
    public void deleteObject(SearchCondition searchCondition) throws Exception {
        String deleteSQL = new String();
        // List<Object> paramList = null;

        // if (searchCondition.isEnableAutoDeleteWhereGen()) {
        // searchCondition.setEnableAutoWhereGen(true);
        // searchCondition.setHibernateQL(searchCondition.getDeleteHQL());
        // searchCondition.setHQLTableAlias(searchCondition.getDeleteTableAlias());
        // searchCondition.setSearchModel(searchCondition.getDeleteModel());
        // searchCondition =
        // HQLUtil.genWhereClause(searchCondition.getDeleteModel(),
        // searchCondition);
        // deleteSQL = searchCondition.getHibernateQL();
        // paramList = searchCondition.getQueryParam();
        // } else {
        // deleteSQL = searchCondition.getDeleteHQL();
        // paramList = searchCondition.getDeleteParam();
        // }

        // Object[] params = (Object[]) paramList.toArray(newweb
        // Object[paramList.size()]);

        // List<Type> types = HQLUtil.parseTypes(paramList);
        // HibernateUtil.getSession().delete(deleteSQL, params, (Type[])
        // types.toArray(newweb Type[types.size()]));
        BatchDeleteCallback bdcb = new BatchDeleteCallback();
        bdcb.setDeleteSQL(deleteSQL);
        bdcb.setDeleteModel(searchCondition.getDeleteModel());
        HibernateUtil.getSession().delete(deleteSQL, searchCondition.getDeleteModel());
        // HibernateUtil.getSession().execute(bdcb);

    }

    /**
     * remove the objects stored in collection
     *
     * @param c
     * @throws Exception
     */
    public void removeObject(Collection<IModel> c) throws Exception {
        for (Iterator<IModel> iterator = c.iterator(); iterator.hasNext(); ) {
            IModel model = iterator.next();

            if (model == null) {
                throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_DELETE_CONDITION_ERROR);
            }

            // ret = this.getObject(o);
            Serializable id = ModelUtil.getIdValue(model);

            if (id == null) {
                SearchCondition searchCondition = new SearchCondition();
                searchCondition.setDeleteModel(model);
                deleteObject(searchCondition);
            }
            // add end
            else {
                model = (IModel) HibernateUtil.getSession().get(model.getClass(), id, LockMode.UPGRADE);
                HibernateUtil.getSession().delete(model);
            }
        }

        HibernateUtil.getSession().flush();
    }

    /**
     * this method is suitable for Hibernate QL based on simple object search,
     * this method return a Pager instance for pagination. And the whole
     * Pagination Architecture should worked with the custom tag lib(pager-lib)
     * and display-tag
     * <p>
     *
     * @param searchCondition which contain the hql string and the queryparam collection. by
     *                        default, the query param is genarated by the queryModel(Type
     *                        is List), but you can set the query param manually.
     *                        <p>
     *                        Remeber that the queryModel's size must match the number of
     *                        "?" in the hql string.
     *                        <p>
     *                        For example:
     *                        <p>
     *                        HQL: select user.lastname, user.firstname from User user where
     *                        user.lastname like ? or user.firstname like ?
     *                        <p>
     *                        <p/>
     *                        queryModel operation:
     *                        <p>
     *                        addQueryModel("%lastname%");
     *                        <p>
     *                        addQueryModel("%firstname%");
     *                        <p>
     * @return Pager
     * @throws Exception
     */
    public Pager searchByHQL(SearchCondition searchCondition) throws Exception {
        Pager ret = new Pager();

        Object[] ph = new Object[]{searchCondition};
        IPagerHandler handler = pagerHandler;
        ret = handler.getPager(SearchConstant.QUERY_TYPE_HQL, ph);
        ret.setPageSize(searchCondition.getSize());

        return ret;
    }

    /**
     * @return Pager
     * @throws BusinessException
     * @deprecated
     */
    public Pager getObjectBySQL(SearchCondition searchCondition) throws Exception {
        Pager ret = new Pager();

        Object[] ph = new Object[]{searchCondition};

        ret = pagerHandler.getPager(SearchConstant.QUERY_TYPE_SQL, ph);
        ret.setPageSize(searchCondition.getSize());

        return ret;
    }

    /**
     * Save all the model in collection and if isInsert is true if each one of
     * the model and the model had exist in the db then will throw an exception.
     * and the operation will cancelled and
     *
     * @param c
     * @param isInsert
     * @throws Exception
     */
    public void saveObject(Collection<IModel> c, boolean isInsert) throws Exception {
        for (Iterator<IModel> iterator = c.iterator(); iterator.hasNext(); ) {
            IModel model = (IModel) iterator.next();

            if (model == null) {
                throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_SAVE_CONDITION_ERROR);
            }

            // check the id or comp_id
            Serializable id = ModelUtil.getIdValue(model);

            if (id == null) {
                throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_NO_IDENTIFIER);
            }

            if (model instanceof IAuditableModel) {
                IAuditableModel changeLogModel = (IAuditableModel) model;
                changeLogModel.setCrtDate(DateUtil.getDate());
                // changeLogModel.setCrtUser(SecurityUtil.getUserId());
                changeLogModel.setMdfDate(DateUtil.getDate());
                // changeLogModel.setUpdUser(SecurityUtil.getUserId());
                model = changeLogModel;
            }

            // if true when no id throw exception
            if (isInsert) {
                IModel temp = null;

                try {
                    temp = (IModel) HibernateUtil.getSession().load(model.getClass(), ModelUtil.getIdValue(model));
                } catch (Exception e) {
                    // nothing to do
                }

                if ((temp != null) && (ModelUtil.getIdValue(temp) != null)) {
                    throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_DUPLICATE_ERROR);
                }
            }

            HibernateUtil.getSession().evict(model);
            HibernateUtil.getSession().saveOrUpdate(model);
        }

        HibernateUtil.getSession().flush();

        // } finally {
        // SessionFactoryUtils.closeSessionIfNecessary(session,
        // getSessionFactory());
        // }
    }

    /**
     * save the objects in collection
     *
     * @param c
     * @throws Exception
     */
    public void saveObject(Collection<IModel> c) throws Exception {
        for (Iterator<IModel> iterator = c.iterator(); iterator.hasNext(); ) {
            IModel model = (IModel) iterator.next();

            if (model == null) {
                throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_SAVE_CONDITION_ERROR);
            }

            if (model instanceof IAuditableModel) {
                IAuditableModel changeLogModel = (IAuditableModel) model;
                changeLogModel.setCrtDate(DateUtil.getDate());
                // changeLogModel.setCrtUser(SecurityUtil.getUserId());
                changeLogModel.setMdfDate(DateUtil.getDate());
                // changeLogModel.setUpdUser(SecurityUtil.getUserId());
                model = changeLogModel;
            }

            // check the id or comp_id???
            Serializable id = ModelUtil.getIdValue(model);

            if (id == null) {
                throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_NO_IDENTIFIER);
            }

            HibernateUtil.getSession().evict(model);
            HibernateUtil.getSession().saveOrUpdate(model);
        }

        HibernateUtil.getSession().flush();

        // } finally {
        // SessionFactoryUtils.closeSessionIfNecessary(session,
        // getSessionFactory());
        // }
    }

    /**
     * search some objects according to the search conditions in SearchCondition
     * please refer to the SearchCondition to master the condition usage
     *
     * @param o
     * @see SearchCondition
     */
    public Pager searchByCriteria(SearchCondition o) throws Exception {
        Pager ret = new Pager();

        if ((o == null) || (o.getSearchModel() == null)) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_SEARCH_CONDITION_ERROR);
        }

        Example example = Example.create(o.getSearchModel());

        if (o.isEnableLike()) {
            switch (o.getMatchModel()) {
                case SearchConstant.MATCH_MODEL_ANYWHERE:
                    example = example.enableLike(MatchMode.ANYWHERE);

                    break;

                case SearchConstant.MATCH_MODEL_EXACT:
                    example = example.enableLike(MatchMode.EXACT);

                    break;

                case SearchConstant.MATCH_MODEL_START:
                    example = example.enableLike(MatchMode.START);

                    break;

                case SearchConstant.MATCH_MODEL_END:
                    example = example.enableLike(MatchMode.END);

                    break;

                default:
                    example = example.enableLike(MatchMode.END);

                    break;
            }
        }

        if (o.isExcludeZeroes()) {
            example = example.excludeZeroes();
        }

        if (o.isExcludeNone()) {
            example = example.excludeNone();
        }

        if (o.isIgnoreCase()) {
            example = example.ignoreCase();
        }

        List<String> excludeProperties = o.getExcludeProperties();

        if ((excludeProperties == null) || (excludeProperties.size() > 0)) {
            for (int i = 0; i < excludeProperties.size(); i++) {
                String excludeProperty = (String) excludeProperties.get(i);
                example = example.excludeProperty(excludeProperty);
            }
        }

        // inject the pager handler
        Object[] ph = new Object[]{o, example};

        ret = pagerHandler.getPager(SearchConstant.QUERY_TYPE_CRITERIA, ph);
        ret.setPageSize(o.getSize());

        return ret;
    }

    /**
     * search objects by sql mapping name the sql is stored in the hibernate
     * mapping file and identified by an uniqued string name If you want to exec
     * the sql you can just refer the sql uniqued name and addQueryParam.
     *
     * <B>Notice</B>:If you write the sql in the hibernate mapping file you
     * should identify all the columns for the alias object
     * <p>
     * For Example:
     * <p>
     * Model: User.java -String id
     * <p>
     * -String firstname
     * <p>
     * -String lastname
     * <p>
     * SQL:
     * <p>
     * <sql-query name="queryByFirst">
     * <p>
     * select {u}.id as {u.id},{u}.firstname as {u.firstname},
     * <p>
     * {u}.lastname as {u.lastname}
     * <p>
     * from users {u} where {u}.firstname = ?
     *
     * <return alias="u" class="com.ial.hbs.model.User"/>
     * <p>
     * </sql-query>
     * <p>
     * if you don't want to select some columns for performance consideration
     * you can write like this,image that i don't want to query the firstname
     * from database: select {u}.id as {u.id}, <B>NULL as {u.firstname}</B>,
     * {u}.lastname as {u.lastname} from users {u} where {u}.firstname = ?
     *
     * @param searchCondition
     * @return Pager
     */
    public Pager searchBySQLMapping(SearchCondition searchCondition) throws Exception {
        Pager ret = new Pager();

        Object[] ph = new Object[]{searchCondition};

        ret = pagerHandler.getPager(SearchConstant.QUERY_TYPE_SQLNAME, ph);
        ret.setPageSize(searchCondition.getSize());

        return ret;
    }

    /**
     * Execute the sql via JDBC and the sql is store in the hibernate mapping
     * files
     * <p>
     * For Example:
     * <p>
     * sc.setQuerySQLName("deleteArchiveIdocById");
     * <p>
     * sc.addParam(pjCd);
     * <p>
     * sc.addParam(arCd);
     * <p>
     * int count = dao.execSQLMapping(sc);
     * <p>
     *
     * @param searchCondition
     * @return
     * @throws Exception
     */
    public int execSQLMapping(SearchCondition searchCondition) throws Exception {
        Connection con = null;
        PreparedStatement pstmt = null;
        int count = 0;
        Session session = null;

        try {
            session = HibernateUtil.getSession();

            String execSQL = session.getNamedQuery(searchCondition.getQuerySQLName()).getQueryString();

            con = HibernateUtil.getConnection();

            count = JDBCUtil.execUpdate(con, execSQL, searchCondition.getQueryParam());
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }

            // if (con != null) {
            // con.close();
            // }
            // SessionFactoryUtils.closeSessionIfNecessary(session,
            // getSessionFactory());
            // SessionFactoryUtils.releaseSession(session,
            // HibernateUtil.getSessionFactory());
            HibernateUtil.closeSession();
        }

        return count;
    }

    /**
     * Execute the SQL by setQuerySQL() and addQueryParam() and then return the
     * cached row set from Result set For Example:
     * <p/>
     * String sql = "select * from REFERENCE where code=?"; sc.addParam(id+"1");
     * sc.addParam(id); CachedRowSet rs = (CachedRowSet) dao.execSQL(sc);
     * <p/>
     * if(rs!=null){ while(rs.next()){ return rs.getString("VALUE"); } }
     *
     * @param searchCondition
     * @return
     * @throws Exception
     */
    public RowSet execSQL(SearchCondition searchCondition) throws Exception {
        ResultSet rs = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        //CachedRowSet crset = null;

        try {
            String execSQL = searchCondition.getQuerySQL();

            con = HibernateUtil.getConnection();

            rs = JDBCUtil.execQuery(con, execSQL, searchCondition.getQueryParam());
            log.info("execSQL=" + execSQL);
            //crset = newweb CachedRowSet();
            //crset.populate(rs);
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

            // SessionFactoryUtils.releaseSession(session,
            // HibernateUtil.getSessionFactory());
            HibernateUtil.closeSession();
        }
        return null;
        //return crset;
    }

    /**
     * exec the update sql via JDBC and return the updated or deleted rows'
     * number For Example: SearchCondition sc = newweb SearchCondition
     * sc.setUpdateSQL("update archive a set a.roll_sts=? where a.prjt_cd=? and
     * a.arcv_cd=?"); sc.addUpdateParam(archive.getRollSts());
     * sc.addUpdateParam(pk.getPrjtCd()); sc.addUpdateParam(pk.getArcvCd()); int
     * count = dao.execUpdateSQL(sc);
     *
     * @param searchCondition
     * @throws Exception
     */
    public int execUpdateSQL(SearchCondition searchCondition) throws Exception {
        Connection con = null;
        PreparedStatement pstmt = null;
        int count = 0;
        try {
            String execSQL = searchCondition.getUpdateSQL();
            con = HibernateUtil.getConnection();
            log.info("update SQL=" + execSQL);

            count = JDBCUtil.execUpdate(con, execSQL, searchCondition.getUpdateParam());
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }

            // if (con != null) {
            // con.close();
            // }
            // SessionFactoryUtils.releaseSession(session,
            // HibernateUtil.getSessionFactory());
            HibernateUtil.closeSession();
        }

        return count;
    }

    /**
     * update the BlobModel set the input String to the Blob column and
     * overwirte the original Blob
     *
     * @param bmodel
     * @param input
     * @return
     * @throws Exception
     */
    public Serializable updateBlob(IBlobModel bmodel, byte[] input) throws Exception {
        Serializable ret = null;

        if (bmodel == null) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_GET_CONDITION_ERROR);
        }

        // use reflection to get the id or composite id and fetch the object
        Session session = null;

        try {
            session = HibernateUtil.getSession();

            if (!HQLUtil.isValidSinglePKModel(bmodel)) {
                throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_NO_IDENTIFIER);
            }

            ret = ModelUtil.getIdValue(bmodel);
            bmodel = (IBlobModel) session.get(bmodel.getClass(), ret, LockMode.UPGRADE);

            // to dis bound the dependence to app server
            Object blobInstance = bmodel.getBlob();

            if (blobInstance != null) {
                Method getBinOutPut = blobInstance.getClass().getMethod("getBinaryOutputStream", new Class[0]);

                if ((blobInstance != null) && (getBinOutPut != null)) {
                    OutputStream out = (OutputStream) getBinOutPut.invoke(blobInstance, new Object[0]);
                    out.write(input);
                    out.close();
                }

                session.flush();
            }
        } catch (IOException e) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.DB_ACCESS_ERROR);
        } catch (NoSuchMethodException e) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.DB_HIBERNATE_ERROR);
        } finally {
            // SessionFactoryUtils.releaseSession(session,
            // HibernateUtil.getSessionFactory());
            HibernateUtil.closeSession();
        }

        return ret;
    }

    /**
     * update the ClobModel set the input String to the Clob column and
     * overwirte the original Clob
     *
     * @param cmodel
     * @param input
     * @return
     * @throws Exception
     */
    public Serializable updateClob(IClobModel cmodel, String input) throws Exception {
        Serializable ret = null;

        if (cmodel == null) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_GET_CONDITION_ERROR);
        }

        // use reflection to get the id or composite id and fetch the object
        Session session = null;

        try {
            session = HibernateUtil.getSession();

            if (!HQLUtil.isValidSinglePKModel(cmodel)) {
                throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_NO_IDENTIFIER);
            }

            ret = ModelUtil.getIdValue(cmodel);
            cmodel = (IClobModel) session.get(cmodel.getClass(), ret, LockMode.UPGRADE);

            Object clobInstance = cmodel.getClob();
            Method getCharOutPut = clobInstance.getClass().getMethod("getCharacterOutputStream", new Class[0]);

            if ((clobInstance != null) && (getCharOutPut != null)) {
                Writer out = (Writer) getCharOutPut.invoke(clobInstance, new Object[0]);
                out.write(input);
                out.close();
            }

            session.flush();
        } catch (IOException e) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.DB_ACCESS_ERROR, e);
        } catch (NoSuchMethodException e) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.DB_HIBERNATE_ERROR, e);
        } finally {
            // SessionFactoryUtils.releaseSession(session,
            // HibernateUtil.getSessionFactory());
            HibernateUtil.closeSession();
        }

        return ret;
    }

    /**
     * this method is used to get the next sequence number via JDBC only
     * suiteable for Oracle
     *
     * @param sequenceName
     * @return
     */
    public Long getSequenceNumber(String sequenceName) throws Exception {
        Long ret = new Long(-1);
        SearchCondition sc = new SearchCondition();
        String sql = "select " + sequenceName + ".nextval as seno from dual";
        sc.setQuerySQL(sql);

        //CachedRowSet rs = (CachedRowSet) execSQL(sc);

//		if (rs != null) {
//			while (rs.next()) {
//				ret = rs.getLong("seno");
//			}
//		}

        return ret;
    }

    class BatchDeleteCallback implements HibernateCallback {
        private String deleteSQL;

        private Object deleteModel;

        public void setDeleteSQL(String dSQL) {
            this.deleteSQL = dSQL;
        }

        public void setDeleteModel(Object obj) {
            this.deleteModel = obj;
        }

        public Object doInHibernate(Session se) throws HibernateException, SQLException {
            se.delete(deleteSQL, deleteModel);
            return deleteModel;
        }
    }
}
