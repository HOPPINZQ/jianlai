package com.hoppinzq.service.dom;

public enum Style {
    WIDTH(0,"width"),
    HEIGHT(1,"height"),
    MARGIN(2,"margin"),
    MARGIN_LEFT(3,"margin-left"),
    MARGIN_RIGHT(4,"margin-right"),
    MARGIN_TOP(5,"margin-top"),
    MARGIN_BOTTOM(6,"margin-bottom"),
    PADDING(7,"padding"),
    PADDING_LEFT(8,"padding-left"),
    PADDING_RIGHT(9,"padding-right"),
    PADDING_TOP(10,"padding-top"),
    PADDING_BOTTOM(11,"padding-bottom"),
    FLOAT(12,"float"),
    CLEAR(13,"clear"),
    DISPLAY(14,"display"),
    COLOR(15,"color"),
    BACKGROUND(16,"background"),
    POSITION(17,"position"),
    TOP(18,"top"),
    BOTTOM(19,"bottom"),
    LEFT(20,"left"),
    RIGHT(21,"right"),
    Z_INDEX(22,"z-index"),
    BORDER(23,"border");

    private int state;
    private String styleKey;

    Style(int state, String styleKey) {
        this.state = state;
        this.styleKey = styleKey;
    }

    public int getState() {
        return state;
    }


    public String getStyleKey() {
        return styleKey;
    }


    public static Style stateOf(int index)
    {
        for (Style state : values())
        {
            if (state.getState()==index)
            {
                return state;
            }
        }
        return null;
    }
}
