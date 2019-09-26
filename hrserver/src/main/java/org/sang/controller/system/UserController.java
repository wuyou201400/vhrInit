package org.sang.controller.system;

import org.sang.bean.RespBean;
import org.sang.service.HrService;
import org.sang.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

/**
 * Created by sang on 2017/12/24.
 */
@RestController
public class UserController {

    @Autowired
    HrService userService;

    @RequestMapping("/currentUserName")
    public String currentUserName() {
        return Util.getCurrentUser().getName();
    }

    @RequestMapping("/currentUserId")
    public Long currentUserId() {
        return Util.getCurrentUser().getId();
    }

    @RequestMapping("/currentUserEmail")
    public String currentUserEmail() {
        return Util.getCurrentUser().getEmail();
    }

    @RequestMapping("/isAdmin")
    public Boolean isAdmin() {
        Collection<? extends GrantedAuthority> authorities = Util.getCurrentUser().getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().contains("admin")) {
                return true;
            }
        }
        return false;
    }

    @RequestMapping(value = "/updateUserEmail",method = RequestMethod.PUT)
    public RespBean updateUserEmail(String email) {
        if (userService.updateUserEmail(email) == 1) {
            return RespBean.ok("开启成功!");
        }
        return RespBean.error( "开启失败!");
    }
}
