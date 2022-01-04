package com.hoppinzq.service.core;

import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.bean.*;
import com.hoppinzq.service.constant.ApiCommConstant;
import com.hoppinzq.service.exception.ResultReturnException;
import com.hoppinzq.service.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;


/**
 * 网关参数处理
 * 通过继承本类，自定义网关的默认行为，如重写afterSuccessRequest方法以在网关成功响应前做一些你自己的事情。
 * 通过RequestContext类，获取该线程本次请求所有信息。使用这些信息，你可以在自定义重写方法做一些事情，如获取用户信息进行鉴权等。
 * 你需要这样做：
 * 1、新建一个gateway类（如BlogGateway）继承本类，添加注解@Component，重写任意要自定义的public方法。
 * 2、新建一个GatewayServlet类（仿照APIGatewayServlet），使用BlogGateway子类作为网关处理器，然后任选一种方法注册servlet。
 *    ①：通过为GatewayServlet类添加@WebServlet注解来将servlet自动注册。
 *    ②：新建一个配置类，使用@Configuration注解或者初始化注解将GatewayServlet注册。
 *    ③：新建一个配置类（仿照GatewayServletConfig）使用@ConditionalOnWebApplication注解，通过开关注解@EnableGateway选择是否为模块启动网关。
 * @author:ZhangQi
 */
