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
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a> Date: 2006-2-17
 * @version 1.0
 */
package org.hummer.newweb.service.user.impl;

import org.hummer.core.service.impl.BasicService;
import org.hummer.core.service.impl.ReturnValue;
import org.hummer.newweb.dao.user.intf.IUserDAO;
import org.hummer.newweb.model.intf.IUser;
import org.hummer.newweb.service.user.intf.IUserService;

/**
 * @author jeff.zhou
 */
public class UserService extends BasicService implements IUserService {
    IUserDAO userDAO = null;

    /**
     * (non-Javadoc)
     *
     * @see org.hummer.newweb.service.user.intf.IUserService#setUserDAO(IUserDAO)
     */
    public void setUserDAO(IUserDAO dao) {
        this.userDAO = dao;
    }

    /**
     * (non-Javadoc)
     *
     * @see org.hummer.newweb.service.user.intf.ITestService##getAllUsers()
     */
    public ReturnValue getAllUsers() throws Exception {
        ReturnValue ret = new ReturnValue();
        ret.setModel(userDAO.getAllUsers());
        return ret;
    }

    public void addUser(IUser user) throws Exception {
        userDAO.saveObject(user);
    }
}
