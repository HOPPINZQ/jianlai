package com.hoppinzq.service.config;

import org.apache.commons.lang3.RandomUtils;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;

public class BeetlConfiguration extends BeetlGroupUtilConfiguration {

    /*
    * 注册自定义方法
    */
    @Override
    public void initOther() {
        groupTemplate.registerFunctionPackage("random", new RandomUtils());
    }
}
