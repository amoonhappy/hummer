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

public interface IMessages {
    int getSize();

    boolean addMessage(IMessage message);

    boolean removeMessage(IMessage message);

    IMessage getMessage(int index);
}
