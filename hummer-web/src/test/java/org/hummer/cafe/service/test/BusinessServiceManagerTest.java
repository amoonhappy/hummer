package org.hummer.cafe.service.test;

import junit.framework.TestCase;
import org.hummer.core.container.impl.BusinessServiceManager;
import org.hummer.core.container.intf.IBusinessServiceManager;
import org.hummer.core.pagination.Pager;
import org.hummer.core.service.impl.ReturnValue;
import org.hummer.web.model.impl.User;
import org.hummer.web.model.intf.IUser;
import org.hummer.web.service.user.intf.IUserService;

public class BusinessServiceManagerTest extends TestCase {

    public void testGetService() {
        IBusinessServiceManager bsm = BusinessServiceManager.getInstance();
        IUserService service = (IUserService) bsm.getService("userService");

        System.out.println(service);
        try {
            // ReturnValue rv = service.getAllUsers();
            IUser user = new User();
            user.setId("44444444444444");
            user.setFirstName("asdf");
            user.setLastName("fda");
            // Serializable ret = rv.getResult();
            service.addUser(user);

            ReturnValue rv = service.getAllUsers();
            Pager pg = (Pager) rv.getResult();
            System.out.println(((IUser) pg.getResult().iterator().next()).getFirstName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
