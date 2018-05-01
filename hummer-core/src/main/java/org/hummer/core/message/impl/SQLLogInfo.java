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
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a> Date: 2005-12-6
 * @version 1.0
 */
package org.hummer.core.message.impl;

import java.util.Arrays;

public class SQLLogInfo {
    private String messages;

    private int sqlErrorCode;

    private String sqlState;

    private String invokerName;

    private Object[] args;

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public int getSqlErrorCode() {
        return sqlErrorCode;
    }

    public void setSqlErrorCode(int sqlErrorCode) {
        this.sqlErrorCode = sqlErrorCode;
    }

    public String getSqlState() {
        return sqlState;
    }

    public void setSqlState(String sqlState) {
        this.sqlState = sqlState;
    }

    public String getInvokerName() {
        return invokerName;
    }

    public void setInvokerName(String invokerName) {
        this.invokerName = invokerName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String toString() {
        return "SQLLogInfo{" + "messages='" + messages + "'" + "\n" + ", sqlErrorCode=" + sqlErrorCode + "\n"
                + ", sqlState='" + sqlState + "'" + "\n" + ", invokerName='" + invokerName + "\n" + "'" + ", args="
                + ((args == null) ? null : Arrays.asList(args)) + "}";
    }
}