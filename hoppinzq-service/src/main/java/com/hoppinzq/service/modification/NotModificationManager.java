package com.hoppinzq.service.modification;


import com.hoppinzq.service.common.ModificationList;

import java.io.Serializable;

/**
 * @author:ZhangQi
 */
public class NotModificationManager implements ModificationManager, Serializable {
    private static final long serialVersionUID = 2783377098145240357L;

    public Object[] applyModificationScheme(Object[] objects) {
        return objects;
    }

    public ModificationList[] getModifications() {
        return null;
    }
}
