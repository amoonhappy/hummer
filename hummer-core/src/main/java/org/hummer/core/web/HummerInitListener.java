package org.hummer.core.web;

import org.hummer.core.container.HummerContainer;
import org.hummer.core.ioc.annotation.HummerPostAutowired;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;
import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("all")
public class HummerInitListener extends ContextLoaderListener {
    private static final Logger log = Log4jUtils.getLogger(HummerInitListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        ApplicationContext context = getCurrentWebApplicationContext();
        HummerContainer hummerContainer = HummerContainer.getInstance();
        //Bond SpringContext with Hummer Container
        hummerContainer.bondWithSpringContext(context);
        String[] postSpringBeanList;
        if (context != null) {
            postSpringBeanList = context.getBeanNamesForAnnotation(HummerPostAutowired.class);
            //Spring MVC beans injections for Hummer Beans
            if (postSpringBeanList.length > 0) {
                Map<String, Object> postSpringBeanMap = context.getBeansWithAnnotation(HummerPostAutowired.class);
                hummerContainer.addPostAutowireSpringBeanNameList(Arrays.asList(postSpringBeanList));
                hummerContainer.addPostAutowireSpringBeanMap(postSpringBeanMap);
            }
        } else {
            log.error("Initiate Spring Context Error!");
        }

    }
}
