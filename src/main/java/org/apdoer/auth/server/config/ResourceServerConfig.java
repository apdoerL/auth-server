package org.apdoer.auth.server.config;

import org.apdoer.auth.server.authentication.UsernameSecurityConfigure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;

/** 配置SpringSecurity 资源配置服务类
 * @author apdoer
 * @version 1.0
 * @date 2020/5/22 14:49
 */
@EnableResourceServer
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private UsernameSecurityConfigure usernameSecurityConfigure;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/getUser",
                        "pauth/token",
                        "refresh").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
        http.apply(usernameSecurityConfigure);
    }


}

