//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 22/11/2016
//===============================================================================
package com.bespoke.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bespoke.Model.SubCategoryModel;

import java.util.ArrayList;

public class SubCategoryListDialogAdapter extends BaseAdapter {
    /** context of current Activity */
    Context mContext;

    ArrayList<SubCategoryModel> subCategoryList;
    private LayoutInflater layoutInflater;

    public SubCategoryListDialogAdapter(Context mContext, ArrayList<SubCategoryModel> subCategoryList) {
        this.mContext = mContext;
        this.subCategoryList = subCategoryList;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return subCategoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return subCategoryList.get(position);
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
        holder.txtApplianceName.setText(subCategoryList.get(position).getSubcategory());
        return convertView;
    }

    //Holder class for Adapter UI components.
    static class ViewHolder {
        TextView txtApplianceName;
    }
}
