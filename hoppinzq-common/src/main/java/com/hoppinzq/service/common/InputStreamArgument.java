package com.hoppinzq.service.common;

import java.io.Serializable;

/**
 * 标记类
 * 请求调用的服务方法的参数是输入流的，比如，有个在线文字识别的服务需要传入一张图片，
 * 输入流的参数将被该类替换，以便服务注册中心可以根据该类序列化请求，通过http输出流重新路由
 */
public class InputStreamArgument implements Serializable {}
