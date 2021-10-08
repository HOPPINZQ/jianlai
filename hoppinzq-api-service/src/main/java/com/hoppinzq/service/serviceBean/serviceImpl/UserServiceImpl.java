package com.hoppinzq.service.serviceBean.serviceImpl;

import com.hoppinzq.service.aop.annotation.*;
import com.hoppinzq.service.serviceBean.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;


/**
 * 测试类2
 */
@Service
@ApiServiceMapping(title = "总服务测试1", description = "总服务描述1")
public class UserServiceImpl {
    @Autowired
    private TransactionTemplate transactionTemplate;

    @ServiceLimit(limitType = ServiceLimit.LimitType.IP, number = 1)
    @Servicelock(lockType=Servicelock.LockType.DEFAULT,description = "新增锁")
    @ApiMapping(value = "getUser", title = "测试1", description = "描述1")
    public UserInfo getUser(Long userId) {
        transactionTemplate.execute(new TransactionCallback(){
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                //干!
                return Boolean.TRUE;
            }
        });
        System.out.println("服务开始处理业务："+userId);
        UserInfo info = new UserInfo();
        info.setName("小明");
        info.setSex("男");
        info.setUserId(userId);
        info.setIdcard("430527198108145443");
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return info;
    }

    @ServiceLimit(limitType = ServiceLimit.LimitType.IP, number = 1)
    @ApiMapping(value = "bit.api.user.getUser2", title = "测试1", description = "描述1")
    public UserInfo getUser4(UserInfo userInfo, String str) {
        userInfo.setName(userInfo.getName());
        userInfo.setSex(userInfo.getSex());
        userInfo.setUserId(userInfo.getUserId());
        userInfo.setIdcard(str);
        return userInfo;
    }

    //@Result
    @ServiceLimit(limitType = ServiceLimit.LimitType.IP, number = 1)
    @ApiMapping("bit.api.user.getUser3")
    public String getUser2(String idcard) {
        return idcard;
    }

}
