package org.hummer.core.codec;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ParamHelper {
    private static final Log logger = LogFactory.getLog(ParamHelper.class);

    public ParamHelper() {
    }

    public static String parsePostParam(ServletRequest servletRequest) throws IOException, UnsupportedEncodingException {
        ServletInputStream inputStream = servletRequest.getInputStream();
        int length = servletRequest.getContentLength();
        if (length < 0) {
            return null;
        } else {
            String encoding = servletRequest.getCharacterEncoding();
            byte[] buffer = new byte[length];
            IOUtils.readFully(inputStream, buffer);
            String content = new String(buffer, encoding);
            logger.debug("接收到的请求body信息：" + content);
            return content;
        }
    }
}