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
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a> Date: 2005-12-8
 * @version 1.0
 */
package org.hummer.core.persistence.impl;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.hummer.core.constants.ExceptionConstant;
import org.hummer.core.constants.SearchConstant;
import org.hummer.core.exception.BusinessException;
import org.hummer.core.model.intf.IModel;
import org.hummer.core.pagination.Pager;
import org.hummer.core.persistence.intf.IPagerHandler;
import org.hummer.core.util.*;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
//import org.springframework.orm.hibernate3.SessionFactoryUtils;

/**
 * @author jeff.zhou
 */
public class HibernatePagerHandler implements IPagerHandler {
    public static final String COUNT_POSTFIX = "Count";

    public static final String COUNT_ALIAS = "countNum";

    protected transient final Logger log = Log4jUtils.getLogger(getClass());
    private IPagerHandler proxy = null;

    public IPagerHandler getProxy() {
        return proxy;
    }

    public void setProxy(IPagerHandler proxy) {
        this.proxy = proxy;
    }

    public Pager getPager(int type, Object[] o) throws HibernateException, BusinessException, SQLException {
        Pager pager = new Pager();

        switch (type) {
            case SearchConstant.QUERY_TYPE_CRITERIA:
                pager = getCriteriaPager(o);

                break;

            case SearchConstant.QUERY_TYPE_HQL:
                pager = getHQLPager(o);

                break;

            case SearchConstant.QUERY_TYPE_SQLNAME:
                pager = getSQLNamePager(o);

                break;

            default:
                pager = getCriteriaPager(o);
        }

        return pager;
    }

