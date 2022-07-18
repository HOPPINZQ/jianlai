package com.hoppinzq.service.spiderService;


import com.hoppinzq.service.html.HTTP;

public interface ISpiderReportable {
    boolean foundInternalLink(String url);

    boolean foundExternalLink(String url);

    boolean foundOtherLink(String url);

    void processPage(HTTP http);

    void completePage(HTTP http, boolean isE);

    boolean getRemoveQuery();

    void spiderComplete();
}
