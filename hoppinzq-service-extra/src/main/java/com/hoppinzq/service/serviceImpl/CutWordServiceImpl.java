package com.hoppinzq.service.serviceImpl;

import com.hoppinzq.service.aop.annotation.ServiceRegister;
import com.hoppinzq.service.fenci.SmallSeg;
import com.hoppinzq.service.interfaceService.CutWordService;


import java.io.Serializable;
import java.util.List;

/**
 * @author:ZhangQi
 **/
@ServiceRegister
public class CutWordServiceImpl implements CutWordService, Serializable {

    private static final long serialVersionUID = 2783377098145240357L;

    @Override
    public List<String> cut(String test) {
        return SmallSeg.cut(test);
    }
}
