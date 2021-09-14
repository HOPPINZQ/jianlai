package com.hoppinzq.service.util;

import javax.servlet.http.Cookie;

/**
 * @author:ZhangQi
 **/
public class CookieUtil {

	/**
	 * 从一个cookie数组中找到具体我们想要的cookie对象
	 */
	public static Cookie findCookie(Cookie[] cookies , String name){
		if(cookies != null){
			for (Cookie cookie : cookies) {
				if(name.equals(cookie.getName())){
					return cookie;
				}
			}
		}
		return null;
	}

	public static Cookie createCookie(String k,String v){
	    return new Cookie(k,v);
    }
}
