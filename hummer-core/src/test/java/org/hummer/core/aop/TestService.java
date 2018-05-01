package org.hummer.core.aop;

/**
 * iFOP Spacee for Developer Party
 *
 * @author jeff.zhou
 */
public class TestService {
    private TestService test = null;

    public TestService getTest() {
        return this.test;
    }

    public void setTest(TestService test) {
        this.test = test;
    }

    public void print() {
        System.out.println("TestService:print()");
    }

    public void print1() {
        System.out.println("TestService:print1()");
    }
}
