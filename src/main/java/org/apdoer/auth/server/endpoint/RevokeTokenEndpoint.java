package org.apdoer.auth.server.endpoint;

import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.common.service.util.ResultVoBuildUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/22 10:08
 */
@FrameworkEndpoint
public class RevokeTokenEndpoint {

    @Autowired
    @Qualifier("consumerTokenServices")
    private ConsumerTokenServices consumerTokenServices;

    @DeleteMapping("/oauth/token")
    @ResponseBody
    public ResultVo revokeToken(String accessToken) {
        if (consumerTokenServices.revokeToken(accessToken)) {
            return ResultVoBuildUtils.buildSuccessResultVo();
        } else {
            return ResultVoBuildUtils.buildFaildResultVo();
        }

    }
}
