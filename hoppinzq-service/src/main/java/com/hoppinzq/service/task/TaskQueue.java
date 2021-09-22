package com.hoppinzq.service.task;

import com.hoppinzq.service.config.TaskTemplate;

import java.util.Arrays;

/**
 * @author:ZhangQi
 **/
public class TaskQueue {
    private int head = 0;
    private int tail  = 0;
    private TaskTemplate[] queue = null;
    private int maxsize = 0;    // 最大存储量个数
    private int len = 0;   // 实际存储数量
    private int minCapacity=16;//最少16个元素

    public TaskQueue(int size){
        maxsize = size;
        queue = new TaskTemplate[size];
    }

    public boolean isFull(){
        return len == maxsize;
    }

    public boolean isEmpty(){
        return len == 0;
    }

    public void push(TaskTemplate ele){
        if (!isFull()){
            queue[tail] = ele;
            tail = (tail + 1) % maxsize;
            len++;
        }else{
            grow();
        }
    }

    /**
     * 扩容1.5倍
     */
    public void grow(){
        maxsize=maxsize+(maxsize >> 1);
        if (maxsize - minCapacity < 0)
            maxsize = minCapacity;
        queue = Arrays.copyOf(queue, maxsize);
    }

    public TaskTemplate pop(){
        if (isEmpty()){
            throw new RuntimeException("队列已经空的，不能取数据！");
        }

        TaskTemplate ele = queue[head];
        head = (head + 1) % maxsize;
        len--;
        return ele;
    }

    public void show(){
        for (int i = head; i < tail; i++) {
            System.out.println(queue[i]);
        }
    }


//    public static void main(String[] args) {
//        TaskQueue qe = new TaskQueue(10);
//        qe.push(2);
//        qe.push(5);
//        qe.push(7);
//        qe.push(20);
//        qe.push(50);
//        qe.push(70);
//        qe.show();
//        int i = qe.pop();
//        System.out.println("弹出数据为：" + i);
//        i = qe.pop();
//        System.out.println("弹出数据为：" + i);
//        System.out.println("============");
//        qe.grow();
//        qe.grow();
//        qe.show();
//    }

//    public static void main(String[] args) {
//        int a=20;
//        a=a+(a >> 1);
//        System.err.println(a);
//    }
}