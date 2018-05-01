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
package org.hummer.core.message.impl;

import org.hummer.core.message.intf.IMessage;
import org.hummer.core.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Message implements IMessage {
    public static final String MESSAGE_PREFIX = "messages.";

    private int messageCode;

    private List<Object> messageParams = new ArrayList<Object>();

    public List<Object> getParams() {
        return messageParams;
    }

    public void setParams(List<Object> params) {
        this.messageParams = params;
    }

    public void addParam(Object param) {
        if (param instanceof Long) {
            Long aLong = (Long) param;
            param = String.valueOf(aLong.longValue());
        } else if (param instanceof Date) {
            Date date = (Date) param;
            param = DateUtil.date2String(date);
        }
        messageParams.add(param);
    }

    public int getCode() {
        return messageCode;
    }

    public void setCode(int code) {
        messageCode = code;
    }

    public String getMessageKey() {
        return MESSAGE_PREFIX + messageCode;
    }
}
