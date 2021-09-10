package com.hoppinzq.service.brap.common;

import java.io.Serializable;

/**
 * 标记类
 * 请求调用的服务方法的参数是输入流的，比如，有个在线文字识别的服务需要传入一张图片的输入流的参数，
 * 该参数将被该类替换，以便注册中心可以根据该类序列化请求，通过http输出流重新路由
 */
public class InputStreamArgumentPlaceholder implements Serializable {}
