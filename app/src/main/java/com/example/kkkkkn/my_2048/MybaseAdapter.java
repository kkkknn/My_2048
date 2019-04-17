package com.example.kkkkkn.my_2048;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
/**
 * 设置网格布局更新数据
 */

public class MybaseAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<item> list=new ArrayList<item>();
    private LayoutInflater layoutInflater;
    //宽度
    private int auto_width;

    public MybaseAdapter(Context context, ArrayList<item> item,int auto) {
        this.list = item;
        auto_width=auto;
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
            //设置每个子元素的数值，根据之前的处理计算
            if(provinceBean.getValue()==0 ){
                holder.text.setText(" ");
            }else{
                holder.text.setText(Integer.toString(provinceBean.getValue()));
            }
            holder.text.setHeight(auto_width/4-30);
            holder.text.setWidth(auto_width/4-30);
        }
        return convertView;
    }
    class ViewHolder {
        TextView text;
    }
}
