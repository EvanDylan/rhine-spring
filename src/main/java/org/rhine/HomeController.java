package org.rhine;

import org.rhine.annotation.Autowried;
import org.rhine.annotation.Controller;
import org.rhine.annotation.RequestMapping;
import org.rhine.annotation.RequestParam;

@Controller("/")
public class HomeController {

    @Autowried
    private UserService userService;

    @RequestMapping("hello")
    public String hello(@RequestParam("name") String name) {
        return userService.dear(name);
    }

}
