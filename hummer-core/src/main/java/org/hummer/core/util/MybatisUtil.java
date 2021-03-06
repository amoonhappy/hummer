package org.hummer.core.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.hummer.core.container.HummerContainer;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;

@SuppressWarnings("all")
public class MybatisUtil {
    private static final ThreadLocal<SqlSession> threadLocal = new ThreadLocal<>();
    private static ThreadLocal<SqlSession> newThreadLocal = new ThreadLocal<>();
    //    private static final ThreadLocal<SqlSession> newThreadLocal = new ThreadLocal<>();
//    private static final ThreadLocal<SqlSession> newThreadLocal = new ThreadLocal<>();
    private static String CONFIG_FILE = "mybatis.xml";
    private static SqlSessionFactory sessionFactory;
    private static final Logger log = Log4jUtils.getLogger(MybatisUtil.class);

    private MybatisUtil() {
    }

    //        public static SqlSession RegisterTransaction() {
//        SqlSession session = threadLocal.get();
//
//        if (session == null) {
//            if (sessionFactory == null) {
//                buildSessionFactory();
//            }
//            session = (sessionFactory != null) ? sessionFactory.openSession() : null;
//            threadLocal.set(session);
//        }
//        ExecutorType executorType = ExecutorType.SIMPLE;
//        //RegisterSession for Transaction Manager
//        registerSession(session, executorType, sessionFactory);
//
//        return session;
//    }
//
//    public static void begin() throws SQLException {
//        RegisterTransaction();
//        //sqlSession.getConnection().setAutoCommit(false);
//    }

    public static synchronized void closeNewSession() {
        SqlSession session = newThreadLocal.get();
        newThreadLocal.set(null);

        if (session != null) {
            session.close();
        }
    }

    public static synchronized SqlSession getNewSession(boolean createNew) {
        if (createNew) {
            ThreadLocal<SqlSession> temp = new ThreadLocal<>();
            newThreadLocal = temp;
        } else {

        }
        SqlSession session = newThreadLocal.get();

        if (session == null) {
            if (sessionFactory == null) {
                buildSessionFactory();
            }
            session = new SqlSessionTemplate(sessionFactory);
            newThreadLocal.set(session);
        }

        return session;
    }

    public static synchronized SqlSession getSession() {
        SqlSession session = threadLocal.get();

        if (session == null) {
            if (sessionFactory == null) {
                buildSessionFactory();
            }
            session = new SqlSessionTemplate(sessionFactory);
            threadLocal.set(session);
        }
        return session;
    }

    public static Connection getConnection() {
        return getSession().getConnection();
    }

    public static synchronized void buildSessionFactory() {
        try {
//            Reader reader = Resources.getResourceAsReader(CONFIG_FILE);
            //https://blog.csdn.net/zjf280441589/article/details/50760942
//            sessionFactory = new SqlSessionFactoryBuilder().build(reader);\
            HummerContainer hummerContainer = HummerContainer.getInstance();
//            ApplicationContext ctx = HummerContainer.getInstance().ge.get
            try {
                sessionFactory = (SqlSessionFactory) hummerContainer.getBeanFromSpring("sqlSessionFactory");
            } catch (NoSuchBeanDefinitionException e) {
                log.warn("Get sqlSessionFactory from Spring failed, try to initiate from Hummer container");
            }
//            sessionFactory = SpringContextUtils.getBean("sqlSessionFactory", SqlSessionFactory.class);
            if (sessionFactory == null) {
                Reader reader = Resources.getResourceAsReader(CONFIG_FILE);
                sessionFactory = new SqlSessionFactoryBuilder().build(reader);
            }
        } catch (IOException e) {
            log.error("Error occurs when initiating Mybatis Session Factory!", e);
        }
    }

    public static synchronized void closeSession() {
        SqlSession session = threadLocal.get();
        threadLocal.set(null);

        if (session != null) {
            session.close();
        }
    }

//    public static void closeSessionFactory() {
//        sessionFactory = null;
//    }

