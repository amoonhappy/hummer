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
import org.apache.log4j.Logger;
import org.hummer.core.constants.SearchConstant;
import org.hummer.core.model.ModelFactory;
import org.hummer.core.model.intf.IModel;
import org.hummer.core.util.BlankObjectUtil;
import org.hummer.core.util.HQLUtil;
import org.hummer.core.util.SerializableUtil;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author jeff.zhou
 */
public class SearchCondition {
    public static final int UPPER_CASE = 0;
    public static final int LOWER_CASE = 1;
    private static final Logger LOG = Logger.getLogger(SearchCondition.class);
    private boolean enableQueryCache = true;

    /**
     * Common Search Properties
     */
    private boolean enablePagination = true;

    // get select clause from sql mapping
    private boolean enableSelectSQLMapping = false;

    // get select clause from sql mapping
    private String selectSQLMappingName;

    // Ignore case for all string-valued properties
    private boolean ignoreCase = false;

    // Don't exclude null or zero-valued properties
    private boolean excludeNone = true;

    // Exclude zero-valued properties
    private boolean excludeZeroes = true;

    // Exclude a particular named property
    private List<String> excludeProperties = new LinkedList<String>();

    // Use the "like" operator for all string-valued properties
    private boolean enableLike = false;

    // default MatchMode is end metch
    private int matchModel = SearchConstant.MATCH_MODEL_END;

    // search model
    private IModel searchModel;

    // start row of the records
    private int startRowNo = 0;

    // total row size returned
    private int size = 0;

    // the query parameter that refered by the HQL
    private List<Object> queryParam = new LinkedList<Object>();

    // enable the pk search
    private boolean enablePkSearch = true;

    // enable the pk like search
    private boolean enablePkLike = true;

    // enable the pk like search
    private int pkMatchModel = SearchConstant.MATCH_MODEL_END;

    // for between
    private Map<String, Object> betweenMapping = new HashMap<String, Object>();

    // for '>','>=','<','<='
    private Map<String, String> nonEqualMapping = new HashMap<String, String>();

    // auto gen where clause?
    private boolean enableAutoWhereGen = true;

    /**
     * SQL API
     */

    // the update sql
    private String updateSQL;

    // the update sql Name
    private String updateSQLName;

    // the update sql params
    private List<Object> updateParam = new LinkedList<Object>();

    // the query SQL
    private String querySQL;

    // the query SQL name
    private String querySQLName;

    // the allias name for query sql
    private String[] querySQLTableAllias;

    // wrap the select count(*) sql
    private String queryCountSQL;

    // the object's class that will be searched
    private Class[] querySQLClazzs;

    /**
     * HQL API
     */

    // the Delete Query Language
    private String deleteHQL;

    private String[] deleteTableAlias;

    private IModel deleteModel;

    private boolean enableAutoDeleteWhereGen = true;

    private List<Object> deleteParam = new LinkedList<Object>();

    // the Hibernate Query Language
    private String hibernateQL;

    // for hibernate table allias
    private String[] HQLTableAlias;

    // wrap the select count(*) hql
    private String countHQL;

    // external HQL
    private String externalHQL;

    // external HQL parameter
    private List<Object> externalHQLParam = new LinkedList<Object>();

    // enabler of exernal HQL
    private boolean enableExternalHQL = false;

    // order by
    // private List orderByList = newweb ArrayList();
    private Map<String, String> orderByMap = new HashMap<String, String>();

    // if true link all the parameter by 'or'
    private boolean enableOrLinker = false;

    private HashMap<String, String> caseInsensitiveMap = new HashMap<String, String>();

    public void addCaseInsensitive(String columnName, int caseType) {
        if (caseType == UPPER_CASE) {
            caseInsensitiveMap.remove(columnName);
            caseInsensitiveMap.put(columnName, "UPPER");
        } else if (caseType == LOWER_CASE) {
            caseInsensitiveMap.remove(columnName);
            caseInsensitiveMap.put(columnName, "LOWER");
        }
    }

