package com.kongzue.dome.cache;

import com.kongzue.dome.activity.WriteActivity;
import com.kongzue.dome.adapter.page.TaskPageAdapter;
import com.kongzue.dome.domain.DomeUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhangChao on 2017/7/20.
 */

public class CacheSet {

    //本类采用单例设计模式，请使用getInstance()获取本类对象后进行使用
    private static CacheSet cacheSet;
    public WriteActivity writeActivity;
    public TaskPageAdapter taskPage;

    private CacheSet() {
    }

    public static CacheSet getInstance() {
        if (cacheSet == null) {
            synchronized (CacheSet.class) {
                if (cacheSet == null) {
                    cacheSet = new CacheSet();
                }
            }
        }
        return cacheSet;
    }

    public List<DomeUser> selectUsers = new ArrayList<>();

}
