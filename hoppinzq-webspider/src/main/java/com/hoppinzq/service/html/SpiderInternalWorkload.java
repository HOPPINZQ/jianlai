package com.hoppinzq.service.html;


import com.hoppinzq.service.log.Log;
import com.hoppinzq.service.spiderService.IWorkloadStorable;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author: zq
 */
public class SpiderInternalWorkload implements IWorkloadStorable {
    Hashtable _complete = new Hashtable();
    Vector _waiting = new Vector();
    Vector _running = new Vector();

    public SpiderInternalWorkload() {
    }

    public synchronized String assignWorkload() {
        if (this._waiting.size() < 1) {
            return null;
        } else {
            String element = (String)this._waiting.firstElement();
            if (element != null) {
                this._waiting.remove(element);
                this._running.addElement(element);
            }

            Log.log(2, "Spider workload assigned:" + element);
            return element;
        }
    }

    public synchronized void addWorkload(String url) {
        if (this.getURLStatus(url) == 'U') {
            this._waiting.addElement(url);
            Log.log(2, "Spider workload added:" + url);
        }
    }

    public synchronized void completeWorkload(String ele, boolean isE) {
        if (this._running.size() > 0) {
            Enumeration elements = this._running.elements();

            while(elements.hasMoreElements()) {
                String var4 = (String)elements.nextElement();
                if (var4.equals(ele)) {
                    this._running.remove(var4);
                    if (isE) {
                        Log.log(2, "Spider workload ended in error:" + ele);
                        this._complete.put(var4, "e");
                    } else {
                        Log.log(2, "Spider workload complete:" + ele);
                        this._complete.put(var4, "c");
                    }

                    return;
                }
            }
        }

        Log.log(4, "Spider workload LOST:" + ele);
    }

    public synchronized char getURLStatus(String url) {
        if (this._complete.get(url) != null) {
            return 'C';
        } else {
            Enumeration enumeration;
            String temp;
            if (this._waiting.size() > 0) {
                enumeration = this._waiting.elements();

                while(enumeration.hasMoreElements()) {
                    temp = (String)enumeration.nextElement();
                    if (temp.equals(url)) {
                        return 'W';
                    }
                }
            }

            if (this._running.size() > 0) {
                enumeration = this._running.elements();

                while(enumeration.hasMoreElements()) {
                    temp = (String)enumeration.nextElement();
                    if (temp.equals(url)) {
                        return 'R';
                    }
                }
            }

            return 'U';
        }
    }

    public synchronized void clear() {
        this._waiting.clear();
        this._complete.clear();
        this._running.clear();
    }
}
