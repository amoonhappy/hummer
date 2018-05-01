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

import org.apache.commons.beanutils.Converter;

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
public class ConvertUtil {
    private static ConvertUtil convertUtil = new ConvertUtil();

    private Converter converter = null;

    private ConvertUtil() {
    }

    public static ConvertUtil getInstance() {
        return convertUtil;
    }

    public void register(Converter converter) {
        this.converter = converter;
    }

    public void deregister() {
        this.converter = null;
    }

    public Converter getConvert() {
        return converter;
    }
}
