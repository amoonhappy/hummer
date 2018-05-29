package org.hummer.core.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

public class MybatisUtil {
    private static final ThreadLocal<SqlSession> threadLocal = new ThreadLocal<>();
    private static String CONFIG_FILE = "mybatis.xml";
    private static SqlSessionFactory sessionFactory;

    private MybatisUtil() {
    }

    public static SqlSession getSession() {
        SqlSession session = threadLocal.get();

        if (session == null) {
            if (sessionFactory == null) {
                buildSessionFactory();
            }
            session = (sessionFactory != null) ? sessionFactory.openSession() : null;
            threadLocal.set(session);
        }

        return session;
    }

    public static void buildSessionFactory() {
        try {
            Reader reader = Resources.getResourceAsReader(CONFIG_FILE);
            //https://blog.csdn.net/zjf280441589/article/details/50760942
            //TODO: datasource injection
            sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeSession() {
        SqlSession session = threadLocal.get();
        threadLocal.set(null);

        if (session != null) {
            session.close();
        }
    }

    public static void closeSessionFactory() {
        sessionFactory = null;
    }
}