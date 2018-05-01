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
package org.hummer.cafe.dao.test;

import org.apache.log4j.Logger;
import org.hummer.core.container.impl.BusinessServiceManager;
import org.hummer.core.container.intf.IBusinessServiceManager;
import org.hummer.core.persistence.impl.SearchCondition;
import org.hummer.core.util.HibernateUtil;
import org.hummer.test.BaseDAOTestCase;
import org.hummer.web.dao.user.intf.IUserDAO;
import org.hummer.web.model.impl.User;
import org.hummer.web.model.intf.IUser;

/**
 * @author jeff.zhou
 */
public class UserDAOTest extends BaseDAOTestCase {
    private final Logger log = Logger.getLogger(BaseDAOTestCase.class);
    IUserDAO userDAO = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        HibernateUtil.getSessionFactory();

    }

    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void testDeleteUsers() throws Exception {
        IBusinessServiceManager bsm = BusinessServiceManager.getInstance();
        userDAO = (IUserDAO) bsm.getService("userDAO");
        System.out.println(userDAO);

        IUser user = new User();
        user = (IUser) this.populate(user);
        user.setId(null);
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setDeleteModel(user);
        userDAO.deleteObject(searchCondition);
    }

    public void testSaveUsers() throws Exception {
        IBusinessServiceManager bsm = BusinessServiceManager.getInstance();
        userDAO = (IUserDAO) bsm.getService("userDAO");
        System.out.println(userDAO);
        IUser user = new User();
        user = (IUser) this.populate(user);
        //HibernateUtil.beginTransaction();

        userDAO.addObject(user);
        //HibernateUtil.commitTransaction();
    }

    public void testGetAllUsers() throws Exception {
        IBusinessServiceManager bsm = BusinessServiceManager.getInstance();
        userDAO = (IUserDAO) bsm.getService("userDAO");
        System.out.println(userDAO);

        IUser user = new User();
        // test.setFirstname("firstName");
        // user = (IUser) populate(user);
        user.setId("111111111111");

        user = (IUser) userDAO.getObject(user);
        assertNotNull(user);
        log.debug("user id = " + user.getId());
    }
}
