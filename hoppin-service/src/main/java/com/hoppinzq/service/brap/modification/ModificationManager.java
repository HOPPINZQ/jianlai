package com.hoppinzq.service.brap.modification;


import com.hoppinzq.service.brap.common.ModificationList;

public interface ModificationManager {
    public Object[] applyModificationScheme(Object[] objects);
    public ModificationList[] getModifications();
}