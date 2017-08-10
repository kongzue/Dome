package com.kongzue.dome.adapter.page;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dome.R;
import com.kongzue.dome.activity.WriteActivity;
import com.kongzue.dome.adapter.list.TaskListAdapter;
import com.kongzue.dome.cache.CacheSet;
import com.kongzue.dome.domain.Task;
import com.kongzue.dome.plugin.BaseActivity;
import com.kongzue.dome.plugin.Preferences;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by ZhangChao on 2017/7/19.
 */

public class TaskPageAdapter {

    private LinearLayout boxBody;
    private LinearLayout btnMenu;
    private TextView btnTitle;
    private TextView btnAdd;
    private RelativeLayout boxList;
    private LinearLayout linearLayout;
    private ListView listTask;
    private RelativeLayout boxProgressBar;
    private ProgressBar progressBar;
    private LinearLayout boxEmpty;

    private View contnetView;
    private Dialog dialog;
    private BaseActivity me;

    public TaskPageAdapter(Dialog dialog, BaseActivity context) {
        this.dialog = dialog;
        this.me = context;
        initViews(contnetView = LayoutInflater.from(context).inflate(R.layout.layout_main, null));
    }

    private boolean isLongClick = false;
    private int longClickItenIndex = -1;

    private void setEvent() {

        btnTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(btnTitle);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                me.startActivity(new Intent(me, WriteActivity.class));
                int version = Integer.valueOf(android.os.Build.VERSION.SDK);
                if (version > 5) {
                    me.overridePendingTransition(R.anim.fade, R.anim.hold);
                }
            }
        });

        listTask.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                longClickItenIndex = position;
                isLongClick = true;
                return true;
            }
        });

        listTask.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!listTask.canScrollVertically(-1)) {
                    listTask.requestDisallowInterceptTouchEvent(false);
                } else {
                    listTask.requestDisallowInterceptTouchEvent(true);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (isLongClick && longClickItenIndex != -1) {
                        datas.get(longClickItenIndex).setChangeProcess(false);
                        taskListAdapter.notifyDataSetChanged();
                        doUpdateProcess(longClickItenIndex);
                        isLongClick = false;
                    }
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (isLongClick && datas != null && datas.get(longClickItenIndex) != null) {
                        listTask.requestDisallowInterceptTouchEvent(true);
                        double bl = event.getX() / me.getDisPlayWidth() * 100 * 1.2;

                        if (bl > 100) bl = 100;
                        if (bl < 0) bl = 0;

                        datas.get(longClickItenIndex).setProgress((int) bl);
                        datas.get(longClickItenIndex).setChangeProcess(true);
                        taskListAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void doUpdateProcess(final int index) {
        me.log("开始更新数据");
        Task task = datas.get(index);
        if (task.getProgress() == 100) {
            task.setWeight(-1);
        } else {
            task.setWeight(0);
        }
        boxProgressBar.setVisibility(View.VISIBLE);
        task.update(task.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                boxProgressBar.setVisibility(View.GONE);
                if (e == null) {
                    me.log("bmob更新成功");
                    getTask();
                } else {
                    me.toast("设置失败，请检查网络");
                    getTask();
                }
            }
        });
    }

    private TaskListAdapter taskListAdapter;
    private List<Task> datas;

    private void initDatas() {
        boxProgressBar.setVisibility(View.VISIBLE);
        getTask();
    }

    private int normalWeight = 0;

    public void getTask() {
        String username = Preferences.getInstance().getPreferencesToString(me, "user", "username");
        datas = new ArrayList<>();

        List<BmobQuery<Task>> queries = new ArrayList<BmobQuery<Task>>();
        BmobQuery<Task> query1 = new BmobQuery<Task>();
        query1.addWhereEqualTo("executor", username);
        query1.addWhereEqualTo("weight", normalWeight);
        queries.add(query1);
        BmobQuery<Task> query2 = new BmobQuery<Task>();
        query2.addWhereEqualTo("author", username);
        query2.addWhereEqualTo("weight", normalWeight);
        queries.add(query2);

        BmobQuery<Task> mainQuery = new BmobQuery<Task>();
//        mainQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);    // 先从缓存获取数据，如果没有，再从网络获取。
        mainQuery.or(queries);
        mainQuery.order("-createdAt");

        mainQuery.setLimit(150);
        boxProgressBar.setVisibility(View.VISIBLE);
        mainQuery.findObjects(new FindListener<Task>() {
            @Override
            public void done(List<Task> object, BmobException e) {
                boxProgressBar.setVisibility(View.GONE);
                if (e == null) {
                    datas = object;
                    if (datas.size() == 0) {
                        boxEmpty.setVisibility(View.VISIBLE);
                        boxList.setVisibility(View.GONE);
                    } else {
                        boxEmpty.setVisibility(View.GONE);
                        boxList.setVisibility(View.VISIBLE);

                        taskListAdapter = new TaskListAdapter(me, datas);
                        listTask.setAdapter(taskListAdapter);
                    }
                } else {
                    e.printStackTrace();
                    me.toast("加载数据失败");
                }
            }
        });
    }

    private void initViews(View v) {
        boxBody = (LinearLayout) v.findViewById(R.id.box_body);
        btnMenu = (LinearLayout) v.findViewById(R.id.btn_menu);
        btnTitle = (TextView) v.findViewById(R.id.btn_title);
        btnAdd = (TextView) v.findViewById(R.id.btn_add);
        boxList = (RelativeLayout) v.findViewById(R.id.box_list);
        linearLayout = (LinearLayout) v.findViewById(R.id.linearLayout);
        listTask = (ListView) v.findViewById(R.id.list_task);
        boxProgressBar = (RelativeLayout) v.findViewById(R.id.box_progressBar);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        boxEmpty = (LinearLayout) v.findViewById(R.id.box_empty);
    }

    public void show() {
        CacheSet.getInstance().taskPage = this;
        dialog.setContentView(contnetView);
        initDatas();
        setEvent();
    }

    public View getContnetView() {
        return contnetView;
    }

    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(me, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                me.log("clickItem.getItemId()=" + item.getItemId());
                boxProgressBar.setVisibility(View.VISIBLE);
                switch (item.getTitle().toString()) {
                    case "事项":
                        btnTitle.setText("事项");
                        normalWeight = 0;
                        getTask();
                        break;
                    case "已完成事项":
                        btnTitle.setText("已完成事项");
                        normalWeight = -1;
                        getTask();
                        break;
                    default:

                        break;
                }
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {

            }
        });

        popupMenu.show();
    }
}