    /**
     * This method is based on the sql mapping
     *
     * @param o
     * @return
     * @throws HibernateException
     * @throws BusinessException
     */
    private Pager getSQLNamePager(Object[] o) throws HibernateException, BusinessException, SQLException {
        if ((o == null) || (o.length != 1) || !(o[0] instanceof SearchCondition)) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_SEARCH_CONDITION_ERROR);
        }

        Pager ret = new Pager();
        List result = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Session session = null;
        long count = 0;

        try {
            session = HibernateUtil.getSession();
            // session = getSession(true);

            SearchCondition searchCondition = (SearchCondition) o[0];

            con = HibernateUtil.getConnection();

            String sqlName = searchCondition.getQuerySQLName();
            List<Object> al = searchCondition.getQueryParam();

            if (searchCondition.isEnablePagination()) {
                String sqlCountName = sqlName + COUNT_POSTFIX;
                String countSQL = session.getNamedQuery(sqlCountName).getQueryString();
                countSQL = JDBCUtil.trimSQL(countSQL);
                log.info("count sql={}", countSQL);
                pstmt = JDBCUtil.proccessParam(con.prepareStatement(countSQL), al);
                rs = pstmt.executeQuery();

                if (rs != null) {
                    if (rs.next()) {
                        count = rs.getLong(COUNT_ALIAS);
                    }
                }
            }

            if ((count > 0) || !searchCondition.isEnablePagination()) {
                Query q = session.getNamedQuery(sqlName);

                for (int i = 0; i < al.size(); i++) {
                    // qcount.setParameter(i, al.get(i));
                    q.setParameter(i, al.get(i));
                }

                if (searchCondition.isEnablePagination()) {
                    if (searchCondition.getStartRowNo() > 0) {
                        q = q.setFirstResult(searchCondition.getStartRowNo());
                    }

                    if (searchCondition.getSize() > 0) {
                        q = q.setMaxResults(searchCondition.getSize());
                    }
                }

                result = q.list();
                // session.flush();

                // HibernateUtil.flushSession();
            }

            if (!searchCondition.isEnablePagination()) {
                count = result.size();
            }

            ret.setTotalRowCount(count);
            ret.setStartRowNo(searchCondition.getStartRowNo());
            ret.setResult(result);
        } finally {
            if (rs != null) {
                rs.close();
            }

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

        return ret;
    }

    /**
     * Get the pager instance for Query API
     *
     * @param o
     * @return
     * @throws BusinessException
     */
    private Pager getHQLPager(Object[] o) throws HibernateException, BusinessException {
        if ((o == null) || (o.length != 1) || !(o[0] instanceof SearchCondition)) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_SEARCH_CONDITION_ERROR);
        }

        Pager ret = new Pager();
        Session session = null;

        try {
            // session = getSession(true);
            session = HibernateUtil.getSession();

            SearchCondition searchCondition = (SearchCondition) o[0];

            // add by jeff
            // ￈￧ﾹ￻ￔￊ￐￭ﾽ￸￐￐PKﾲ￩￑ﾯ
            if (searchCondition.isEnableAutoWhereGen()) {
                IModel searchModel = searchCondition.getSearchModel();
                searchCondition = HQLUtil.genWhereClause(searchModel, searchCondition);
            }

            // add by jeff for external HQL
            if (searchCondition.isEnableExternalHQL()) {
                searchCondition = HQLUtil.extend(searchCondition);
            }

            // get hql add by jeff for select sql mapping
            String hsql = "";
            String countSQL = "";
            int count = 0;

            if (searchCondition.isEnableSelectSQLMapping()) {
                String selectClause = JDBCUtil
                        .trimSQL(session.getNamedQuery(searchCondition.getSelectSQLMappingName()).getQueryString());
                hsql = selectClause + "" + searchCondition.getHibernateQL();

                searchCondition.setHibernateQL(hsql);
                countSQL = searchCondition.getCountHQL();
            } else {
                // get the count hql before order by
                countSQL = searchCondition.getCountHQL();

                // gen order by clause
                searchCondition = HQLUtil.genOrderByClause(searchCondition);

                hsql = searchCondition.getHibernateQL();
            }

            List<Object> al = searchCondition.getQueryParam();

            Query q = session.createQuery(hsql);
            Query qcount = null;

            if (searchCondition.isEnablePagination()) {
                qcount = session.createQuery(countSQL);
            }

            // only if the param is empty
            // cache the query result
            if ((al == null) || (al.size() == 0)) {
                q.setCacheable(true);
                searchCondition.setEnableQueryCache(true);
            }

            // should not cache the count number of query
            // qcount.setCacheable(searchCondition.isEnableQueryCache());
            for (int i = 0; i < al.size(); i++) {
                if (searchCondition.isEnablePagination()) {
                    qcount.setParameter(i, al.get(i));
                }

                q.setParameter(i, al.get(i));
            }

            if (searchCondition.isEnablePagination()) {
                Object it = qcount.uniqueResult();

                if (it != null) {
                    count = Integer.parseInt(it.toString());
                }

                // if (it.hasNext()) {
                // Object obj = it.next();
                // if (obj != null) {
                // count = Integer.parseInt(obj.toString()); //ﾻ￱ﾵￃﾼￇￂﾼￗￜￊ�
                // }
                // }
            }

            List result = new ArrayList();

            if (searchCondition.isEnablePagination()) {
                if (searchCondition.getStartRowNo() > 0) {
                    q = q.setFirstResult(searchCondition.getStartRowNo());
                }

                if (searchCondition.getSize() > 0) {
                    q = q.setMaxResults(searchCondition.getSize());
                }
            }

            if ((count > 0) || !searchCondition.isEnablePagination()) {
                result = q.list();

                // session.flush();
            }

            if (!searchCondition.isEnablePagination()) {
                count = result.size();
            }

            ret.setTotalRowCount(count);
            ret.setStartRowNo(searchCondition.getStartRowNo());
            ret.setResult(result);
        } finally {
            // SessionFactoryUtils.releaseSession(session,
            // HibernateUtil.getSessionFactory());
            HibernateUtil.closeSession();
            // HibernateUtil.closeSession();
        }

        return ret;
    }

    /**
     * Get the pager instance for Criteria API
     *
     * @param o
     * @return
     * @throws BusinessException
     */
    public Pager getCriteriaPager(Object[] o) throws HibernateException, BusinessException {
        if ((o == null) || (o.length != 2) || !(o[0] instanceof SearchCondition) || !(o[1] instanceof Example)) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_SEARCH_CONDITION_ERROR);
        }

        Pager ret = new Pager();
        Session session = null;

        try {
            // session = getSession(true);
            session = HibernateUtil.getSession();

            SearchCondition searchCondition = (SearchCondition) o[0];
            Example example = (Example) o[1];
            long count = 0;
            Collection result = new ArrayList();

            Criteria criteria = session.createCriteria(searchCondition.getSearchModel().getClass());

            if (searchCondition.isEnablePagination()) {
                if (searchCondition.getStartRowNo() > 0) {
                    criteria = criteria.setFirstResult(searchCondition.getStartRowNo());
                }

                if (searchCondition.getSize() > 0) {
                    criteria = criteria.setMaxResults(searchCondition.getSize());
                }
            }

            criteria = criteria.add(example);
            // FIXME:http://www.java2s.com/Code/Java/Hibernate/CriteriaRowCountCriteria.htm
            //
            // I solved the problem using Projections.
            // My code is like that:
            // Criteria criteria =
            // session.createCriteria(getReferenceClass());
            // criteria.setProjection(Projections.rowCount());
            // return ((Integer)criteria.list().get(0)).intValue();

            if (searchCondition.isEnablePagination()) {
                Criteria criteriaCount = session.createCriteria(searchCondition.getSearchModel().getClass());
                criteriaCount.setProjection(Projections.rowCount());

                count = ((Integer) criteriaCount.list().get(0)).intValue();
            }

            if ((count > 0) || !searchCondition.isEnablePagination()) {
                result = criteria.list();
                // session.flush();
            }

            if (searchCondition.isEnablePagination()) {
                count = result.size();
            }

            ret.setTotalRowCount(count);
            ret.setStartRowNo(searchCondition.getStartRowNo());
            ret.setResult(result);
        } finally {
            // SessionFactoryUtils.releaseSession(session,
            // HibernateUtil.getSessionFactory());
            HibernateUtil.closeSession();
        }

        return ret;
    }
}
