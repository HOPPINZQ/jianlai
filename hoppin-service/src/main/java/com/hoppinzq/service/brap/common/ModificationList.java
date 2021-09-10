package com.hoppinzq.service.brap.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 修改列表
 */
public class ModificationList implements Serializable {
    private Map<String, Object> modifiedProperties = new HashMap<String, Object>();

    public void addModification(String key, Object value) {
        modifiedProperties.put(key, value);
    }

    public Map<String, Object> getModifiedProperties() {
        return modifiedProperties;
    }

    public void setModifiedProperties(Map<String, Object> modifiedProperties) {
        this.modifiedProperties = modifiedProperties;
    }
}