package org.hummer.cafe.dao.test;

import org.hummer.core.container.impl.BusinessServiceManager;
import org.hummer.core.container.intf.IBusinessServiceManager;

public class Test {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        IBusinessServiceManager bsm = BusinessServiceManager.getInstance();
        UserDAOTest service = (UserDAOTest) bsm.getService("userDAOTest");
        System.out.println(service);
        service.testSaveUsers();
    }
}
