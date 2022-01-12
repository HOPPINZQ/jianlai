package com.hoppinzq.service.javasist;

import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.CtMethod;
import org.apache.ibatis.javassist.CtNewMethod;

/**
 * @author: zq
 */
public class TestAB {
    public static void main(String[] args) throws Exception {
        //用于取得字节码类，必须在当前的classpath中，使用全称
        CtClass ctClass = ClassPool.getDefault().get("com.hoppinzq.service.javasist.A");
        //需要修改的方法名称
        String mname = "method";
        CtMethod mold = ctClass.getDeclaredMethod(mname);
        //修改原有的方法名称
        String nname = mname + "$impl";
        mold.setName(nname);
        //创建新的方法，复制原来的方法
        CtMethod mnew = CtNewMethod.copy(mold, mname, ctClass, null);
        //主要的注入代码
        StringBuffer body = new StringBuffer();
        body.append("{long start = System.currentTimeMillis();");
        //调用原有代码，类似于method();()表示所有的参数
        body.append(nname+"();");
        body.append("System.out.println(\"Call to method "
                + mname
                + " took \" + (System.currentTimeMillis()-start) + "
                + "\" ms.\");");
        body.append("}");
        //替换新方法
        mnew.setBody(body.toString());
        //增加新方法
        ctClass.addMethod(mnew);
        //类已经更改，注意不能使用A a=new A();，因为在同一个classloader中，不允许装载同一个类两次
        A a = (A) ctClass.toClass().newInstance();
        a.method();
    }
}
