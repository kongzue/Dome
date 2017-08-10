package com.kongzue.dome.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dome.R;
import com.kongzue.dome.adapter.list.SelectUserAdapter;
import com.kongzue.dome.cache.CacheSet;
import com.kongzue.dome.domain.DomeUser;
import com.kongzue.dome.plugin.BaseActivity;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SelectAtUserActivity extends BaseActivity {

    private RelativeLayout boxBody;
    private LinearLayout sysStatusBar;
    private ImageView btnCancel;
    private MaterialEditText editUsername;
    private ListView listUser;
    private LinearLayout boxEmpty;
    private TextView txtEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_select_at_user);
        setTranslucentStatus(true, true);
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void initViews() {
        boxBody = (RelativeLayout) findViewById(R.id.box_body);
        sysStatusBar = (LinearLayout) findViewById(R.id.sys_statusBar);
        btnCancel = (ImageView) findViewById(R.id.btn_cancel);
        editUsername = (MaterialEditText) findViewById(R.id.edit_username);
        listUser = (ListView) findViewById(R.id.list_user);
        boxEmpty = (LinearLayout) findViewById(R.id.box_empty);
        txtEmpty = (TextView) findViewById(R.id.txt_empty);
    }

    private List<DomeUser> datas;
    private SelectUserAdapter selectUserAdapter;

    @Override
    public void setEvent() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                int version = Integer.valueOf(android.os.Build.VERSION.SDK);
                if (version > 5) {
                    overridePendingTransition(R.anim.hold, R.anim.back);
                }
            }
        });

        editUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                BmobQuery<DomeUser> query = new BmobQuery<DomeUser>();
                query.addWhereEqualTo("username", s.toString());
                query.setLimit(10);
                query.findObjects(new FindListener<DomeUser>() {
                    @Override
                    public void done(List<DomeUser> object, BmobException e) {
                        if (e == null) {
                            datas = object;
                            if (object.size() == 0) {
                                txtEmpty.setText("没有搜索结果");
                                listUser.setVisibility(View.GONE);
                                boxEmpty.setVisibility(View.VISIBLE);
                            } else {
                                listUser.setVisibility(View.VISIBLE);
                                boxEmpty.setVisibility(View.GONE);
                                selectUserAdapter = new SelectUserAdapter(me, datas);
                                listUser.setAdapter(selectUserAdapter);
                            }
                        } else {
                            txtEmpty.setText("网络异常，请重试");
                            listUser.setVisibility(View.GONE);
                            boxEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

        listUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (datas.get(position).isSettingDoNotFindMe()){
                    toast("该用户设置不可邀请或您被拉黑");
                    return;
                }
                CacheSet.getInstance().selectUsers.add(datas.get(position));
                if (CacheSet.getInstance().writeActivity!=null) CacheSet.getInstance().writeActivity.getAddAtUser(datas.get(position).getUsername()+" ");
                finish();
                int version = Integer.valueOf(android.os.Build.VERSION.SDK);
                if (version > 5) {
                    overridePendingTransition(R.anim.hold, R.anim.back);
                }
            }
        });
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
