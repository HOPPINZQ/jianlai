package com.hoppinzq.service.cache;


import com.hoppinzq.service.core.ApiRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author:ZhangQi
 **/
public class apiCache {

    //供内部调用api缓存,在项目启动前会将api映射存入这里
    public static HashMap<String, ApiRunnable> apiMap = new HashMap<String, ApiRunnable>();

    //供外部查看api缓存，在项目启动前会将暴露的服务跟api存入这里
    public static List<HashMap> outApiList=new ArrayList<>();


}
