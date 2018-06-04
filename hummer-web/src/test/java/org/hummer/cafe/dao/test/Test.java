package org.hummer.cafe.dao.test;

import org.hummer.core.container.impl.HummerContainer;
import org.hummer.core.container.intf.IBusinessServiceManager;
import org.hummer.core.container.intf.IHummerContainer;

public class Test {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        IHummerContainer iHummerContainer = HummerContainer.getInstance();
        IBusinessServiceManager bsm = iHummerContainer.getServiceManager();
        UserDAOTest service = (UserDAOTest) bsm.getService("userDAOTest");
        System.out.println(service);
        service.testSaveUsers();
    }
}
