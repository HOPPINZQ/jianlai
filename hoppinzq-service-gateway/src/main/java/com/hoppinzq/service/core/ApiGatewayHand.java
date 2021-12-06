package com.hoppinzq.service.core;

import com.alibaba.fastjson.JSONObject;
import com.hoppinzq.service.bean.ApiPropertyBean;
import com.hoppinzq.service.bean.ApiResponse;
import com.hoppinzq.service.bean.FormInfo;
import com.hoppinzq.service.bean.RequestInfo;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
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
 * @author:ZhangQi
 */
@Component
public class ApiGatewayHand implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(ApiGatewayHand.class);

    @Autowired
    private ApiPropertyBean apiPropertyBean;

    ApiStore apiStore;
//    @Autowired
//    private LogService logService;

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

//    public static void main(String[] args) {
//        String str="method=getUser_>_<_hoppinzq_>_<_params={\"userId\":123}";
//        System.out.println("加密后："+EncryptUtil.DESencode(str,"hoppinzq"));
//        String sqlCode= EncryptUtil.DESdecode("311D9D25C9110C70ED0728639F2B9D1E7641D019C108965F57BD4CAE2ACBCF407B9BC02AFE768748CDF18F3E0759264C993512907C583D14", "hoppinzq");
//        System.out.println("解密后："+sqlCode);
//    }

    /**
     * 具体执行方法
     *
     * @param request
     * @return
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long start = System.currentTimeMillis();
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        Object result = null;
        ApiRunnable apiRun = null;
        String method=null;
        String params=null;
        String returnDate=null;//是否封装返回值为null是用系统封装，否则不封装返回值
        RequestInfo requestInfo=null;
        String ip= IPUtils.getIpAddress();
        String id= UUIDUtil.getUUID();
        String url=request.getRequestURL().toString();
        Map<String,String> decodeResult=decodeParams(request);
        if(decodeResult==null){
            List<FormInfo> fileInfos=getPostData(request);
            method = request.getParameter(ApiCommConstant.METHOD);
            params = request.getParameter(ApiCommConstant.PARAMS);
            returnDate=request.getParameter(ApiCommConstant.RETURN);
            String methodType=request.getMethod();//GET POST
//            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//            HttpServletRequest request1111 = attributes.getRequest();


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
            if(!("GET".equals(methodType)&&fileInfos.size()==0)){
                paramsMap.put("formInfos",formInfoStr);
            }
            //params="{\"fileInfos\":"+JSONObject.toJSONString(fileInfos)+"}";
            params=JSONObject.toJSONString(paramsMap);
        }else{
            method=decodeResult.get(ApiCommConstant.METHOD);
            params=decodeResult.get(ApiCommConstant.PARAMS);
            returnDate=decodeResult.get(ApiCommConstant.RETURN);
        }

        try {
            apiRun = sysParamsValdate(request,method,params);

            logger.debug("请求接口={" + method + "} 参数=" + params + "");
            Object[] args = buildParams(apiRun, params, request);
            result = apiRun.run(args);
            result = JSONObject.toJSON(ApiResponse.data(result,"操作成功"));
            long createTime = System.currentTimeMillis();
            logger.debug("接口:" + request.getRequestURL().toString() + " 请求时长:" + (createTime - start));
            requestInfo = new RequestInfo(ip, url,
                    "INFO", method, params,
                    result, DateFormatUtil.stampToDate(createTime), createTime - start
                    , null);
            logger.debug("请求信息:\n {}", requestInfo.toString());
        } catch (ResultReturnException e) {
            logger.error("调用接口={" + method + "}异常  参数=" + params + "", e);
            requestInfo = new RequestInfo(ip, url, "ERROR",
                    method, params,  null,
                    DateFormatUtil.stampToDate(System.currentTimeMillis()), 0L, e);
            result = handleError(e,requestInfo);
            logger.error("错误的请求:\n {}", requestInfo.toString());
        } catch (InvocationTargetException e) {
            logger.error("调用接口={" + method + "}异常  参数=" + params + "", e.getTargetException());
            requestInfo = new RequestInfo(ip, url, "ERROR",
                    method, params,  null,
                    DateFormatUtil.stampToDate(System.currentTimeMillis()), 0L, e.getTargetException());
            result = handleError(e.getTargetException(),requestInfo);
            logger.error("错误的请求:\n {}", requestInfo.toString());
        } catch (Exception e) {
            logger.error("其他异常", e);
            requestInfo = new RequestInfo(ip, url, "ERROR",
                    method, params,  null,
                    DateFormatUtil.stampToDate(System.currentTimeMillis()), 0L, e);
            result = handleError(e,requestInfo);
            logger.error("错误的请求:\n {}", requestInfo.toString());
        } finally {
            //logService.saveRequestInfo(requestInfo);

            PrintWriter out = response.getWriter();
            if(returnDate==null){
                out.println(result.toString());
            }else{
                JSONObject resultJson=JSONObject.parseObject(result.toString());
                out.println(JSONObject.parseObject(resultJson.get("data").toString()));
            }
        }
    }


    private Map<String,String> decodeParams(HttpServletRequest request) throws ResultReturnException{
        String encode = request.getParameter(ApiCommConstant.ENCODE);
        if(encode==null){
            return null;
        }else {
            String encodeResult= EncryptUtil.DESdecode(encode,"hoppinzq");
            String encode_str="_>_<_hoppinzq_>_<_";
            if(encodeResult==null){
                throw new ResultReturnException("调用失败：无法解密");
            }
            if(encodeResult.indexOf(encode_str)<0){
                throw new ResultReturnException("调用失败：加密的格式有误");
            }
            Map<String,String> map=new HashMap();
            try{
                String method=encodeResult.split(encode_str)[0].split("method=")[1];
                String params=encodeResult.split(encode_str)[1].split("params=")[1];
                map.put(ApiCommConstant.METHOD,method);
                map.put(ApiCommConstant.PARAMS,params);
            }catch (Exception ex){
                throw new ResultReturnException("调用失败：加密的格式有误");
            }
            return map;
        }
    }

    /**
     * 系统参数校验
     *
     * @param request
     * @return
     * @throws ResultReturnException
     */
    private ApiRunnable sysParamsValdate(HttpServletRequest request,String apiName,String json) throws ResultReturnException {
//        String apiName = request.getParameter(ApiCommConstant.METHOD);
//        String json = request.getParameter(ApiCommConstant.PARAMS);
        sign(request);
        ApiRunnable api;
        if (apiName == null || apiName.trim().equals("")) {
            throw new ResultReturnException("调用失败：参数'method'为空");
        } else if (json == null) {
            throw new ResultReturnException("调用失败：参数'params'为空");
        } else if ((api = apiStore.findApiRunnable(apiName)) == null) {
            throw new ResultReturnException("调用失败：指定API不存在，API:" + apiName);
        }
        return api;
    }

    /**
     * 签名验证
     *
     * @param request
     */
    private void sign(HttpServletRequest request) throws ResultReturnException {
        String params = request.getParameter(ApiCommConstant.PARAMS);
        String method = request.getParameter(ApiCommConstant.METHOD);
        String token = request.getParameter(ApiCommConstant.TOKEN);
        String sign = request.getParameter(ApiCommConstant.SIGN);
        String timestamp = request.getParameter(ApiCommConstant.TIMESTAMP);
//        if (System.currentTimeMillis() - (24 * 60 * 60 * 1000) > Long.parseLong(timestamp)) {
//            throw new ResultReturnException("调用失败：请求已过期");
//        }
        //TODO:签名认证
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
        //paramJson="{'userId':123}";// %7B { // %7D }
        try {
            map = JSONUtil.toMap(paramJson);
        } catch (IllegalArgumentException e) {
            throw new ResultReturnException("调用失败：json字符串格式异常，请检查params参数 ");
        }
        if (map == null) {
            map = new HashMap<>();
        }

        Method method = run.getTargetMethod();
        List<String> paramNames = Arrays.asList(parameterUtil.getParameterNames(method));
        Class<?>[] paramTypes = method.getParameterTypes();

        for (Map.Entry<String, Object> m : map.entrySet()) {
            if (!paramNames.contains(m.getKey())) {
                throw new ResultReturnException("调用失败：接口不存在‘" + m.getKey() + "’参数");
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
                    throw new ResultReturnException("调用失败：指定参数格式错误或值错误‘" + paramNames.get(i) + "’"
                            + e.getMessage());
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
                throw new IllegalArgumentException("日期必须是长整型的时间戳");
            }
        } else if (String.class.equals(targetClass)) {
            if (val instanceof String) {
                result = val;
            } else {
                throw new IllegalArgumentException("转换目标类型为字符串");
            }
        } else {
            result = JSONUtil.convertValue(val, targetClass);
        }
        return result;
    }


    private ApiResponse handleError(Throwable throwable) {
        Integer code = 500;
        String message = "";
        if (throwable instanceof ResultReturnException) {
            code = ((ResultReturnException) throwable).getCode();
            message = throwable.getMessage();
        } // 扩展异常规范
        else {
            code = 500;
            //SQLException跟IOException调用的是父类有参的构造方法，而RuntimeException调用的是无参的构造方法
            if(throwable instanceof SQLException||throwable instanceof IOException){
                message = throwable.getMessage();
            }else{
                message = throwable.toString();
            }
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(out);
        throwable.printStackTrace(stream);
        return ApiResponse.error(code, message);
    }

    private ApiResponse handleError(Throwable throwable, RequestInfo info) {
        Integer code = 500;
        String message = "";
        if (throwable instanceof ResultReturnException) {
            code = ((ResultReturnException) throwable).getCode();
            message = throwable.getMessage();
        } // 扩展异常规范
        else {
            code = 500;
            //SQLException跟IOException调用的是父类有参的构造方法，而RuntimeException调用的是无参的构造方法
            if(throwable instanceof SQLException||throwable instanceof IOException){
                message = throwable.getMessage();
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

    private static List<FormInfo> getPostData(HttpServletRequest request) {
        List<FormInfo> list=new ArrayList<>();
        //将当前上下文初始化给CommonsMutipartResolver（多部分解析器）
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
                ex.printStackTrace();
            }
        }else{//直接取request的输出流
//           StringBuffer data = new StringBuffer();
//            String line = null;
//            BufferedReader reader = null;
//            try {
//                reader = request.getReader();
//                while (null != (line = reader.readLine()))
//                    data.append(line);
//            } catch (IOException e) {
//
//            } finally {
//
//            }
        }
        return list;
    }

}
