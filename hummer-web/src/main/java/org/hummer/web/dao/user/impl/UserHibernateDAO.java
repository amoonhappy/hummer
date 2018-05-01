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
package org.hummer.web.dao.user.impl;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.hummer.core.pagination.Pager;
import org.hummer.core.persistence.impl.BasicHibernateDAO;
import org.hummer.core.persistence.impl.SearchCondition;
import org.hummer.web.dao.user.intf.IUserDAO;
import org.hummer.web.model.impl.User;

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

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = new User();
        user.setFirstName(username);
        try {
            return (UserDetails) super.getObject(user);
        } catch (Exception e) {
            throw new UsernameNotFoundException("");
        }

    }

}
