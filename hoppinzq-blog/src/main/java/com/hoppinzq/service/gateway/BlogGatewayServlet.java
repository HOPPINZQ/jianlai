package com.hoppinzq.service.gateway;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 初始化网关及指定请求 URL:http://localhost:${port}/hoppinzq?xxx
 * 可以使用@WebServlet注解自动注册
 * @author:ZhangQi
 */
@WebServlet(urlPatterns = "/hoppinzq")
@MultipartConfig//标识Servlet支持文件上传
public class BlogGatewayServlet extends HttpServlet {
    private static final long serialVersionUID=1L;
    ApplicationContext context;
    private BlogGateway apiHandler;

    @Override
    public void init() throws ServletException {
        super.init();
        context= WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        apiHandler=context.getBean(BlogGateway.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        Request request1=(Request)request;
        ContextHandler contextHandler = request1.getContext().getContextHandler();
        contextHandler.setMaxFormContentSize(6000000);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "4200");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        apiHandler.handle(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Request request1=(Request)request;
        ContextHandler contextHandler = request1.getContext().getContextHandler();
        contextHandler.setMaxFormContentSize(6000000);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "4200");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        apiHandler.handle(request,response);
    }
}
