package org.hummer.core.aop.impl;

import org.aopalliance.intercept.MethodInvocation;
import org.hummer.core.util.Log4jUtils;
import org.hummer.core.util.MybatisUtil;
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
        Object result = null;

        log.info("Start transaction before [{}.{}] ", simpleName, methodName);
        try {
            MybatisUtil.getSession();
            result = methodInvocation.proceed();
            MybatisUtil.getSession().commit();
        } catch (Exception e) {
            log.error("exec method [{}.{}] failed! rollback transaction", simpleName, methodName, e);
            log.info("params = [{}]", methodInvocation.getArguments());
            MybatisUtil.getSession().rollback();
        } finally {
            MybatisUtil.closeSession();
        }
        //HibernateUtil.beginTransaction();
        //HibernateUtil.commitTransaction();

        //HibernateUtil.closeSession();
        log.info("commit transaction after [{}.{}]", simpleName, methodName);
        log.info("params = [{}]", methodInvocation.getArguments());


        return result;
    }
}
