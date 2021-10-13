package com.hoppinzq.test;

import java.util.stream.IntStream;

/**
 * @author:ZhangQi
 **/
public class ThreadPoolTest {

    public static void main(String[] args){
        ThreadPool threadPool = new ThreadPool(10);
//        IntStream.range(0, 10).forEach((i) -> {
//
//        });
        for(int i=1;i<10;i++){
            threadPool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "--->> Hello ThreadPool");
            });
        }
    }
}
