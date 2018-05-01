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
package org.hummer.core.exception;

import org.hummer.core.message.intf.IMessage;
import org.hummer.core.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ErrorInfo implements IMessage {
    public static final String MESSAGE_PREFIX = "errors.";

    private int errorCode;

    private List<Object> errorParams = new ArrayList<Object>();

    public List<Object> getParams() {
        return errorParams;
    }

    public void setParams(List<Object> params) {
        this.errorParams = params;
    }

    public void addParam(Object param) {
        if (param instanceof Long) {
            Long aLong = (Long) param;
            param = String.valueOf(aLong.longValue());
        } else if (param instanceof Date) {
            Date date = (Date) param;
            param = DateUtil.date2String(date);
        }
        errorParams.add(param);
    }

    public int getCode() {
        return errorCode;
    }

    public void setCode(int code) {
        errorCode = code;
    }

    public String getMessageKey() {
        return MESSAGE_PREFIX + (-errorCode);
    }
}
