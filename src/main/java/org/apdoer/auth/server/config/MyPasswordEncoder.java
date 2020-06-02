package org.apdoer.auth.server.config;

import org.apdoer.common.service.util.EncryptUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 2.0 没有默认的PASSWORDEncoder ,需要提供一个bean
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/24 19:19
 */
@Component
public class MyPasswordEncoder implements PasswordEncoder {

//    @Override
//    public String encode(CharSequence charSequence) {
//        return EncryptUtil.myMD5((String)charSequence);
//    }
//
//    @Override
//    public boolean matches(CharSequence charSequence, String s) {
//        return s.equals(EncryptUtil.myMD5((String)charSequence));
//    }


    @Override
    public String encode(CharSequence charSequence) {
        return ((String) charSequence);
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return s.equals((charSequence));
    }

}
