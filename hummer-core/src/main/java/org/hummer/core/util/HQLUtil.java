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
package org.hummer.core.util;

import org.apache.commons.lang3.StringUtils;
import org.hummer.core.constants.SearchConstant;
import org.hummer.core.model.ModelFactory;
import org.hummer.core.model.intf.ICompositePKModel;
import org.hummer.core.model.intf.IModel;
import org.hummer.core.model.intf.ISingleLongPKModel;
import org.hummer.core.model.intf.ISingleStringPKModel;
import org.hummer.core.persistence.impl.SearchCondition;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author jeff.zhou
 */
public class HQLUtil {
    public static final String HQL_ORACLE_WILDCARD = "%";

    public static final String SINGLEPK_MODEL_GETMETHOD = "getId";

    public static final String COMPPK_MODEL_GETMETHOD = "getComp_id";

    public static final String TEMP_WHERE = " where 1=1 ";

    public static final String HQL_EQUAL = " = ";

    public static final String HQL_LIKE = " like ";

    public static final String HQL_BETWEEN = " between ? and ? ";

    public static final String HQL_PARAM_ALIAS = " ? ";

    public static final String HQL_LINK_OR = " or ";

    public static final String HQL_LINK_AND = " and ";

    public static final String HQL_LINK_JOIN = ".";

    public static final String HQL_ORDER_BY = " order by ";

    public static final String HQL_LINK_EMPTY = " ";

    public static final String HQL_EMPTY_EXPRESSION = " 1=1 ";

    public static final String HQL_SINGLEPK_NAME = "id";

    public static final String HQL_UPPER = "upper";

    public static final String HQL_LEFT_BRACKET = "(";

    public static final String HQL_RIGHT_BRACKET = ")";

    public static final String HQL_EXPRESSION_LINK = " , ";

    /**
     * Generate the Where Clause String from the inputed model properties This
     * method only suitable for simple object.
     *
     * @param searchModel
     * @param searchCondition
     * @return
     */
    public static SearchCondition genWhereClause(IModel searchModel, SearchCondition searchCondition) {
        if (searchModel instanceof ICompositePKModel) {
            searchCondition = genCompPKWhereClause((ICompositePKModel) searchModel, searchCondition);
        } else if (searchModel instanceof ISingleLongPKModel || searchModel instanceof ISingleStringPKModel) {
            searchCondition = genSinglePKWhereClause(searchModel, searchCondition);
        }

        return searchCondition;
    }

    private static SearchCondition genCompPKWhereClause(ICompositePKModel compositePKModel,
                                                        SearchCondition searchCondition) {
        if (compositePKModel == null) {
            return searchCondition;
        }

        if ((searchCondition == null) || (searchCondition.getHibernateQL() == null)) {
            return searchCondition;
        }

        StringBuffer whereClause = new StringBuffer(TEMP_WHERE);

        // todo Maybe should add the aliasName to HashMap bind to the Class
        String modelName = ModelUtil.getModelName(compositePKModel);
        String aliasName = ((searchCondition.getHQLTableAlias() == null) || (searchCondition.getHQLTableAlias().length == 0)) ? modelName
                : searchCondition.getHQLTableAlias()[0];
        List<Object> includeValues = new ArrayList<Object>();

        if (searchCondition.isEnableAutoWhereGen()) {
            // ���PKֵ��Ч��������pklike��ѯ
            if (genCompPkHQL(whereClause, searchCondition, aliasName, includeValues, compositePKModel)) {
                // HashMap includeProperties = newweb HashMap();
                Method[] methods = ModelFactory.getInstance().getPGMethod(compositePKModel.getClass());
                String matchedFieldName = "";

                if ((methods != null) && (methods.length > 0)) {
                    for (int i = 0; i < methods.length; i++) {
                        try {
                            String methodName = methods[i].getName();

                            if (!methodName.equals(COMPPK_MODEL_GETMETHOD)) {
                                matchedFieldName = Character.toLowerCase(methodName.charAt(3))
                                        + methodName.substring(4);

                                Object param = methods[i].invoke(compositePKModel, new Object[0]);

                                if (param instanceof ICompositePKModel) {
                                    searchCondition = genCompositePKModelParamWhereClause(searchCondition,
                                            (ICompositePKModel) param, whereClause, aliasName, matchedFieldName,
                                            includeValues);
                                } else if ((!(param instanceof Collection)) && (param != null)
                                        && isValidSinglePKModel(param)) {
                                    genParamWhereClause(searchCondition, param, whereClause, aliasName,
                                            matchedFieldName, includeValues);
                                }
                            }
                        } catch (Exception e) {
                            return searchCondition;
                        }
                    }
                }
            }

            searchCondition.setQueryParam(includeValues);

            String temp = searchCondition.getHibernateQL() + whereClause.toString();
            int count = StringUtils.countMatches(temp, TEMP_WHERE);

            if (count > 1) {
                temp = searchCondition.getHibernateQL()
                        + StringUtils.replace(whereClause.toString(), TEMP_WHERE.trim(), HQL_LINK_EMPTY);
            }

            searchCondition.setHibernateQL(temp);
        }

        return searchCondition;
    }

