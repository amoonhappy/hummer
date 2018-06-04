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
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a> Date: 2005-11-21 23:11:58
 * @version 1.0
 */
package org.hummer.core.util;

import org.apache.commons.lang3.StringUtils;
import org.hummer.core.constants.ExceptionConstant;
import org.hummer.core.exception.BusinessException;
import org.hummer.core.model.ModelFactory;
import org.hummer.core.model.intf.IModel;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Map;

public class ModelUtil {
    public static final String MODEL_NAME_LINKER = ".";
    private static Class[] BLANK_CLASS = new Class[0];

    public static String getModelName(IModel model) {
        return getModelName(model.getClass());
    }

    public static String getModelName(Class clazz) {
        String className = clazz.getName();
        return StringUtils.substring(className, StringUtils.lastIndexOf(className, MODEL_NAME_LINKER) + 1);
    }

    /**
     * for batch update
     *
     * @param clazz
     * @param columnName
     * @param value
     * @return
     * @throws BusinessException
     */
    public static Object transValuetoOrgType(Class clazz, String columnName, String value) throws BusinessException {
        Map<String, Class> columnTypes = ModelFactory.getInstance().getColumnType(clazz);
        Class columnType = columnTypes.get(columnName);
        String className = columnType.getName();

        try {
            if (StringUtils.equals(className, "java.lang.String")) {
                return value;
            } else if (StringUtils.equals(className, "java.lang.Long")) {
                return Long.valueOf(value);
            } else if (StringUtils.equals(className, "java.lang.Integer")) {
                return Integer.valueOf(value);
            } else if (StringUtils.equals(className, "java.lang.Double")) {
                return Double.valueOf(value);
            } else if (StringUtils.equals(className, "java.lang.Float")) {
                return Float.valueOf(value);
            } else {
                if (StringUtils.equals(className, "java.util.Date")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

                    return sdf.parse(value);
                } else {
                    throw new Exception();
                }
            }
        } catch (Exception e) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_SAVE_CONDITION_FORMAT_ERROR);
        }
    }

    /**
     * get the identifier of the entity model
     *
     * @param o
     * @return
     * @throws BusinessException
     */
    public static Serializable getIdValue(IModel o) throws BusinessException {
        Serializable id;
        Method meth = null;

        try {
            meth = o.getClass().getMethod("getId", BLANK_CLASS);
        } catch (Exception e) {
            // try other identifier
            try {
                meth = o.getClass().getMethod("getComp_id", BLANK_CLASS);
            } catch (NoSuchMethodException e1) {
                // no action
            }
        }

        // if there is no identifier
        if (meth == null) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_NO_IDENTIFIER);
        }

        try {
            id = (Serializable) meth.invoke(o, new Object[0]);
        } catch (Exception e) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_NO_IDENTIFIER);
        }

        return id;
    }
}
