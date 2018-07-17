package org.hummer.test;

import junit.framework.TestCase;
import org.hummer.core.container.HummerContainer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

@SuppressWarnings("unchecked")
public class RedisCacheAopTest extends TestCase {
    //private static Logger log = Log4jUtils.getLogger(RedisCacheAopTest.class);
//    private String file = "hummer-cfg-main.properties";
//    private String file1 = "hummer-web-cfg-aop.xml";
    public void testAddUser() {
//        IHummerContainer iHummerContainer = HummerContainer.getInstance();
//        IBusinessServiceManager ibsv = iHummerContainer.getServiceManager();
//        ITestService testService = (ITestService) ibsv.getService("testService");
//        IUser temp = new User();
//        temp.setFirstName("test for redis cache annotation");
//        testService.insertUser(temp);
//        ITest1Service test1Service = (ITest1Service) ibsv.getService("test1Service");
//        IUser temp = new User();
//        temp.setFirstName("test for redis cache annotation");
//        test1Service.saveUser(temp);
    }

    public void testGetUser() {

        try {
            /**
             * Redis Cache Interceptor 测试
             */
//            IHummerContainer iHummerContainer = HummerContainer.getInstance();
//            IBusinessServiceManager ibsv = iHummerContainer.getServiceManager();
//            ITestService testService = (ITestService) ibsv.getService("testService");
//
////            List result = (List) testService.getAllUsers();
//            Assert.notNull(result, "Result is Null!");
//            System.out.println("returned size:[" + result.size() + "]");
//            for (Object user : result) {
//                System.out.println(((IUser) user).getFirstName());
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testCollector() {
//        List<IUser> returnValue = new ArrayList<>();
//        returnValue.add(new User(Long.valueOf("1")));
//        returnValue.add(new User(Long.valueOf("2")));
//        returnValue.add(new User(Long.valueOf("3")));
//        returnValue.add(new User(Long.valueOf("4")));
//        returnValue.add(new User(Long.valueOf("5")));

//        String idstr = (returnValue).stream().map(IUser::getId).collect(joining(","));
//        System.out.println(idstr);
    }

    public void testSpEl() throws NoSuchMethodException {
//        Parameter[] params = new Parameter[2];
//        params[0] = new Parameter();
//        params[1] = new User();
        HummerContainer container = HummerContainer.getInstance();
//        ITestService testService = (ITestService) bsv.getService("testService");

//        Method method = testService.getClass().getMethod(
//                "getLatestUser", String.class, IUser.class, Integer.class, List.class);
//        Method method = ReflectionUtil.findMethod(ITestService.class, "getLatestUser", String.class, IUser.class, Integer.class, List.class);
//        Object[] params = new Object[4];
//        params[0] = "test";
//        params[1] = new User();
//        params[2] = new Integer(1000);
//        params[3] = new ArrayList<>();
//        IUser user1 = new User();
//        user1.setFirstName("list param1.first name");
//        IUser user2 = new User();
//        user2.setLastName("list param1.last name");
//        ((List) params[3]).add(user1);
//        ((List) params[3]).add(user2);
////        params[3] = new String[]{"1", "2", "3"};
//
//        ((IUser) params[1]).setFirstName("Jeff Zhou");
////        Parameter[] params = method.getParameters();
//
//        PrioritizedParameterNameDiscoverer a = new PrioritizedParameterNameDiscoverer();

//        CacheEvaluationContext itemContext = new CacheEvaluationContext(testService, method, params, a);
        //public IUser getLatestUser(String status, IUser user, Integer time, List list)
//        itemContext.setVariable("");
//        String exp1 = "#p0.concat(#p1.firstName).concat(#p2).concat(#p3[0].firstName).concat(#p3[1].lastName).hashCode()";//-2021782716
//        String exp2 = "#status.hashCode() + #user.firstName.hashCode() + #time.hashCode() + #list[0].firstName.hashCode()";
//        String exp3 = "#p0.hashCode() + #p1.firstName.hashCode() + #p2.hashCode() + #p3[0].firstName.hashCode()";//1845423542
        String exp1 = "#p0.concat(#p1.firstName).concat(#p2).concat(#p3[0].firstName).concat(#p3[1].lastName)";//testJeff Zhou1000list param1.first namelist param1.last name
//        String exp1 = "T(org.hummer.core.util.CodecUtil).base64Encode(#p0.concat(#p1.firstName).concat(#p2).concat(#p3[0].firstName).concat(#p3[1].lastName))";//dGVzdEplZmYgWmhvdTEwMDBsaXN0IHBhcmFtMS5maXJzdCBuYW1lbGlzdCBwYXJhbTEubGFzdCBuYW1l
//        String exp1 = "#base64('test11111')";
        ExpressionParser parser = new SpelExpressionParser();
//        ExpressionParser parser1 = new SpelExpressionParser();
//
        Expression expression = parser.parseExpression(exp1);
//        EvaluationContext context = new StandardEvaluationContext();
////        context.
//        ((StandardEvaluationContext) context).registerFunction("base64", ReflectionUtil.findMethod(CodecUtil.class, "base64Encode", String.class));
//        String value = (String) expression.getValue(context);
//        String value2 = expression.getValue(itemContext, String.class);//-2021782716

//        System.out.println(value2);
//        String value2 = expression.getValue(itemContext, String.class);//-2021782716
        //dGVzdEplZmYgWmhvdTEwMDBsaXN0IHBhcmFtMS5maXJzdCBuYW1lbGlzdCBwYXJhbTEubGFzdCBuYW1l
        //dGVzdEplZmYgWmhvdTEwMDBsaXN0IHBhcmFtMS5maXJzdCBuYW1lbGlzdCBwYXJhbTEubGFzdCBuYW1l
//        Base64 base64 = new Base64();
//        try {
//            value2 = base64.encodeToString(value2.getBytes("UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        System.out.println(value2);
//        String value3 = CodecUtil.base64Decode(value2);
//        System.out.println(value3);

//        byte[] valueByte = SerializableUtil.toByte(value);
//        String value1 = (String) SerializableUtil.toObject(valueByte, container.getClass().getClassLoader());
//        System.out.println("orginal value =" + value + " hashCode is " + value.hashCode());
//        System.out.println("new value =" + value1 + " hashCode is " + value1.hashCode());

    }
}
