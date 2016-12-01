package com.bespoke.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bespoke.Model.Category;

import java.util.ArrayList;

/**
 * Created by admin on 11/17/2016.
 */

public class CategoryListDialogAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Category> categoryList;
    private LayoutInflater layoutInflater;

    public CategoryListDialogAdapter(Context mContext, ArrayList<Category> categoryList) {
        this.mContext = mContext;
        this.categoryList = categoryList;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(android.R.layout.simple_list_item_1, null);
            holder = new ViewHolder();
            holder.txtApplianceName = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtApplianceName.setText(categoryList.get(position).getCategory());
        return convertView;
    }

    static class ViewHolder {
        TextView txtApplianceName;
    }
}
