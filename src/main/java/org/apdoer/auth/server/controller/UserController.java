package org.apdoer.auth.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.auth.server.enums.UserEnabledStatusEnum;
import org.apdoer.common.service.model.po.UserPo;
import org.apdoer.common.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author Li
 * @version 1.0
 * @date 2020/5/22 9:56
 */
@RestController
@Slf4j
public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

    @GetMapping("/userInfo")
    public UserPo getUserInfo(Principal user) {
        UserPo userPo = userService.getUserByUsername(user.getName());
        if (userPo == null || userPo.getEnabled() != UserEnabledStatusEnum.ENABLED.getCode()) {
            log.error("");
            return null;
        } else {
            log.info("");
            return userPo;
        }
    }
}
