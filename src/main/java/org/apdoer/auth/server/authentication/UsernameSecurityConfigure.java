package org.apdoer.auth.server.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 用户名登录配置入口
 * @author apdoer
 * @version 1.0
 * @date 2020/5/22 14:41
 */
@Component
public class UsernameSecurityConfigure extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private AuthenticationSuccessHandler usernameAuthenticationSuccessHandler;

    @Autowired
    private UsernameAuthenticationProvider usernameAuthenticationProvider;

    @Override
    public void configure(HttpSecurity builder) {
        UsernameAuthenticationFilter usernameAuthenticationFilter = new UsernameAuthenticationFilter();
        usernameAuthenticationFilter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
        usernameAuthenticationFilter.setAuthenticationSuccessHandler(usernameAuthenticationSuccessHandler);

        builder.authenticationProvider(usernameAuthenticationProvider)
                .addFilterAfter(usernameAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
