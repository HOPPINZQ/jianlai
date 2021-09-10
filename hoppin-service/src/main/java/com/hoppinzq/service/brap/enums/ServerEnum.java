package com.hoppinzq.service.brap.enums;

public enum ServerEnum {

    INNER(0,"内部服务"),
    OUTER(1,"外部服务");

    private int state;
    private String info;

    ServerEnum(int state, String info) {
        this.state = state;
        this.info = info;
    }

    public int getState() {
        return state;
    }


    public String getInfo() {
        return info;
    }


    public static ServerEnum stateOf(int index)
    {
        for (ServerEnum state : values())
        {
            if (state.getState()==index)
            {
                return state;
            }
        }
        return null;
    }
}
