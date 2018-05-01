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
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a> Date: 2005-11-21 22:48:32
 * @version 1.0
 */
package org.hummer.core.exception;

import org.hummer.core.message.intf.IMessage;
import org.hummer.core.util.MessageUtil;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author jeff.zhou
 */
public class BusinessException extends CheckedException {
    /**
     *
     */
    private static final long serialVersionUID = 3833461825219017009L;
    protected Throwable cause;
    private ErrorInfo errorInfo = new ErrorInfo();
    private String exceptionId;

    public BusinessException(ErrorInfo errorInfo) {
        super();
        this.errorInfo = errorInfo;
        genExceptionId();
    }

    public BusinessException(ErrorInfo errorInfo, Throwable cause) {
        this(errorInfo);
        this.cause = cause;
        genExceptionId();
    }

    // Basic Constructor
    public BusinessException(int errorCode) {
        super();
        errorInfo.setCode(errorCode);
        genExceptionId();
    }

    public BusinessException(String message, int errorCode) {
        super(message);
        errorInfo.setCode(errorCode);
        genExceptionId();
    }

    public BusinessException(Throwable cause, int errorCode) {
        super(cause);
        this.cause = cause;
        errorInfo.setCode(errorCode);
        genExceptionId();
    }

    public BusinessException(String message, Throwable cause, int errorCode) {
        super(message, cause);
        errorInfo.setCode(errorCode);
        genExceptionId();
    }

    // External Constructor
    public BusinessException(int errorCode, List<Object> errorParams) {
        this(errorCode);
        errorInfo.setParams(errorParams);
    }

    public BusinessException(String message, int errorCode, List<Object> errorParams) {
        this(message, errorCode);
        errorInfo.setParams(errorParams);
    }

    public BusinessException(Throwable cause, int errorCode, List<Object> errorParams) {
        this(cause, errorCode);
        errorInfo.setParams(errorParams);
    }

    public BusinessException(String message, Throwable cause, int errorCode, List<Object> errorParams) {
        this(message, cause, errorCode);
        errorInfo.setParams(errorParams);
    }

    public List<Object> getErrorParams() {
        return errorInfo.getParams();
    }

    public void addErrorParam(Object o) {
        if (errorInfo == null) {
            errorInfo = new ErrorInfo();
        }

        errorInfo.addParam(o);
    }

    public int getErrorCode() {
        return errorInfo.getCode();
    }

    public void setErrorCode(int errorCode) {
        errorInfo.setCode(errorCode);
    }

    public void printStackTrace(PrintWriter s) {
        s.println("<app_exception>\r\n\t<exception_id>" + exceptionId + "</exception_id>\r\n");
        s.println("\t<exception-localized-message>" + "ErrorCode=" + getErrorCode() + " Messages="
                + MessageUtil.getMessage(getErrorCode(), getErrorParams()) + " LocalizedMessage="
                + getLocalizedMessage() + "</exception-localized-message>\r\n\t<exception_stack_trace>");

        if (cause != null) {
            cause.printStackTrace(s);
        }

        super.printStackTrace(s);
        s.println("\t</exception_stack_trace>\r\n</app_exception>");
    }

    public void printStackTrace(PrintStream s) {
        s.println("<app_exception>\r\n\t<exception_id>" + exceptionId + "</exception_id>\r\n");
        s.println("\t<exception-localized-message>" + "ErrorCode=" + getErrorCode() + " Messages="
                + MessageUtil.getMessage(getErrorCode(), getErrorParams()) + " LocalizedMessage="
                + getLocalizedMessage() + "</exception-localized-message>\r\n\t<exception_stack_trace>");

        if (cause != null) {
            cause.printStackTrace(s);
        }

        super.printStackTrace(s);
        s.println("\t</exception_stack_trace>\r\n</app_exception>");
    }

    public void genExceptionId() {
        String idFormat = "yyMMddHHmmssS";
        SimpleDateFormat sdf = new SimpleDateFormat(idFormat);
        this.exceptionId = sdf.format(new Date());
    }

    public String getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(String exceptionId) {
        this.exceptionId = exceptionId;
    }

    public IMessage getError() {
        return this.errorInfo;
    }

    public void setError(ErrorInfo errorInfo) {
        this.errorInfo = errorInfo;
    }
}
