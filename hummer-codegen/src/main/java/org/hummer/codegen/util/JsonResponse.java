package org.hummer.codegen.util;


import java.util.HashMap;
import java.util.Map;

/**
 * 返回实体
 *
 * @ClassName: JsonResponse
 * @Description: TODO
 * @author: Jason
 * @date: 2017年12月25日 下午9:57:19
 */
public class JsonResponse extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public JsonResponse() {
        put("code", 0);
    }

    public static JsonResponse error() {
        return error(500, "服务器执行异常，请联系管理员");
    }

    public static JsonResponse error(String msg) {
        return error(500, msg);
    }

    public static JsonResponse error(int code, String msg) {
        JsonResponse r = new JsonResponse();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static JsonResponse ok(String msg) {
        JsonResponse r = new JsonResponse();
        r.put("msg", msg);
        return r;
    }

    public static JsonResponse ok(Map<String, Object> map) {
        JsonResponse r = new JsonResponse();
        r.putAll(map);
        return r;
    }

    public static JsonResponse ok() {
        return new JsonResponse();
    }

    public JsonResponse put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