    //for transactions

//    private static void registerSession(SqlSession sqlSession, ExecutorType executorType, SqlSessionFactory sqlSessionFactory) {
//        if (TransactionSynchronizationManager.isSynchronizationActive()) {
//            log.info("Registering transaction synchronization for SqlSession [{}]", sqlSession);
//            TransactionSynchronizationManager.registerSynchronization(new MybatisUtil.SqlSessionSynchronization(sqlSession, sessionFactory));
//        } else {
//            log.info("SqlSession [{}] was not registered for synchronization because synchronization is not active", sqlSession);
//        }
//    }
//
//    private static final class SqlSessionSynchronization implements TransactionSynchronization {
//        SqlSession sqlSession;
//        private final SqlSessionFactory sessionFactory;
//        private boolean holderActive = true;

//        public SqlSessionSynchronization(SqlSession sqlSession, SqlSessionFactory sessionFactory) {
//            Assert.notNull(sqlSession, "Parameter 'sqlSession' must be not null");
//            Assert.notNull(sessionFactory, "Parameter 'sessionFactory' must be not null");
//            this.sqlSession = sqlSession;
//            this.sessionFactory = sessionFactory;
//        }

//        public int getOrder() {
//            return 999;
//        }

//        @Override
//        public void suspend() {
//            if (this.holderActive) {
//                MybatisUtil.log.info("Transaction synchronization suspending SqlSession [{}]", sqlSession);
//            }

    //   TransactionSynchronizationManager.unbindResource(this.sessionFactory);
//        }

//        @Override
//        public void resume() {
//        if (this.holderActive) {
//            if (SqlSessionUtils.LOGGER.isDebugEnabled()) {
//                SqlSessionUtils.LOGGER.debug("Transaction synchronization resuming SqlSession [" + this.holder.getSqlSession() + "]");
//            }
//
//            TransactionSynchronizationManager.bindResource(this.sessionFactory, this.holder);
//        }

//        }

//        @Override
//        public void flush() {

//        }

//        public void beforeCommit(boolean readOnly) {
//            if (TransactionSynchronizationManager.isActualTransactionActive()) {
//                try {
//                    MybatisUtil.log.info("Transaction synchronization committing SqlSession [{}]", getSession());
//
//                    getSession().commit();
//                } catch (PersistenceException var4) {
//                    throw var4;
//                }
//            }
//
//        }
//
//        public void beforeCompletion() {
//        if (!this.holder.isOpen()) {
//            if (SqlSessionUtils.LOGGER.isDebugEnabled()) {
//                SqlSessionUtils.LOGGER.debug("Transaction synchronization deregistering SqlSession [" + this.holder.getSqlSession() + "]");
//            }
//
//            TransactionSynchronizationManager.unbindResource(this.sessionFactory);
//            this.holderActive = false;
//            if (SqlSessionUtils.LOGGER.isDebugEnabled()) {
//                SqlSessionUtils.LOGGER.debug("Transaction synchronization closing SqlSession [" + this.holder.getSqlSession() + "]");
//            }

    //this.holder.getSqlSession().close();
//        }

//            getSession().close();
//
//        }

//        @Override
//        public void afterCommit() {
//
//        }

//        public void afterCompletion(int status) {
//        if (this.holderActive) {
//            if (SqlSessionUtils.LOGGER.isDebugEnabled()) {
//                SqlSessionUtils.LOGGER.debug("Transaction synchronization deregistering SqlSession [" + this.holder.getSqlSession() + "]");
//            }
//
//            TransactionSynchronizationManager.unbindResourceIfPossible(this.sessionFactory);
//            this.holderActive = false;
//            if (SqlSessionUtils.LOGGER.isDebugEnabled()) {
//                SqlSessionUtils.LOGGER.debug("Transaction synchronization closing SqlSession [" + this.holder.getSqlSession() + "]");
//            }
//
//            this.holder.getSqlSession().close();
//        }
//
//        this.holder.reset();
//        }
//    }

}