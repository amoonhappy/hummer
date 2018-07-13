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
package org.hummer.core.model.intf;

import java.util.Date;

/**
 * @author jeff.zhou
 */
public interface IAuditableModel extends IModel {
    Date getCrtDate();

    void setCrtDate(Date crtDate);

    String getCrtUserId();

    void setCrtUserId(String crtUserid);

    Date getMdfDate();

    void setMdfDate(Date mdfDate);

    String getMdfUserId();

    void setMdfUserId(String mdfUserid);

}