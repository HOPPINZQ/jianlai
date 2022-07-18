package com.hoppinzq.service.html;


import com.hoppinzq.service.log.Log;

import com.hoppinzq.service.spiderService.ISpiderReportable;
import com.hoppinzq.service.spiderService.IWorkloadStorable;

/**
 * @author: zq
 */
public class Spider extends Thread implements ISpiderReportable {
    protected IWorkloadStorable _workload;
    protected SpiderWorker[] _pool;
    protected boolean _worldSpider;
    protected ISpiderReportable _manager;
    protected boolean _halted;
    protected SpiderDone _done;
    protected int _maxBodySize;

    public Spider(ISpiderReportable iSpiderReportable, String str, HTTP http, int size) {
        this(iSpiderReportable, str, http, size, new SpiderInternalWorkload());
    }

    public Spider(ISpiderReportable iSpiderReportable, String str, HTTP http, int size, IWorkloadStorable iWorkloadStorable) {
        this._halted = false;
        this._done = new SpiderDone();
        this._manager = iSpiderReportable;
        this._worldSpider = false;
        this._pool = new SpiderWorker[size];

        for(int var6 = 0; var6 < this._pool.length; ++var6) {
            HTTP var7 = http.copy();
            this._pool[var6] = new SpiderWorker(this, var7);
        }

        this._workload = iWorkloadStorable;
        if (str.length() > 0) {
            this._workload.clear();
            this.addWorkload(str);
        }

    }

    public SpiderDone getSpiderDone() {
        return this._done;
    }

    public void run() {
        if (!this._halted) {
            for(int i = 0; i < this._pool.length; ++i) {
                this._pool[i].start();
            }

            try {
                this._done.waitBegin();
                this._done.waitDone();
                Log.log(3, "Spider has no work.");
                this.spiderComplete();

                for(int j = 0; j < this._pool.length; ++j) {
                    this._pool[j].interrupt();
                    this._pool[j].join();
                    this._pool[j] = null;
                }
            } catch (Exception var3) {
                Log.logException("Exception while starting spider", var3);
            }

        }
    }

    public synchronized String getWorkload() {
        try {
            while(!this._halted) {
                String workload = this._workload.assignWorkload();
                if (workload != null) {
                    return workload;
                }

                this.wait();
            }

            return null;
        } catch (InterruptedException var2) {
            return null;
        }
    }

    public synchronized void addWorkload(String var1) {
        if (!this._halted) {
            this._workload.addWorkload(var1);
            this.notify();
        }
    }

    public void setWorldSpider(boolean var1) {
        this._worldSpider = var1;
    }

    public boolean getWorldSpider() {
        return this._worldSpider;
    }

    public synchronized boolean foundInternalLink(String url) {
        if (this._manager.foundInternalLink(url)) {
            this.addWorkload(url);
        }

        return true;
    }

    public synchronized boolean foundExternalLink(String url) {
        if (this._worldSpider) {
            this.foundInternalLink(url);
            return true;
        } else {
            if (this._manager.foundExternalLink(url)) {
                this.addWorkload(url);
            }

            return true;
        }
    }

    public synchronized boolean foundOtherLink(String url) {
        if (this._manager.foundOtherLink(url)) {
            this.addWorkload(url);
        }

        return true;
    }

    public synchronized void processPage(HTTP http) {
        this._manager.processPage(http);
    }

    public synchronized boolean getRemoveQuery() {
        return true;
    }

    public synchronized void completePage(HTTP http, boolean isE) {
        this._workload.completeWorkload(http.getURL(), isE);
    }

    public synchronized void spiderComplete() {
        this._manager.spiderComplete();
    }

    public synchronized void halt() {
        this._halted = true;
        this._workload.clear();
        this.notifyAll();
    }

    public boolean isHalted() {
        return this._halted;
    }

    public void setMaxBody(int size) {
        this._maxBodySize = size;

        for(int i = 0; i < this._pool.length; ++i) {
            this._pool[i].getHTTP().setMaxBody(size);
        }

    }

    public int getMaxBody() {
        return this._maxBodySize;
    }
}

