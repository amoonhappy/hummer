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
package org.hummer.core.context.impl;

import org.acegisecurity.userdetails.UserDetails;
import org.hummer.core.context.intf.IContext;

/**
 * @author jinyangzhou
 */
public class AuthContext implements IContext {
    private UserDetails authUser = null;

    public UserDetails getAuthUser() {
        return authUser;
    }

    public void setAuthUser(UserDetails authUser) {
        this.authUser = authUser;
    }
}