package org.hummer.core.aop.impl;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.ibatis.session.SqlSession;
import org.hummer.core.transaction.Transaction;
import org.hummer.core.transaction.TransactionManager;
import org.hummer.core.util.Log4jUtils;
import org.hummer.core.util.MybatisUtil;
import org.slf4j.Logger;

import java.lang.reflect.Method;


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
        Class targetClass = methodInvocation.getThis().getClass();
        String simpleName = targetClass.getSimpleName();
        Method method = methodInvocation.getMethod();
        String methodName = method.getName();
        Object result = null;
        SqlSession sqlSession = null;

        Transaction transaction = TransactionManager.RegisterTransaction(targetClass, methodInvocation.getMethod());
        try {
            log.info("Starting transaction before [{}.{}]", simpleName, methodName);

            if (transaction.needNewTransaction()) {
                sqlSession = MybatisUtil.getNewSession(false);
            } else {
                sqlSession = MybatisUtil.getSession();
            }
            sqlSession.getConnection().setTransactionIsolation(transaction.getIsolationLevel());
            sqlSession.getConnection().setReadOnly(transaction.isReadonly());
            log.info("Started transaction before [{}.{}]", simpleName, methodName);
            result = methodInvocation.proceed();
            log.info("Committing transaction after [{}.{}]", simpleName, methodName);
            if (TransactionManager.existTransactionOrNot(targetClass, methodInvocation.getMethod())) {
                //do nothing
            } else {
                sqlSession.commit();
            }
            log.info("Committed transaction after [{}.{}]", simpleName, methodName);
        } catch (Exception e) {
            log.error("Execute method [{}.{}] failed! Rolling back transaction", simpleName, methodName, e);
            log.info("params = [{}]", methodInvocation.getArguments());
            if (TransactionManager.existTransactionOrNot(targetClass, methodInvocation.getMethod())) {
                //do nothing
            } else {
                sqlSession.rollback();
            }
            log.info("Rolled back transaction after [{}.{}]", simpleName, methodName);
        } finally {
            log.info("Closing SqlSession [{}.{}]", simpleName, methodName);
            if (TransactionManager.existTransactionOrNot(targetClass, methodInvocation.getMethod())) {
                //do nothing
            } else {
                //MybatisUtil.closeSession();
                sqlSession.close();
            }
            TransactionManager.clearupAfterCompletion(targetClass);
            log.info("Closed transaction after [{}.{}]", simpleName, methodName);
        }

        return result;
    }
}
