package com.hoppinzq.service.config;

import org.apache.commons.lang3.RandomUtils;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;

/**
 * @ClassName： BeetlConfiguration
 * @Description：  beetl拓展配置, 绑定一些工具类, 方便在模板中直接调用.直接配置groupTemplate
 * @Author： xie jing
 * @Date： 2019/5/23
 * @Vision： 1.0
 */

public class BeetlConfiguration extends BeetlGroupUtilConfiguration {

    /*
    * 注册自定义方法
    */
    @Override
    public void initOther() {
        groupTemplate.registerFunctionPackage("random", new RandomUtils());
    }
}

