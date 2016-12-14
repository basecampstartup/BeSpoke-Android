package com.bespoke.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bespoke.Model.Category;
import com.bespoke.Model.TicketModel;
import com.bespoke.R;
import com.bespoke.utils.TicketStatus;

import java.util.ArrayList;

/**
 * Created by admin on 11/9/2016.
 */

public class CategoryListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Category> ticketModels;
    private LayoutInflater lInflater;

    public CategoryListAdapter(Context mContext, ArrayList<Category> issueModels) {
        this.mContext = mContext;
        this.ticketModels = issueModels;
        lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ticketModels.size();
    }

    @Override
    public Object getItem(int position) {
        return ticketModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = lInflater.inflate(R.layout.categorylist, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvIssueID = (TextView) convertView.findViewById(R.id.tvIDValue);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tvCategoryValue);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        try {
            viewHolder.tvIssueID.setText(""+ticketModels.get(position).getCat_id());
            viewHolder.tvDescription.setText(ticketModels.get(position).getCategory());
        }catch(Exception e){
            e.printStackTrace();
        }

        return convertView;
    }

    public static class ViewHolder {
        public TextView tvIssueID;
        public TextView tvDescription;
    }
}
