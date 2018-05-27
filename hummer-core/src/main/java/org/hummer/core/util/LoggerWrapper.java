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
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a> Date: 2006-6-24
 * @version 1.0
 */
package org.hummer.core.util;

import org.acegisecurity.userdetails.UserDetails;
import org.hummer.core.context.impl.AuthContext;
import org.hummer.core.context.impl.ContextHolder;

/**
 * @author jinyangzhou
 */
@Deprecated
public class LoggerWrapper {
    // private static Logger log = Logger.getLogger(LoggerWrapper.class);

    public static void debug(Object caller, String message1, String message2) {
        StringBuffer msg = new StringBuffer();
        AuthContext ac = ContextHolder.getAuthContext();
        UserDetails user = null;
        if (ac != null) {
            user = ContextHolder.getAuthContext().getAuthUser();
        } else {
            // user = newweb AuthUser();
        }
        String userName = user == null ? "SYSTEM" : user.getUsername();
        msg.append('[');
        msg.append(userName);
        msg.append(']');
        msg.append(' ');
        msg.append(caller.getClass().getSimpleName());
        msg.append(':');
        msg.append(message1);
        msg.append('-');
        msg.append(message2);
        LogFactory.getApplicationLog(caller.getClass()).debug(msg.toString());
        // log.debug("!!!!!!!!!!!!!!!!!!!!!!!222222222");
    }

    public static void debug(Class caller, String message1, String message2) {
        StringBuffer msg = new StringBuffer();
        AuthContext ac = ContextHolder.getAuthContext();
        String userName = null;
        UserDetails user = null;
        if (ac != null) {
            user = ac.getAuthUser();
        }
        userName = user == null ? "SYSTEM" : user.getUsername();
        msg.append('[');
        msg.append(userName);
        msg.append(']');
        msg.append(' ');
        msg.append(caller.getClass().getSimpleName());
        msg.append(':');
        msg.append(message1);
        msg.append('-');
        msg.append(message2);
        LogFactory.getApplicationLog(caller).debug(msg.toString());
        // log.debug("!!!!!!!!!!!!!!!!!!!!!!!222222222");
    }
}
