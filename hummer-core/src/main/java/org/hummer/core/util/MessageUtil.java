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
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a> Date: 2005-12-7
 * @version 1.0
 */
package org.hummer.core.util;

import org.apache.commons.lang3.StringUtils;
import org.hummer.core.context.impl.LocaleUtil;
import org.hummer.core.message.intf.IMessage;

import java.util.*;

public class MessageUtil {
    /**
     * Initialization size for StringBuffer
     */
    private final static int MESSAGE_BUFFER_SIZE = 128;
    /**
     * Default Locale class
     */
    public static Locale defaultLocale_ = new Locale("en", "US");
    /**
     * structure used for locale name to Locale class mapping
     */
    private static Properties prop_ = new Properties();
    // message file
    // private static String MSG_ROOT = "com.ial.hbs.common.exception.";
    private static String MSG_ROOT = "";
    private static String MSG_COMM_FILE = MSG_ROOT + "ApplicationResources";

    static {
        init(MSG_COMM_FILE, LocaleUtil.getLocale());
    }

    // init
    public static void init(String msgFile, Locale locale) {
        try {
            PropertyResourceBundle prb_ = (PropertyResourceBundle) PropertyResourceBundle.getBundle(msgFile, locale);

            String key = null;
            Object value = null;
            Enumeration<String> en = prb_.getKeys();

            while (en.hasMoreElements()) {
                key = en.nextElement();
                value = prb_.getString(key);
                prop_.put(key, value);
            }

            prb_ = null;
        } catch (MissingResourceException e) {
            return;
        } catch (Exception e) {
            e.printStackTrace();

            return;
        }
    }

    /**
     * Basic full parameters to get the messages
     *
     * @param messageCode
     * @param messageParams
     * @param locale
     * @return String
     */
    public static String getMessage(int messageCode, List<Object> messageParams, Locale locale) {
        StringBuffer message = new StringBuffer(MESSAGE_BUFFER_SIZE);

        final String MESSAGE_NOT_FOUND = "Internal Error: Unable to find error message file " + "for message number "
                + messageCode + ".";

        if ((locale != null) && !locale.equals(defaultLocale_)) {
            init(MSG_COMM_FILE, locale);
        }

        if (prop_ == null) {
            return MESSAGE_NOT_FOUND;
        }

        // construtor the message key
        String messageKey = getMessageKey(messageCode);

        try {
            message = applyParamsForMessages(messageKey, messageParams);
        } catch (Exception e) {
            return MESSAGE_NOT_FOUND;
        }

        return message.toString();
    }

    /**
     * Basic full parameters to get the messages
     *
     * @param messageKey
     * @param messageParams
     * @param locale
     * @return String
     */
    public static String getMessage(String messageKey, List<Object> messageParams, Locale locale) {
        StringBuffer message = new StringBuffer(MESSAGE_BUFFER_SIZE);

        final String MESSAGE_NOT_FOUND = "Internal Error: Unable to find error message file " + "for message number "
                + messageKey + ".";

        if (((locale != null) && !locale.equals(defaultLocale_)) || (prop_ == null)) {
            init(MSG_COMM_FILE, locale);
        }

        // if (prop_ == null) {
        // return MESSAGE_NOT_FOUND;
        // }
        try {
            message = applyParamsForMessages(messageKey, messageParams);
        } catch (Exception e) {
            return MESSAGE_NOT_FOUND;
        }

        return message.toString();
    }

    public static String getMessage(String messageKey, List<Object> errorParams) {
        Locale locale = LocaleUtil.getLocale();

        return getMessage(messageKey, errorParams, locale);
    }

    public static String getMessage(String messageKey) {
        Locale locale = LocaleUtil.getLocale();

        return getMessage(messageKey, null, locale);
    }

    /**
     * Looks up an error code in a resource bundle. Return the message text
     * found. If the error number can't be found in this locale, it looks under
     * the "en_US" locale and under no parameters.
     *
     * @param errorCode Description of the Parameter
     * @param locale    Description of the Parameter
     * @return The message value
     */
    public static String getMessage(int errorCode, Locale locale) {
        return getMessage(errorCode, null, locale);
    }

    /**
     * Looks up an error code in a resource bundle. Return the message text
     * found. If the error number can't be found in this locale, it looks under
     * the "en_US" locale.
     *
     * @param errorCode
     * @param errorParams
     * @return String
     */
    public static String getMessage(int errorCode, List<Object> errorParams) {
        Locale locale = LocaleUtil.getLocale();

        return getMessage(errorCode, errorParams, locale);
    }

    /**
     * Looks up an error code in a resource bundle. Return the message text
     * found. return the message by the default Locale.
     *
     * @param errorCode Description of the Parameter
     * @return The message value
     */
    public static String getMessage(int errorCode) {
        Locale locale = LocaleUtil.getLocale();

        return getMessage(errorCode, null, locale);
    }

    public static String getMessageKey(int messageCode) {
        String messageKey = "messages.0";

        if (messageCode > 0) { // messages
            messageKey = "messages." + messageCode;
        } else if (messageCode < 0) { // errors
            messageKey = "errors." + (-messageCode);
        }

        return messageKey;
    }

    public static String getMessage(IMessage iMessage) {
        StringBuffer message = new StringBuffer(MESSAGE_BUFFER_SIZE);

        final String MESSAGE_NOT_FOUND = "Internal Error: Unable to find error message file " + "for message number "
                + iMessage.getCode() + ".";
        Locale locale = LocaleUtil.getLocale();

        if (((locale != null) && !locale.equals(defaultLocale_)) || (prop_ == null)) {
            init(MSG_COMM_FILE, locale);
        }

        try {
            message = applyParamsForMessages(iMessage);
        } catch (Exception e) {
            return MESSAGE_NOT_FOUND;
        }

        return message.toString();
    }

    private static StringBuffer applyParamsForMessages(String messageKey, List<Object> messageParams) {
        StringBuffer message = new StringBuffer();
        String originalMessage = prop_.getProperty(messageKey);

        if ((messageParams != null) && (messageParams.size() > 0)) { // has
            // parameters

            for (int i = 0; i < messageParams.size(); i++) {
                String alias = "{" + i + "}";
                originalMessage = StringUtils.replace(originalMessage, alias, String.valueOf(messageParams.get(i)));
            }
        }

        return message.append(originalMessage);
    }

    private static StringBuffer applyParamsForMessages(IMessage iMessage) {
        return applyParamsForMessages(iMessage.getMessageKey(), iMessage.getParams());
    }
}
