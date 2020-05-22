package org.apdoer.auth.server.security;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.auth.server.enums.UserEnabledStatusEnum;
import org.apdoer.common.service.model.po.PermissionPo;
import org.apdoer.common.service.model.po.UserPo;
import org.apdoer.common.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/22 10:59
 */
@Component
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserPo userPo = userService.getUserByUsername(username);
        if (userPo != null && userPo.getEnabled() != null && userPo.getEnabled().equals(UserEnabledStatusEnum.ENABLED.getCode())) {
            List<PermissionPo> permissionList = userService.getPermissionList(userPo.getId());
            Set<GrantedAuthority> authorities = new HashSet<>();
            for (PermissionPo permissionPo : permissionList) {
                authorities.add(new SimpleGrantedAuthority(permissionPo.getValue()));
            }
            log.info("======={} auth success", username);
            return new User(userPo.getUsername(), userPo.getPassword(), authorities);
        } else {
            log.error("====={} auth failed", username);
            throw new UsernameNotFoundException("用户[" + username + "]不存在");
        }
    }
}
