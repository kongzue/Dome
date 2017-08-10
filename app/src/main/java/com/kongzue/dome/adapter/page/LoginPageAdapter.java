package com.kongzue.dome.adapter.page;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dome.MainActivity;
import com.kongzue.dome.R;
import com.kongzue.dome.domain.DomeUser;
import com.kongzue.dome.plugin.BaseActivity;
import com.kongzue.dome.plugin.Preferences;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by ZhangChao on 2017/7/19.
 */

public class LoginPageAdapter {

    private LinearLayout boxBody;
    private LinearLayout sysStatusBar;
    private TextView txtTitle;
    private TextView btnLogin;
    private LinearLayout boxUsername;
    private MaterialEditText editUsername;
    private LinearLayout boxPassword;
    private MaterialEditText editPasssword;
    private RelativeLayout boxProgressBar;
    private ProgressBar progressBar;

    private Dialog dialog;
    private BaseActivity me;
    private View contnetView;

    public LoginPageAdapter(Dialog dialog, BaseActivity context) {
        this.dialog = dialog;
        this.me = context;
        initViews(contnetView = LayoutInflater.from(context).inflate(R.layout.layout_login, null));
        setEvent();
    }

    private void setEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btnStr= btnLogin.getText().toString();
                if (btnStr.equals("下一步")) {
                    me.moveAnimation(boxUsername, "X", -me.getDisPlayWidth());
                    txtTitle.setText("检查中...");
                    btnLogin.setVisibility(View.GONE);
                    me.setIMMStatus(false, editUsername);

                    boxProgressBar.setX(me.getDisPlayWidth());
                    boxProgressBar.setVisibility(View.VISIBLE);
                    me.moveAnimation(boxProgressBar, "X", 0);

                    BmobQuery<DomeUser> query = new BmobQuery<DomeUser>();
                    query.addWhereEqualTo("username", editUsername.getText().toString());
                    query.setLimit(1);
                    query.findObjects(new FindListener<DomeUser>() {
                        @Override
                        public void done(List<DomeUser> object, BmobException e) {
                            if (e == null) {
                                if (object.size() != 0) {
                                    txtTitle.setText("登录");
                                    btnLogin.setText("登录");
                                } else {
                                    txtTitle.setText("注册");
                                    btnLogin.setText("注册");
                                }
                                btnLogin.setVisibility(View.VISIBLE);
                                boxPassword.setX(me.getDisPlayWidth());
                                boxPassword.setVisibility(View.VISIBLE);
                                me.moveAnimation(boxProgressBar, "X", -me.getDisPlayWidth());
                                me.moveAnimation(boxPassword, "X", 0);
                            } else {
                                e.printStackTrace();
                                me.toast("网络连接错误，请重试");
                                btnLogin.setVisibility(View.VISIBLE);
                                me.moveAnimation(boxProgressBar, "X", me.getDisPlayWidth());
                                me.moveAnimation(boxUsername, "X", 0);
                            }
                        }
                    });
                }else if (btnStr.equals("注册")){
                    DomeUser user = new DomeUser();
                    user.setUsername(editUsername.getText().toString());
                    user.setPassword(editPasssword.getText().toString());
                    //注意：不能用save方法进行注册
                    user.signUp(new SaveListener<DomeUser>() {
                        @Override
                        public void done(DomeUser bmobUser, BmobException e) {
                            if(e==null){
                                me.toast("欢迎您，"+bmobUser.getUsername());
                                Preferences.getInstance().setPreferences(me,"user","username",bmobUser.getUsername());
                                Preferences.getInstance().setPreferences(me,"user","token",bmobUser.getObjectId());
                                ((MainActivity)me).onLoginFinished();
                            }else{
                                me.toast("网络连接错误，请重试");
                            }
                        }
                    });
                }else if (btnStr.equals("登录")){
                    DomeUser user = new DomeUser();
                    user.setUsername(editUsername.getText().toString());
                    user.setPassword(editPasssword.getText().toString());
                    user.login(new SaveListener<DomeUser>() {

                        @Override
                        public void done(DomeUser bmobUser, BmobException e) {
                            if(e==null){
                                me.toast("欢迎回来，"+bmobUser.getUsername());
                                Preferences.getInstance().setPreferences(me,"user","username",bmobUser.getUsername());
                                Preferences.getInstance().setPreferences(me,"user","token",bmobUser.getObjectId());
                                ((MainActivity)me).onLoginFinished();
                            }else{
                                if (e.getErrorCode()==101){
                                    me.toast("密码错误，请重试");
                                }else {
                                    me.toast("网络连接错误，请重试");
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void initViews(View v) {
        boxBody = (LinearLayout) v.findViewById(R.id.box_body);
        sysStatusBar = (LinearLayout) v.findViewById(R.id.sys_statusBar);
        txtTitle = (TextView) v.findViewById(R.id.txt_title);
        btnLogin = (TextView) v.findViewById(R.id.btn_login);
        boxUsername = (LinearLayout) v.findViewById(R.id.box_username);
        editUsername = (MaterialEditText) v.findViewById(R.id.edit_username);
        boxPassword = (LinearLayout) v.findViewById(R.id.box_password);
        editPasssword = (MaterialEditText) v.findViewById(R.id.edit_passsword);
        boxProgressBar = (RelativeLayout) v.findViewById(R.id.box_progressBar);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
    }

    public void show(){
        dialog.setContentView(contnetView);
    }

    public View getContnetView() {
        return contnetView;
    }
}