    public static boolean isValidSinglePKModel(Object param) {
        if (param instanceof ISingleStringPKModel) {
            ISingleStringPKModel singleStringPKModel = (ISingleStringPKModel) param;
            String pk = singleStringPKModel.getId();

            if (StringUtils.isEmpty(pk)) {
                return false;
            }
        }

        if (param instanceof ISingleLongPKModel) {
            ISingleLongPKModel singleLongPKModel = (ISingleLongPKModel) param;
            Long pk = singleLongPKModel.getId();

            if ((pk == null) || (pk.longValue() == 0)) {
                return false;
            }
        }

        return true;
    }

    private static SearchCondition genCompositePKModelParamWhereClause(SearchCondition searchCondition,
                                                                       ICompositePKModel param, StringBuffer whereClause, String aliasName, String modelName,
                                                                       List<Object> includeValues) {
        if (param == null) {
            return searchCondition;
        }

        if ((searchCondition == null) || (searchCondition.getHibernateQL() == null)) {
            return searchCondition;
        }

        aliasName += (HQL_LINK_JOIN + modelName);

        // todo Maybe should add the aliasName to HashMap bind to the Class
        // String modelName = getModelName(param);
        if (searchCondition.isEnableAutoWhereGen()) {
            // ���PKֵ��Ч��������pklike��ѯ
            if (genCompPkHQL(whereClause, searchCondition, aliasName, includeValues, param)) {
                // HashMap includeProperties = newweb HashMap();
                Method[] methods = ModelFactory.getInstance().getPGMethod(param.getClass());
                String matchedFieldName = "";

                if ((methods != null) && (methods.length > 0)) {
                    for (int i = 0; i < methods.length; i++) {
                        try {
                            String methodName = methods[i].getName();

                            if (!methodName.equals(COMPPK_MODEL_GETMETHOD)) {
                                matchedFieldName = Character.toLowerCase(methodName.charAt(3))
                                        + methodName.substring(4);

                                Object sub_param = methods[i].invoke(param, BlankObjectUtil.ObjectArray);

                                if (!(sub_param instanceof Collection)) {
                                    genParamWhereClause(searchCondition, sub_param, whereClause, aliasName,
                                            matchedFieldName, includeValues);
                                }
                            }
                        } catch (Exception e) {
                            return searchCondition;
                        }
                    }
                }
            }

            // searchCondition.setQueryParam(includeValues);
            // searchCondition.setHibernateQL(searchCondition.getHibernateQL() +
            // whereClause.toString());
        }

        return searchCondition;
    }

