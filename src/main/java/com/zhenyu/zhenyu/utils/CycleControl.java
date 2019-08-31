package com.zhenyu.zhenyu.utils;

public class CycleControl {
    private int timecycle;
    private static CycleControl mInstance;
    private double lambda;

    private CycleControl(){
        timecycle = 0;
        lambda = 0.99;
    }

    public static CycleControl getInstance(){
        if(mInstance == null)
            mInstance = new CycleControl();
        return mInstance;
    }

    public void elapse(){
        timecycle += 1;
    }

    public int getTimecycle(){ return timecycle; }

    public double decayRate(int time){
        return Math.pow(timecycle - time, lambda);
    }
}
