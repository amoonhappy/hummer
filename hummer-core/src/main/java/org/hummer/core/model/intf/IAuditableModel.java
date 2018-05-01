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

    /**
     * @hibernate.property column="crt_date" length="19"
     */
    public abstract Date getCrtDate();

    public abstract void setCrtDate(Date crtDate);

    /**
     * @hibernate.property column="crt_userid" length="32"
     */
    public abstract String getCrtUserId();

    public abstract void setCrtUserId(String crtUserid);

    /**
     * @hibernate.property column="mdf_date" length="19"
     */
    public abstract Date getMdfDate();

    public abstract void setMdfDate(Date mdfDate);

    /**
     * @hibernate.property column="mdf_userid" length="32"
     */
    public abstract String getMdfUserId();

    public abstract void setMdfUserId(String mdfUserid);

}
/**
 * <p>
 * Open Source Architecture Project -- Hummer
 * </p>
 * <p>
 * Class Description
 * </p>
 * <p>
 * </p>
 * <p>
 * </p>
 * <p>
 * Change History
 * </p>
 * <p>
 * Author Date Description
 * </p>
 * <p>
 * </p>
 * <p>
 * </p>
 *
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a>
 * Date:2006-4-723:34:22
 * @version 1.0
 */
