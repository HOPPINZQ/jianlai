package com.hoppinzq.service.modification;


import com.hoppinzq.service.common.ModificationList;

public interface ModificationManager {
    Object[] applyModificationScheme(Object[] objects);
    ModificationList[] getModifications();
}