package com.hoppinzq.service.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: zq
 */
@Component
@ConfigurationProperties("weibo")
public class WeiboProperty {

    private String cilent_id;
    private String client_secret;
    private String reurl;

    public String getCilent_id() {
        return cilent_id;
    }

    public void setCilent_id(String cilent_id) {
        this.cilent_id = cilent_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getReurl() {
        return reurl;
    }

    public void setReurl(String reurl) {
        this.reurl = reurl;
    }
}
