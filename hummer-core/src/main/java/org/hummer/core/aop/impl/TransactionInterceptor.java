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
        StringBuilder info = new StringBuilder();
        StringBuilder info1 = new StringBuilder();
        info.append("TransactionInterceptor:before ");
        info.append(arg0.getMethod().getName());
        info.append(" invoke");
        info.append(" start Transaction");
        log.info(info.toString());
        //if (log.isDebugEnabled()) {
        //	log.debug("TransactionInterceptor:before " + arg0.getMethod().getName() + " invoke");

        //	log.debug("start Transaction");
        //}

        HibernateUtil.beginTransaction();
        Object result = arg0.proceed();
        HibernateUtil.commitTransaction();
        //HibernateUtil.closeSession();

        info1.append("TransactionInterceptor:after ");
        info1.append(arg0.getMethod().getName());
        info1.append(" invoke");
        info1.append(" commit Transaction");
        log.info(info1.toString());
        //if (log.isDebugEnabled()) {
        //	log.debug("TransactionInterceptor:after " + arg0.getMethod().getName() + " invoke");
        //	log.debug("commit Transaction");
        //}
        //log.info(info.toString());
        return result;
    }
}
