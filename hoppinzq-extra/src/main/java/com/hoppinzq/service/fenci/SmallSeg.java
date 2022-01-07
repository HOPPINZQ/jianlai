package com.hoppinzq.service.fenci;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:ZhangQi
 **/
public class SmallSeg {
    private static Seg seg = new Seg();

    static {
        seg.useDefaultDict();
    }

    public SmallSeg() {
    }

    public static List<String> cut(String text) {
        new ArrayList();
        List<String> sr = seg.cut(text);
        return sr;
    }
}
