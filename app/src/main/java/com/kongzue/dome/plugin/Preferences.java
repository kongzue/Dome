package com.kongzue.dome.plugin;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by ZhangChao on 2017/5/19.
 */

public class Preferences {

    //本类采用单例设计模式，请使用getInstance()获取本类对象后进行使用
    private static Preferences preferences;

    private Preferences() {
    }

    public static Preferences getInstance() {
        if (preferences == null) {
            synchronized (Preferences.class) {
                if (preferences == null) {
                    preferences = new Preferences();
                }
            }
        }
        return preferences;
    }

    public String getPreferencesToString(Context context, String path, String preferencesName){
        SharedPreferences preferences = context.getSharedPreferences(path, Context.MODE_PRIVATE);
        String value = preferences.getString(preferencesName, "");
        return value;
    }

    public boolean getPreferencesToBoolean(Context context, String path, String preferencesName){
        SharedPreferences preferences = context.getSharedPreferences(path, Context.MODE_PRIVATE);
        boolean value = preferences.getBoolean(preferencesName, false);
        return value;
    }

    public int getPreferencesToInt(Context context, String path, String preferencesName){
        SharedPreferences preferences = context.getSharedPreferences(path, Context.MODE_PRIVATE);
        int value = preferences.getInt(preferencesName, -1);
        return value;
    }

    public void setPreferences(Context context, String path, String preferencesName,String value){
        Log.i(">>>","存储属性"+path + "." + preferencesName+":"+value);
        SharedPreferences preferences = context.getSharedPreferences(path, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(preferencesName, value);
        editor.commit();
    }

    public void setPreferences(Context context, String path, String preferencesName,boolean value){
        Log.i(">>>","存储属性"+path + "." + preferencesName+":"+value);
        SharedPreferences preferences = context.getSharedPreferences(path, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(preferencesName, value);
        editor.commit();
    }

    public void setPreferences(Context context, String path, String preferencesName,int value){
        Log.i(">>>","存储属性"+path + "." + preferencesName+":"+value);
        SharedPreferences preferences = context.getSharedPreferences(path, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(preferencesName, value);
        editor.commit();
    }

    public void cleanAll(Context context, String path){
        SharedPreferences sp=context.getSharedPreferences(path,Context.MODE_PRIVATE);
        if(sp!=null) {
            sp.edit().clear().commit();
        }
    }

}