    public void removeCaseInsensitive(String columnName) {
        if (StringUtils.isNotEmpty(columnName)) {
            caseInsensitiveMap.remove(columnName);
        }
    }

    public boolean isCaseInsensitive(String columnName) {
        if (StringUtils.isEmpty(columnName))
            return false;
        return caseInsensitiveMap.containsKey(columnName);
    }

    public String getCaseInsensitiveFunc(String columnName) {
        if (StringUtils.isEmpty(columnName))
            return "";
        return (String) caseInsensitiveMap.get(columnName);
    }

    public List<Object> getDeleteParam() {
        return deleteParam;
    }

    public void setDeleteParam(List<Object> deleteParam) {
        this.deleteParam = deleteParam;
    }

    public void addDeleteParam(Object param) {
        deleteParam.add(param);
    }

    public boolean isEnableAutoDeleteWhereGen() {
        return enableAutoDeleteWhereGen;
    }

    public void setEnableAutoDeleteWhereGen(boolean enableAutoDeleteWhereGen) {
        this.enableAutoDeleteWhereGen = enableAutoDeleteWhereGen;
    }

    public String getDeleteHQL() {
        return deleteHQL;
    }

    public void setDeleteHQL(String deleteHQL) {
        this.deleteHQL = deleteHQL;
    }

    public String[] getDeleteTableAlias() {
        return deleteTableAlias;
    }

    public void setDeleteTableAlias(String[] deleteTableAlias) {
        this.deleteTableAlias = deleteTableAlias;
    }

    public IModel getDeleteModel() {
        return deleteModel;
    }

    public void setDeleteModel(IModel deleteModel) {
        this.deleteModel = deleteModel;
        this.deleteHQL = "from " + HQLUtil.getModelName(deleteModel) + " a ";
        this.deleteTableAlias = new String[]{"a"};
    }

    public boolean isEnableQueryCache() {
        return enableQueryCache;
    }

    public void setEnableQueryCache(boolean enableQueryCache) {
        this.enableQueryCache = enableQueryCache;
    }

    public boolean isEnablePagination() {
        return enablePagination;
    }

    public void setEnablePagination(boolean enablePagination) {
        this.enablePagination = enablePagination;
    }

    public boolean isEnableOrLinker() {
        return enableOrLinker;
    }

    public void setEnableOrLinker(boolean enableOrLinker) {
        this.enableOrLinker = enableOrLinker;
    }

    public String getUpdateSQL() {
        return updateSQL;
    }

    public void setUpdateSQL(String updateSQL) {
        this.updateSQL = updateSQL;
    }

    public String getUpdateSQLName() {
        return updateSQLName;
    }

    public void setUpdateSQLName(String updateSQLName) {
        this.updateSQLName = updateSQLName;
    }

    public List<Object> getUpdateParam() {
        return updateParam;
    }

    public void setUpdateParam(List<Object> updateParam) {
        this.updateParam = updateParam;
    }

    public void addUpdateParam(Object param) {
        this.updateParam.add(param);
    }

    /**
     * true is order by desc, false is order by asc
     *
     * @param column
     * @param desc
     */
    public void addOrderBy(String column, boolean desc) {
        String order = SearchConstant.ORDER_BY_DESC;

        if (!desc) {
            order = SearchConstant.ORDER_BY_ASC;
        }

        orderByMap.put(column, order);
    }

    public Map<String, String> getOrderByMap() {
        return orderByMap;
    }

    public boolean isEnableSelectSQLMapping() {
        return enableSelectSQLMapping;
    }

    public void setEnableSelectSQLMapping(boolean enableSelectSQLMapping) {
        this.enableSelectSQLMapping = enableSelectSQLMapping;
    }

    public String getSelectSQLMappingName() {
        return selectSQLMappingName;
    }