    private static SearchCondition genSinglePKWhereClause(IModel singlePKModel, SearchCondition searchCondition) {
        if (singlePKModel == null) {
            return searchCondition;
        }

        if ((searchCondition == null) || (searchCondition.getHibernateQL() == null)) {
            return searchCondition;
        }

        Object pk = null;

        if (singlePKModel instanceof ISingleStringPKModel) {
            ISingleStringPKModel singleStringPKModel = (ISingleStringPKModel) singlePKModel;
            pk = singleStringPKModel.getId();
        } else if (singlePKModel instanceof ISingleLongPKModel) {
            ISingleLongPKModel singleLongPKModel = (ISingleLongPKModel) singlePKModel;
            pk = singleLongPKModel.getId();
        }

        StringBuffer whereClause = new StringBuffer(" where ");

        // todo Maybe should add the aliasName to HashMap bind to the Class
        String modelName = getModelName(singlePKModel);
        String aliasName = ((searchCondition.getHQLTableAlias() == null) || (searchCondition.getHQLTableAlias().length == 0)) ? modelName
                : searchCondition.getHQLTableAlias()[0];
        List<Object> includeValues = new ArrayList<Object>();
        List excludeProperties = searchCondition.getExcludeProperties();

        if (searchCondition.isEnableAutoWhereGen()) {
            // ���PKֵ��Ч��������pklike��ѯ
            if (genSinglePkHQL(whereClause, searchCondition, aliasName, includeValues, pk)) {
                // HashMap includeProperties = newweb HashMap();
                Method[] methods = ModelFactory.getInstance().getPGMethod(singlePKModel.getClass());
                Method[] setMethods = ModelFactory.getInstance().getPSMethod(singlePKModel.getClass());
                String matchedFieldName = "";

                if ((methods != null) && (methods.length > 0)) {
                    for (int i = 0; i < methods.length; i++) {
                        try {
                            String methodName = methods[i].getName();

                            if (!methodName.equals(SINGLEPK_MODEL_GETMETHOD) && !methodName.equals("getOriginalState")) {
                                matchedFieldName = Character.toLowerCase(methodName.charAt(3))
                                        + methodName.substring(4);

                                // apply excludeProperties
                                if (excludeProperties.contains(matchedFieldName)) {
                                    if (setMethods[i] != null) {
                                        setMethods[i].invoke(singlePKModel, new Object[]{null});
                                    }
                                }

                                Object param = methods[i].invoke(singlePKModel, BlankObjectUtil.ObjectArray);

                                if (param instanceof ICompositePKModel) {
                                    searchCondition = genCompositePKModelParamWhereClause(searchCondition,
                                            (ICompositePKModel) param, whereClause, aliasName, matchedFieldName,
                                            includeValues);
                                } else if ((!(param instanceof Collection)) && (param != null)
                                        && isValidSinglePKModel(param) && !(param instanceof Boolean)) {
                                    genParamWhereClause(searchCondition, param, whereClause, aliasName,
                                            matchedFieldName, includeValues);
                                }
                            }
                        } catch (Exception e) {
                            return searchCondition;
                        }
                    }
                }
            }

            searchCondition.setQueryParam(includeValues);
            searchCondition.setHibernateQL(searchCondition.getHibernateQL() + whereClause.toString());
        }

        return searchCondition;
    }

