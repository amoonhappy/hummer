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
        Class targetClass = methodInvocation.getThis().getClass();
        String simpleName = targetClass.getSimpleName();
        String methodName = methodInvocation.getMethod().getName();
        Object result = null;
        log.info("Start transaction before [{}.{}] ", simpleName, methodName);
//        log.info("Method found:[{}]", method.getName());
//        log.info("Method found Annotation:[{}]", method.getAnnotations());
//        log.info("target Class found Annotation {}", targetClass.isAnnotationPresent(Transactional.class));
//        Annotation[] annotations = method.getAnnotations();
        //get Transaction parameters and if annotation is found then apply the strategy
//        if (annotations != null && annotations.length > 0) {
//            Transactional transactional = (Transactional) annotations[0];
//            Propagation propagation = transactional.propagation();
//            Isolation isolation = transactional.isolation();
//            boolean readOnly = transactional.readOnly();
//            int timeout = transactional.timeout();
//
//            log.info("method [{}]'s Transactional parameters [0]: Propagation = [{}]", methodName, propagation);
//            log.info("method [{}]'s Transactional parameters [1]: Isolation = [{}]", methodName, isolation.value());
//            log.info("method [{}]'s Transactional parameters [2]: readOnly = [{}]", methodName, readOnly);
//            log.info("method [{}]'s Transactional parameters [3]: timeout = [{}]", methodName, timeout);
//            List<TransactionSynchronization> transactionSynchronizations = TransactionSynchronizationManager.getSynchronizations();
//            if (transactionSynchronizations != null && transactionSynchronizations.size() > 0) {
//                log.info("when exec method [{}] already in a transactions chain", methodName);
//                log.info("Register Transaction", methodName);
//                MybatisUtil.RegisterTransaction();
//                TransactionSynchronizationManager.setActualTransactionActive(true);
//            } else {
//                log.info("when exec method [{}] don't have Transactions", methodName);
//                TransactionSynchronizationManager.initSynchronization();
//                MybatisUtil.begin();
//                log.info("when exec method [{}] set the connections autocommit to false", methodName);
//                TransactionSynchronizationManager.setActualTransactionActive(true);
//            }
//            try {
//                //find whether the class is registered or not
//                result = methodInvocation.proceed();
//                transactionSynchronizations = TransactionSynchronizationManager.getSynchronizations();
//                for (TransactionSynchronization transactionSynchronization : transactionSynchronizations) {
//                    transactionSynchronization.beforeCommit(readOnly);
//                    transactionSynchronization.beforeCompletion();
//                }
        //set autocommit status
//            switch (propagation) {
//                case REQUIRED:
//                    break;
//                case SUPPORTS:
//                    break;
//                case MANDATORY:
//                    break;
//                case REQUIRES_NEW:
//                    break;
//                case NOT_SUPPORTED:
//                    break;
//                case NEVER:
//                    break;
//                case NESTED:
//                    break;
//            }
//            } catch (Exception e) {
//                log.error("exec method [{}.{}] failed! rollback transaction", simpleName, methodName, e);
//                log.info("params = [{}]", methodInvocation.getArguments());
//                MybatisUtil.getSession().rollback();
//            } finally {
//                MybatisUtil.closeSession();
//            }

//        } else {//apply default transaction strategy
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
        log.info("commit transaction after [{}.{}]", simpleName, methodName);
        //log.info("params = [{}]", methodInvocation.getArguments());


        return result;
    }
}
