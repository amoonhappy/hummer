package org.hummer.openapi;

import org.hummer.core.container.impl.HummerContainer;
import org.hummer.core.container.intf.IBusinessServiceManager;
import org.hummer.core.container.intf.IHummerContainer;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;

public class HummerInitServlet extends ContextLoaderListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        IHummerContainer hummerContainer = HummerContainer.getInstance();
        IBusinessServiceManager bsvMgr = hummerContainer.getServiceManager();
        bsvMgr.getService("testService");
    }
}