    private static void genParamWhereClause(SearchCondition searchCondition, Object param, StringBuffer whereClause,
                                            String aliasName, String matchedFieldName, List<Object> includeValues) {
        // if exclude null, zero, ""
        if (searchCondition.isExcludeNone()) {
            if (param instanceof Long) {
                Long aLong = (Long) param;

                if ((aLong != null) && (aLong.longValue() > 0)) {
                    genParamSQL(whereClause, aliasName, matchedFieldName, searchCondition, param, includeValues);
                }
            } else if (param instanceof Double) {
                Double aDouble = (Double) param;

                if ((aDouble != null) && (aDouble.doubleValue() > 0.0)) {
                    genParamSQL(whereClause, aliasName, matchedFieldName, searchCondition, param, includeValues);
                }
            } else if (param instanceof Integer) {
                Integer integer = (Integer) param;

                if ((integer != null) && (integer.intValue() > 0)) {
                    genParamSQL(whereClause, aliasName, matchedFieldName, searchCondition, param, includeValues);
                }
            } else if (searchCondition.isBetweenColumn(matchedFieldName)) {
                Object[] values = searchCondition.getBetweenValue(matchedFieldName);

                if ((values != null) && (values.length == 2)) {
                    genParamSQL(whereClause, aliasName, matchedFieldName, searchCondition, param, includeValues);
                }
            } else {
                if ((param != null) && (!"".equals(param.toString()))) {
                    genParamSQL(whereClause, aliasName, matchedFieldName, searchCondition, param, includeValues);
                }
            }

            // if exclude null, zero
        } else if (searchCondition.isExcludeZeroes()) {
            if (param instanceof Long) {
                Long aLong = (Long) param;

                if ((aLong != null) && (aLong.longValue() > 0)) {
                    genParamSQL(whereClause, aliasName, matchedFieldName, searchCondition, param, includeValues);
                }
            } else if (param instanceof Double) {
                Double aDouble = (Double) param;

                if ((aDouble != null) && (aDouble.doubleValue() > 0.0)) {
                    genParamSQL(whereClause, aliasName, matchedFieldName, searchCondition, param, includeValues);
                }
            } else if (param instanceof Integer) {
                Integer integer = (Integer) param;

                if ((integer != null) && (integer.intValue() > 0)) {
                    genParamSQL(whereClause, aliasName, matchedFieldName, searchCondition, param, includeValues);
                }
            } else if (searchCondition.isBetweenColumn(matchedFieldName)) {
                Object[] values = searchCondition.getBetweenValue(matchedFieldName);

                if ((values != null) && (values.length == 2)) {
                    genParamSQL(whereClause, aliasName, matchedFieldName, searchCondition, param, includeValues);
                }
            } else {
                if ((param != null)) {
                    genParamSQL(whereClause, aliasName, matchedFieldName, searchCondition, param, includeValues);
                }
            }

            // if default
        } else if (searchCondition.isBetweenColumn(matchedFieldName)) {
            Object[] values = searchCondition.getBetweenValue(matchedFieldName);

            if ((values != null) && (values.length == 2)) {
                genParamSQL(whereClause, aliasName, matchedFieldName, searchCondition, param, includeValues);
            }
        } else {
            if ((param != null)) {
                genParamSQL(whereClause, aliasName, matchedFieldName, searchCondition, param, includeValues);
            }
        }
    }

    private static void genCompPKParamWhereClause(SearchCondition searchCondition, Object param,
                                                  StringBuffer whereClause, String aliasName, String matchedFieldName, List<Object> includeValues,
                                                  boolean isFirst) {
        // if exclude null, zero, ""
        if (searchCondition.isExcludeNone()) {
            if (param instanceof Long) {
                Long aLong = (Long) param;

                if ((aLong != null) && (aLong.longValue() > 0)) {
                    genCompPKParamSQL(whereClause, aliasName, matchedFieldName, searchCondition, param, includeValues,
                            isFirst);
                }
            } else if (param instanceof Double) {
                Double aDouble = (Double) param;

                if ((aDouble != null) && (aDouble.doubleValue() > 0.0)) {
                    genCompPKParamSQL(whereClause, aliasName, matchedFieldName, searchCondition, param, includeValues,
                            isFirst);
                }
            } else if (param instanceof Integer) {
                Integer integer = (Integer) param;

                if ((integer != null) && (integer.intValue() > 0)) {
                    genCompPKParamSQL(whereClause, aliasName, matchedFieldName, searchCondition, param, includeValues,
                            isFirst);
                }
            } else {
                if ((param != null) && (!"".equals(param.toString()))) {
                    genCompPKParamSQL(whereClause, aliasName, matchedFieldName, searchCondition, param, includeValues,
                            isFirst);
                }
            }

            // if exclude null, zero
        } else if (searchCondition.isExcludeZeroes()) {
            if (param instanceof Long) {
                Long aLong = (Long) param;

                if ((aLong != null) && (aLong.longValue() > 0)) {
                    genCompPKParamSQL(whereClause, aliasName, matchedFieldName, searchCondition, param, includeValues,
                            isFirst);
                }
            } else if (param instanceof Double) {
                Double aDouble = (Double) param;

                if ((aDouble != null) && (aDouble.doubleValue() > 0.0)) {
                    genCompPKParamSQL(whereClause, aliasName, matchedFieldName, searchCondition, param, includeValues,
                            isFirst);
                }
            } else if (param instanceof Integer) {
                Integer integer = (Integer) param;

                if ((integer != null) && (integer.intValue() > 0)) {
                    genCompPKParamSQL(whereClause, aliasName, matchedFieldName, searchCondition, param, includeValues,
                            isFirst);
                }
            } else {
                if ((param != null)) {
                    genCompPKParamSQL(whereClause, aliasName, matchedFieldName, searchCondition, param, includeValues,
                            isFirst);
                }
            }

            // if default
        } else {
            if ((param != null)) {
                genCompPKParamSQL(whereClause, aliasName, matchedFieldName, searchCondition, param, includeValues,
                        isFirst);
            }
        }
    }

