package com.kongzue.dome;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.WindowManager;

import com.kongzue.dome.adapter.page.LoginPageAdapter;
import com.kongzue.dome.adapter.page.TaskPageAdapter;
import com.kongzue.dome.plugin.BaseActivity;
import com.kongzue.dome.plugin.MyBottomSheetDialog;
import com.kongzue.dome.plugin.Preferences;
import com.kongzue.dome.push.MyBmobInstallation;

import java.util.List;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDatas();
    }

    private MyBottomSheetDialog dialog;

    private LoginPageAdapter loginPageAdapter;
    private TaskPageAdapter taskPageAdapter;

    @Override
    public void initDatas() {
        overridePendingTransition(0, 0);
        setTranslucentStatus(true, false);

        initDialog();
        updateUserInfo();
    }

    //推送用，更新用户设备信息
    private void updateUserInfo() {
        BmobQuery<MyBmobInstallation> query = new BmobQuery<MyBmobInstallation>();
        query.addWhereEqualTo("installationId", BmobInstallation.getInstallationId(this));
        query.findObjects(new FindListener<MyBmobInstallation>() {

            @Override
            public void done(List<MyBmobInstallation> object, BmobException e) {
                if (e==null && object!=null) {
                    if (object.size() != 0) {
                        MyBmobInstallation mbi = object.get(0);
                        mbi.setUid(Preferences.getInstance().getPreferencesToString(me, "user", "token"));
                        mbi.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    log("设备信息更新成功");
                                } else {
                                    log("设备信息更新失败");
                                }
                            }
                        });
                    }
                }
            }

        });
    }

    private void initDialog() {
        dialog = new MyBottomSheetDialog(me);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        loginPageAdapter = new LoginPageAdapter(dialog, (BaseActivity) me);
        taskPageAdapter = new TaskPageAdapter(dialog, (BaseActivity) me);

        if (Preferences.getInstance().getPreferencesToString(me, "user", "token").isEmpty()) {
            loginPageAdapter.show();
        } else {
            taskPageAdapter.show();
        }

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        dialog.getWindow().setDimAmount(0.5f);      //透明度
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
    }

    @Override
    public void initViews() {

    }

    @Override
    public void setEvent() {

    }

    public void onLoginFinished() {
        taskPageAdapter.show();
    }
}
