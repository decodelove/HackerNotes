package com.logincore.hackernotes.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logincore.hackernotes.NewNoteActivity;
import com.logincore.hackernotes.R;
import com.logincore.hackernotes.bean.NotesBean;
import com.logincore.hackernotes.bean.NotesDbBean;
import com.logincore.hackernotes.utils.LogUtils;
import com.logincore.hackernotes.utils.ToastUtils;

import java.util.ArrayList;

public class NotesAdapter extends BaseAdapter {
    private final Context mContext;
    private ArrayList<NotesDbBean> mList;

    public NotesAdapter(Context context,ArrayList<NotesDbBean> list){
        this.mContext = context;
        this.mList = list;
    }
    @Override
    public int getCount() {
        LogUtils.i(this,"返回数据:"+mList.size());
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.listview_item, null);
        }
        viewHolder = ViewHolder.getViewHolder(convertView);
        viewHolder.itemTime.setText(mList.get(position).getTime());
        viewHolder.itemContent.setText(mList.get(position).getContent());

        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show(mContext,"你点击了第"+(position+1)+"条数据");
                Intent intent = new Intent(mContext, NewNoteActivity.class);
                intent.putExtra("items",position+1);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    public void setAdapterData(ArrayList<NotesDbBean> dbBeans) {
        mList = dbBeans;
    }

    private static class ViewHolder {
        TextView itemTime;
        TextView itemContent;
        LinearLayout item;

        public ViewHolder(View convertView) {
            item = convertView.findViewById(R.id.item);
            itemTime = convertView.findViewById(R.id.item_time);
            itemContent = convertView.findViewById(R.id.item_content);
        }

        public static ViewHolder getViewHolder(View convertView) {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder==null){
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            return viewHolder;
        }
    }
}
