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
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a> Date: 2005-11-21 21:02:41
 * @version 1.0
 */
package org.hummer.core.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * @author jeff.zhou
 */
public class TypeUnmatchedException extends CheckedException {
    public static final int CONVERT_TO_STRING = 0;
    public static final int CONVERT_TO_NUMBER = 1;
    public static final int CONVERT_TO_DATE = 2;
    private static final long serialVersionUID = 3976731476052096305L;
    private String convertTypeString;

    private String convertString;

    public TypeUnmatchedException() {
        super();
    }

    public TypeUnmatchedException(Throwable cause) {
        super(cause);
    }

    public TypeUnmatchedException(String message) {
        super(message);
    }

    public TypeUnmatchedException(String inputString, int convertType) {
        this();
        switch (convertType) {
            case CONVERT_TO_STRING:
                convertTypeString = "STRING";
                break;
            case CONVERT_TO_NUMBER:
                convertTypeString = "NUMBER";
                break;
            case CONVERT_TO_DATE:
                convertTypeString = "DATE";
                break;
            default:
                convertTypeString = "";
        }
        this.convertString = inputString;

    }

    public TypeUnmatchedException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getMessage() {
        if (StringUtils.isEmpty(this.convertTypeString)) {
            return super.getMessage();
        }
        StringBuffer message = new StringBuffer(super.getMessage());
        message.append(" Input String [").append(convertString).append("] is not match the type [").append(
                convertTypeString).append("]");
        return message.toString();
    }
}
