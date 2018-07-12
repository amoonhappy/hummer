package org.apache.velocity.tools.view.context;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ViewContext {
    String REQUEST = "request";
    String RESPONSE = "response";
    String SESSION = "session";
    String APPLICATION = "application";
    String XHTML = "XHTML";

    HttpServletRequest getRequest();

    HttpServletResponse getResponse();

    ServletContext getServletContext();

    Object getAttribute(String var1);

    Context getVelocityContext();

    VelocityEngine getVelocityEngine();
}