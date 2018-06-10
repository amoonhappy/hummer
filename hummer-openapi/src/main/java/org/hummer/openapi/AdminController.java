package org.hummer.openapi;

import org.hummer.core.config.impl.SupportedAppInfos;
import org.hummer.core.container.impl.HummerContainer;
import org.hummer.core.container.intf.IBusinessServiceManager;
import org.hummer.core.container.intf.IHummerContainer;
import org.hummer.core.util.ObjectUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequestMapping("/rest/hummer")
@RestController
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
    @RequestMapping(value = "/test", method = {RequestMethod.GET})
    public Map<String, String> testHummer() {
        Map<String, String> ret = new HashMap<>();
        IHummerContainer hummerContainer = HummerContainer.getInstance();
        IBusinessServiceManager businessServiceManager = hummerContainer.getServiceManager();
        Object o = businessServiceManager.getService("testService");
        ret.put("Test Service", o.toString());
        return ret;
    }

    @RequestMapping(value = "/serverInfo", method = {RequestMethod.GET})
    public Map<String, String> serverInfo(ServletRequest request) {
        Map<String, String> ret = new HashMap<>();
        ret.put("Server Name", request.getLocalName());
        ret.put("Server IP", request.getLocalAddr());
        ret.put("Server Port", String.valueOf(request.getLocalPort()));
        return ret;
    }

    @RequestMapping(value = "/status", method = {RequestMethod.GET})
    public Map<String, String> getHummerStatus() {
        IHummerContainer iHummerContainer = HummerContainer.getInstance();
        Map<String, String> ret = new HashMap<>();
        ret = checkHummerStatus(iHummerContainer, ret);
        return ret;
    }

    private Map<String, String> checkHummerStatus(IHummerContainer iHummerContainer, Map<String, String> ret) {
        if (!ObjectUtil.isNull(iHummerContainer)) {
            Set<SupportedAppInfos.SupportedAppInfo> supportedAppInfos = iHummerContainer.getConfigManager().getSupportedComponents();
            String dataSourceType = iHummerContainer.getDataSourcePoolType();
            ret.put("retCode", "0");
            ret.put("retMsg", "Hummer Container is initialized");
            ret.put("Data Source Type", dataSourceType);
            ret.put("HummerContainer", iHummerContainer.toString());
            ret.put("Supported Components:", supportedAppInfos.toString());
        } else {
            ret.put("retCode", "-1");
            ret.put("retMsg", "Hummer Container is not initialized");
        }
        return ret;
    }

    @RequestMapping(value = "/reInit", method = {RequestMethod.GET})
    public Map<String, String> reInitHummerContainer() {
        Map<String, String> ret = new HashMap<>();
        IHummerContainer iHummerContainer = HummerContainer.getInstance();
        iHummerContainer.reInit();
        IHummerContainer newHummerContainer = HummerContainer.getInstance();
        ret = checkHummerStatus(newHummerContainer, ret);
        return ret;
    }

    @RequestMapping(value = "/user/{userId}", method = {RequestMethod.GET})
    public List getUserById(@PathVariable("userId") String userId) {
        IHummerContainer iHummerContainer = HummerContainer.getInstance();
        IBusinessServiceManager bsv = iHummerContainer.getServiceManager();

        return null;
    }

    @RequestMapping(value = "/user/{userId}", method = {RequestMethod.DELETE})
    public Object getUser(@PathVariable("userId") String userId) {
//        IHummerContainer iHummerContainer = HummerContainer.getInstance();
//        IBusinessServiceManager bsv = iHummerContainer.getServiceManager();
//        ITestService testService = (ITestService) bsv.getService("testService");
        return "successful for userId delete" + userId;
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
