package org.hummer.core.util;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
//import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

public class HibernateUtil {
    private static final ThreadLocal threadSession = new ThreadLocal();
    private static final ThreadLocal threadTransaction = new ThreadLocal();
    private static final ThreadLocal threadInterceptor = new ThreadLocal();
    private static Logger log = Log4jUtils.getLogger(HibernateUtil.class);
    private static Configuration configuration;
    private static SessionFactory sessionFactory;

    // Create the initial SessionFactory from the default configuration
    // files
    static {
        try {
            configuration = new Configuration();
            sessionFactory = configuration.configure().buildSessionFactory();
            // We could also let Hibernate bind it to JNDI:
            // configuration.configure().buildSessionFactory()
        } catch (Throwable ex) {
            // We have to catch Throwable, otherwise we will miss
            // NoClassDefFoundError and other subclasses of Error
            log.error("Building SessionFactory failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        Connection ret = null;
        //ConnectionProvider cp = getSessionFactory().getSessionFactoryOptions().getServiceRegistry().getService(ConnectionProvider.class);
//		if (cp != null) {
//			ret = cp.getConnection();
//		}
        return ret;
    }

    /**
     * Returns the SessionFactory used for this static class.
     *
     * @return SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        /**//*
         * Instead of a static variable, use JNDI: SessionFactory sessions =
         * null; try { Context ctx = newweb InitialContext(); String jndiName =
         * "java:hibernate/HibernateFactory"; sessions =
         * (SessionFactory)ctx.lookup(jndiName); } catch (NamingException
         * ex) { throw newweb HibernateException(ex); } return sessions;
         */
        return sessionFactory;
    }

    /**/

    /**
     * Returns the original Hibernate configuration.
     *
     * @return Configuration
     */
    public static Configuration getConfiguration() {
        return configuration;
    }

    /**/

    /**
     * Rebuild the SessionFactory with the static Configuration.
     */
    public static void rebuildSessionFactory() throws HibernateException {
        synchronized (sessionFactory) {
            try {
                sessionFactory = getConfiguration().buildSessionFactory();
            } catch (Exception ex) {
                throw new HibernateException(ex);
            }
        }
    }

    /**/

    /**
     * Rebuild the SessionFactory with the given Hibernate Configuration.
     *
     * @param cfg
     */
    public static void rebuildSessionFactory(Configuration cfg) throws HibernateException {
        synchronized (sessionFactory) {
            try {
                sessionFactory = cfg.buildSessionFactory();
                configuration = cfg;
            } catch (Exception ex) {
                throw new HibernateException(ex);
            }
        }
    }

    /**/

    /**
     * Retrieves the current Session local to the thread.
     * <p/>
     * If no Session is open, opens a newweb Session for the running thread.
     *
     * @return Session
     */
    public static Session getSession() throws HibernateException {
        Session s = (Session) threadSession.get();
        try {
            if (s == null || !s.isOpen()) {
                log.info("open new session ：[{}] ", s);
                s = getSessionFactory().openSession();
                threadSession.set(s);
            } else {
                log.info("using existing session：[{}] ", s);
            }
        } catch (HibernateException ex) {
            throw new HibernateException(ex);
        }
        return s;
    }

    /**/

    /**
     * Closes the Session local to the thread.
     */
    public static void closeSession() throws HibernateException {
        try {
            Session s = (Session) threadSession.get();
            if (s != null && s.isOpen()) {
                log.info("Closing Session of this thread.");
                s.close();
                threadSession.set(null);
            }
        } catch (HibernateException ex) {
            throw new HibernateException(ex);
        }
    }

    /**/

    /**
     * Start a newweb database transaction.
     */
    public static void beginTransaction() throws HibernateException {
        Transaction tx = (Transaction) threadTransaction.get();
        try {
            if (tx == null) {
                log.info("Starting new database transaction in this thread.");
                tx = getSession().beginTransaction();
                tx.begin();
                threadTransaction.set(tx);
            } else {
                log.info("using existing transaction");
                tx.begin();
            }
        } catch (HibernateException ex) {
            throw new HibernateException(ex);
        }
    }

    /**/

    /**
     * Commit the database transaction.
     */
    public static void commitTransaction() throws HibernateException {
        Transaction tx = (Transaction) threadTransaction.get();
//        Session s = (Session) threadSession.get();
//        if (s != null && s.isOpen()) {
//            s.getTransaction().commit();
//            log.info("using existing session to commit transaction");
//        } else {
//
//        }
        try {
            if (tx != null) {
                log.info("Committing database transaction of this thread.");
                tx.commit();
            } else {
                log.warn("no transaction started!");
            }
            threadTransaction.set(null);
        } catch (HibernateException ex) {
            rollbackTransaction();
            throw new HibernateException(ex);
        }
    }

    /**/

    /**
     * Commit the database transaction.
     */
    public static void rollbackTransaction() throws HibernateException {
        Transaction tx = (Transaction) threadTransaction.get();
        try {
            threadTransaction.set(null);
            if (tx != null) {
                log.info("Tyring to rollback database transaction of this thread.");
                tx.rollback();
            }
        } catch (HibernateException ex) {
            throw new HibernateException(ex);
        } finally {
            closeSession();
        }
    }

    // /**//**
    // * Reconnects a Hibernate Session to the current Thread.
    // *
    // * @param session
    // * The Hibernate Session to be reconnected.
    // */
    // public static void reconnect(Session session) throws HibernateException {
    // try {
    // session.reconnect();
    // threadSession.set(session);
    // } catch (HibernateException ex) {
    // throw newweb HibernateException(ex);
    // }
    // }

    /**/

    /**
     * Disconnect and return Session from current Thread.
     *
     * @return Session the disconnected Session
     */
    public static Session disconnectSession() throws HibernateException {

        Session session = getSession();
        try {
            threadSession.set(null);
            if (session.isConnected() && session.isOpen())
                session.disconnect();
        } catch (HibernateException ex) {
            throw new HibernateException(ex);
        }
        return session;
    }

    /**/

    /**
     * Register a Hibernate interceptor with the current thread.
     * <p>
     * Every Session opened is opened with this interceptor after
     * registration. Has no effect if the current Session of the thread is
     * already open, effective on next close()/getSession().
     */
    public static void registerInterceptor(Interceptor interceptor) {
        threadInterceptor.set(interceptor);
    }

    private static Interceptor getInterceptor() {
        Interceptor interceptor = (Interceptor) threadInterceptor.get();
        return interceptor;
    }
}