    public void setSelectSQLMappingName(String selectSQLMappingName) {
        this.selectSQLMappingName = selectSQLMappingName;
    }

    public boolean isEnableExternalHQL() {
        return enableExternalHQL;
    }

    public void setEnableExternalHQL(boolean enableExternalHQL) {
        this.enableExternalHQL = enableExternalHQL;
    }

    public void addExternalHQLParam(Object param) {
        externalHQLParam.add(param);
    }

    public boolean removeExternalHQLParam(Object param) {
        if ((externalHQLParam != null) && (externalHQLParam.size() > 0)) {
            return externalHQLParam.remove(param);
        }

        return true;
    }

    // /**
    // * connect the external param list to original param list
    // * @param original
    // * @return
    // */
    // public List joinExternalHQLParam(List original) {
    // if ((original == null) || (original.size() == 0)) {
    // return this.getExternalHQLParam();
    // } else {
    // return (List) CollectionUtil.union(original, getExternalHQLParam());
    // }
    // }
    public List<Object> getExternalHQLParam() {
        return externalHQLParam;
    }

    public void setExternalHQLParam(List<Object> externalHQLParam) {
        this.externalHQLParam = externalHQLParam;
    }

    public String getExternalHQL() {
        return externalHQL;
    }

    public void setExternalHQL(String externalHQL) {
        this.externalHQL = externalHQL;
    }

    public List<Object> getQueryParam() {
        // if (!isEnableAutoWhereGen() ) {
        // if ((this.searchModel != null) && (queryParam.size() == 0)) {
        // // Field[] fields = searchModel.getClass().getDeclaredFields();
        // Method[] methods =
        // ModelFactory.getInstance().getPGMethod(searchModel.getClass());
        //
        // if ((methods != null) && (methods.length > 0)) {
        // for (int i = 0; i < methods.length; i++) {
        // try {
        // Object param = methods[i].invoke(searchModel,
        // BlankObjectUtil.ObjectArray);
        //
        // if (!(param instanceof Model)) {
        // //if exclude null, zero, ""
        // if (excludeNone) {
        // if (param instanceof Long) {
        // Long aLong = (Long) param;
        //
        // if ((aLong != null) &&
        // (aLong.longValue() > 0)) {
        // queryParam.add(param);
        // }
        // } else {
        // if ((param != null) &&
        // (!"".equals(param.toString()))) {
        // queryParam.add(param);
        // }
        // }
        //
        // //if exclude null, zero
        // } else if (excludeZeroes) {
        // if (param instanceof Long) {
        // Long aLong = (Long) param;
        //
        // if ((aLong != null) &&
        // (aLong.longValue() > 0)) {
        // queryParam.add(param);
        // }
        // } else {
        // if ((param != null)) {
        // queryParam.add(param);
        // }
        // }
        //
        // //if default
        // } else {
        // if ((param != null)) {
        // queryParam.add(param);
        // }
        // }
        // }
        // } catch (Exception e) {
        // return null;
        // }
        // }
        // }
        // }
        // }
        if ((queryParam == null) || (queryParam.size() == 0)) {
            queryParam = new LinkedList<Object>();
        }

        return queryParam;
    }

    public void setQueryParam(List<Object> queryParam) {
        this.queryParam = queryParam;
    }

    /**
     * if you don't want to set the hibernateQL and want to use auto where
     * clause generation set the to false
     *
     * @return
     */
    public boolean isEnableAutoWhereGen() {
        return enableAutoWhereGen;
    }

    public void setEnableAutoWhereGen(boolean enableAutoWhereGen) {
        this.enableAutoWhereGen = enableAutoWhereGen;
    }

    public int getPkMatchModel() {
        return pkMatchModel;
    }

    public void setPkMatchModel(int pkMatchModel) {
        this.pkMatchModel = pkMatchModel;
    }

