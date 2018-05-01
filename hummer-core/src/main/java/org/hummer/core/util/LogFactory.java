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
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a> Date: 2006-4-7
 * @version 1.0
 */
package org.hummer.core.util;

import org.apache.log4j.Logger;

/**
 * @author jeff.zhou
 */
public class LogFactory {

    public static Logger getPerformanceLog(Class clazz) {
        return Logger.getLogger(clazz);
    }

    public static Logger getSystemLog(Class clazz) {
        return Logger.getLogger(clazz);
    }

    public static Logger getApplicationLog(Class clazz) {
        return Logger.getLogger(clazz);
    }
}
