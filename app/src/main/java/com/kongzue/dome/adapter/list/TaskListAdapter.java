package com.kongzue.dome.adapter.list;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kongzue.dome.R;
import com.kongzue.dome.domain.Task;
import com.kongzue.dome.plugin.BaseActivity;
import com.kongzue.dome.plugin.Preferences;

import java.util.List;

/**
 * Created by ZhangChao on 2017/7/19.
 */

public class TaskListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Task> datas;
    private Context context;

    public TaskListAdapter(Context context, List<Task> datas) {
        this.datas = datas;
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Task obj = datas.get(i);
        int type = getItemViewType(i);
        ViewContext viewHolder = null;
        if (view == null) {
            switch (type) {
                case 1:
                    viewHolder = new ViewContext();
                    view = mInflater.inflate(R.layout.item_task, null);

                    viewHolder.txtProgress = (TextView) view.findViewById(R.id.txt_progress);
                    viewHolder.txtProgressCaption = (TextView) view.findViewById(R.id.txt_progress_caption);
                    viewHolder.boxMain = (LinearLayout) view.findViewById(R.id.box_main);
                    viewHolder.txtTitle = (TextView) view.findViewById(R.id.txt_title);
                    viewHolder.txtTip = (TextView) view.findViewById(R.id.txt_tip);
                    viewHolder.boxTime = (LinearLayout) view.findViewById(R.id.box_time);
                    viewHolder.txtTime = (TextView) view.findViewById(R.id.txt_time);
                    view.setTag(viewHolder);
                    break;
                default:
                    break;
            }
        } else {
            switch (type) {
                case 1:
                    viewHolder = (ViewContext) view.getTag();
                    break;
                default:
                    break;
            }
        }
        switch (type) {
            case 1:
                viewHolder.txtTitle.setText(obj.getTaskText());
                String strTip = ">>>";
                String myUserName = Preferences.getInstance().getPreferencesToString(context, "user", "username");
//                ((BaseActivity) context).log(">>>" + obj);
                if (obj.getExecutor().equals(obj.getAuthor())) {
                    strTip = "我创建的";
                } else {
                    if (obj.getAuthor().equals(myUserName)) {
                        strTip = "我 指派给 @" + obj.getExecutor();
                    }
                    if (obj.getExecutor().equals(myUserName)) {
                        strTip = "@" + obj.getAuthor() + " 指派给 我";
                    }
                }
                viewHolder.txtTip.setText(strTip);
                viewHolder.boxTime.setVisibility(View.GONE);
                if (obj.getProgress() != 0) {
                    double width = (double) ((BaseActivity) context).getDisPlayWidth() * 1.2 * ((double) obj.getProgress() / 100.0);
//                    ((BaseActivity) context).log(">>>" + width);
                    ViewGroup.LayoutParams p = viewHolder.txtProgress.getLayoutParams();
                    p.width = (int) width;
                    p.height = view.getHeight();
                    viewHolder.txtProgress.setLayoutParams(p);
                } else {
                    double width = 0;
                    ViewGroup.LayoutParams p = viewHolder.txtProgress.getLayoutParams();
                    p.width = (int) width;
                    p.height = view.getHeight();
                    viewHolder.txtProgress.setLayoutParams(p);
                }
                if (obj.isChangeProcess()) {
                    viewHolder.boxMain.setVisibility(View.INVISIBLE);
                    viewHolder.txtProgressCaption.setText("设置进度：" + obj.getProgress() + "%");
                } else {
                    viewHolder.boxMain.setVisibility(View.VISIBLE);
                    viewHolder.txtProgressCaption.setText("");
                }
                break;
            default:
                break;
        }
        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    class ViewContext {
        TextView txtProgress;
        TextView txtProgressCaption;
        LinearLayout boxMain;
        TextView txtTitle;
        TextView txtTip;
        LinearLayout boxTime;
        TextView txtTime;
    }

    public void zoomAnim(View view, int value) {
        ViewWrapper viewWrapper = new ViewWrapper(view);
        ObjectAnimator.ofInt(viewWrapper, "width", value).setDuration(200).start();
    }

    private class ViewWrapper {

        private View rView;

        public ViewWrapper(View target) {
            rView = target;
        }

        public int getWidth() {
            return rView.getLayoutParams().width;
        }

        public void setWidth(int width) {
            rView.getLayoutParams().width = width;
            rView.requestLayout();
        }

        public int getHeight() {
            return rView.getLayoutParams().height;
        }

        public void setHeight(int height) {
            rView.getLayoutParams().height = height;
            rView.requestLayout();
        }
    }

}
