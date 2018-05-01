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
import org.hummer.core.message.intf.IMessages;

import java.util.ArrayList;
import java.util.List;

public class Messages implements IMessages {
    List<IMessage> messages = new ArrayList<IMessage>();

    public int getSize() {
        return messages.size();
    }

    public boolean addMessage(IMessage message) {
        return messages.add(message);
    }

    public boolean removeMessage(IMessage message) {
        return messages.remove(message);
    }

    public IMessage getMessage(int index) {
        return messages.get(index);
    }
}
