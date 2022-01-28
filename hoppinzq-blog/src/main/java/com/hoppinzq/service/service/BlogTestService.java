package com.hoppinzq.service.service;

import com.hoppinzq.service.aop.annotation.*;

/**
 * @author: zq
 */
@ApiServiceMapping(title = "博客测试服务", description = "博客测试服务，只是测试用",roleType = ApiServiceMapping.RoleType.RIGHT)
public class BlogTestService {

    @ServiceLimit(description = "限流",number = 3,limitType = ServiceLimit.LimitType.IP)
    @Servicelock(description = "同步锁，有可能操作同一个数据或对象就加上",lockType = Servicelock.LockType.DEFAULT)
    @ReturnTypeUseDefault
    @ApiCache(time = 60)
    @AutoIdempotent
    @Timeout(timeout=10000)
    @ApiMapping(value = "test", title = "测试测试",description = "测试测试",
            type = ApiMapping.Type.POST,roleType = ApiMapping.RoleType.ADMIN,returnType = false)
    public void test(){}
}
