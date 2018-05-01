/**
 * <p>Open Source Architecture Project -- hummer-common            </p>
 * <p>Class Description                                     </p>
 * <p>                                                      </p>
 * <p>                                                      </p>
 * <p>Change History                                        </p>
 * <p>Author    Date      Description                       </p>
 * <p>                                                      </p>
 * <p>                                                      </p>
 * Copyright (c) 2004-2007 hummer-common, All rights reserved
 *
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a>
 * Date: 2005-12-7
 * @version 1.0
 */
package org.hummer.core.util;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.collections.FastHashMap;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * <br>
 * <br>
 * <ul>
 * <li>
 * <li>
 * </ul>
 * <br>
 *
 * @author jeff
 * @version <br>
 * 1.0.0 2007-6-23
 * @since 1.0.0
 */
public class BeanUtil {
    public static final char NESTED_DELIM = '.';

    public static final String ARRAY_DELIM = "[]";

    private static BeanUtil beanUtils = new BeanUtil();

    private ConvertUtilsBean convertUtilsBean;

    private FastHashMap fieldCache = null;

    private BeanUtil() {
        BeanUtilsBean beanUtilsBean = BeanUtilsBean.getInstance();
        this.convertUtilsBean = beanUtilsBean.getConvertUtils();

        fieldCache = new FastHashMap();
        fieldCache.setFast(true);

        // this.propertyUtilsBean = beanUtilsBean.getPropertyUtils();
    }

    public static BeanUtil getInstance() {
        return beanUtils;
    }

    public ConvertUtilsBean getConvertUtilsBean() {
        return convertUtilsBean;
    }

    public void setConvertUtilsBean(ConvertUtilsBean convertUtilsBean) {
        this.convertUtilsBean = convertUtilsBean;
    }

