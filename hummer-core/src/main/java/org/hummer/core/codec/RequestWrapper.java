package org.hummer.core.codec;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

public class RequestWrapper extends HttpServletRequestWrapper {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private String mode;
    private Map<String, String[]> parameterMap = new HashMap();
    private JSONObject json;
    private String parameterFormat;
    private String content;
    private ServletInputStream inputStream;

    public RequestWrapper(HttpServletRequest request, String mode) {
        super(request);
        this.mode = mode;
    }

    public String getParameter(String name) {
        Map<String, String[]> map = this.getParameterMap();
        String[] values = (String[]) map.get(name);
        return values != null && values.length != 0 ? values[0] : null;
    }

    public Map<String, String[]> getParameterMap() {
        if (StringUtils.isEmpty(this.parameterFormat)) {
            this.initParameterMap();
        }

        if ("JSON".equals(this.parameterFormat)) {
            if (this.json != null) {
                Map<String, String[]> map = new HashMap();
                Iterator var2 = this.json.keySet().iterator();

                while (var2.hasNext()) {
                    Object key = var2.next();
                    if (key != null) {
                        String k = key.toString();
                        Object v = this.json.get(k);
                        if (v != null) {
                            map.put(k, new String[]{v.toString()});
                        }
                    }
                }

                return map;
            } else {
                return Collections.EMPTY_MAP;
            }
        } else {
            return this.parameterMap != null ? this.parameterMap : Collections.EMPTY_MAP;
        }
    }

    public Enumeration<String> getParameterNames() {
        Map<String, String[]> map = this.getParameterMap();
        return IteratorUtils.asEnumeration(map.keySet().iterator());
    }

    public String[] getParameterValues(String name) {
        Map<String, String[]> map = this.getParameterMap();
        return (String[]) map.get(name);
    }

    private void initParameterMap() {
        boolean isJSON = false;

        try {
            String src = ParamHelper.parsePostParam(this.getRequest());
            this.content = Coder.dec(src, this.mode);
            this.logger.debug("解码后的Body为：" + this.content);
            this.json = JSONObject.parseObject(this.content);
            isJSON = true;
            this.parameterFormat = "JSON";
        } catch (Exception var14) {
            this.logger.debug("请求的数据不是JSON格式：" + this.content);
            isJSON = false;
        }

        if (!isJSON) {
            this.parameterFormat = "FROM";
            String[] pairs = this.content.split("&");
            String[] var3 = pairs;
            int var4 = pairs.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String p = var3[var5];
                if (!StringUtils.isEmpty(p)) {
                    String[] array = p.split("=");
                    String k = array[0];
                    String v = p.substring(k.length() + 1);

                    try {
                        v = URLDecoder.decode(v, "UTF-8");
                    } catch (UnsupportedEncodingException var13) {
                        this.logger.error(var13.getMessage(), var13);
                    }

                    String[] valueArray = (String[]) this.parameterMap.get(k);
                    if (valueArray == null) {
                        valueArray = new String[]{v};
                    } else {
                        int length = valueArray.length;
                        String[] _valueArray = new String[length + 1];
                        System.arraycopy(valueArray, 0, _valueArray, 0, length);
                        _valueArray[length] = v;
                        valueArray = _valueArray;
                    }

                    this.parameterMap.put(k, valueArray);
                }
            }
        }

    }

    public ServletInputStream getInputStream() throws IOException {
        if (this.inputStream == null) {
            if (this.content == null) {
                this.initParameterMap();
            }

            assert this.content != null;

            byte[] buffer = this.content.getBytes("UTF-8");
            this.inputStream = new CodecInputStream(buffer);
        }

        assert this.inputStream != null;

        return this.inputStream;
    }

    public int getContentLength() {
        if (this.content == null) {
            this.initParameterMap();
        }

        assert this.content != null;

        try {
            byte[] buffer = this.content.getBytes("UTF-8");
            return buffer.length;
        } catch (UnsupportedEncodingException var2) {
            this.logger.error(var2.getMessage(), var2);
            return 0;
        }
    }

    public long getContentLengthLong() {
        return Integer.valueOf(this.getContentLength()).longValue();
    }
}
