package com.zhenyu.zhenyu.RequestData;

public class LoginEntity {
    private int icode;
    private String info;


    public LoginEntity(int icode, String info){
        this.icode = icode;
        this.info = info;
    }

    public void setIcode(int icode){
        this.icode = icode;
    }
    public void setInfo(String info){
        this.info = info;
    }
    public String getInfo() {
        return info;
    }

    public int getIcode() {
        return icode;
    }
}
