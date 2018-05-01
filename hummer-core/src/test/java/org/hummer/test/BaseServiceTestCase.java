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
import org.acegisecurity.userdetails.User;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hummer.core.context.impl.AuthContext;
import org.hummer.core.context.impl.ContextHolder;
import org.hummer.core.util.CollectionUtil;

import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class BaseServiceTestCase extends TestCase {
    protected final Log log = LogFactory.getLog(getClass());

    protected ResourceBundle rb;

    public BaseServiceTestCase() {
        // Since a ResourceBundle is not required for each class, just
        // do a simple check to see if one exists
        String className = this.getClass().getName();

        try {
            rb = ResourceBundle.getBundle(className);
        } catch (MissingResourceException mre) {
            // log.warn("No resource bundle found for: " + className);
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // set User Authentication Information
        UserDetails authUser = new User("", "", false, false, false, false, null);
        AuthContext authContext = new AuthContext();
        authContext.setAuthUser(authUser);

        ContextHolder.setAuthContext(authContext);
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
        Map<String, String> map = CollectionUtil.convertBundleToMap(rb);

        BeanUtils.copyProperties(obj, map);

        return obj;
    }

    /**
     * Utility method to populate a javabean-style object with values from a
     * Properties file For example: user = (User)populate(user,1); will copy the
     * properties from UserServiceTest1.properties
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
