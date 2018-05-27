package org.hummer.core.aop.impl;

import org.aopalliance.intercept.MethodInvocation;
import org.hummer.core.util.HibernateUtil;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;


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
        String simpleName = targetClass.getSimpleName();
        String methodName = methodInvocation.getMethod().getName();

        log.info("Start transaction before [{}.{}] ", simpleName, methodName);

        HibernateUtil.beginTransaction();
        Object result = methodInvocation.proceed();
        HibernateUtil.commitTransaction();

        //HibernateUtil.closeSession();
        log.info("commit transaction after [{}.{}]", simpleName, methodName);

        return result;
    }
}
