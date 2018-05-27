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
//        StringBuffer info = new StringBuffer();
        String simpleName = targetClass.getSimpleName();
        String methodName = methodInvocation.getMethod().getName();

        log.info("Start transaction before [{}.{}] ", simpleName, methodName);
//        info.append("Start transaction before [");
//        info.append(targetClass.getSimpleName());
//        info.append(".");
//        info.append(methodInvocation.getMethod().getName());
//        info.append("]");
//        log.info(String.valueOf(info));

        HibernateUtil.beginTransaction();
        Object result = methodInvocation.proceed();
        HibernateUtil.commitTransaction();

        //HibernateUtil.closeSession();
        log.info("ommit transaction after [{}.{}]", simpleName, methodName);

//        if (log.isInfoEnabled()) {
//            StringBuffer info1 = new StringBuffer();
//            info1.append("Commit transaction after [");
//            info1.append(targetClass.getSimpleName());
//            info1.append(".");
//            info1.append(methodInvocation.getMethod().getName());
//            info1.append("]");
//            log.info(info1.toString());
//        }
        return result;
    }
}
