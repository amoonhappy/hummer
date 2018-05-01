/**
 * <p>Open Source Architecture Project -- hummer-common            </p>
 * <p>Class Description                                     </p>
 * <p>                                                      </p>
 * <p>                                                      </p>
 * <p>Change History                                        </p>
 * <p>Author    Date      Description                       </p>
 * <p>                                                      </p>
 * <p>                                                      </p>
 * Copyright (c) 2004-2007 hummer-common, All rights reserved
 *
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a>
 * Date: 2005-12-7
 * @version 1.0
 */
package org.hummer.core.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <br>
 * <br>
 * <ul>
 * <li>
 * <li>
 * </ul>
 * <br>
 *
 * @author jeff
 * @version <br>
 * 1.0.0 2007-6-23
 * @since 1.0.0
 */
public class BlankObjectUtil {
    public final static Object object = new Object();

    public final static Object[] ObjectArray = new Object[0];

    public final static String[] StringArray = new String[0];

    public final static Method[] MethodArray = new Method[0];

    public final static Collection<?> Collection = new ArrayList<Object>(0);

    public final static List<?> List = new ArrayList<Object>(0);
}
