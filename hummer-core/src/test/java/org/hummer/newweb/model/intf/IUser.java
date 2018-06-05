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
package org.hummer.newweb.model.intf;

import org.hummer.core.model.intf.IAuditableModel;
import org.hummer.core.model.intf.ISingleStringPKModel;

/**
 * @author jeff.zhou
 */
public interface IUser extends ISingleStringPKModel, IAuditableModel {

    /**
     * @hibernate.property column="firstname" length="50"
     */
    String getFirstName();

    void setFirstName(String firstname);

    /**
     * @hibernate.property column="lastname" length="50"
     */
    String getLastName();

    void setLastName(String lastname);

    /**
     * @hibernate.property column="status" length="50"
     */
    String getStatus();

    void setStatus(String status);

}