/**
 * <p>Open Source Architecture Project -- Hummer            </p>
 * <p>Class Description                                     </p>
 * <p>                                                      </p>
 * <p>                                                      </p>
 * <p>Change History                                        </p>
 * <p>Author    Date      Description                       </p>
 * <p>                                                      </p>
 * <p>                                                      </p>
 *
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a> Date: 2005-12-6
 * @version 1.0
 */
package org.hummer.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
//import org.springframework.mock.web.MockServletContext;
//import org.springframework.web.context.ContextLoader;
//import org.springframework.web.context.ContextLoaderListener;
//import org.springframework.web.context.WebApplicationContext;

public class BaseStrutsTestCase {
    protected transient final Log log = LogFactory.getLog(getClass());

    // protected User user = null;
    protected ResourceBundle rb = null;

//	protected WebApplicationContext ctx = null;

    public BaseStrutsTestCase(String name) {
        // super(name);

        String className = this.getClass().getName();

        try {
            rb = ResourceBundle.getBundle(className);
        } catch (MissingResourceException mre) {
            // log.warn("No resource bundle found for: " + className);
        }
    }

    protected void setUp() throws Exception {
        // super.setUp();

        // initialize Spring
//		MockServletContext sc = newweb MockServletContext("");
//		sc.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, "/WEB-INF/applicationContext*.xml");
//
//		ServletContextListener contextListener = newweb ContextLoaderListener();
//		ServletContextEvent event = newweb ServletContextEvent(sc);
//		contextListener.contextInitialized(event);

        // magic bridge to make StrutsTestCase aware of Spring's Context
        // getSession()
        // .getServletContext()
        // .setAttribute(
        // WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
        // sc
        // .getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE));

        // ctx = WebApplicationContextUtils
        // .getRequiredWebApplicationContext(getSession()
        // .getServletContext());

        // populate the userForm and place into session
        // TODO this is prepared for login and should be changed if the
        // TODO Acegi Security System is applied to our system
//		IBasicService basicService = (IBasicService) ctx.getBean("basicService");
        // User user = newweb User();
        // user.setFirstname("test");

//		ReturnValue returnValue = basicService.search(null);
//		Pager pager = (Pager) returnValue.getResult();

//		if (pager.getRowCount() == 1) {
        // getSession().setAttribute("",
        // pager.getResult().iterator().next());
//		}
    }
}
