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

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Class targetClass = ((CglibMethodInvocation) methodInvocation).getTargetClass();
        if (log.isInfoEnabled()) {

            StringBuffer info = new StringBuffer();
            info.append("Start transaction before [");
            info.append(targetClass.getSimpleName());
            info.append(".");
            info.append(methodInvocation.getMethod().getName());
            info.append("]");
            log.info(info);
        }
        HibernateUtil.beginTransaction();
        Object result = methodInvocation.proceed();
        HibernateUtil.commitTransaction();

        //HibernateUtil.closeSession();
        if (log.isInfoEnabled()) {
            StringBuffer info1 = new StringBuffer();
            info1.append("Commit transaction after [");
            info1.append(targetClass.getSimpleName());
            info1.append(".");
            info1.append(methodInvocation.getMethod().getName());
            info1.append("]");
            log.info(info1.toString());
        }
        return result;
    }
}
