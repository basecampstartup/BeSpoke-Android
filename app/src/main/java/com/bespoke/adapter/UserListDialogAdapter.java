package com.bespoke.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bespoke.Model.Category;
import com.bespoke.Model.UserModel;

import java.util.ArrayList;

/**
 * Created by admin on 11/17/2016.
 */

public class UserListDialogAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<UserModel> userList;
    private LayoutInflater layoutInflater;

    public UserListDialogAdapter(Context mContext, ArrayList<UserModel> userList) {
        this.mContext = mContext;
        this.userList = userList;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
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
        holder.txtApplianceName.setText(userList.get(position).getUserName());
        return convertView;
    }

    static class ViewHolder {
        TextView txtApplianceName;
    }
}
