package org.rhine.annotation;

import java.lang.annotation.*;

/**
 * @author qs.zhou
 * @date 2019/06/20
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestMapping {

    String value();

}
