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

/**
 * @author jeff.zhou
 */
public interface IUser extends IAuditableModel {


    String getFirstName();

    void setFirstName(String firstname);

    String getLastName();

    void setLastName(String lastname);

    String getStatus();

    void setStatus(String status);

    String getRole();

    void setRole(String role);

}