    /**
     * copy the values in orig object to dest object. pls notice , I don't copy
     * the null value because of the service and preformance reasons. if you
     * don't want to remain the old value in dest, pls clear it before call
     * these method. or you need call the
     * org.apache.commons.beanutils.BeanUtils. This method is used mainly to
     * server for the hbs framework.
     *
     * @param dest
     * @param orig
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void copyProperties(Object dest, Object orig) throws Exception {
        copyProperties(dest, orig, null, null);
    }

    public void copyProperties(Object dest, Object orig, Callback beforeCopyProperty, ExceptionHandler exceptionHandler)
            throws Exception {
        // Validate existence of the specified beans
        if (dest == null) {
            throw new IllegalArgumentException("No destination bean specified");
        }

        if (orig == null) {
            throw new IllegalArgumentException("No origin bean specified");
        }

        Class clazz = orig.getClass();
        Field[] fields = getFields(clazz);

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Object value = field.get(orig);
            Field destField = getField(dest.getClass(), field.getName());

            if (beforeCopyProperty != null) {
                Class destType = null;

                if (destField != null) {
                    destType = destField.getType();
                }

                value = beforeCopyProperty.execute(destType, orig, field.getName(), value);
            }

            // NOTICE: in order to improve the performance, I don't set the null
            // value
            if ((value != null) && (destField != null)) {
                if (exceptionHandler == null) {
                    copyProperty(dest, destField, field.getName(), value);
                } else {
                    try {
                        copyProperty(dest, destField, field.getName(), value);
                    } catch (Exception ex) {
                        exceptionHandler.execute(ex);
                    }
                }
            }
        }
    }

    public void copyProperty(Object bean, String name, Object value) throws IllegalAccessException,
            InvocationTargetException {
        Field field = getField(bean.getClass(), name);

        if (field == null) {
            return;
        }

        copyProperty(bean, field, name, value);
    }

    private void copyProperty(Object bean, Field destField, String name, Object value) throws IllegalAccessException,
            InvocationTargetException {
        Class<?> type = destField.getType();

        if (value != null) {
            if (!type.isAssignableFrom(value.getClass())) {
                Converter converter = convertUtilsBean.lookup(type);

                if (converter != null) {
                    value = converter.convert(type, value);
                } else {
                    converter = ConvertUtil.getInstance().getConvert();

                    if (converter != null) {
                        value = converter.convert(type, value);
                    }
                }
            }

            destField.set(bean, value);
        } else {
            if (destField.getType().isPrimitive()) {
                // Object tmp = destField.getType().getConstructors();
            } else {
                destField.set(bean, null);
            }
        }
    }

    public void copyNestedProperty(Object bean, String name, Object value) throws IllegalAccessException,
            InvocationTargetException {
        int index = name.lastIndexOf(NESTED_DELIM);

        if (index > 0) {
            // TODO: in future, we need change this to let it support []
            // operation
            bean = getNestedProperty(bean, name.substring(0, index));
            name = name.substring(index + 1);
        }

        copyProperty(bean, name, value);
    }

    public Object getProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException {
        Field field = getField(bean.getClass(), name);

        if (field == null) {
            return null;
        }

        return field.get(bean);
    }

    /**
     * get the property of bean, it support nested name
     *
     * @param bean
     * @param name
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Object getNestedProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException {
        List<Object> retValues = new ArrayList<Object>();
        getNestedProperty(bean, name, retValues);

        if (retValues.size() == 1) {
            return retValues.get(0);
        } else {
            return retValues;
        }
    }

    private Object getArrayProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException {
        int fieldLen = name.length();
        Object value = null;
        if (fieldLen >= 2 && name.charAt(fieldLen - 1) == ']') {
            // TODO: now, we only support [], in future we should support [0]
            int pos = name.lastIndexOf('[');
            if (pos == (fieldLen - 2)) {
                // []
                name = name.substring(0, pos);
                value = getProperty(bean, name);
            } else {
                int index = Integer.parseInt(name.substring(pos + 1, fieldLen - 1));
                name = name.substring(0, pos);
                value = getProperty(bean, name);
                if (value instanceof Collection && ((Collection) value).size() > index) {
                    if (value instanceof List) {
                        value = ((List) value).get(index);
                    } else {
                        Iterator iter = ((Collection) value).iterator();
                        while (index > 0) {
                            iter.next();
                        }
                        value = iter.next();
                    }
                } else {
                    // FIXME:
                    // if the parameter is error , do we need set null to the
                    // value?
                }
            }
        } else {
            value = getProperty(bean, name);
        }
        return value;
    }

    private void getNestedProperty(Object bean, String name, List<Object> retValues) throws IllegalAccessException,
            InvocationTargetException {

        int indexEnd = 0;
        // 1. find the fist name
        indexEnd = name.indexOf(NESTED_DELIM);
        if (indexEnd < 0) {
            indexEnd = name.length();
            // NOTICE: now , the follow logic don't change the indexEnd value
        }

        // 2. get its property
        Object value = getArrayProperty(bean, name.substring(0, indexEnd));

        boolean isLastProperty = indexEnd == name.length() ? true : false;
        boolean isArray = (value != null && value instanceof Collection);
        // 3. if it's not the lastest property , call getNestedProperty
        if (!isLastProperty) {
            name = name.substring(indexEnd + 1);
            // if it's array , we get each element in array
            if (isArray) {
                Iterator iter = ((Collection) value).iterator();
                while (iter.hasNext()) {
                    getNestedProperty(iter.next(), name, retValues);
                }
            } else {
                getNestedProperty(value, name, retValues);
            }

            // 4. if it's the lastest property, save it to retValues
        } else {
            // if it's array , we add each element in array
            if (isArray) {
                Iterator iter = ((Collection) value).iterator();
                while (iter.hasNext()) {
                    retValues.add(iter.next());
                }
            } else {
            }
        }
    }

    /**
     * @param bean
     * @return return all the name and value of bean
     */
    public Collection getNameValues(Object bean) {
        Field[] fields = getFields(bean.getClass());
        ArrayList<NameValue> list = new ArrayList<NameValue>(fields.length);

        for (int i = 0; i < fields.length; i++) {
            Object value = null;

            try {
                value = fields[i].get(bean);
            } catch (Exception ex) {
            }

            list.add(new NameValue(fields[i].getName(), value));
        }

        return list;
    }

    private Field getField(Class clazz, String name) {
        Field[] fields = getFields(clazz);

        if (fields != null) {
            for (int i = 0; i < fields.length; i++) {
                if (name.equals(fields[i].getName())) {
                    return fields[i];
                }
            }
        }

        return null;
    }

    private Field[] getFields(Class clazz) {
        Field[] fields = (Field[]) fieldCache.get(clazz);

        if (fields == null) {
            Class superClazz = clazz;
            ArrayList<Field> list = new ArrayList<Field>();

            while (superClazz != Object.class) {
                Field[] tmpFields = superClazz.getDeclaredFields();
                AccessibleObject.setAccessible(tmpFields, true);

                for (int i = 0; i < tmpFields.length; i++) {
                    if (!Modifier.isFinal(tmpFields[i].getModifiers())
                            && !Modifier.isStatic(tmpFields[i].getModifiers())) {
                        list.add(tmpFields[i]);
                    }
                }

                superClazz = superClazz.getSuperclass();
            }

            fields = new Field[list.size()];
            list.toArray(fields);
            fieldCache.put(clazz, fields);
        }

        return fields;
    }

    public interface Callback {
        /**
         * fieldName != null
         */
        public Object execute(Class destType, Object srcObj, String srcFieldName, Object srcValue);
    }

    public interface ExceptionHandler {
        public void execute(Exception ex) throws Exception;
    }

    public class NameValue {
        private String name;

        private Object value;

        public NameValue(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

}
