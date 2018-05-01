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
package org.hummer.web.model.intf;

import org.acegisecurity.userdetails.UserDetails;
import org.hummer.core.model.intf.IAuditableModel;
import org.hummer.core.model.intf.ISingleStringPKModel;

/**
 * @author jeff.zhou
 */
public interface IUser extends ISingleStringPKModel, IAuditableModel, UserDetails {

    /**
     * @hibernate.property column="firstname" length="50"
     */
    public abstract String getFirstName();

    public abstract void setFirstName(String firstname);

    /**
     * @hibernate.property column="lastname" length="50"
     */
    public abstract String getLastName();

    public abstract void setLastName(String lastname);

    /**
     * @hibernate.property column="status" length="50"
     */
    public abstract String getStatus();

    public abstract void setStatus(String status);

}