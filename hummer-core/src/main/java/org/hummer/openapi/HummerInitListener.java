package org.hummer.openapi;

import org.hummer.core.container.impl.HummerContainer;
import org.hummer.core.container.intf.IBusinessServiceManager;
import org.hummer.core.container.intf.IHummerContainer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;

public class HummerInitListener extends ContextLoaderListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        super.loadParentContext(event.getServletContext());
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        ctx.getBean("redisTemplate");
        IHummerContainer hummerContainer = HummerContainer.getInstance();
        IBusinessServiceManager bsvMgr = hummerContainer.getServiceManager();
        hummerContainer.bondWithSpringContext(ctx);
        bsvMgr.getService("testService");
    }
}
