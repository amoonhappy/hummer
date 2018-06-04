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
package org.hummer.newweb.dao.user.intf;

import org.hummer.core.pagination.Pager;
import org.hummer.core.persistence.intf.IBasicDAO;

/**
 * @author jeff.zhou
 */
public interface IUserDAO extends IBasicDAO {
    Pager getAllUsers() throws Exception;
}