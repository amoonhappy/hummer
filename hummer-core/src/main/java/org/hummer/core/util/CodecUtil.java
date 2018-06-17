package org.hummer.core.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;

public class CodecUtil {
    final static Logger log = Log4jUtils.getLogger(CodecUtil.class);

    public static String base64Encode(String input) {
        String ret;
        if (StringUtil.isEmpty(input)) {
            ret = null;
        } else {
            Base64 base64 = new Base64();
            try {
                ret = base64.encodeToString(input.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                log.warn("encoding error, unsupported encoding for [{}] on charsetName [{}]", input, "UTF-8");
                ret = null;
            }
        }
        return ret;
    }

    public static String base64Decode(String input) {
        String ret;
        if (StringUtil.isEmpty(input)) {
            ret = null;
        } else {
            ret = new String(Base64.decodeBase64(input));
        }
        return ret;
    }
}
