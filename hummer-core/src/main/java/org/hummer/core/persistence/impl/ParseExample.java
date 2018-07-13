package org.hummer.core.persistence.impl;

import org.apache.commons.lang.StringUtils;
import org.hummer.core.constants.Constant;
import org.hummer.core.dto.BasicDto;
import org.hummer.core.util.DateUtil;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Date;

@SuppressWarnings("all" )
public class ParseExample {
    private static final String ORDER_BY_KEY = "sidx" ;
    private static final String SORT_KEY = "order" ;
    private static final String UNDER_LINE = "_" ;
    private static final String QUERY_KEY = "QueryDate" ;
    private static final String SPLIT_CHAR = "," ;

    public static String camelToUnderline(String param) {
        if (StringUtils.isBlank(param)) {
            return StringUtils.EMPTY;
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDER_LINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String getOrderByClause(BasicDto dto, String modelName) {
        String aliasName = Constant.getTabAliasName(modelName);
        String clause = aliasName + "_update_time desc" ;
        String methodOrderBy = "get" + ORDER_BY_KEY.substring(0, 1).toUpperCase() + ORDER_BY_KEY.substring(1);
        String methodSort = "get" + SORT_KEY.substring(0, 1).toUpperCase() + SORT_KEY.substring(1);
        Method method = ParseExample.getMethod(dto.getClass(), methodOrderBy);
        if (method != null) {
            Object returnObject = ParseExample.invokeMethod(dto, method);
            if (returnObject != null && StringUtils.isNotBlank((String) returnObject)) {
                String orderBy = (String) returnObject;
                String sort = "asc" ;
                method = ParseExample.getMethod(dto.getClass(), methodSort);
                if (method != null) {
                    returnObject = ParseExample.invokeMethod(dto, method);
                    if (returnObject != null && StringUtils.isNotBlank((String) returnObject)) {
                        sort = (String) returnObject;
                    }
                }
                clause = aliasName + UNDER_LINE + camelToUnderline(orderBy) + " " + sort;
            }
        }
        return clause;
    }

    private static Method getMethod(Class<?> c, String methodName) {
        Method method = null;
        for (Class<?> cl = c; cl != null; cl = cl.getSuperclass()) {
            Method[] methods = cl.getMethods();
            for (int i = 0; i < methods.length; i++) {
                method = methods[i];
                if (method == null) {
                    continue;
                }
                if (method.getName().equals(methodName)) {
                    return method;
                }
            }
        }
        method = null;
        return method;
    }

    private static Object invokeMethod(Object target, Method method, Object... args) {
        Object o;
        try {
            o = method.invoke(target, args);
        } catch (Exception e) {
            return null;
        }
        return o;
    }

    public static <C> C parseExample(BasicDto dto, C c) {
        Field[] fields = dto.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (f.getName().endsWith(QUERY_KEY)) {
                String fieldName = f.getName();
                String upperFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method method = ParseExample.getMethod(dto.getClass(), "get" + upperFieldName);
                if (method != null) {
                    Object returnObject = ParseExample.invokeMethod(dto, method);
                    if (returnObject != null && StringUtils.isNotBlank((String) returnObject)) {
                        String returnStr = (String) returnObject;
                        try {
                            returnStr = URLDecoder.decode(returnStr, "UTF-8" );
                        } catch (UnsupportedEncodingException e) {
                            returnStr = (String) returnObject;
                        }
                        int count = 0;
                        String start = null;
                        String end = null;
                        String[] dateRange = returnStr.split(SPLIT_CHAR);
                        for (int i = 0; i < dateRange.length; i++) {
                            if (StringUtils.isNotBlank(dateRange[i])) {
                                if (count == 0) {
                                    start = dateRange[i];
                                    count++;
                                } else {
                                    end = dateRange[i];
                                }
                            }
                        }
                        if (StringUtils.isNotBlank(start) && StringUtils.isNotBlank(end)) {
                            Date startDate = DateUtil.parse(start + " 00:00:00", DateUtil.SDF_CN);
                            Date endDate = DateUtil.parse(end + " 23:59:59", DateUtil.SDF_CN);

                            String upperDateFieldName = upperFieldName.replace(QUERY_KEY, StringUtils.EMPTY);
                            method = ParseExample.getMethod(c.getClass(), "and" + upperDateFieldName + "GreaterThanOrEqualTo" );
                            ParseExample.invokeMethod(c, method, startDate);
                            method = ParseExample.getMethod(c.getClass(), "and" + upperDateFieldName + "LessThanOrEqualTo" );
                            ParseExample.invokeMethod(c, method, endDate);
                        }
                    }
                }
            } else {
                if (String.class.getName().equals(f.getType().getName())) {
                    String upperFieldName = f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                    Method method = ParseExample.getMethod(dto.getClass(), "get" + upperFieldName);
                    if (method != null) {
                        Object returnObject = ParseExample.invokeMethod(dto, method);
                        if (returnObject != null && StringUtils.isNotBlank((String) returnObject)) {
                            String returnStr = (String) returnObject;
                            try {
                                returnStr = URLDecoder.decode(returnStr, "UTF-8" );
                            } catch (UnsupportedEncodingException e) {
                                returnStr = (String) returnObject;
                            }
                            method = ParseExample.getMethod(c.getClass(), "and" + upperFieldName + "Like" );
                            ParseExample.invokeMethod(c, method, "%" + returnStr + "%" );
                        }
                    }
                } else if (Integer.class.getName().equals(f.getType().getName())) {
                    String upperFieldName = f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                    Method method = ParseExample.getMethod(dto.getClass(), "get" + upperFieldName);
                    if (method != null) {
                        Object returnObject = ParseExample.invokeMethod(dto, method);
                        if (returnObject != null) {
                            method = ParseExample.getMethod(c.getClass(), "and" + upperFieldName + "EqualTo" );
                            ParseExample.invokeMethod(c, method, returnObject);
                        }
                    }
                } else if (Date.class.getName().equals(f.getType().getName())) {
                    String upperFieldName = f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                    Method method = ParseExample.getMethod(dto.getClass(), "get" + upperFieldName);
                    if (method != null) {
                        Object returnObject = ParseExample.invokeMethod(dto, method);
                        if (returnObject != null) {
                            method = ParseExample.getMethod(c.getClass(), "and" + upperFieldName + "EqualTo" );
                            ParseExample.invokeMethod(c, method, returnObject);
                        }
                    }
                }
            }
        }
        return c;
    }

    public static <C> C parseExampleWithExact(BasicDto dto, C c) {
        Field[] fields = dto.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (String.class.getName().equals(f.getType().getName())) {
                String upperFieldName = f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                Method method = ParseExample.getMethod(dto.getClass(), "get" + upperFieldName);
                if (method != null) {
                    Object returnObject = ParseExample.invokeMethod(dto, method);
                    if (returnObject != null && StringUtils.isNotBlank((String) returnObject)) {
                        String returnStr = (String) returnObject;
                        try {
                            returnStr = URLDecoder.decode(returnStr, "UTF-8" );
                        } catch (UnsupportedEncodingException e) {
                            returnStr = (String) returnObject;
                        }
                        method = ParseExample.getMethod(c.getClass(), "and" + upperFieldName + "EqualTo" );
                        ParseExample.invokeMethod(c, method, returnStr);
                    }
                }
            } else if (Integer.class.getName().equals(f.getType().getName())) {
                String upperFieldName = f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                Method method = ParseExample.getMethod(dto.getClass(), "get" + upperFieldName);
                if (method != null) {
                    Object returnObject = ParseExample.invokeMethod(dto, method);
                    if (returnObject != null) {
                        method = ParseExample.getMethod(c.getClass(), "and" + upperFieldName + "EqualTo" );
                        ParseExample.invokeMethod(c, method, returnObject);
                    }
                }
            } else if (Long.class.getName().equals(f.getType().getName())) {
                String upperFieldName = f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                Method method = ParseExample.getMethod(dto.getClass(), "get" + upperFieldName);
                if (method != null) {
                    Object returnObject = ParseExample.invokeMethod(dto, method);
                    if (returnObject != null) {
                        method = ParseExample.getMethod(c.getClass(), "and" + upperFieldName + "EqualTo" );
                        ParseExample.invokeMethod(c, method, returnObject);
                    }
                }
            }
        }
        return c;
    }

    public static <C> C parseCheckExample(BasicDto dto, C c) {
        Field[] fields = dto.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (String.class.getName().equals(f.getType().getName())) {
                String upperFieldName = f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                Method method = ParseExample.getMethod(dto.getClass(), "get" + upperFieldName);
                if (method != null) {
                    Object returnObject = ParseExample.invokeMethod(dto, method);
                    if (returnObject != null && StringUtils.isNotBlank((String) returnObject)) {
                        if ("id".equals(f.getName())) {
                            method = ParseExample.getMethod(c.getClass(), "and" + upperFieldName + "NotEqualTo" );
                        } else {
                            method = ParseExample.getMethod(c.getClass(), "and" + upperFieldName + "EqualTo" );
                        }
                        ParseExample.invokeMethod(c, method, returnObject);
                    }
                }
            }
        }
        return c;
    }
}