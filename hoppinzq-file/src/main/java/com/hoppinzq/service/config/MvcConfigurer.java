package com.hoppinzq.service.config;

import com.hoppinzq.service.constants.ProjectParm;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;


/**
 * 配置全局MVC特性
 * @author:ZhangQi
 **/

@Configuration
public class MvcConfigurer implements WebMvcConfigurer {

    /**
     * 配置拦截器
     * @param registry
     */
    public void addInterceptors(InterceptorRegistry registry){

        //URL使用user开头的请求进入此拦截器
//        registry.addInterceptor(new UserSessionHandlerInterceptor()).addPathPatterns("/user/**");
//        registry.addInterceptor(new BlogSessionHandlerInterceptor()).addPathPatterns("/blog/*/deleteBlog").addPathPatterns("/video/*/deleteVideo");
//        registry.addInterceptor(new Urladd()).addPathPatterns("/**").excludePathPatterns("/static/**");
    }

    /**
     * 配置跨域访问
     * @param registry
     */
    public void addCorsMappings(CorsRegistry registry){
        //registry.addMapping("*/**");//允许所有跨域访问
        registry.addMapping("/**")     //允许的路径
                .allowedMethods("*")     //允许的方法
                .allowedOrigins("*")       //允许的网站
                .allowedHeaders("*")     //允许的请求头
                .allowCredentials(true)
                .maxAge(3600);
        //允许某某网址的跨域访问，限定访问路径是/api，限定方法是get，post
        //registry.addMapping("/api/**").allowedOrigins("https://domain2.com").allowedMethods("GET","POST");

    }

    /**
     * 配置转换器
     * @param registrar
     */
    public void addFormatters(FormatterRegistrar registrar){

    }

    /**
     * 配置URI到视图的映射
     * @param registry
     */
    public void addViewController(ViewControllerRegistry registry){

    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {

    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {

    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {

    }

    @Override
    public void addFormatters(FormatterRegistry registry) {

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("windows")) {
            registry.addResourceHandler("/blog/**").addResourceLocations("file:D:/baby_img/blog/");
            ProjectParm.resourcesPath="D:\\baby_img\\";
        }else{
            registry.addResourceHandler("/blog/**").addResourceLocations("file:/home/file/blog/");
            ProjectParm.resourcesPath="/home/file/";
        }
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {

    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {

    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {

    }

    @Override
    public Validator getValidator() {
        return null;
    }

    @Override
    public MessageCodesResolver getMessageCodesResolver() {
        return null;
    }
}

