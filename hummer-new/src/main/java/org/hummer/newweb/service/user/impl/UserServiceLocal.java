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

import org.hummer.core.pagination.Pager;
import org.hummer.core.service.impl.ReturnValue;
import org.hummer.newweb.model.impl.User;
import org.hummer.newweb.service.user.intf.IUserService;

import java.util.Collection;

/**
 * @author jeff.zhou
 */
public class UserServiceLocal extends UserService implements IUserService {
    public ReturnValue getAllUsers() throws Exception {
        ReturnValue ret = new ReturnValue();
        Pager pg = userDAO.getAllUsers();
        Collection<Object> result = pg.getResult();
        result.add(new User());
        pg.setResult(result);
        pg.setTotalRowCount(pg.getTotalRowCount() + 1);
        ret.setModel(pg);
        return ret;
    }
}
