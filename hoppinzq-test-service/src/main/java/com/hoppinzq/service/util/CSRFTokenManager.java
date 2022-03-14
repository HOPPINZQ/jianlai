package com.hoppinzq.service.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.UUID;

public final class CSRFTokenManager {

	/**
	 * The token parameter name
	 */
	static final String CSRF_PARAM_NAME = "ZQtoken";
    static final String CSRF_PARAM_NAME1 = "description";

	/**
	 * The location on the session which stores the token
	 */
	public static final String CSRF_TOKEN_FOR_SESSION_ATTR_NAME = CSRFTokenManager.class.getName() + ".tokenval";

	public static boolean hasToken(HttpSession session) {
		return session.getAttribute(CSRF_TOKEN_FOR_SESSION_ATTR_NAME) == null ? false : true;
	}

	public static String getTokenForSession(HttpSession session) {
		String token = null;

		synchronized (session) {
			token = (String) session.getAttribute(CSRF_TOKEN_FOR_SESSION_ATTR_NAME);
			if (null == token) {
				token = UUID.randomUUID().toString();
				session.setAttribute(CSRF_TOKEN_FOR_SESSION_ATTR_NAME, token);
			}
		}
		return token;
	}

	/**
	 * Extracts the token value from the session
	 * 
	 * @param request
	 * @return
	 */
	public static String getTokenFromRequest(HttpServletRequest request,HttpSession session) {

        System.err.println("qweqweqwewqeq: " + request.getHeader(CSRF_PARAM_NAME1));
        //获取客户端向服务器端传送数据的协议名称
        System.err.println("rotocol: " + request.getProtocol());
        //返回的协议名称.默认是http
        System.err.println("Scheme: " + request.getScheme());
        //可以返回当前页面所在的服务器的名字;如果你的应用部署在本机那么其就返回localhost或者127.0.0.1 ，这两个是等价的
        System.err.println("ServerName: " + request.getServerName());
        //可以返回当前页面所在的服务器使用的端口,就是8083
        System.err.println("ServerPort: " + request.getServerPort());
        //request.getRemoteAddr()是获得客户端的ip地址
        System.err.println("RemoteAddr: " + request.getRemoteAddr());
        //request.getRemoteHost()是获得客户端的主机名。
        System.err.println("RemoteHost: " + request.getRemoteHost());
        //返回字符编码
        System.err.println("CharacterEncoding: " + request.getCharacterEncoding());
        //描述HTTP消息实体的传输长度
        System.err.println("ContentLength: " + request.getContentLength());
        //定义网络文件的类型和网页的编码，决定浏览器将以什么形式、什么编码读取这个文件，
        System.err.println("ContentType: " + request.getContentType());
        //如果servlet由一个鉴定方案所保护，如HTTP基本鉴定，则返回方案名称
        System.err.println("AuthType: " + request.getAuthType());
        //返回HTTP请求方法（例如GET、POST等等）
        System.err.println("HttpMethod: " + request.getMethod());
        //返回在URL中指定的任意附加路径信息。
        System.err.println("pathInfo: " + request.getPathInfo());
        //返回在URL中指定的任意附加路径信息，被子转换成一个实际路径
        System.err.println("pathTrans: " + request.getPathTranslated());
        //返回查询字符串，即URL中?后面的部份。
        System.err.println("QueryString: " + request.getQueryString());
        //如果用户通过鉴定，返回远程用户名，否则为null。
        System.err.println("RemoteUser: " + request.getRemoteUser());
        //返回客户端的会话ID
        System.err.println("SessionId: " + request.getRequestedSessionId());
        //返回URL中一部分，从“/”开始，包括上下文，但不包括任意查询字符串。
        System.err.println("RequestURI: " + request.getRequestURI());
        //返回请求URI上下文后的子串
        System.err.println("ServletPath: " + request.getServletPath());
        //返回指定的HTTP头标指。如果其由请求给出，则名字应为大小写不敏感。
        System.err.println("Accept: " + request.getHeader("Accept"));
        //获取请求的头部信息
        System.err.println("Host: " + request.getHeader("Host"));
        //获取来源页地址
        System.err.println("Referer : " + request.getHeader("Referer"));
        //获取请求方地址
        System.err.println("Origin : " + request.getHeader("Origin"));
        //获取请求方语言
        System.err.println("Accept-Language : " + request.getHeader("Accept-Language"));
        //浏览器支持的编码类型
        System.err.println("Accept-Encoding : " + request.getHeader("Accept-Encoding"));
        //浏览器标识
        System.err.println("User-Agent : " + request.getHeader("User-Agent"));
        //在浏览器中不设置Connection，会默认是keep-alive（长连接）
        System.err.println("Connection : " + request.getHeader("Connection"));
        //获取请求方Cookie
        System.err.println("Cookie : " + request.getHeader("Cookie"));
        //获取请求方时间
        System.err.println("Created : " + session.getCreationTime());
        System.err.println("LastAccessed : " + session.getLastAccessedTime());


        System.err.println("------获取单个请求头name对应的value值--------");
        String headValue = request.getHeader("Accept-Encoding");
        System.err.println(headValue);

        System.err.println("------获取多个同名请求头对应的一组value值，因此返回枚举类型数据--------");
        /**
         * 获取多个同名请求头对应的一组value值，因此返回枚举类型数据
         */
        Enumeration enumeration = request.getHeaders("Accept-Encoding");
        /**
         * 将数据遍历出来
         */
        while (enumeration.hasMoreElements()) {
            //遍历枚举中存储的每一个元素
            String value = (String) enumeration.nextElement();
            //将值输出
            System.err.println(value);
        }

        System.err.println("-----获取请求头的所有name值,返回的数据也是一个枚举类型的数据，将枚举中的元素依次遍历出来，根据name获取对应的value值，即可得到Http请求头的所有信息-----------");

        Enumeration er = request.getHeaderNames();
        while (er.hasMoreElements()) {
            String name = (String) er.nextElement();
            String value = request.getHeader(name);
            System.err.println(name + "=" + value);
        }

        return request.getHeader(CSRF_PARAM_NAME);
	}

	private CSRFTokenManager() {
	};

}