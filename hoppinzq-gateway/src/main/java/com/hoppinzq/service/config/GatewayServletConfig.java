package com.hoppinzq.service.config;

import com.hoppinzq.service.core.APIGatewayServlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * 注册网关服务，请求后缀需要携带/hoppinzq
 * @Conditional 是Spring的注解，声明类在某个判断条件下生效。
 * @ConditionalOnWebApplication 当Spring为web服务时，才使注解的类生效
 * @author: ZhangQi
 */
@ConditionalOnWebApplication
public class GatewayServletConfig{
    @Bean
    public ServletRegistrationBean register() {
        return new ServletRegistrationBean(new APIGatewayServlet(), "/hoppinzq");
    }
}
