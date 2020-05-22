package org.apdoer.auth.server.authentication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户名登录验证filter
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/22 11:41
 */
public class UsernameAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String USERNAME_TOKEN_URL = "/username/token";
    private static final String SPRING_SECURITY_FROM_USERNAME_KEY = "username";
    private static final String AUTH_METHOD = "POST";

    @Getter
    @Setter
    private String usernameParameter = SPRING_SECURITY_FROM_USERNAME_KEY;

    @Getter
    @Setter
    private boolean postOnly = true;

    public UsernameAuthenticationFilter() {
        super(new AntPathRequestMatcher(USERNAME_TOKEN_URL, "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (postOnly && !request.getMethod().equals(AUTH_METHOD)) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String username = obtainUsername(request);
        if (username == null) {
            username = "";
        }
        username = username.trim();
        UsernameAuthenticationToken authenticationToken = new UsernameAuthenticationToken(username);

        // Allow subclasses to set the details property
        setDetails(request, authenticationToken);
        return getAuthenticationManager().authenticate(authenticationToken);
    }

    private void setDetails(HttpServletRequest request, UsernameAuthenticationToken authenticationToken) {
        authenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    private String obtainUsername(HttpServletRequest request) {
        return request.getParameter(usernameParameter);
    }

}
