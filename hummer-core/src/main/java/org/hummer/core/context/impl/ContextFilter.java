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
package org.hummer.core.context.impl;

//import org.apache.struts.Globals;

//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;

/**
 * @author jeff.zhou
 */
public class ContextFilter { //implements Filter {
//    public void destroy() {
//    }
//
//    public void init(FilterConfig filterConfig) throws ServletException {
//    }
//
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
//            ServletException {
//        RequestContext requestContext = new RequestContext();
//
//        if (request instanceof HttpServletRequest) {
//            // set requestcontext
//            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//            requestContext.setRequest(httpServletRequest);
//
//            // set localecontext
//            Locale locale = (Locale) httpServletRequest.getSession().getAttribute(Globals.LOCALE_KEY);
//
//            if (locale == null) {
//                locale = Locale.getDefault();
//            }
//
//            requestContext.setLocale(locale);
//            ContextHolder.setRequestContext(requestContext);
//        }
//
//        filterChain.doFilter(request, response);
//    }
}