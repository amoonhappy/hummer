/*
  <p>Open Source Architecture Project -- Hummer            </p>
  <p>Class Description                                     </p>
  <p>                                                      </p>
  <p>                                                      </p>
  <p>Change History                                        </p>
  <p>Author    Date      Description                       </p>
  <p>                                                      </p>
  <p>                                                      </p>

  @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a> Date: 2005-12-6
 * @version 1.0
 */
package org.hummer.core.context.impl;

/**
 * @author jeff.zhou
 */
public class ContextHolder {
    private static ThreadLocal<RequestContext> requestContextHolder = new ThreadLocal<>();

    private static ThreadLocal<AuthContext> authContextHolder = new ThreadLocal<>();

    private static ThreadLocal<AuthContext> secureContextHolder = new ThreadLocal<>();

    public static RequestContext getRequestContext() {
        return requestContextHolder.get();
    }

    static void setRequestContext(RequestContext requestContext) {
        RequestContext context = requestContextHolder.get();

        if (context == null) {
            requestContextHolder.set(requestContext);
        }
    }

    public static AuthContext getAuthContext() {
        return authContextHolder.get();
    }

    public static void setAuthContext(AuthContext authContext) {
        AuthContext context = authContextHolder.get();

        if (context == null) {
            authContextHolder.set(authContext);
        }
    }

}