    /**
     * get comp pk search sql
     *
     * @param whereClause
     * @param searchCondition
     * @param aliasName
     * @param includeValues
     * @param CompositePKModel
     * @return
     */
    private static boolean genCompPkHQL(StringBuffer whereClause, SearchCondition searchCondition, String aliasName,
                                        List<Object> includeValues, ICompositePKModel CompositePKModel) {
        boolean ret = true;
        Object pk = CompositePKModel.getComp_id();

        if (pk != null) {
            Method[] methods = ModelFactory.getInstance().getPGMethod(pk.getClass());
            String matchedFieldName = "";

            if ((methods != null) && (methods.length > 0)) {
                if (searchCondition.isEnablePkLike()) {
                    searchCondition.setEnableLike(true);
                } else {
                    searchCondition.setEnableLike(false);
                    ret = false;
                }

                boolean isFirst = false;

                for (int i = 0; i < methods.length; i++) {
                    try {
                        String methodName = methods[i].getName();

                        matchedFieldName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);

                        Object param = methods[i].invoke(pk, BlankObjectUtil.ObjectArray);
                        boolean orgvalue = searchCondition.isEnableLike();

                        if (param instanceof ICompositePKModel) {
                            searchCondition.setHQLTableAlias(new String[]{"a.comp_id." + matchedFieldName});
                            searchCondition = genCompPKWhereClause((ICompositePKModel) param, searchCondition);

                            for (Iterator<Object> iterator = searchCondition.getQueryParam().iterator(); iterator
                                    .hasNext(); ) {
                                Object o = iterator.next();
                                includeValues.add(o);
                            }
                        } else if (!(param instanceof Collection || searchCondition.isBetweenColumn(matchedFieldName))
                                || searchCondition.isNonEqualColumn(matchedFieldName)) {
                            genCompPKParamWhereClause(searchCondition, param, whereClause, aliasName, matchedFieldName,
                                    includeValues, isFirst);
                        }

                        isFirst = false;
                        searchCondition.setEnableLike(orgvalue);
                    } catch (Exception e) {
                        return ret;
                    }
                }
            }
        } else {
            // whereClause.append(" 1=1 ");
            return true;
        }

