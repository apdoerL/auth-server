package org.apdoer.auth.server.config;

import org.apdoer.auth.server.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * </?>@RefreshScope</> 配置文件自动刷新
 * </>@EnableAuthorizationServer</>配置成授权服务器
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/22 15:11
 */
@RefreshScope
@EnableAuthorizationServer
@Configuration
public class AuthenticationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private Integer accessTokenExpire;

    private Integer refreshTokenExpire;


}
