package com.kongzue.dome.domain;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by ZhangChao on 2017/7/19.
 */

public class DomeUser extends BmobUser {

    private String userAvatar;
    private boolean settingDoNotFindMe;      //禁止发现（可：其他人可以圈我指派任务）
    private BmobRelation blackList;

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public boolean isSettingDoNotFindMe() {
        return settingDoNotFindMe;
    }

    public void setSettingDoNotFindMe(boolean settingDoNotFindMe) {
        this.settingDoNotFindMe = settingDoNotFindMe;
    }

    public BmobRelation getBlackList() {
        return blackList;
    }

    public void setBlackList(BmobRelation blackList) {
        this.blackList = blackList;
    }

    @Override
    public String toString() {
        return "DomeUser{" +
                "userAvatar='" + super.getUsername() + '\'' +
                '}';
    }
}
