package org.hummer.core.aop;

import junit.framework.TestCase;
import org.hummer.core.util.StringUtil;

/**
 * iFOP Spacee for Developer Party
 *
 * @author jeff.zhou
 */
public class TestChildServiceTest extends TestCase {

    public void testPrint() {
        TestChildService proxy = new TestChildService();

        proxy.print();
        String[] test = StringUtil.joinArray("a,b", ",");//StringUtils.delimitedListToStringArray("b,a", ",");

        System.out.println(test[0] + "->" + test[1]);

    }

}
