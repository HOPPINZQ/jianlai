package com.hoppinzq.service.util;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

//将所有警告取消
@SuppressWarnings("all")
public class TestClassLoad {
    public static void loadMethod() throws MalformedURLException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int result = compiler.run(null, null, null, "D:/test/De.java");
        System.out.println(result==0?"成功":"失败");
        URL[] urls = new URL[] {new URL("file:/"+"D:/test/")};
        URLClassLoader loader = new URLClassLoader(urls);
        Class c = loader.loadClass("De");
        Method m = c.getDeclaredMethod("main", String[].class);
        //通过Object把数组转化为参数
        m.invoke(null, (Object)new String[] {});
    }

    //               Advice advice=null;
//                   try{
//                        URL targetUrl=new URL("D://qweqwe.java");
//                        URLClassLoader urlClassLoader=(URLClassLoader)getClass().getClassLoader();
//                        boolean isLoader=false;
//                        for(URL url:urlClassLoader.getURLs()){
//                            if(url.equals(targetUrl)){
//                                isLoader=true;
//                                break;
//                            }
//                        }
//                        if(!isLoader){
//                            Method add=URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
//                            add.setAccessible(true);
//                            add.invoke(urlClassLoader,targetUrl);
//                        }

//                    Class<?> adviceClass=urlClassLoader.loadClass("com.hoppinzq.plugin.CountTimesPlugin");
//                    advice=(Advice)adviceClass.newInstance();
//
//                    Advised advised = (Advised)bean;
//                    advised.addAdvice(advice);

//                    }catch (Exception ex){
//                        throw new RuntimeException("增强失败:"+ex.getMessage());
//                    }


}