        return ret;
    }

    /**
     * get single pk search sql
     *
     * @param whereClause
     * @param searchCondition
     * @param aliasName
     * @param includeValues
     * @param pk
     * @return
     */
    private static boolean genSinglePkHQL(StringBuffer whereClause, SearchCondition searchCondition, String aliasName,
                                          List<Object> includeValues, Object pk) {
        String spk = "";
        boolean ret = true;

        if (pk != null) {
            boolean isValidStringPK = pk instanceof String && (((String) pk).length() == 0);
            boolean isValidLongPK = pk instanceof Long && (((Long) pk).longValue() == 0);

            if (isValidStringPK || isValidLongPK) {
                whereClause.append(HQL_EMPTY_EXPRESSION);

                return true;
            } else {
                if (searchCondition.isEnablePkLike()) {
                    whereClause.append(aliasName);
                    whereClause.append(HQL_LINK_JOIN);
                    whereClause.append(HQL_SINGLEPK_NAME);
                    whereClause.append(HQL_LIKE);
                    whereClause.append(HQL_PARAM_ALIAS);
                    spk = String.valueOf(pk);
                    spk = genOracleLike(spk, searchCondition.getPkMatchModel());
                    includeValues.add(spk);
                } else {
                    whereClause.append(aliasName);
                    whereClause.append(HQL_LINK_JOIN);
                    whereClause.append(HQL_SINGLEPK_NAME);
                    whereClause.append(HQL_EQUAL);
                    whereClause.append(HQL_PARAM_ALIAS);
                    includeValues.add(pk);
                    ret = false;
                }
            }
        } else {
            whereClause.append(HQL_EMPTY_EXPRESSION);

            return true;
        }

        return ret;
    }

    /**
     * construct param sql like " and user.username = ? " and add the param to
     * includeValues list for model
     *
     * @param whereClause
     * @param aliasName
     * @param matchedFieldName
     * @param searchCondition
     * @param param
     * @param includeValues
     */
    private static void genParamSQL(StringBuffer whereClause, String aliasName, String matchedFieldName,
                                    SearchCondition searchCondition, Object param, Collection<Object> includeValues) {
        if (searchCondition.isEnableOrLinker()) {
            whereClause.append(HQL_LINK_OR);
        } else {
            whereClause.append(HQL_LINK_AND);
        }
        // add by jeff for column case insensitive function support
        if (searchCondition.isCaseInsensitive(matchedFieldName)) {
            whereClause.append(searchCondition.getCaseInsensitiveFunc(matchedFieldName));
            whereClause.append(HQL_LEFT_BRACKET);
        }
        whereClause.append(aliasName);
        whereClause.append(HQL_LINK_JOIN);
        whereClause.append(matchedFieldName);

        // add by jeff for column case insensitive function support
        if (searchCondition.isCaseInsensitive(matchedFieldName)) {
            whereClause.append(HQL_RIGHT_BRACKET);
            whereClause.append(HQL_LINK_EMPTY);
        }

        // boolean isCompModel = param instanceof CompositePKModel;
        // if (isCompModel) {
        // genCompPkHQL(whereClause, searchCondition, aliasName,
        // (List) includeValues, (CompositePKModel) param);
        // }
        param = genEqualsString(searchCondition, whereClause, matchedFieldName, param);

        if (searchCondition.isBetweenColumn(matchedFieldName)) {
            includeValues.add(searchCondition.getBetweenValue(matchedFieldName)[0]);
            includeValues.add(searchCondition.getBetweenValue(matchedFieldName)[1]);
        } else {
            includeValues.add(param);
        }
    }

    /**
     * construct param sql like " and user.comp_id.username = ? " and add the
     * param to includeValues list for CompositePKModel
     *
     * @param whereClause
     * @param aliasName
     * @param matchedFieldName
     * @param searchCondition
     * @param param
     * @param includeValues
     * @param isFirst
     */
    private static void genCompPKParamSQL(StringBuffer whereClause, String aliasName, String matchedFieldName,
                                          SearchCondition searchCondition, Object param, Collection<Object> includeValues, boolean isFirst) {
        if (!isFirst) {
            if (searchCondition.isEnableOrLinker()) {
                whereClause.append(HQL_LINK_OR);
            } else {
                whereClause.append(HQL_LINK_AND);
            }
        }

        whereClause.append(aliasName);
        whereClause.append(".comp_id.");
        whereClause.append(matchedFieldName);
        param = genCompPKEqualsString(searchCondition, whereClause, matchedFieldName, param);
        includeValues.add(param);
    }

    private static Object genCompPKEqualsString(SearchCondition searchCondition, StringBuffer whereClause,
                                                String matchedFieldName, Object param) {
        Object ret = param;

        String nonEqualString = searchCondition.getNonEqualString(matchedFieldName);
        boolean isModel = param instanceof ISingleStringPKModel || param instanceof ISingleLongPKModel
                || param instanceof ICompositePKModel;
        boolean isDate = param instanceof Date;

        if (StringUtils.isNotEmpty(nonEqualString)) {
            whereClause.append(nonEqualString);
        } else if (searchCondition.isBetweenColumn(matchedFieldName)) {
            whereClause.append(HQL_BETWEEN);
        } else if (isModel || isDate) {
            whereClause.append(HQL_EQUAL);
        } else if (searchCondition.isEnablePkLike()) {
            whereClause.append(HQL_LIKE);
            ret = genLikeValueString(searchCondition, param);
        } else {
            whereClause.append(HQL_EQUAL);
        }

        if (!searchCondition.isBetweenColumn(matchedFieldName)) {
            if (isModel || isDate) {
                whereClause.append(HQL_PARAM_ALIAS);
            } else if (searchCondition.isIgnoreCase()) {
                whereClause.append(HQL_LINK_EMPTY);
                whereClause.append(HQL_UPPER);
                whereClause.append(HQL_LEFT_BRACKET);
                whereClause.append(HQL_PARAM_ALIAS.trim());
                whereClause.append(HQL_RIGHT_BRACKET);
                whereClause.append(HQL_LINK_EMPTY);
            } else {
                whereClause.append(HQL_PARAM_ALIAS);
            }
        }

        return ret;
    }

    /**
     * get "=" or "like" and "lower(?)" or "?"
     *
     * @param searchCondition
     * @param whereClause
     * @param param
     * @param matchedFieldName
     * @return
     */
    private static Object genEqualsString(SearchCondition searchCondition, StringBuffer whereClause,
                                          String matchedFieldName, Object param) {
        Object ret = param;

        String nonEqualString = searchCondition.getNonEqualString(matchedFieldName);

        boolean isModel = param instanceof ISingleStringPKModel || param instanceof ISingleLongPKModel
                || param instanceof ICompositePKModel;
        boolean isDate = param instanceof Date;

        if (StringUtils.isNotEmpty(nonEqualString)) {
            whereClause.append(nonEqualString);
        } else if (searchCondition.isBetweenColumn(matchedFieldName)) {
            whereClause.append(HQL_BETWEEN);
        } else if (isModel || isDate) {
            whereClause.append(HQL_EQUAL);
        } else if (searchCondition.isEnableLike()) {
            whereClause.append(HQL_LIKE);

            ret = genLikeValueString(searchCondition, param);
        } else {
            whereClause.append(HQL_EQUAL);
        }

        if (!searchCondition.isBetweenColumn(matchedFieldName)) {

            if (isModel || isDate) {
                whereClause.append(HQL_PARAM_ALIAS);
            } else if (searchCondition.isIgnoreCase()) {
                whereClause.append(HQL_LINK_EMPTY);
                // add by jeff for column case insensitive function support
                if (searchCondition.isCaseInsensitive(matchedFieldName)) {
                    whereClause.append(searchCondition.getCaseInsensitiveFunc(matchedFieldName));
                    whereClause.append(HQL_LEFT_BRACKET);
                }

                whereClause.append(HQL_PARAM_ALIAS);

                // add by jeff for column case insensitive function support
                if (searchCondition.isCaseInsensitive(matchedFieldName)) {
                    whereClause.append(HQL_RIGHT_BRACKET);
                }
                whereClause.append(HQL_LINK_EMPTY);
            } else {
                whereClause.append(HQL_PARAM_ALIAS);
            }
        }

        return ret;
    }

    /**
     * convert number type params to String in order to add wildcard to params
     *
     * @param searchCondition
     * @param param
     * @return
     */
    private static Object genLikeValueString(SearchCondition searchCondition, Object param) {
        String ret = String.valueOf(param);

        if (param instanceof Long) {
            Long aLong = (Long) param;
            ret = aLong.longValue() + "";
        } else if (param instanceof Integer) {
            Integer integer = (Integer) param;
            ret = integer.intValue() + "";
        }
        // else if (param instanceof Date) {
        // Date date = (Date) param;
        // ret = DateUtil.date2String(date);
        // }
        else if (param instanceof String) {
            ret = (String) param;
        } else if (param instanceof Double) {
            Double aDouble = (Double) param;
            ret = aDouble.doubleValue() + "";
        }

        ret = genOracleLike(ret, searchCondition.getMatchModel());

        return ret;
    }

    /**
     * add oracle wildcard to params
     *
     * @param ret
     * @param metchModel
     * @return
     */
    private static String genOracleLike(String ret, int metchModel) {
        if (ret instanceof String) {
            switch (metchModel) {
                case SearchConstant.MATCH_MODEL_ANYWHERE:
                    ret = HQL_ORACLE_WILDCARD + ret + HQL_ORACLE_WILDCARD;

                    break;

                case SearchConstant.MATCH_MODEL_END:
                    ret += HQL_ORACLE_WILDCARD;

                    break;

                case SearchConstant.MATCH_MODEL_EXACT:
                    break;

                case SearchConstant.MATCH_MODEL_START:
                    ret = HQL_ORACLE_WILDCARD + ret;

                    break;

                default:
                    ret += HQL_ORACLE_WILDCARD;

                    break;
            }
        }

        return ret;
    }

    public static String getModelName(IModel model) {
        return getModelName(model.getClass());
    }

    public static String getModelName(Class clazz) {
        String className = clazz.getName();
        String modelName = StringUtils.substring(className, StringUtils.lastIndexOf(className, HQL_LINK_JOIN) + 1);

        return modelName;
    }

    public static SearchCondition extend(SearchCondition searchCondition) {
        if (StringUtils.isNotEmpty(searchCondition.getExternalHQL())) {
            // join HQL
            searchCondition.setHibernateQL(searchCondition.getHibernateQL() + HQL_LINK_EMPTY
                    + searchCondition.getExternalHQL());

            // join Param
            searchCondition.setQueryParam((List<Object>) CollectionUtil.union(searchCondition.getQueryParam(),
                    searchCondition.getExternalHQLParam()));
        }

        return searchCondition;
    }

    public static SearchCondition genOrderByClause(SearchCondition searchCondition) {
        Map<String, String> orderByMap = searchCondition.getOrderByMap();
        StringBuffer hql = new StringBuffer(searchCondition.getHibernateQL());
        int i = 0;

        if ((orderByMap != null) && (orderByMap.size() > 0)) {
            Set<String> orderByColumns = orderByMap.keySet();

            for (Iterator<String> iterator = orderByColumns.iterator(); iterator.hasNext(); ) {
                String column = iterator.next();
                String order = (String) orderByMap.get(column);

                if (i == 0) {
                    hql.append(HQL_ORDER_BY);
                } else {
                    hql.append(HQL_EXPRESSION_LINK);
                }

                hql.append(column);
                hql.append(HQL_LINK_EMPTY);
                hql.append(order);
                hql.append(HQL_LINK_EMPTY);

                i++;
            }
        }

        searchCondition.setHibernateQL(hql.toString());

        return searchCondition;
    }

