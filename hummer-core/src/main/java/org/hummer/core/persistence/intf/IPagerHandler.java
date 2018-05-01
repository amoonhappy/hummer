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
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a> Date: 2005-12-7
 * @version 1.0
 */
package org.hummer.core.persistence.intf;

import org.hibernate.HibernateException;
import org.hummer.core.exception.BusinessException;
import org.hummer.core.pagination.Pager;

import java.sql.SQLException;

/**
 * @author jeff.zhou
 */
public interface IPagerHandler {
    public Pager getPager(int type, Object[] o) throws HibernateException, BusinessException, SQLException;
}
