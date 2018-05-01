package org.hummer.core.aop.impl;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.hummer.core.util.HibernateUtil;
import org.hummer.core.util.Log4jUtils;


/**
 * iFOP Spacee for Developer Party
 *
 * @author jeff.zhou
 */
public class TransactionInterceptor extends Perl5DynamicMethodInterceptor {

    private static Logger log = Log4jUtils.getLogger(TransactionInterceptor.class);

    /**
     * read config from xml
     */
    public TransactionInterceptor() {
    }

    public Object invoke(MethodInvocation arg0) throws Throwable {
        if (log.isDebugEnabled()) {
            StringBuilder info = new StringBuilder();
            info.append("TransactionInterceptor:before ");
            info.append(arg0.getMethod().getName());
            info.append(" invoke start Transaction");
            log.debug(info);
        }
        HibernateUtil.beginTransaction();
        Object result = arg0.proceed();
        HibernateUtil.commitTransaction();

        //HibernateUtil.closeSession();
        if (log.isDebugEnabled()) {
            StringBuilder info1 = new StringBuilder();
            info1.append("TransactionInterceptor:after ");
            info1.append(arg0.getMethod().getName());
            info1.append(" invoke commit Transaction");
            log.debug(info1.toString());
        }
        return result;
    }
}
