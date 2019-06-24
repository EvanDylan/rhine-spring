package org.rhine.framework;

import org.rhine.framework.annotation.Autowried;
import org.rhine.framework.annotation.Controller;
import org.rhine.framework.annotation.RequestMapping;
import org.rhine.framework.annotation.RequestParam;

@Controller("/")
public class HomeController {

    @Autowried
    private UserService userService;

    @RequestMapping("hello")
    public String hello(@RequestParam("name") String name) {
        return userService.dear(name);
    }

}
