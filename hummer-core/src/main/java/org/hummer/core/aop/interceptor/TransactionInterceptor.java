package org.hummer.core.aop.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.ibatis.session.SqlSession;
import org.hummer.core.aop.impl.Perl5DynamicMethodInterceptor;
import org.hummer.core.transaction.Transaction;
import org.hummer.core.transaction.TransactionManager;
import org.hummer.core.transaction.exceptions.IllegalTransactionStateException;
import org.hummer.core.transaction.exceptions.NoTransactionException;
import org.hummer.core.transaction.exceptions.TransactionSystemException;
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

    TransactionInterceptor() {
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Class targetClass = methodInvocation.getThis().getClass();
        String simpleName = targetClass.getSimpleName();
        Method method = methodInvocation.getMethod();
        String methodName = method.getName();
        Object result = null;
        SqlSession sqlSession = null;
        Transaction transaction;
        /*
         * register a new transaction
         */
        try {
            transaction = TransactionManager.RegisterTransaction(targetClass, methodInvocation.getMethod());
        } catch (NoTransactionException e) {
            log.error("No Transaction error [{}.{}]", simpleName, methodName, e);
            throw e;

        } catch (IllegalTransactionStateException e) {
            log.error("Illegal Transaction State [{}.{}]", simpleName, methodName, e);
            throw e;

        } catch (TransactionSystemException e) {
            log.error("Transaction System Error, Pls check Transaction Annotation and Config for [{}.{}]", simpleName, methodName, e);
            throw e;
        }
        //if the transaction is defined, start a transaction via getSession
        if (transaction != null) {
            try {
                log.debug("Starting transaction before [{}.{}]", simpleName, methodName);
                if (transaction.needNewTransaction()) {
                    sqlSession = MybatisUtil.getNewSession(false);
                    log.debug("Starting a transaction on a new SqlSession before [{}.{}]", simpleName, methodName);
                } else {
                    sqlSession = MybatisUtil.getSession();
                }
                //sqlSession.getConnection().setTransactionIsolation(transaction.getIsolationLevel());
                sqlSession.getConnection().setReadOnly(transaction.isReadonly());

                log.debug("Started transaction before [{}.{}]", simpleName, methodName);
                result = methodInvocation.proceed();
                log.debug("Committing transaction after [{}.{}]", simpleName, methodName);
                if (TransactionManager.existTransactionOrNot(targetClass, methodInvocation.getMethod())) {
                    //do nothing
                } else {
                    sqlSession.commit();
                }
                log.debug("Committed transaction after [{}.{}]", simpleName, methodName);
            } catch (Exception e) {
                log.error("Execute method [{}.{}] failed! Rolling back transaction", simpleName, methodName, e);
                if (TransactionManager.existTransactionOrNot(targetClass, methodInvocation.getMethod())) {
                    //do nothing
                } else {
                    assert sqlSession != null;
                    sqlSession.rollback();
                    log.debug("Rolled back transaction after [{}.{}]", simpleName, methodName);
                }
            } finally {
                // close session if it's not in a nested transaction
                if (TransactionManager.existTransactionOrNot(targetClass, methodInvocation.getMethod())) {
                    //do nothing
                } else {
                    if (transaction.needNewTransaction()) {
                        log.debug("Closing New SqlSession [{}.{}]", simpleName, methodName);
                        MybatisUtil.closeNewSession();
                        log.debug("Closed New SqlSession after [{}.{}]", simpleName, methodName);
                    } else {
                        log.debug("Closing SqlSession [{}.{}]", simpleName, methodName);
                        MybatisUtil.closeSession();
                        log.debug("Closed SqlSession after [{}.{}]", simpleName, methodName);
                    }
                    // must use this method to cleanup threadLocal
                    TransactionManager.cleanupsAfterCompletion();
                }
            }
        } else {
            //if no transaction defined, proceed the method directly
            result = methodInvocation.proceed();
        }
        return result;
    }
}