    public boolean isEnablePkLike() {
        return enablePkLike;
    }

    public void setEnablePkLike(boolean enablePkLike) {
        this.enablePkLike = enablePkLike;
    }

    public boolean isEnablePkSearch() {
        return enablePkSearch;
    }

    public void setEnablePkSearch(boolean enablePkSearch) {
        this.enablePkSearch = enablePkSearch;
        setEnablePkLike(enablePkSearch);
    }

    public String[] getHQLTableAlias() {
        return HQLTableAlias;
    }

    public void setHQLTableAlias(String[] HQLTableAlias) {
        this.HQLTableAlias = HQLTableAlias;
    }

    public void addParam(Object o) {
        this.queryParam.add(o);
    }

    public Class[] getQuerySQLClazzs() {
        return querySQLClazzs;
    }

    public void setQuerySQLClazzs(Class[] querySQLClazzs) {
        this.querySQLClazzs = querySQLClazzs;
    }

    public String getQueryCountSQL() {
        if (querySQL != null) {
            this.queryCountSQL = "select count(*) from (" + querySQL + ")";
        } else if (hibernateQL != null) {
            String counthql = hibernateQL.substring(hibernateQL.toLowerCase().indexOf(" from "));
            this.queryCountSQL = "select count(*)  " + counthql;
        }

        return queryCountSQL;
    }

    public void setQueryCountSQL(String queryCountSQL) {
        this.queryCountSQL = queryCountSQL;
    }

    public String getCountHQL() {
        if (StringUtils.isEmpty(countHQL)) {
            if (hibernateQL != null) {
                String counthql = hibernateQL.substring(hibernateQL.toLowerCase().indexOf(" from "));
                this.countHQL = "select count(*) " + counthql + " ";
            }
        }

        return countHQL;
    }

    public void setCountHQL(String countHQL) {
        this.countHQL = countHQL;
    }

    public String[] getQuerySQLTableAllias() {
        return querySQLTableAllias;
    }

    public void setQuerySQLTableAllias(String[] querySQLTableAllias) {
        this.querySQLTableAllias = querySQLTableAllias;
    }

    public String getHibernateQL() {
        return hibernateQL;
    }

    public void setHibernateQL(String hibernateQL) {
        this.hibernateQL = hibernateQL;
    }

    public String getQuerySQL() {
        return querySQL;
    }

    public void setQuerySQL(String querySQL) {
        this.querySQL = querySQL;
    }

    public int getStartRowNo() {
        return startRowNo;
    }

