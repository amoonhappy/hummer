package org.hummer.core.web;

import org.hummer.core.container.HummerContainer;
import org.hummer.core.ioc.annotation.HummerPostAutowired;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("all")
public class HummerDispatcherServlet extends DispatcherServlet {
    private static final Logger log = Log4jUtils.getLogger(HummerDispatcherServlet.class);

    /**
     * Initialize the strategy objects that this servlet uses.
     * <p>May be overridden in subclasses in order to initialize further strategy objects.
     *
     * @param context
     */
    @Override
    protected void initStrategies(ApplicationContext context) {
        super.initStrategies(context);
        if (context != null) {
            String[] postSpringBeanList = context.getBeanNamesForAnnotation(HummerPostAutowired.class);
            //Spring MVC beans injections for Hummer Beans
            if (postSpringBeanList != null && postSpringBeanList.length > 0) {
                Map<String, Object> postSpringBeanMap = context.getBeansWithAnnotation(HummerPostAutowired.class);
                HummerContainer hummerContainer = HummerContainer.getInstance();
                hummerContainer.addPostAutowireSpringMVCBeanNameList(Arrays.asList(postSpringBeanList));
                hummerContainer.addPostAutowireSpringMVCBeanMap(postSpringBeanMap);
                hummerContainer.postAutowireSpringBeans();
            }
        } else {
            log.error("Initiate Spring MVC Context Error!");
        }
    }
}
