package org.hummer.newweb.controller;

import io.swagger.annotations.Api;
import org.hummer.core.ioc.annotation.Autowired;
import org.hummer.core.ioc.annotation.BeanType;
import org.hummer.core.ioc.annotation.HummerPostAutowired;
import org.hummer.newweb.model.impl.User;
import org.hummer.newweb.model.intf.IUser;
import org.hummer.newweb.service.user.intf.ITest1Service;
import org.hummer.newweb.service.user.intf.ITestService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/rest/v1")
@RestController
@Api(tags = "Test Controller例子類")
@HummerPostAutowired
public class TestController {
    @Autowired(BeanType.HUMMER_BEAN)
    ITestService testService;
    @Autowired(BeanType.HUMMER_BEAN)
    ITest1Service test1Service;
    /*
        @RequestMapping(value = "info/{userNum}", method = {RequestMethod.GET, RequestMethod.POST})
        public Object test1(@RequestParam(value = "loginName", required = true) String loginName,
                            @PathVariable(value = "userNum") String userNum,
                            ModelAndView mv) {
            mv.addObject("userName", "wolf");
            mv.addObject("id", 35323L);
            mv.setViewName("redirect:/user/index.jsp");

            return mv;
        }
    */

    @RequestMapping(value = "/user/{firstName}/{role}", method = {RequestMethod.GET})
    public List selectActiveUsersByName(@PathVariable("firstName") String firstName, @PathVariable("role") String role) {
        IUser user = new User();
        user.setFirstName(firstName);
        user.setRole(role);
        return testService.getUserByFirstNameAndStatus(user);
    }

    @RequestMapping(value = "/user/all", method = {RequestMethod.GET})
    public List selectAllUser() {
        return (List) testService.getAllUsers();
    }

    @RequestMapping(value = "/user/{userId}", method = {RequestMethod.GET})
    public List<org.hummer.newweb.model.intf.IUser> getUserById(@PathVariable("userId") String userId) {
        List<org.hummer.newweb.model.intf.IUser> ret = new ArrayList<>();
        ret.add(testService.getUserById(Long.valueOf(userId)));
        return ret;
    }

    @RequestMapping(value = "/user/{userId}", method = {RequestMethod.DELETE})
    public Object deleteUserById(@PathVariable("userId") String userId) {
        testService.deleteUser(userId);
        return "successful for userId delete" + userId;
    }

    @RequestMapping(value = "/user", method = {RequestMethod.POST})
    public Object insertUser(User user) {
        testService.insertUser(user);
        return user;
    }

    @RequestMapping(value = "/user", method = {RequestMethod.PUT})
    public Object updateUserById(User user) {
        testService.updateUser(user);
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