    public void setStartRowNo(int startRowNo) {
        this.startRowNo = startRowNo;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public boolean isExcludeNone() {
        return excludeNone;
    }

    public void setExcludeNone(boolean excludeNone) {
        this.excludeNone = excludeNone;
    }

    public boolean isExcludeZeroes() {
        return excludeZeroes;
    }

    public void setExcludeZeroes(boolean excludeZeroes) {
        this.excludeZeroes = excludeZeroes;
    }

    public List<String> getExcludeProperties() {
        if ((this.searchModel != null)) {
            // Field[] fields = searchModel.getClass().getDeclaredFields();
            Method[] methods = ModelFactory.getInstance().getPGMethod(searchModel.getClass());
            String matchedFieldName = "";

            if ((methods != null) && (methods.length > 0)) {
                for (int i = 0; i < methods.length; i++) {
                    try {
                        String methodName = methods[i].getName();

                        matchedFieldName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);

                        Object param = methods[i].invoke(searchModel, BlankObjectUtil.ObjectArray);

                        // if exclude null, zero, ""
                        if (excludeNone) {
                            if (param instanceof Long) {
                                Long aLong = (Long) param;

                                if ((aLong == null) || (aLong.longValue() <= 0)) {
                                    excludeProperties.add(matchedFieldName);
                                }
                            } else if (param instanceof Double) {
                                Double aDouble = (Double) param;

                                if ((aDouble == null) || (aDouble.doubleValue() <= 0.0)) {
                                    excludeProperties.add(matchedFieldName);
                                }
                            } else if (param instanceof Integer) {
                                Integer integer = (Integer) param;

                                if ((integer == null) || (integer.intValue() <= 0)) {
                                    excludeProperties.add(matchedFieldName);
                                }
                            } else {
                                if ((param == null) || ("".equals(param.toString()))) {
                                    excludeProperties.add(matchedFieldName);
                                }
                            }

                            // if exclude null, zero
                        } else if (excludeZeroes) {
                            if (param instanceof Long) {
                                Long aLong = (Long) param;

                                if ((aLong == null) && (aLong.longValue() <= 0)) {
                                    excludeProperties.add(matchedFieldName);
                                }
                            } else if (param instanceof Double) {
                                Double aDouble = (Double) param;

                                if ((aDouble == null) || (aDouble.doubleValue() <= 0.0)) {
                                    excludeProperties.add(matchedFieldName);
                                }
                            } else if (param instanceof Integer) {
                                Integer integer = (Integer) param;

                                if ((integer == null) || (integer.intValue() <= 0)) {
                                    excludeProperties.add(matchedFieldName);
                                }
                            } else {
                                if ((param == null)) {
                                    excludeProperties.add(matchedFieldName);
                                }
                            }

                            // if default
                        } else {
                            if ((param != null)) {
                                excludeProperties.add(matchedFieldName);
                            }
                        }
                    } catch (Exception e) {
                        return new ArrayList<String>();
                    }
                }
            }
        }

        return excludeProperties;
    }

    public void setExcludeProperties(List<String> excludeProperties) {
        this.excludeProperties = excludeProperties;
    }

    public boolean isEnableLike() {
        return enableLike;
    }

    public void setEnableLike(boolean enableLike) {
        this.enableLike = enableLike;
    }

    public int getMatchModel() {
        return matchModel;
    }

    public void setMatchModel(int matchModel) {
        this.matchModel = matchModel;
    }

    public IModel getSearchModel() {
        return searchModel;
    }

    public void setSearchModel(IModel searchModel) {
        this.searchModel = searchModel;
    }

    /**
     * set the propertys which you don't want to be the search condition
     *
     * @param prop
     */
    public void addExcludeProperty(String prop) {
        excludeProperties.add(prop);
    }

    /**
     * reset the propertys which you don't want to be the search condition
     *
     * @param prop
     */
    public void removeExcludeProperty(String prop) {
        excludeProperties.remove(prop);
    }

    public String getQuerySQLName() {
        return querySQLName;
    }

    public void setQuerySQLName(String querySQLName) {
        this.querySQLName = querySQLName;
    }

    public String toString() {
        return "SearchCondition{" + "ignoreCase=" + ignoreCase + ", excludeNone=" + excludeNone + ", excludeZeroes="
                + excludeZeroes + ", excludeProperties=" + excludeProperties + ", enableLike=" + enableLike
                + ", matchModel=" + matchModel + ", searchModel=" + searchModel + ", startRowNo=" + startRowNo
                + ", size=" + size + ", querySQL='" + querySQL + "'" + ", querySQLName='" + querySQLName + "'"
                + ", querySQLTableAllias="
                + ((querySQLTableAllias == null) ? null : Arrays.asList(querySQLTableAllias)) + ", queryCountSQL='"
                + queryCountSQL + "'" + ", querySQLClazzs="
                + ((querySQLClazzs == null) ? null : Arrays.asList(querySQLClazzs)) + ", hibernateQL='" + hibernateQL
                + "'" + ", queryParam=" + queryParam + "}";
    }

