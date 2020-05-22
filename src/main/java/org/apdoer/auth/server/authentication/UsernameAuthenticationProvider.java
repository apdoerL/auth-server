package org.apdoer.auth.server.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * 用户名登录校验逻辑
 * @author apdoer
 * @version 1.0
 * @date 2020/5/22 10:55
 */
@Slf4j
@Component
public class UsernameAuthenticationProvider implements AuthenticationProvider {

    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Autowired
    private UserDetailsService customUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernameAuthenticationToken usernameAuthenticationToken = (UsernameAuthenticationToken) authentication;

        String principal = usernameAuthenticationToken.getPrincipal().toString();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(principal);
        if (null == userDetails){
            log.debug("Authentication failed: no credentials provided");
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.noopBindAccount",
                    "Noop Bind Account"));
        }
        UsernameAuthenticationToken token = new UsernameAuthenticationToken(userDetails, userDetails.getAuthorities());
        token.setDetails(usernameAuthenticationToken.getDetails());
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernameAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
