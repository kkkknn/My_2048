package com.example.kkkkkn.my_2048;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

public class MybaseAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<item> list=new ArrayList<item>();
    private LayoutInflater layoutInflater;

    public MybaseAdapter(Context context, ArrayList<item> item) {
        this.list = item;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_game, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.item_view);
            convertView.setClickable(false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        item provinceBean = list.get(position);
        if (provinceBean != null) {
            if(provinceBean.getValue()==0){
                holder.text.setText(" ");
            }else{
                holder.text.setText(provinceBean.getValue()+" ");
            }
            holder.text.setBackgroundColor(255*255*255+215*215);
            holder.text.setHeight(100);
            holder.text.setWidth(100);
        }
        return convertView;
    }
    class ViewHolder {
        TextView text;
    }
}
