package com.hoppinzq.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;


/**
 * @Description: socket配置类,往 spring 容器中注入ServerEndpointExporter实例
 */

//@Configuration
//@EnableWebSocketMessageBroker  //注解开启STOMP协议来传输基于代理的消息，此时控制器支持使用@MessageMapping
//public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.enableSimpleBroker("/topic","/user");//topic用来广播，user用来实现p2p
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/webServer").withSockJS();
//        registry.addEndpoint("/queueServer").withSockJS();//注册两个STOMP的endpoint，分别用于广播和点对点
//    }
//
//}
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}

