package org.hummer.newweb.controller;

import org.hummer.core.container.impl.HummerContainer;
import org.hummer.core.container.intf.IBusinessServiceManager;
import org.hummer.core.container.intf.IHummerContainer;
import org.hummer.newweb.model.impl.User;
import org.hummer.newweb.service.user.intf.ITestService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/rest/v1")
@RestController
public class TestController {
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
    @RequestMapping(value = "/user/all", method = {RequestMethod.GET})
    public List selectAllUser() {
        IHummerContainer iHummerContainer = HummerContainer.getInstance();
        IBusinessServiceManager bsv = iHummerContainer.getServiceManager();
        ITestService testService = (ITestService) bsv.getService("testService");
        return (List) testService.getAllUsers();
    }

    @RequestMapping(value = "/user/{userId}", method = {RequestMethod.GET})
    public List getUserById(@PathVariable("userId") String userId) {
        IHummerContainer iHummerContainer = HummerContainer.getInstance();
        IBusinessServiceManager bsv = iHummerContainer.getServiceManager();
        ITestService testService = (ITestService) bsv.getService("testService");
        //Map ret = new HashMap();
        //ret.put("1",testService.getUserById(Integer.valueOf(userId)));
        List ret = new ArrayList();
        ret.add(testService.getUserById(Integer.valueOf(userId)));
        return ret;
    }

    @RequestMapping(value = "/user/{userId}", method = {RequestMethod.DELETE})
    public Object deleteUserById(@PathVariable("userId") String userId) {
        IHummerContainer iHummerContainer = HummerContainer.getInstance();
        IBusinessServiceManager bsv = iHummerContainer.getServiceManager();
        ITestService testService = (ITestService) bsv.getService("testService");
        testService.deleteUser(userId);
        return "successful for userId delete" + userId;
    }

    @RequestMapping(value = "/user", method = {RequestMethod.POST})
    public Object insertUser(User user) {
        IHummerContainer iHummerContainer = HummerContainer.getInstance();
        IBusinessServiceManager bsv = iHummerContainer.getServiceManager();
        ITestService testService = (ITestService) bsv.getService("testService");
        testService.insertUser(user);
        System.out.println(user.getFirstName());
//        testService.insertUser(mv.getModel());
        return user;
    }

    @RequestMapping(value = "/user", method = {RequestMethod.PUT})
    public Object updateUserById(User user) {
        IHummerContainer iHummerContainer = HummerContainer.getInstance();
        IBusinessServiceManager bsv = iHummerContainer.getServiceManager();
        ITestService testService = (ITestService) bsv.getService("testService");
//        user.setId(userId);
        testService.updateUser(user);
        System.out.println(user.getFirstName());
        return user;
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
