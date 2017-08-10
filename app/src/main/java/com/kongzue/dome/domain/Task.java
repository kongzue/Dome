package com.kongzue.dome.domain;

import cn.bmob.v3.BmobObject;

/**
 * Created by ZhangChao on 2017/7/19.
 */

public class Task extends BmobObject {

    private String taskText;        //内容
    private String author;          //发起人
    private String executor;        //执行人
    private int progress;           //进度
    private int colorId;            //主题颜色
    private int weight;             //权重

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    private boolean isChangeProcess;

    public boolean isChangeProcess() {
        return isChangeProcess;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public void setChangeProcess(boolean changeProcess) {
        isChangeProcess = changeProcess;
    }

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskText='" + taskText + '\'' +
                ", author='" + author + '\'' +
                ", executor='" + executor + '\'' +
                ", progress=" + progress +
                '}';
    }
}
