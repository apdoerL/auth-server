package org.apdoer.auth.server.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 包扫描
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/22 11:01
 */
@ComponentScan({"org.apdoer.common.service.service"})
@MapperScan(basePackages = {"org.apdoer.common.service.mapper"})
@Configuration
public class ServiceScanConfig {
}
