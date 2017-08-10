package com.kongzue.dome.push;

import cn.bmob.v3.BmobInstallation;

/**
 * Created by ZhangChao on 2017/7/20.
 */

public class MyBmobInstallation extends BmobInstallation {

    /**
     * 用户id-这样可以将设备与用户之间进行绑定
     */
    private String uid;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