//	public static List<Type> parseTypes(List<Object> paramList) {
//		List<Type> types = newweb ArrayList<Type>();
//		for (Iterator<Object> iterator = paramList.iterator(); iterator.hasNext();) {
//			Object param = iterator.next();
//			if (param instanceof Long) {
//				types.add(Hibernate.LONG);
//			} else if (param instanceof Short) {
//				types.add(Hibernate.SHORT);
//			} else if (param instanceof Integer) {
//				types.add(Hibernate.INTEGER);
//			} else if (param instanceof Byte) {
//				types.add(Hibernate.BYTE);
//			} else if (param instanceof Float) {
//				types.add(Hibernate.FLOAT);
//			} else if (param instanceof Double) {
//				types.add(Hibernate.DOUBLE);
//			} else if (param instanceof Character) {
//				types.add(Hibernate.CHARACTER);
//			} else if (param instanceof String) {
//				types.add(Hibernate.STRING);
//			} else if (param instanceof Time) {
//				types.add(Hibernate.TIME);
//			} else if (param instanceof Date) {
//				types.add(Hibernate.DATE);
//			} else if (param instanceof Timestamp) {
//				types.add(Hibernate.TIMESTAMP);
//			} else if (param instanceof Boolean) {
//				types.add(Hibernate.BOOLEAN);
//			} else if (param instanceof BigDecimal) {
//				types.add(Hibernate.BIG_DECIMAL);
//			} else if (param instanceof Blob) {
//				types.add(Hibernate.BLOB);
//			} else if (param instanceof Clob) {
//				types.add(Hibernate.CLOB);
//			} else if (param instanceof IModel) {
//				types.add(Hibernate.entity(param.getClass()));
//			} else {
//				types.add(Hibernate.OBJECT);
//			}
//		}
//		return types;
//	}
}
