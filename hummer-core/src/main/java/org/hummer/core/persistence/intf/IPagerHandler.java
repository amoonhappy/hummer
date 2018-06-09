
package org.hummer.core.persistence.intf;

import org.hummer.core.exception.BusinessException;
import org.hummer.core.pagination.Pager;

import java.sql.SQLException;

/**
 * @author jeff.zhou
 */
public interface IPagerHandler {
    Pager getPager(int type, Object[] o) throws BusinessException, SQLException;
}
