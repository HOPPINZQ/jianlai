package com.hoppinzq;

import javassist.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * @author: zq
 */
public class MyAgent {
//    public static void premain(String args, Instrumentation instrumentation) throws Exception {
//        System.out.println("Hello javaagent permain:"+args);
//
//    }
public static void premain(String args, Instrumentation instrumentation) throws Exception {
    instrumentation.addTransformer(new ClassFileTransformer() {
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                                byte[] classfileBuffer) throws IllegalClassFormatException {
            if(!"com/hoppinzq/service/MyServer".equals(className)){
                return null;
            }
            try {
                return buildMonitorClass();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    },true);

}
    private static byte[] buildMonitorClass() throws Exception{
        /**
         * 1、拷贝一个新的方法
         * 2、修改原方法名
         * 3、加入监听代码
         */
        ClassPool pool = new ClassPool();
        pool.appendSystemPath();
        CtClass ctClass = pool.get("com.hoppinzq.service.MyServer");
        CtMethod ctMethod = ctClass.getDeclaredMethod("sayHello");
        CtMethod copyMethod = CtNewMethod.copy(ctMethod,ctClass,new ClassMap());
        ctMethod.setName("sayHello$agent");
        copyMethod.setBody("{\n" +
                "    long begin = System.nanoTime();\n" +
                "    try {\n" +
                "        return sayHello$agent($1,$2);\n" +
                "    } finally {\n" +
                "        System.out.println(System.nanoTime() - begin);}\n" +
                "    }");
        ctClass.addMethod(copyMethod);
        return ctClass.toBytecode();
    }
}
