package com.kongzue.dome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kongzue.dialog.ProgressbarDialog;
import com.kongzue.dome.R;
import com.kongzue.dome.cache.CacheSet;
import com.kongzue.dome.domain.DomeUser;
import com.kongzue.dome.domain.Task;
import com.kongzue.dome.plugin.BaseActivity;
import com.kongzue.dome.plugin.Preferences;
import com.kongzue.dome.push.MyBmobInstallation;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class WriteActivity extends BaseActivity {

    private LinearLayout boxBody;
    private LinearLayout sysStatusBar;
    private ImageView btnBack;
    private TextView txtTitle;
    private TextView btnSend;
    private EditText editWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.layout_write);
        setTranslucentStatus(true, true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setIMMStatus(true, editWrite);
            }
        }, 100);
    }

    private ProgressbarDialog progressbarDialog;

    @Override
    public void initDatas() {
        CacheSet.getInstance().writeActivity = this;
        CacheSet.getInstance().selectUsers = new ArrayList<>();
    }

    @Override
    public void initViews() {
        boxBody = (LinearLayout) findViewById(R.id.box_body);
        sysStatusBar = (LinearLayout) findViewById(R.id.sys_statusBar);
        btnBack = (ImageView) findViewById(R.id.btn_back);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        btnSend = (TextView) findViewById(R.id.btn_send);
        editWrite = (EditText) findViewById(R.id.edit_write);
    }

    public List<DomeUser> executorList = new ArrayList<>();

    @Override
    public void setEvent() {
        editWrite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                log(s + "\nstart=" + start + "before=" + before + "count=" + count);
                try {
                    if (s.toString().substring(start, start + count).equals("@")) {
                        startActivity(new Intent(me, SelectAtUserActivity.class));
                        int version = Integer.valueOf(android.os.Build.VERSION.SDK);
                        if (version > 5) {
                            me.overridePendingTransition(R.anim.fade, R.anim.hold);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (progressbarDialog == null) progressbarDialog = new ProgressbarDialog(me);
                progressbarDialog.show();

                String username = Preferences.getInstance().getPreferencesToString(me, "user", "username");

                if (editWrite.getText().toString().isEmpty()) {
                    toast("不能创建空的事项");
                    return;
                }

                if (CacheSet.getInstance().selectUsers.size() == 0) {
                    Task task = new Task();
                    task.setTaskText(editWrite.getText().toString());
                    task.setAuthor(username);
                    task.setExecutor(username);
                    task.save(new SaveListener<String>() {

                        @Override
                        public void done(String objectId, BmobException e) {
                            if (e == null) {
                                toast("完成");
                                if (CacheSet.getInstance().taskPage != null)
                                    CacheSet.getInstance().taskPage.getTask();
                                onBackPressed();
                            } else {
                                toast("网络连接失败，请重试");
                            }
                        }
                    });
                } else {
                    for (DomeUser u : CacheSet.getInstance().selectUsers) {
                        if (editWrite.getText().toString().contains(u.getUsername())) {
                            editWrite.setText(editWrite.getText().toString().replace("@" + u.getUsername() + " ", ""));
                            executorList.add(u);
                            log("\n>>>已@" + u.getUsername());
                        }
                    }
                    inviteCount = executorList.size();
                    pushCount = executorList.size();

                    log(">>>！！！总共邀请人数：" + inviteCount);
                    for (DomeUser u : executorList) {
                        doInviteUser(u);
                        pushMessage(u);
                    }
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private int inviteCount;
    private int inviteNum;

    private void doInviteUser(final DomeUser u) {
        log(u);
        String username = Preferences.getInstance().getPreferencesToString(me, "user", "username");
        Task task = new Task();
        task.setTaskText(editWrite.getText().toString());
        task.setAuthor(username);
        task.setWeight(0);
        task.setExecutor(u.getUsername());
        task.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    inviteNum++;
                    log(">>>已邀请人数：" + inviteNum);
                    if (inviteNum == inviteCount && pushNum == pushCount) {
                        doFinish();
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private int pushCount;
    private int pushNum;

    private void pushMessage(final DomeUser u) {
        String username = Preferences.getInstance().getPreferencesToString(me, "user", "username");
        String uid = u.getObjectId();
        BmobPushManager bmobPush = new BmobPushManager();
        BmobQuery<MyBmobInstallation> query = BmobInstallation.getQuery();
        query.addWhereEqualTo("uid", uid);
        bmobPush.setQuery(query);
        bmobPush.pushMessage("@" + username + "派给您一个新的事项");

        log(">>>开始推送给" + uid + "，消息："+"@" + username + "派给您一个新的事项");

        pushNum++;
        if (inviteNum == inviteCount && pushNum == pushCount) {
            doFinish();
        }
    }

    private void doFinish() {
        progressbarDialog.dismiss();
        log(">>>邀请完毕");
        toast("完成");
        if (CacheSet.getInstance().taskPage != null)
            CacheSet.getInstance().taskPage.getTask();
        onBackPressed();
    }

    public void getAddAtUser(String username) {
        int index = editWrite.getSelectionStart();//获取光标所在位置
        Editable edit = editWrite.getEditableText();//获取EditText的文字
        if (index < 0 || index >= edit.length()) {
            edit.append(username);
        } else {
            edit.insert(index, username);//光标所在位置插入文字
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        int version = Integer.valueOf(android.os.Build.VERSION.SDK);
        if (version > 5) {
            overridePendingTransition(R.anim.hold, R.anim.back);
        }
    }
}
