package org.rhine;

import org.rhine.annotation.Component;

/**
 * @author qs.zhou
 * @date 2019/06/21
 */
@Component
public class UserService {


    public String dear(String name) {
        return "Dear : " + name;
    }

}
