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
package org.hummer.newweb.dao.user.impl;

import org.hummer.core.pagination.Pager;
import org.hummer.core.persistence.impl.BasicHibernateDAO;
import org.hummer.core.persistence.impl.SearchCondition;
import org.hummer.newweb.dao.user.intf.IUserDAO;
import org.hummer.newweb.model.impl.User;

/**
 * @author jeff.zhou
 */
public class UserHibernateDAO extends BasicHibernateDAO implements IUserDAO {

    /*
     * (non-Javadoc)
     *
     * @see org.opensource.hummer.example.dao.UserDAO#getAllUsers()
     */
    public Pager getAllUsers() throws Exception {
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setSearchModel(new User());
        searchCondition.setHibernateQL("select a from User a");
        searchCondition.setQuerySQLTableAllias(new String[]{"a"});
        searchCondition.setEnablePagination(false);

        return super.searchByHQL(searchCondition);
    }
}
