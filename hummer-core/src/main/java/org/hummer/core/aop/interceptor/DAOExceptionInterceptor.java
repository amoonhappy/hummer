/*
  <p>Open Source Architecture Project -- Hummer            </p>
  <p>Class Description                                     </p>
  <p>                                                      </p>
  <p>                                                      </p>
  <p>Change History                                        </p>
  <p>Author    Date      Description                       </p>
  <p>                                                      </p>
  <p>                                                      </p>

  @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a> Date: 2005-12-6
 * @version 1.0
 */
package org.hummer.core.aop.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.hummer.core.constants.ExceptionConstant;
import org.hummer.core.exception.BusinessException;
import org.hummer.core.message.impl.SQLLogInfo;
import org.hummer.core.util.ExceptionUtil;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;

import java.sql.SQLException;

/**
 * @author jeff.zhou
 */
public class DAOExceptionInterceptor implements org.aopalliance.intercept.MethodInterceptor {
    private static final Logger log = Log4jUtils.getLogger(DAOExceptionInterceptor.class);

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object returnValue;

        try {
            returnValue = methodInvocation.proceed();
        } catch (Exception e) {
            if (!(e instanceof BusinessException)) {
                SQLException sqle = ExceptionUtil.getSQLException(e);

                if (sqle != null) {
                    SQLLogInfo sqlLogInfo = new SQLLogInfo();
                    sqlLogInfo.setArgs(methodInvocation.getArguments());
                    sqlLogInfo.setInvokerName(methodInvocation.getThis().getClass().getName() + "@"
                            + methodInvocation.getMethod().getName());
                    sqlLogInfo.setMessages(sqle.getMessage());
                    sqlLogInfo.setSqlErrorCode(sqle.getErrorCode());
                    sqlLogInfo.setSqlState(sqle.getSQLState());

                    log.error(sqlLogInfo.toString(), e);

                    Object[] params = methodInvocation.getArguments();
                    throw ExceptionUtil.getBusinessException(sqlLogInfo, params, sqle);
                } else {
                    log.error("Other DB Error", e);
                    throw ExceptionUtil.getBusinessException(ExceptionConstant.DB_OTHER_DATA_ACCESS_ERROR, e);
                }
            } else {
                throw e;
            }
        }

        return returnValue;
    }
}
