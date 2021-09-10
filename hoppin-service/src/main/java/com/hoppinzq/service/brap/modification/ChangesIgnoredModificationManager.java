package com.hoppinzq.service.brap.modification;


import com.hoppinzq.service.brap.common.ModificationList;

import java.io.Serializable;

public class ChangesIgnoredModificationManager implements ModificationManager, Serializable {
    private static final long serialVersionUID = 2783377098145240357L;

    public Object[] applyModificationScheme(Object[] objects) {
        return objects;
    }

    public ModificationList[] getModifications() {
        return null;
    }
}
