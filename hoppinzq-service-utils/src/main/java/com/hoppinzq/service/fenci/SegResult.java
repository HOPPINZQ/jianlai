package com.hoppinzq.service.fenci;

import java.util.List;

/**
 * @author:ZhangQi
 **/
public class SegResult {
    public List<String> recognised;
    public List<String> unrecognised;

    public SegResult() {
    }

    public String toString() {
        return "r:" + this.recognised + "\r\nu:" + this.unrecognised;
    }
}
