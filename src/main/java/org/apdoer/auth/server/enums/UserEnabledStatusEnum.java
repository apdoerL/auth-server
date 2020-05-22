package org.apdoer.auth.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author apdoer
 */
@AllArgsConstructor
@Getter
public enum UserEnabledStatusEnum {

    /**
     * 用户状态
     */
    ENABLED(0, "正常"),
    LOCK(1, "锁定"),
    CANNEL(2, "注销");


    private Integer code;
    private String desc;

}
