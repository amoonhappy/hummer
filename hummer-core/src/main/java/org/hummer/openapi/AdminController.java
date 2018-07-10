package org.hummer.openapi;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hummer.core.config.impl.SupportedAppInfos;
import org.hummer.core.container.impl.HummerContainer;
import org.hummer.core.container.intf.IBusinessServiceManager;
import org.hummer.core.container.intf.IHummerContainer;
import org.hummer.core.util.ObjectUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    @ApiOperation(value = "测试", notes = "测试Hummer正常启动", httpMethod = "GET")
    @RequestMapping(value = "/test", method = {RequestMethod.GET})
    public Map<String, String> testHummer() {
        Map<String, String> ret = new HashMap<>();
        IHummerContainer hummerContainer = HummerContainer.getInstance();
        IBusinessServiceManager businessServiceManager = hummerContainer.getServiceManager();
        Object o = businessServiceManager.getService("testService");
        ret.put("Test Service", o.toString());
        return ret;
    }

    @ApiOperation(value = "服务器信息", notes = "提供Hummer服务器IP，端口，名称等信息", httpMethod = "GET")
    @RequestMapping(value = "/serverInfo", method = {RequestMethod.GET})
    public Map<String, String> serverInfo(ServletRequest request) {
        Map<String, String> ret = new HashMap<>();
        ret.put("Server Name", request.getLocalName());
        ret.put("Server IP", request.getLocalAddr());
        ret.put("Server Port", String.valueOf(request.getLocalPort()));
        return ret;
    }

    @ApiOperation(value = "Hummer状态", notes = "返回Hummer状态信息", httpMethod = "GET")
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

    @ApiOperation(value = "Hummer容器重新初始化", notes = "Hummer容器重新初始化", httpMethod = "GET")
    @RequestMapping(value = "/reInit", method = {RequestMethod.GET})
    public Map<String, String> reInitHummerContainer() {
        Map<String, String> ret = new HashMap<>();
        IHummerContainer iHummerContainer = HummerContainer.getInstance();
        iHummerContainer.reInit();
        IHummerContainer newHummerContainer = HummerContainer.getInstance();
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
