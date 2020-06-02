package org.apdoer.auth.server.config;

import org.apdoer.auth.server.security.CustomUserDetailsService;
import org.apdoer.common.service.util.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/25 10:17
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private PasswordEncoder myPasswordEncoder;

    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService())
                .passwordEncoder(myPasswordEncoder);
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(new BCryptPasswordEncoder());

//        auth
//                .userDetailsService(userDetailsService())
//                .passwordEncoder(new PasswordEncoder() {
//
//                    @Override
//                    public String encode(CharSequence rawPassword) {
//                        return EncryptUtil.myMD5((String) rawPassword);
//                    }
//
//                    @Override
//                    public boolean matches(CharSequence rawPassword, String encodedPassword) {
//                        return encodedPassword.equals(EncryptUtil.myMD5((String) rawPassword));
//                    }
//                });
//        auth.userDetailsService(userDetailsService())
//                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }


    /**
     * 这个bean不定义的话,不支持password模式
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
