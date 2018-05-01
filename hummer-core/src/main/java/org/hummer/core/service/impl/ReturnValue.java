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
package org.hummer.core.service.impl;

import org.hummer.core.message.impl.Message;
import org.hummer.core.message.impl.Messages;
import org.hummer.core.message.intf.IMessage;

import java.io.Serializable;
import java.util.List;

/**
 * @author jeff.zhou
 */
public class ReturnValue implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 3258413923999889460L;

    private Serializable result;

    private Messages messages = new Messages();

    public Serializable getResult() {
        return result;
    }

    public void setModel(Serializable model) {
        this.result = model;
    }

    public void addMessage(int messageCode, List<Object> messageParams) {
        IMessage message = new Message();
        message.setCode(messageCode);
        message.setParams(messageParams);
        messages.addMessage(message);
    }

    public void addMessage(IMessage message) {
        messages.addMessage(message);
    }

    public Messages getMessages() {
        return messages;
    }

    public void setMessages(Messages messages) {
        this.messages = messages;
    }
}