    public void addNonEqual(String column, int type) {
        switch (type) {
            case SearchConstant.LARGE:
                nonEqualMapping.put(column, " > ");

                break;

            case SearchConstant.LARGE_EQUAL:
                nonEqualMapping.put(column, " >= ");

                break;

            case SearchConstant.LESS:
                nonEqualMapping.put(column, " < ");

                break;

            case SearchConstant.LESS_EQUAL:
                nonEqualMapping.put(column, " <= ");

                break;
        }

        // this.addParam(value);
    }

    public void addNonEqual(String column, int type, Object value) {
        switch (type) {
            case SearchConstant.LARGE:
                nonEqualMapping.put(column, " > ");

                break;

            case SearchConstant.LARGE_EQUAL:
                nonEqualMapping.put(column, " >= ");

                break;

            case SearchConstant.LESS:
                nonEqualMapping.put(column, " < ");

                break;

            case SearchConstant.LESS_EQUAL:
                nonEqualMapping.put(column, " <= ");

                break;
        }

        this.addParam(value);
    }

    public void addBetween(String column, Object value1, Object value2) {
        Object[] values = new Object[]{value1, value2};
        betweenMapping.put(column, values);
        this.addParam(value1);
        this.addParam(value2);
    }

    public Object[] getBetweenValue(String column) {
        return (Object[]) betweenMapping.get(column);
    }

    public String getNonEqualString(String column) {
        if (StringUtils.isEmpty(column) || (nonEqualMapping.get(column) == null)) {
            return null;
        }

        return (String) nonEqualMapping.get(column);
    }

    public Map getBetweenMapping() {
        return betweenMapping;
    }

    public void setBetweenMapping(Map<String, Object> betweenMapping) {
        this.betweenMapping = betweenMapping;
    }

    public Map getNonEqualMapping() {
        return nonEqualMapping;
    }

    public void setNonEqualMapping(HashMap<String, String> nonEqualMapping) {
        this.nonEqualMapping = nonEqualMapping;
    }

    public boolean isNonEqualColumn(String column) {
        boolean ret = false;

        if (nonEqualMapping.get(column) != null) {
            ret = true;
        }

        return ret;
    }

    public boolean isBetweenColumn(String column) {
        boolean ret = false;

        if (betweenMapping.get(column) != null) {
            ret = true;
        }

        return ret;
    }

    public Object Clone() {
        try {
            // // deep clone the search model
            // Model model = (Model) this.searchModel.getClass().newInstance();
            //
            // // ConvertUtils.register(newweb BigDecimalConvert(),
            // BigDecimal.class);
            // // BeanUtils.copyProperties(model, this.searchModel);
            // ConvertUtil.getInstance().register(newweb BigDecimalConverter());
            // BeanUtil.getInstance().copyProperties(model, this.searchModel);
            //
            // // deep clone the param list
            // List newExcludeProperties = (List) CollectionUtils.union(
            // excludeProperties, newweb LinkedList());
            // List newDeleteParam = (List) CollectionUtils.union(deleteParam,
            // newweb LinkedList());
            // List newUpdateParam = (List) CollectionUtils.union(updateParam,
            // newweb LinkedList());
            //
            // List newQueryParam = (List) CollectionUtils.union(queryParam,
            // newweb LinkedList());
            // List newExtenalHQLParam = (List) CollectionUtils.union(
            // externalHQLParam, newweb LinkedList());
            //
            // SearchCondition searchCondition = (SearchCondition)
            // super.clone();
            // searchCondition.setSearchModel(model);
            // searchCondition.setQueryParam(newQueryParam);
            // searchCondition.setExternalHQLParam(newExtenalHQLParam);
            // searchCondition.setExcludeProperties(newExcludeProperties);
            // searchCondition.setDeleteParam(newDeleteParam);
            // searchCondition.setUpdateParam(newUpdateParam);
            SearchCondition searchCondition = (SearchCondition) SerializableUtil.cloneSerializable(this);
            return searchCondition;
        } catch (Exception e) {
            LOG.error("clone search condition error", e);
            return this;
        }
    }
}
