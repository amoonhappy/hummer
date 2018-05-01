package org.hummer.core.aop;

/**
 * iFOP Spacee for Developer Party
 *
 * @author jeff.zhou
 */
public class TestChildService extends TestService {
    public void print() {
        System.out.println("TestChildService:print start");
        super.print();
        System.out.println("TestChildService:print end");
    }

    public void print1() {
        // System.out.println("TestChildService:print1 start");
        super.print1();
        // System.out.println("TestChildService:print1 end");
    }
}
