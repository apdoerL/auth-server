package org.apdoer.auth.server.config;

import org.apdoer.auth.server.security.CustomUserDetailsService;
import org.apdoer.auth.server.tokenstore.ExtRedisTokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

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

    @Autowired(required = false)
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Value("${oauth2.access-token.validity-seconds:43200}")
    private Integer accessTokenExpire;

    @Value("${oauth2.refresh-token.validity-seconds:2592000}")
    private Integer refreshTokenExpire;


    @Bean
    public RedisTokenStore redisTokenStore() {
        return new ExtRedisTokenStore(connectionFactory, accessTokenExpire, refreshTokenExpire);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(customUserDetailsService)
                .tokenStore(redisTokenStore());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient("webapp")
                .scopes("xx")
                .secret("webapp")
                .authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(accessTokenExpire)
                .refreshTokenValiditySeconds(refreshTokenExpire);
    }
}
