package org.hummer.test;

import junit.framework.TestCase;
import org.hummer.core.container.impl.HummerContainer;
import org.hummer.core.container.intf.IBusinessServiceManager;
import org.hummer.core.container.intf.IHummerContainer;
import org.hummer.core.util.Assert;
import org.hummer.newweb.service.user.intf.ITestService;

import java.util.List;

public class RedisCacheAopTest extends TestCase {
    //private static Logger log = Log4jUtils.getLogger(RedisCacheAopTest.class);
//    private String file = "hummer-cfg-main.properties";
//    private String file1 = "hummer-web-cfg-aop.xml";

    public void testGetCore() {

        try {
            /**
             * Redis Cache Interceptor 测试
             */
            IHummerContainer iHummerContainer = HummerContainer.getInstance();
            IBusinessServiceManager ibsv = iHummerContainer.getServiceManager();
            ITestService testService = (ITestService) ibsv.getService("testService");
//            ITest1Service test1Service = (ITest1Service) ibsv.getService("test1Service");
//            IUser temp = new User();
//            temp.setFirstName("test for redis cache annotation");
//            test1Service.saveUser(temp);
//            IUser temp = new User();
//            temp.setFirstName("test for redis cache annotation");
//            testService.insertUser(temp);
            List result = (List) testService.getAllUsers();
            Assert.notNull(result, "Result is Null!");
            System.out.println("returned size:[" + result.size() + "]");
//            for (Object user : result) {
//                System.out.println(((IUser) user).getFirstName());
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
