<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="org.hummer.core.container.impl.HummerContainer" %>
<%@ page import="org.hummer.core.container.intf.IBusinessServiceManager" %>
<%@ page import="org.hummer.core.container.intf.IHummerContainer" %>
<%@ page import="org.hummer.newweb.model.impl.User" %>
<%@ page import="org.hummer.newweb.model.intf.IUser" %>
<%@ page import="org.hummer.newweb.service.user.intf.ITestService" %>

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

    System.out.println(service);
    try {
        // ReturnValue rv = service.getAllUsers();
        IUser user = new User();
        ///user.setId("2222333");
        user.setFirstName("??");
        user.setLastName("xxx");
        // Serializable ret = rv.getResult();
        service.insertUser(user);

//        ReturnValue rv = service.getAllUsers();
//        Pager pg = (Pager) rv.getResult();
//        System.out.println(((IUser) pg.getResult().iterator().next()).getFirstName());
    } catch (Exception e) {
        e.printStackTrace();
    }
%>
</body>
</html>