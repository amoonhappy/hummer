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
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a> Date: 2006-1-3
 * @version 1.0
 */
package org.hummer.newweb.service.user.intf;

import org.hummer.core.service.impl.ReturnValue;
import org.hummer.core.service.intf.IBasicService;
import org.hummer.newweb.dao.user.intf.IUserDAO;
import org.hummer.newweb.model.intf.IUser;

/**
 * @author jeff.zhou
 */
public interface IUserService extends IBasicService {
    void setUserDAO(IUserDAO dao);

    ReturnValue getAllUsers() throws Exception;

    void addUser(IUser user) throws Exception;
}