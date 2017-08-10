package com.kongzue.dome.adapter.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongzue.dome.R;
import com.kongzue.dome.domain.DomeUser;
import com.kongzue.dome.domain.Task;

import java.util.List;

/**
 * Created by ZhangChao on 2017/7/20.
 */

public class SelectUserAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<DomeUser> datas;
    private Context context;

    public SelectUserAdapter(Context context, List<DomeUser> datas) {
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
        DomeUser obj = datas.get(i);
        int type = getItemViewType(i);
        ViewContext viewHolder = null;
        if (view == null) {
            switch (type) {
                case 1:
                    viewHolder = new ViewContext();
                    view = mInflater.inflate(R.layout.item_user, null);

                    viewHolder.imgIco = (ImageView) view.findViewById(R.id.img_ico);
                    viewHolder.txtName = (TextView) view.findViewById(R.id.txt_name);
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
                viewHolder.txtName.setText(obj.getUsername());
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
        ImageView imgIco;
        TextView txtName;
    }
}
