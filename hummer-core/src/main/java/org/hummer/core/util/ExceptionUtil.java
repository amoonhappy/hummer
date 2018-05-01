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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hummer.core.constants.ExceptionConstant;
import org.hummer.core.context.impl.LocaleUtil;
import org.hummer.core.exception.BusinessException;
import org.hummer.core.exception.ErrorInfo;
import org.hummer.core.message.impl.SQLLogInfo;
import org.hummer.core.model.intf.IModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public class ExceptionUtil {
    /**
     * Set of String 2-digit codes that indicate bad SQL
     */
    // private static Set BAD_SQL_CODES = newweb HashSet();
    /**
     * Set of String 2-digit codes that indicate RDBMS integrity violation
     */
    // private static Set INTEGRITY_VIOLATION_CODES = newweb HashSet();
    protected static final Logger logger = Logger.getLogger(ExceptionUtil.class);

    // Populate reference data.
    static {
        // BAD_SQL_CODES.add("07");
        // BAD_SQL_CODES.add("42");
        // BAD_SQL_CODES.add("65"); // Oracle throws on unknown identifier
        // BAD_SQL_CODES.add("S0"); // MySQL uses this - from ODBC error codes?

        // INTEGRITY_VIOLATION_CODES.add("22"); // Integrity constraint
        // violation
        // INTEGRITY_VIOLATION_CODES.add("23"); // Integrity constraint
        // violation
        // INTEGRITY_VIOLATION_CODES.add("27"); // Triggered data change
        // violation
        // INTEGRITY_VIOLATION_CODES.add("44"); // With check violation
    }

    public static SQLException getSQLException(Exception e) {
        if (e instanceof SQLException) {
            SQLException sqlException = (SQLException) e;

            if (sqlException.getErrorCode() == 0) {
                Exception nestedException = (Exception) sqlException.getCause();

                if (nestedException == null) {
                    return null;
                }

                return getSQLException(nestedException);
            }

            return sqlException;
        } else {
            Exception nestedException = (Exception) e.getCause();

            if (nestedException == null) {
                return null;
            }

            return getSQLException(nestedException);
        }
    }

    /**
     * Get a TMGException
     *
     * @param errorCode error code
     * @param locale    locale
     * @return The tMGException value
     */
    public static BusinessException getBusinessException(int errorCode, Locale locale) {
        String message = MessageUtil.getMessage(errorCode, locale);
        BusinessException e = new BusinessException(message, errorCode);

        return e;
    }

    /**
     * @param errorCode
     * @return
     */
    public static BusinessException getBusinessException(int errorCode) {
        String message = MessageUtil.getMessage(errorCode);

        BusinessException e = new BusinessException(message, errorCode);

        return e;
    }

    public static BusinessException getBusinessException(int errorCode, List<Object> errorParams) {
        String message = MessageUtil.getMessage(errorCode, errorParams);

        BusinessException e = new BusinessException(message, errorCode, errorParams);

        return e;
    }

    public static BusinessException getBusinessException(int errorCode, List<Object> errorParams, Locale locale) {
        String message = MessageUtil.getMessage(errorCode, errorParams, locale);

        BusinessException e = new BusinessException(message, errorCode, errorParams);

        return e;
    }

    public static BusinessException getBusinessException(int errorCode, List<Object> errorParams, Locale locale,
                                                         Throwable throwable) {
        String message = MessageUtil.getMessage(errorCode, errorParams, locale);

        BusinessException e = new BusinessException(message, throwable, errorCode, errorParams);

        return e;
    }

    /**
     * Get a BusinessException
     *
     * @param t         the exception that triggered this newweb BusinessException. Its
     *                  stack trace will be saved in the BusinessException.
     * @param errorCode Description of the Parameter
     * @param locale    Description of the Parameter
     * @return The BusinessException value
     */
    public static BusinessException getBusinessException(int errorCode, Locale locale, Throwable t) {
        String message = MessageUtil.getMessage(errorCode, locale);
        BusinessException e = new BusinessException(message, t, errorCode);

        return e;
    }

    /**
     * Get a BusinessException
     *
     * @param t         the exception that triggered this newweb BusinessException. Its
     *                  stack trace will be saved in the BusinessException.
     * @param errorCode Description of the Parameter
     * @return The BusinessException value
     */
    public static BusinessException getBusinessException(int errorCode, Throwable t) {
        String message = MessageUtil.getMessage(errorCode, LocaleUtil.getLocale());
        BusinessException e = new BusinessException(message, t, errorCode);

        return e;
    }

    public static BusinessException getBusinessException(ErrorInfo errorInfo, Throwable t) {
        BusinessException e = new BusinessException(errorInfo, t);

        return e;
    }

    /**
     * Get a BusinessException
     *
     * @param t          the exception that triggered this newweb BusinessException. Its
     *                   stack trace will be saved in the BusinessException.
     * @param sqlLogInfo SQL Exception Detail Information
     * @return The BusinessException value
     */
    public static BusinessException getBusinessException(SQLLogInfo sqlLogInfo, Object[] params, SQLException t) {
        if ((sqlLogInfo == null) || (t == null)) {
            return getBusinessException(ExceptionConstant.DB_OTHER_DATA_ACCESS_ERROR, t);
        }

        // if the errorCode is 2292,it's a fk exception
        // exception message likes:
        //
        BusinessException e = null;
        try {
            IModel model = null;

            String refName = getRefenceName(sqlLogInfo.getMessages());
            if (sqlLogInfo.getSqlErrorCode() == 2292) {
                if ((params != null) && (params.length > 0)) {
                    if (params[0] instanceof IModel) {
                        model = (IModel) params[0];
                    }
                }
                ErrorInfo message = new ErrorInfo();

                message.setCode(ExceptionConstant.BIZ_RECORD_USED_BY_OTHERS);
                message.addParam(ModelUtil.getModelName(model));

                message.addParam(ModelUtil.getIdValue(model));

                message.addParam(refName);

                String messageStr = MessageUtil.getMessage(message);
                e = new BusinessException(messageStr, t, sqlLogInfo.getSqlErrorCode());
                e.setError(message);
            }

        } catch (BusinessException e1) {
            e = e1;
        }
        return e;
    }

    private static String getRefenceName(String messages) {
        String ret = "";

        if (StringUtils.isBlank(messages)) {
            return ret;
        }

        ret = StringUtils.substring(messages, StringUtils.indexOf(messages, ".") + 1, StringUtils
                .indexOf(messages, ")"));

        ret = MessageUtil.getMessage(ret);

        return ret;
    }
}
