package com.hoppinzq.service.modification;


import com.hoppinzq.service.common.ModificationList;

public interface ModificationManager {
    public Object[] applyModificationScheme(Object[] objects);
    public ModificationList[] getModifications();
}