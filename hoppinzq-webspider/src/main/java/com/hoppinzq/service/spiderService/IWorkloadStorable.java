package com.hoppinzq.service.spiderService;

public interface IWorkloadStorable {
    char RUNNING = 'R';
    char ERROR = 'E';
    char WAITING = 'W';
    char COMPLETE = 'C';
    char UNKNOWN = 'U';

    String assignWorkload();

    void addWorkload(String url);

    void completeWorkload(String url, boolean isE);

    char getURLStatus(String url);

    void clear();
}
