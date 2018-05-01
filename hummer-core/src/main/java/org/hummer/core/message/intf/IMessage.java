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
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a> Date: 2005-11-21 22:47:37
 * @version 1.0
 */
package org.hummer.core.message.intf;

import java.util.List;

public interface IMessage {
    public int getCode();

    public void setCode(int code);

    public void addParam(Object param);

    public List<Object> getParams();

    public void setParams(List<Object> params);

    public String getMessageKey();
}