@Component
public class ApiGatewayHand implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(ApiGatewayHand.class);

    RequestParam requestParam;
    ApiStore apiStore;

    @Autowired
    private RedisUtils redisUtils;

    final ParameterNameDiscoverer parameterUtil;

    /**
     * 构造器
     */
    public ApiGatewayHand() {
        parameterUtil = new LocalVariableTableParameterNameDiscoverer();
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        apiStore = new ApiStore(context);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        apiStore.loadApiFromSpringBeans();
    }

    /**
     * 具体执行方法
     *
     * @param request
     * @return
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long start = System.currentTimeMillis();
        requestParam=new RequestParam();
        RequestContext.enter(requestParam);
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        requestParam.setRequest(request);
        requestParam.setResponse(response);
        Object result = null;
        ApiRunnable apiRun = null;

        String method=null;
        String params=null;

        RequestInfo requestInfo=null;
        String ip= IPUtils.getIpAddress();
        String id= UUIDUtil.getUUID();
        String url=request.getRequestURL().toString();
        requestParam.setUrl(url);
        Map<String,String> decodeResult=decodeParams(request,response);

        if(decodeResult==null){
            method = request.getParameter(ApiCommConstant.METHOD);
            requestParam.setMethod(method);
            params = request.getParameter(ApiCommConstant.PARAMS);
            requestParam.setParams(params);
            if(params==null){
                List<FormInfo> fileInfos=getPostData(request);
                params=requestParam.getParams();
                if(fileInfos.size()!=0){
                    String methodType=request.getMethod();//GET POST
                    if("GET".equals(methodType)){
                        throw new ResultReturnException(ErrorEnum.ZQ_GATEWAY_FILE_LOAD_MUST_POST);
                    }
                    Map paramsMap=JSONObject.parseObject(params,Map.class);
                    if(paramsMap==null){
                        paramsMap=new HashMap();
                    }
                    StringBuilder formInfoStr=new StringBuilder();
                    formInfoStr.append("[");
                    for(int i=0,j=fileInfos.size();i<j;i++){
                        formInfoStr.append(fileInfos.get(i).toJsonString());
                        if(i<j-1){
                            formInfoStr.append(",");
                        }
                    }
                    formInfoStr.append("]");
                    paramsMap.put("formInfos",formInfoStr);
                    params=JSONObject.toJSONString(paramsMap);
                    requestParam.setParams(params);
                }
            }
        }else{
            method=decodeResult.get(ApiCommConstant.METHOD);
            requestParam.setMethod(method);
            params=decodeResult.get(ApiCommConstant.PARAMS);
            requestParam.setParams(params);
        }

        try {
            apiRun = sysParamsValdate(request,response,method,params);
            sign(request,response);
            token(request,response);
            //cache(request,response);
            logger.debug("请求接口={" + method + "} 参数=" + params + "");
            String timestamp = request.getParameter(ApiCommConstant.TIMESTAMP);
            requestParam.setTimestamp(timestamp);
            ServiceMethodApiBean serviceMethodApiBean=requestParam.getApiRunnable().getServiceMethodApiBean();
            if(timestamp==null&&serviceMethodApiBean.isCache){
                logger.debug("请求的接口方法："+serviceMethodApiBean.getServiceMethod()+"有缓存策略");
                String encodePAM=EncryptUtil.MD5(method+params);
                String encodeCacheKey="APICACHE:"+encodePAM;
                requestParam.setCacheKey(encodeCacheKey);
                requestParam.setCacheTime(serviceMethodApiBean.cacheTime);
                Object cacheResult=redisUtils.get(encodeCacheKey);
                if(cacheResult!=null){
                    result=cacheResult;
                    logger.debug("发现缓存数据！");
                }else{
                    Object[] args = buildParams(apiRun, params, request);
                    result = apiRun.run(args);
                    result = JSONObject.toJSON(ApiResponse.data(result,"操作成功"));
                }
            }else{
                Object[] args = buildParams(apiRun, params, request);
                result = apiRun.run(args);
                result = JSONObject.toJSON(ApiResponse.data(result,"操作成功"));
            }
            long createTime = System.currentTimeMillis();
            logger.debug("接口:" + request.getRequestURL().toString() + " 请求时长:" + (createTime - start));
            requestInfo = new RequestInfo(ip, url,
                    "INFO", method, params,
                    result, DateFormatUtil.stampToDate(createTime), createTime - start
                    , null);
            requestParam.setRequestInfo(requestInfo);
            afterSuccessRequest(request,response);
            logger.debug("请求信息:\n {}", requestInfo.toString());
        } catch (ResultReturnException e) {
            logger.error("调用接口={" + method + "}异常  参数=" + params + "", e);
            requestInfo = new RequestInfo(ip, url, "ERROR",
                    method, params,  null,
                    DateFormatUtil.stampToDate(System.currentTimeMillis()), 0L, e);
            result = handleError(e,requestInfo);
            requestParam.setRequestInfo(requestInfo);
            afterErrorRequest(request,response);
            logger.error("错误的请求:\n {}", requestInfo.toString());
        } catch (InvocationTargetException e) {
            logger.error("调用接口={" + method + "}异常  参数=" + params + "", e.getTargetException());
            requestInfo = new RequestInfo(ip, url, "ERROR",
                    method, params,  null,
                    DateFormatUtil.stampToDate(System.currentTimeMillis()), 0L, e.getTargetException());
            result = handleError(e.getTargetException(),requestInfo);
            requestParam.setRequestInfo(requestInfo);
            afterErrorRequest(request,response);
            logger.error("错误的请求:\n {}", requestInfo.toString());
        } catch (Exception e) {
            logger.error("其他异常", e);
            requestInfo = new RequestInfo(ip, url, "ERROR",
                    method, params,  null,
                    DateFormatUtil.stampToDate(System.currentTimeMillis()), 0L, e);
            result = handleError(e,requestInfo);
            requestParam.setRequestInfo(requestInfo);
            afterErrorRequest(request,response);
            logger.error("错误的请求:\n {}", requestInfo.toString());
        } finally {
            afterRequest(request,response);
            setOutParam(request,response,result);
            RequestContext.exit();
        }
    }

    /**
     * 网关统一返回值封装返回值
     * @param request
     * @param response
     * @param result
     * @throws IOException
     */
    public void setOutParam(HttpServletRequest request,HttpServletResponse response,Object result) throws IOException{
        PrintWriter out = response.getWriter();
        if(requestParam.getCacheKey()!=null&&!redisUtils.hasKey(requestParam.getCacheKey())){
            redisUtils.set(requestParam.getCacheKey(),result.toString(),requestParam.getCacheTime());
        }
        if(requestParam.getApiRunnable()==null){
            out.println(result.toString());
        }else{
            ServiceMethodApiBean serviceMethodApiBean=requestParam.getApiRunnable().getServiceMethodApiBean();
            if(serviceMethodApiBean.methodReturn){
                out.println(result.toString());
            }else{
                JSONObject resultJson=JSONObject.parseObject(result.toString());
                out.println(resultJson.get("data").toString());
            }
        }
    }

    /**
     * 对参数解码，返回解码后的参数列表
     * 重写该方法以实现自定义解码
     * @param request
     * @return
     * @throws ResultReturnException
     */
    public Map<String,String> decodeParams(HttpServletRequest request,HttpServletResponse response) throws ResultReturnException,IOException{
        String encode = request.getParameter(ApiCommConstant.ENCODE);
        requestParam.setEncode(encode);
        if(encode==null){
            return null;
        }else {
            String encodeResult= EncryptUtil.DESdecode(encode,"hoppinzq");
            String encode_str="_>_<_hoppinzq_>_<_";
            if(encodeResult==null){
                throw new ResultReturnException(ErrorEnum.ZQ_GATEWAY_CANT_ENCODE);
            }
            if(encodeResult.indexOf(encode_str)<0){
                throw new ResultReturnException(ErrorEnum.ZQ_GATEWAY_ENCODE_ERROR_FORMAT);
            }
            Map<String,String> map=new HashMap();
            try{
                String method=encodeResult.split(encode_str)[0].split("method=")[1];
                requestParam.setMethod(method);
                String params=encodeResult.split(encode_str)[1].split("params=")[1];
                requestParam.setParams(params);
                map.put(ApiCommConstant.METHOD,method);
                map.put(ApiCommConstant.PARAMS,params);
            }catch (Exception ex){
                throw new ResultReturnException(ErrorEnum.ZQ_GATEWAY_ENCODE_ERROR_FORMAT);
            }
            return map;
        }
    }

    /**
     * 系统参数校验
     * 重写该方法以实现自己的参数校验
     * 返回ApiRunnable对象
     * @param request
     * @return
     * @throws ResultReturnException
     */
    private ApiRunnable sysParamsValdate(HttpServletRequest request,HttpServletResponse response,String apiName,String json) throws ResultReturnException,IOException {
        ApiRunnable api;
        if (apiName == null || apiName.trim().equals("")) {
            throw new ResultReturnException(ErrorEnum.ZQ_GATEWAY_METHOD_NOT_FOUND);
        } else if (json == null) {
            throw new ResultReturnException(ErrorEnum.ZQ_GATEWAY_PARAMS_NOT_FOUND);
        } else if ((api = apiStore.findApiRunnable(apiName)) == null) {
            throw new ResultReturnException(ErrorEnum.errorAddMsg(ErrorEnum.ZQ_GATEWAY_API_NOT_FOUND,",API:"+apiName));
        }
        requestParam.setApiRunnable(api);
        if(!rightCheck(request,response)){
            throw new ResultReturnException(ErrorEnum.COMMON_USER_TOKEN_OUT_DATE);
        }
        return api;
    }

    /**
     * 权限校验
     * 重写该方法以实现自己的权限校验
     * 返回true校验通过
     * 该方法需要注册中心和auth服务，耦合非常高，你可以通过继承本类，并重写rightCheck方法解耦
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public Boolean rightCheck(HttpServletRequest request,HttpServletResponse response) throws IOException{
        return true;
    }


    /**
     * 签名验证
     * 重写该方法以实现自己的签名校验
     * todo
     * @param request
     */
    public void sign(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String sign = request.getParameter(ApiCommConstant.SIGN);
        requestParam.setSign(sign);

//        if (System.currentTimeMillis() - (24 * 60 * 60 * 1000) > Long.parseLong(timestamp)) {
//            throw new ResultReturnException("调用失败：请求已过期");
//        }
        //TODO:签名认证
    }

    /**
     * 缓存
     * 重写该方法实现自己的缓存
     * 直接返回输出流
     * @param request
     * @param response
     * @throws ResultReturnException
     * @throws IOException
     */
    public void cache(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String timestamp = request.getParameter(ApiCommConstant.TIMESTAMP);
        requestParam.setTimestamp(timestamp);
        if(timestamp==null){
            ServiceMethodApiBean serviceMethodApiBean=requestParam.getApiRunnable().getServiceMethodApiBean();
            if(serviceMethodApiBean.isCache){
                PrintWriter out = response.getWriter();
                out.println("走了缓存！");
            }
        }
    }

    /**
     * token 检验token，实现接口幂等
     * 方法被自动幂等注解 AutoIdempotent 所环绕即生效
     * 在请求中，没有token或者过期的token都会抛出异常
     * 校验通过继续往下执行，并在接受到成功的响应中删除token
     * @param request
     * @param response
     * @throws ResultReturnException
     */
    public void token(HttpServletRequest request,HttpServletResponse response) throws ResultReturnException,IOException{
        String token = request.getParameter(ApiCommConstant.TOKEN);
        requestParam.setToken(token);
        ServiceMethodApiBean serviceMethodApiBean=requestParam.getApiRunnable().getServiceMethodApiBean();
        if(serviceMethodApiBean.tokenCheck){
            if(token==null){
                //抛出没有token的异常
                throw new ResultReturnException(ErrorEnum.ZQ_GATEWAY_TOKEN_NOT_FOUND);
            }
            if(!redisUtils.hasKey(token)){
                //token已过期,即重复的请求
                throw new ResultReturnException(ErrorEnum.ZQ_GATEWAY_REQUEST_REPEAT);
            }
        }
    }

    /**
     * 成功后执行操作
     * 重写该方法执行自定义操作
     * @param request
     * @param response
     * @throws IOException
     */
    public void afterSuccessRequest(HttpServletRequest request,HttpServletResponse response) throws IOException{
        System.out.println("请求成功！");
        //1、删除token，无论是否存在redis中
        String token=requestParam.getToken();
        if(token!=null){
            redisUtils.del(requestParam.getToken());
        }
    }

    /**
     * 失败后执行操作
     * 重写该方法执行自定义操作
     * @param request
     * @param response
     */
    public void afterErrorRequest(HttpServletRequest request,HttpServletResponse response) throws IOException{
        System.out.println("请求失败！");
        //可以记录日志等
    }

    /**
     * 请求完成后执行操作（无论成功与否）
     * 总会执行afterSuccessRequest或者afterErrorRequest中一个方法，所以逻辑不要写重了
     * 可以记录日志，但是考虑到要连接数据库且入库操作不能影响正常的响应时间，所以交予各个模块重写该方法以实现异步入库
     * 重写该方法执行自定义操作
     * @param request
     * @param response
     */
    public void afterRequest(HttpServletRequest request,HttpServletResponse response) throws IOException{
        System.out.println("请求完成！");
        //可以删除临时缓存，记录日志等，记录成功响应的日志似乎是多余的。
    }


    /***
     * 验证业务参数，和构建业务参数对象
     * @param run
     * @param paramJson
     * @param request
     * @return
     * @throws ResultReturnException
     */
    private Object[] buildParams(ApiRunnable run, String paramJson, HttpServletRequest request)
            throws ResultReturnException {
        Map<String, Object> map = null;
        try {
            map = JSONUtil.toMap(paramJson);
        } catch (IllegalArgumentException e) {
            throw new ResultReturnException(ErrorEnum.ZQ_GATEWAY_JSON_FORMAT_ERROR);
        }
        if (map == null) {
            map = new HashMap<>();
        }

        Method method = run.getTargetMethod();
        List<String> paramNames = Arrays.asList(parameterUtil.getParameterNames(method));
        Class<?>[] paramTypes = method.getParameterTypes();

        for (Map.Entry<String, Object> m : map.entrySet()) {
            if (!paramNames.contains(m.getKey())) {
                throw new ResultReturnException(ErrorEnum.errorChangeMsg(ErrorEnum.ZQ_GATEWAY_API_METHOD_PARAM_NOT_FOUND,"调用失败：接口不存在‘" + m.getKey() + "’参数"));
            }
        }
        Object[] args = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            if (paramTypes[i].isAssignableFrom(HttpServletRequest.class)) {
                args[i] = request;
            } else if (map.containsKey(paramNames.get(i))) {
                try {
                    args[i] = convertJsonToBean(map.get(paramNames.get(i)), paramTypes[i]);
                } catch (Exception e) {
                    throw new ResultReturnException(ErrorEnum.errorChangeMsg(ErrorEnum.ZQ_GATEWAY_API_METHOD_ERROR_DATA,"调用失败：指定参数格式错误或值错误:‘" + paramNames.get(i) + "’,"
                            + e.getMessage()));
                }
            } else {
                args[i] = null;
            }
        }
        return args;
    }


    /**
     * 将MAP转换成具体的目标方方法参数对象
     *
     * @param val
     * @param targetClass
     * @param <T>
     * @return
     * @throws Exception
     */
    private <T> Object convertJsonToBean(Object val, Class<T> targetClass) throws Exception {
        Object result = null;
        if (val == null) {
            return null;
        } else if (Integer.class.equals(targetClass)) {
            result = Integer.parseInt(val.toString());
        } else if (Long.class.equals(targetClass)) {
            result = Long.parseLong(val.toString());
        } else if (Date.class.equals(targetClass)) {
            if (val.toString().matches("[0-9]+")) {
                result = new Date(Long.parseLong(val.toString()));
            } else {
                throw new ResultReturnException(ErrorEnum.COMMON_DATE_MUST_TIMESTAMP);
            }
        } else if (String.class.equals(targetClass)) {
            if (val instanceof String) {
                result = val;
            } else {
                throw new ResultReturnException(ErrorEnum.COMMON_DATE_TARGET_MUST_STRING);
            }
        } else {
            result = JSONUtil.convertValue(val, targetClass);
        }
        return result;
    }

    /**
     * 异常
     * @param throwable
     * @return
     */
    private ApiResponse handleError(Throwable throwable) {
        Integer code = 500;
        String message = "";
        if (throwable instanceof ResultReturnException) {
            code = ((ResultReturnException) throwable).getCode();
            message = throwable.getMessage();
        } // 扩展异常规范
        else {
            code = 500;
            if(throwable instanceof Exception){
                if(throwable instanceof SQLException){
                    message = "SQL错误，请检查日志";
                }else{
                    message = throwable.getMessage();
                }
            }else{
                message = throwable.toString();
            }
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(out);
        throwable.printStackTrace(stream);
        return ApiResponse.error(code, message);
    }

    /**
     * 异常
     * @param throwable
     * @param info
     * @return
     */
    private ApiResponse handleError(Throwable throwable, RequestInfo info) {
        Integer code = 500;
        String message = "";
        if (throwable instanceof ResultReturnException) {
            code = ((ResultReturnException) throwable).getCode();
            message = throwable.getMessage();
        } // 扩展异常规范
        else {
            code = 500;
            if(throwable instanceof Exception){
                if(throwable instanceof SQLException){
                    message = "SQL错误，请检查日志";
                }else{
                    message = throwable.getMessage();
                }
            }else{
                message = "出错了，错误码："+info.getId();
            }
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(out);
        throwable.printStackTrace(stream);
        info.setException(throwable.toString());
        return ApiResponse.error(code, message);
    }

    /**
     * 解析form表单提交的数据，主要是针对前端传文件流，从而单独写了一套解析过程
     * @param request
     * @return
     */
    private static List<FormInfo> getPostData(HttpServletRequest request) {
        RequestParam requestParam=(RequestParam)RequestContext.getPrincipal();
        List<FormInfo> list=new ArrayList<>();
        //将当前上下文初始化给CommonsMutipartResolver
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        // 判断是否是多数据段提交格式
        if (multipartResolver.isMultipart(request)) {
            try{
                Collection<Part> parts = request.getParts();
                for (Iterator<Part> iterator = parts.iterator(); iterator.hasNext();) {
                    Part part = iterator.next();
                    FormInfo fileInfo=new FormInfo();
                    fileInfo.setName(part.getName());
                    fileInfo.setContentType(part.getContentType());
                    fileInfo.setSubmittedFileName(part.getSubmittedFileName());
                    fileInfo.setSize(part.getSize());
                    InputStream inputStream=part.getInputStream();
                    if(part.getContentType()==null){
                        StringBuffer out = new StringBuffer();
                        byte[] b = new byte[4096];
                        for (int n; (n = inputStream.read(b)) != -1;) {
                            out.append(new String(b, 0, n));
                        }
                        fileInfo.setInputStream(out.toString());
                    }else{
                        fileInfo.setInputStream(Base64Util.inputStreamToBase(inputStream));
                    }
                    list.add(fileInfo);
                }
            }catch (Exception ex){
                //直接取request的输出流 todo 未实现，情形：后台调用传文件流
                StringBuffer data = new StringBuffer();
                String line = null;
                BufferedReader reader = null;
                try {
                    reader = request.getReader();
                    while (null != (line = reader.readLine()))
                        data.append(line);
                } catch (IOException e) {

                } finally {

                }
            }
        }else{

//            Enumeration<String> strings=request.getParameterNames();
//            while (strings.hasMoreElements()){
//                String postParam=strings.nextElement();
//                System.err.println(postParam);
//
//            }
            Map map=request.getParameterMap();
            Set keSet=map.entrySet();
            for(Iterator itr=keSet.iterator();itr.hasNext();){
                Map.Entry me=(Map.Entry)itr.next();
                Object requestKey=me.getKey();
                Object requestValue=me.getValue();
                String[] value=new String[1];
                if(requestValue instanceof String[]){
                    value=(String[])requestValue;
                }else{
                    value[0]=requestValue.toString();
                    break;
                }
                for(int k=0;k<value.length;k++){
                    //todo 可以将method和params都作为传参的data，得额外解析ok
                    String _value=String.valueOf(requestKey);
                    if(value[k].length()!=0){
                         _value+="="+value[k];//特殊字符=截取字符串补充
                    }
                    if(_value.indexOf("{")!=-1&&_value.indexOf("}")!=-1){
                        requestParam.setParams(_value);
                    }
                }
            }
        }
        requestParam.setFormInfoList(list);
        return list;
    }
}
