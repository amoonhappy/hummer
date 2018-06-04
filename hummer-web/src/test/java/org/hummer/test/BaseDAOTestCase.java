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
import org.hummer.core.util.CollectionUtil;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;

import java.util.*;

public class BaseDAOTestCase extends TestCase {

    private final Logger log = Log4jUtils.getLogger(BaseDAOTestCase.class);

    protected ResourceBundle rb;

    // protected String[] getConfigLocations() {
    // setAutowireMode(AUTOWIRE_BY_NAME);
    // return newweb String[] {
    // "classpath*:/**/dao/hibernate3/applicationContext-*.xml",
    // "classpath*:applicationContext-*.xml" };
    // }

    public BaseDAOTestCase() {
        // Since a ResourceBundle is not required for each class, just
        // do a simple check to see if one exists
        String className = this.getClass().getName();

        try {
            rb = ResourceBundle.getBundle(className);
        } catch (MissingResourceException mre) {
            // log.debug(this, "", "No resource bundle found for: " +
            // className);
            // log.debug("No resource bundle found for: " + className, mre);
        }
    }

    @Override
    protected void setUp() throws Exception {
        // TODO Auto-generated method stub
        super.setUp();
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
