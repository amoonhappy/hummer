package org.hummer.core.aop;

import junit.framework.TestCase;

/**
 * iFOP Spacee for Developer Party
 *
 * @author jeff.zhou
 */
public class TestChildServiceTest extends TestCase {

    public void testPrint() {
        TestChildService proxy = new TestChildService();
        TestService proxy1 = (TestService) proxy;

        proxy1.print();

    }

}
