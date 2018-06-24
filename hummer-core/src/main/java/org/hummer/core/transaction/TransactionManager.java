package org.hummer.core.transaction;

import org.hummer.core.transaction.annotation.Isolation;
import org.hummer.core.transaction.annotation.Propagation;
import org.hummer.core.transaction.annotation.Transactional;
import org.hummer.core.transaction.exceptions.IllegalTransactionStateException;
import org.hummer.core.transaction.exceptions.NoTransactionException;
import org.hummer.core.transaction.exceptions.TransactionSystemException;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;

import java.lang.reflect.Method;

public abstract class TransactionManager {
    private static final ThreadLocal<Class> transactionExist = new ThreadLocal<>();
    private static final Logger log = Log4jUtils.getLogger(TransactionManager.class);


    public static Transaction RegisterTransaction(Class clazz, Method method) throws NoTransactionException, IllegalTransactionStateException, TransactionSystemException {
        Transactional transactional = method.getAnnotation(Transactional.class);
        if (transactional != null) {
            Transaction transaction = new Transaction();

            Propagation propagation = transactional.propagation();
            Isolation isolation = transactional.isolation();
            boolean readOnly = transactional.readOnly();
            int timeout = transactional.timeout();
            transaction.setIsolationlevel(isolation);
            transaction.setReadonly(readOnly);
            transaction.setTimeout(timeout);
            transaction.setPropagation(propagation);
        /*-------------------------------------------------------------
        |                      环境已经存在事务
        |
        | PROPAGATION_REQUIRED     ：加入已有事务（不处理）
        | RROPAGATION_REQUIRES_NEW ：独立事务（挂起当前事务，开启新事务）
        | PROPAGATION_NESTED       ：嵌套事务（设置保存点）
        | PROPAGATION_SUPPORTS     ：跟随环境（不处理）
        | PROPAGATION_NOT_SUPPORTED：非事务方式（仅挂起当前事务）
        | PROPAGATION_NEVER        ：排除事务（异常）
        | PROPAGATION_MANDATORY    ：强制要求事务（不处理）
        ===============================================================*/
        /*-------------------------------------------------------------
        |                      环境不经存在事务
        |
        | PROPAGATION_REQUIRED     ：加入已有事务（开启新事务）
        | RROPAGATION_REQUIRES_NEW ：独立事务（开启新事务）
        | PROPAGATION_NESTED       ：嵌套事务（开启新事务）
        | PROPAGATION_SUPPORTS     ：跟随环境（不处理）
        | PROPAGATION_NOT_SUPPORTED：非事务方式（不处理）
        | PROPAGATION_NEVER        ：排除事务（不处理）
        | PROPAGATION_MANDATORY    ：强制要求事务（异常）
        ===============================================================*/
            boolean existTransation = transactionExist.get() != null;

            switch (propagation) {
                case REQUIRED:
                    if (existTransation) {
                        //Do nothing
                    } else {
                        //Start new transaction
                        transactionExist.set(clazz);
                    }
                    break;
                case SUPPORTS:
                    if (existTransation) {
                        //Do nothing
                    } else {
                        //Do nothing
                    }
                    break;
                case MANDATORY:
                    if (existTransation) {
                        //Do nothing
                    } else {
                        //Throw exception
                        throw new NoTransactionException("Transaction defined as MANDATORY, but no transaction is registered previously");
                    }
                    break;
                case REQUIRES_NEW:
                    if (existTransation) {
                        //replace existing transaction
                        transactionExist.set(clazz);
                    } else {
                        //Start new transaction
                        transactionExist.set(clazz);
                    }
                    break;
                case NOT_SUPPORTED:
                    if (existTransation) {
                        //clear existing transaction
                        transactionExist.set(null);
                    } else {
                        //Do nothing
                    }
                    break;
                case NEVER:
                    if (existTransation) {
                        log.error("Transaction Annotation is not properly configured!");
                        throw new IllegalTransactionStateException("Transaction defined as NEVER, but transaction is registered previously");
                    } else {
                        //Do nothing
                    }
                    break;
                case NESTED:
                    if (transactionExist.get() != null) {
                        //Do nothing
                    } else {
                        transactionExist.set(clazz);
                    }
                    break;
            }
            return transaction;
        } else {
            log.error("Transaction Annotation is not properly configured!");
            throw new TransactionSystemException("Transaction Annotation is not properly configured!");
        }
    }


    public static boolean existTransactionOrNot(Class clazz, Method method) {
        if (clazz == null || method == null) {
            return false;
        } else {
            //if a clazz is already registered, return true
            Class existedClazz = transactionExist.get();
            if (existedClazz != null && !clazz.equals(existedClazz)) {
                return true;
            }
        }
        return false;
    }

    public static void clearupAfterCompletion(Class targetClass) {
        transactionExist.set(null);
    }

//    public static boolean doCommit(Class clazz, Method method, Transaction transaction, SqlSession sqlSession) {
//        boolean existTransaction = existTransactionOrNot(clazz, method);
//        boolean requiredNew = (Propagation.REQUIRES_NEW == transaction.getPropagation());
//        boolean ret = true;
//        if (sqlSession != null) {
//            MybatisUtil.getSession();
//        }
//        return ret;
//    }
}
