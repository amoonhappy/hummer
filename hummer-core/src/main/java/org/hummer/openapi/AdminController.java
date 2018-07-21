package org.hummer.openapi;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.Api;
import org.apache.commons.collections.FastHashMap;
import org.hummer.core.config.impl.SupportedComponent;
import org.hummer.core.container.HummerContainer;
import org.hummer.core.util.ObjectUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import java.util.*;

@RequestMapping("/rest/hummer")
@RestController
@Api(tags = "Hummer Framework容器类")
@SuppressWarnings("all")
public class AdminController {
    /*
        @RequestMapping(value = "info/{userNum}", method = {RequestMethod.GET, RequestMethod.POST})
        public Object test1(@RequestParam(value = "loginName", required = true) String loginName,
                            @PathVariable(value = "userNum") String userNum,
                            ModelAndView mv) {
            mv.addObject("userName", "wolf");
            mv.addObject("id", 35323L);
            mv.setViewName("redirect:/user/index.jsp");

            return mv;
        }*/
    @RequestMapping(value = "/cacheKeyMapping", method = {RequestMethod.GET})
    public Map<String, String> getHummerCacheKeyMapping() {
        HummerContainer container = HummerContainer.getInstance();
        Map<String, String> ret = new FastHashMap();
        Map<?, ?> cachedRedisKeys = container.getCachedRedisKeys();
        ret.put("1-Total stored Hummer Cache Keys: ", String.valueOf(cachedRedisKeys.size()));
        ret.put("2-Stored CacheKeys", JSONArray.toJSONString(cachedRedisKeys));
        return ret;
    }

    @RequestMapping(value = "/beanList", method = {RequestMethod.GET})
    public Set<String> getHummerBeanList() {
        HummerContainer container = HummerContainer.getInstance();
        Set<String> ret = new LinkedHashSet<>();
        Set<String> beanList = container.getBeanFactory().getHummerBeanList();
        ret.add("Total Initiated Hummer Bean: " + beanList.size());
        ret.addAll(beanList);

        return ret;
    }

    @RequestMapping(value = "/postSpringbeanList", method = {RequestMethod.GET})
    public List<String> getPostAutowireSpringBeanNameList() {
        HummerContainer container = HummerContainer.getInstance();
        List<String> ret = new LinkedList<>();
        List<String> beanList = container.getPostAutowireSpringBeanNameList();
        ret.add("Total Post Initiated Spring Bean: " + beanList.size());
        ret.addAll(beanList);
        return ret;
    }

    @RequestMapping(value = "/serverInfo", method = {RequestMethod.GET})
    public Map<String, String> serverInfo(ServletRequest request) {
        Map<String, String> ret = new HashMap<>();
        ret.put("Server Name", request.getServerName());
        ret.put("Server IP", request.getRemoteAddr());
        ret.put("Server Port", String.valueOf(request.getServerPort()));
        return ret;
    }

    @RequestMapping(value = "/status", method = {RequestMethod.GET})
    public Map<String, String> getHummerStatus() {
        HummerContainer container = HummerContainer.getInstance();
        Map<String, String> ret = new HashMap<>();
        ret = checkHummerStatus(container, ret);
        return ret;
    }

    private Map<String, String> checkHummerStatus(HummerContainer hummerContainer, Map<String, String> ret) {
        if (!ObjectUtil.isNull(hummerContainer)) {
            Set<SupportedComponent.SupportedComponentInfo> supportedComponentInfos = hummerContainer.getConfigManager().getSupportedComponents();
            String dataSourceType = hummerContainer.getDataSourcePoolType();
            ret.put("retCode", "0");
            ret.put("retMsg", "Hummer Container is initialized");
            ret.put("Data Source Type", dataSourceType);
            ret.put("HummerContainer", hummerContainer.toString());
            ret.put("ComponentConfigManager", hummerContainer.getConfigManager().toString());
            ret.put("BeanFactory", hummerContainer.getBeanFactory().toString());
            ret.put("SpringContext", hummerContainer.getSpringContext().toString());
            ret.put("Supported Components:", supportedComponentInfos.toString());
        } else {
            ret.put("retCode", "-1");
            ret.put("retMsg", "Hummer Container is not initialized");
        }
        return ret;
    }

    @RequestMapping(value = "/reInit", method = {RequestMethod.GET})
    public Map<String, String> reInitHummerContainer() {
        Map<String, String> ret = new HashMap<>();
        HummerContainer hummerContainer = HummerContainer.getInstance();
        hummerContainer.reInit();
        HummerContainer newHummerContainer = HummerContainer.getInstance();
        ret = checkHummerStatus(newHummerContainer, ret);
        return ret;
    }
    // for example: init the data of selector in a form
    /*
    @ModelAttribute(value = "statusList")
    public Object status() {
        List list = new ArrayList();
        list.add("ENABLE");
        list.add("DISABLE");
        return list;
    }*/
}
