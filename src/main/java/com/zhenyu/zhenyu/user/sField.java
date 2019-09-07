package com.zhenyu.zhenyu.user;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Double;

public class sField {
    private HashMap<String, Double> mKeyword;
    private String category;
    private final double upperbound = 0.1;
    private final double favorateRate = 3.;

    public sField(String category){
        this.category = category;
        mKeyword = new HashMap<>();
    }

    public void addKeywords(HashMap<String, Double> keys){
        for(Map.Entry<String, Double> entry: keys.entrySet()){
            if(mKeyword.containsKey(entry.getKey()))
                mKeyword.put(entry.getKey(), entry.getValue() + mKeyword.get(entry.getKey()));
            else
                mKeyword.put(entry.getKey(), entry.getValue());
        }
    }

    public void addsingleword(String word, double sco){
        if(mKeyword.containsKey(word))
            mKeyword.put(word, sco + mKeyword.get(word));
        else
            mKeyword.put(word, sco);
    }

    public boolean containsKey(String str){ return mKeyword.containsKey(str);}
    public void decayTime(){}
    public void reduceLiking(String mkey, double mscore){
        if(mKeyword.containsKey(mkey)){
            double score = mKeyword.get(mkey) - mscore;
            if(score <= 0)
                mKeyword.remove(mkey);
            else
                mKeyword.put(mkey, score);
        }
    }

    public String getUsedKeys(){
        int value_num = mKeyword.size();
        if(value_num == 0)
            return null;
        List<String> keys = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        double sum = 0;
        for(Map.Entry<String, Double> w: mKeyword.entrySet()){
            if(w.getValue() > upperbound){
                keys.add(w.getKey());
                values.add(w.getValue());
                sum += w.getValue();
            }
        }
        sum = Math.exp(sum);
        double rd = Math.random();
        double tsum = 0;

        int valid_sum = values.size();
        for(int i = 0 ; i < valid_sum; i++) {
            tsum += Math.exp(values.get(i)) / sum;
            if(tsum >= rd)
                return keys.get(i);
        }
        return keys.get(valid_sum-1);
    }

    public void addFavorate(HashMap<String, Double> ms) {
        for(Map.Entry<String, Double> entry: ms.entrySet()){
            if(mKeyword.containsKey(entry.getKey()))
                mKeyword.put(entry.getKey(), entry.getValue() * favorateRate + mKeyword.get(entry.getKey()));
            else
                mKeyword.put(entry.getKey(), entry.getValue() * favorateRate);
        }
    }
}
