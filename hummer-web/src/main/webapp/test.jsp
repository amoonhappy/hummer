<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="org.hummer.core.container.impl.BusinessServiceManager" %>
<%@ page import="org.hummer.core.container.intf.IBusinessServiceManager" %>
<%@ page import="org.hummer.core.pagination.Pager" %>
<%@ page import="org.hummer.core.service.impl.ReturnValue" %>
<%@ page import="org.hummer.web.model.impl.User" %>
<%@ page import="org.hummer.web.model.intf.IUser" %>
<%@ page import="org.hummer.web.service.user.intf.IUserService" %>


<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Insert title here</title>
</head>
<body>
11

<%
    IBusinessServiceManager bsm = BusinessServiceManager.getInstance();
    IUserService service = (IUserService) bsm.getService("userService");

    System.out.println(service);
    try {
        // ReturnValue rv = service.getAllUsers();
        IUser user = new User();
        user.setId("22222");
        user.setFirstName("jeff");
        user.setLastName("zhou");
        // Serializable ret = rv.getResult();
        service.addUser(user);

        ReturnValue rv = service.getAllUsers();
        Pager pg = (Pager) rv.getResult();
        System.out.println(((IUser) pg.getResult().iterator().next()).getFirstName());
    } catch (Exception e) {
        e.printStackTrace();
    }
%>
</body>
</html>