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

import junit.framework.TestCase;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hummer.core.util.CollectionUtil;

import java.util.*;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BaseServiceDBTest extends TestCase {
//	protected final static ApplicationContext ctx;

    // This static block ensures that Spring's BeanFactory is only loaded
    // once for all tests
    static {
        // the dao.type is written to the database.properties file
        // in properties.xml
        // ResourceBundle db = ResourceBundle.getBundle("database");
        String daoType = "hibernate"; // db.getString("dao.type");
        String[] paths = {"applicationContext-resources.xml", "applicationContext-service.xml",
                "applicationContext-" + daoType + ".xml"};
//		ctx = newweb ClassPathXmlApplicationContext(paths);
    }

    protected final Log log = LogFactory.getLog(getClass());

    protected ResourceBundle rb;

    public BaseServiceDBTest() {
        // Since a ResourceBundle is not required for each class, just
        // do a simple check to see if one exists
        String className = this.getClass().getName();

        try {
            rb = ResourceBundle.getBundle(className);
        } catch (MissingResourceException mre) {
            // log.warn("No resource bundle found for: " + className);
        }
    }

    /**
     * Utility method to populate a javabean-style object with values from a
     * Properties file
     *
     * @param obj
     * @return
     * @throws Exception
     */
    protected Object populate(Object obj) throws Exception {
        // loop through all the beans methods and set its properties from
        // its .properties file
        Map<String, String> map = new HashMap<String, String>();

        for (Enumeration<String> keys = rb.getKeys(); keys.hasMoreElements(); ) {
            String key = keys.nextElement();
            map.put(key, rb.getString(key));
        }

        BeanUtils.copyProperties(obj, map);

        return obj;
    }

    /**
     * Utility method to populate a javabean-style object with values from a
     * Properties file For example: user = (User)populate(user,1); will copy the
     * properties from UserDAOTest1.properties
     *
     * @param obj
     * @return
     * @throws Exception
     */
    protected Object populate(Object obj, int i) throws Exception {
        // Since a ResourceBundle is not required for each class, just
        // do a simple check to see if one exists
        String className = this.getClass().getName();

        try {
            rb = ResourceBundle.getBundle(className + i);
        } catch (MissingResourceException mre) {
            // log.warn("No resource bundle found for: " + className);
        }

        // loop through all the beans methods and set its properties from
        // its .properties file
        Map<String, String> map = CollectionUtil.convertBundleToMap(rb);
        BeanUtils.copyProperties(obj, map);

        return obj;
    }
}
