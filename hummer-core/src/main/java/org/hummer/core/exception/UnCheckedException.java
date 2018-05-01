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
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a> Date: 2005-11-21 21:05:37
 * @version 1.0
 */
package org.hummer.core.exception;

public class UnCheckedException extends RuntimeException {

    private static final long serialVersionUID = 4123103957137568052L;

    public UnCheckedException() {
    }

    public UnCheckedException(String message) {
        super(message);
    }

    public UnCheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnCheckedException(Throwable cause) {
        super(cause);
    }
}
