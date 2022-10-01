package com.itheima.aop;

import java.util.List;

public class countTime {
    public void count(){
        long start = System.nanoTime();//单位是纳秒

        long end = System.nanoTime();

        double spentTime = (double) end - start;

        System.out.println(spentTime);
    }



}
