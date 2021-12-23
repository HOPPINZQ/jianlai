package com.hoppinzq.service.config;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * 保存有进程类的set
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 * @ServerEndpoint 可以把当前类变成websocket服务类
 */
@ServerEndpoint(value = "/websocketProcess/{llq_uuid_zq}")
@Controller
public class WebSocketProcess {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketProcess> webSocketSet = new CopyOnWriteArraySet<WebSocketProcess>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private String userno = "";
    /**
     * 连接建立成功调用的方法
     * */
    @OnOpen
    public void onOpen(Session session,@PathParam(value = "llq_uuid_zq") String llq_uuid_zq) {
        this.session = session;
        this.userno=llq_uuid_zq;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        try {
            System.err.println("生成ID：" + llq_uuid_zq);
            System.err.println("当前在线人数为：" + getOnlineCount());
            sendMessage("生成浏览器标识：" + llq_uuid_zq);
        } catch (IOException e) {
            System.err.println("IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.err.println("有一连接关闭！当前在线人数为：" + getOnlineCount());
    }

    /**
     *
     * @param message
     */
    public void sendCmdMessgae(String message,String uuid){
        for (WebSocketProcess item : webSocketSet) {
            try {
                if(item.userno.equals(uuid)){
                    item.sendMessage(200,message,"base");
                }else{
                    //不在线了
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param message
     */
    public void sendCmdMessgae(int code,String message,String uuid){
        for (WebSocketProcess item : webSocketSet) {
            try {
                if(item.userno.equals(uuid)){
                    item.sendMessage(code,message,"base");
                }else{
                    //不在线了
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param message
     */
    public void sendCmdMessgae(String message,String mark,String uuid){
        for (WebSocketProcess item : webSocketSet) {
            try {
                if(item.userno.equals(uuid)){
                    item.sendMessage(200,message,mark);
                }else{
                    //不在线了
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param message
     */
    public void sendCmdMessgae(int code,String message,String mark,String uuid){
        for (WebSocketProcess item : webSocketSet) {
            try {
                if(item.userno.equals(uuid)){
                    item.sendMessage(code,message,mark);
                }else{
                    //不在线了
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        System.err.println("来自客户端的消息:" + message);
        Map map=session.getRequestParameterMap();
        String user=map.get("userno").toString().substring(1,3);
        //群发消息
        for (WebSocketProcess item : webSocketSet) {
            try {
                if(item.userno.equals(user)){
                    item.sendMessage("zq:"+message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.err.println("发生错误");
        error.printStackTrace();
    }


    public void sendMessage(String message) throws IOException {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",200);
        jsonObject.put("msg",message);
        this.session.getBasicRemote().sendText(jsonObject.toJSONString());
    }

    public void sendMessage(int code,String message) throws IOException {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("msg",message);
        this.session.getBasicRemote().sendText(jsonObject.toJSONString());
    }

    public void sendMessage(int code,String message,String mark) throws IOException {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("msg",message);
        jsonObject.put("mark",mark);
        this.session.getBasicRemote().sendText(jsonObject.toJSONString());
    }


    /**
     * 群发自定义消息
     * */
    public static void sendInfo(String message) throws IOException {
        for (WebSocketProcess item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketProcess.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketProcess.onlineCount--;
    }

}

