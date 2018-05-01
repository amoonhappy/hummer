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

import org.hummer.core.context.intf.IContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * @author jeff.zhou
 */
public class RequestContext implements IContext {
    private HttpServletRequest request;

    private Locale locale;

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

}
