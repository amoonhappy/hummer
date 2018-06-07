<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="org.hummer.core.container.impl.HummerContainer" %>
<%@ page import="org.hummer.core.container.intf.IBusinessServiceManager" %>
<%@ page import="org.hummer.core.container.intf.IHummerContainer" %>
<%@ page import="org.hummer.newweb.model.intf.IUser" %>
<%@ page import="org.hummer.newweb.service.user.intf.ITestService" %>
<%@ page import="java.util.List" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Insert title here</title>
</head>
<body>
11

<%
    IHummerContainer hummerContainer = HummerContainer.getInstance();
    IBusinessServiceManager bsm = hummerContainer.getServiceManager();
    ITestService service = (ITestService) bsm.getService("testService");

    try {
        //get by user id test
//        IUser user = service.getUserById(new Integer(29868));
//        if (user != null) {
//            System.out.println(user.getId() + ":" + user.getFirstName());
//        }
//        get all users test
        List<IUser> result = (List<IUser>) service.getAllUsers();
        if (result != null) {
            System.out.println("1st search getAllUsers:" + result.size());
//            for (IUser user : result) {
//                System.out.println(user.getId() + ":" + user.getFirstName());
//            }
        } else {
            System.out.println("getAllUsers return null!");
        }
//        //insert user test
//        IUser user = new User();
//        user.setFirstName("new test");
//        user.setLastName("new test");
//        service.insertUser(user);
        //get all users test after insert
//        result = (List<IUser>) service.getAllUsers();
//        if (result != null) {
//            System.out.println("2nd search getAllUsers:" + result.size());
////            for (IUser user1 : result) {
////                System.out.println(user1.getId() + ":" + user1.getFirstName());
////            }
//        } else {
//            System.out.println("getAllUsers return null!");
//        }
    } catch (Exception e) {
        e.printStackTrace();
    }
%>
</body>
</html>