//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 21/11/2016
//===============================================================================
package com.bespoke.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bespoke.Model.DocumentModel;
import com.bespoke.R;
import java.util.ArrayList;

public class DocumentListAdapter extends BaseAdapter {
    /** context of current Activity */
    private Context mContext;

    private ArrayList<DocumentModel> documentModels;
    private LayoutInflater lInflater;

    public DocumentListAdapter(Context mContext, ArrayList<DocumentModel> issueModels) {
        this.mContext = mContext;
        this.documentModels = issueModels;
        lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return documentModels.size();
    }

    @Override
    public Object getItem(int position) {
        return documentModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = lInflater.inflate(R.layout.documentlistitem, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvDocName);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tvCategoryValue);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tvCategoryValue);
            viewHolder.layoutDescription = (LinearLayout) convertView.findViewById(R.id.layoutContentDescription);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try {
            viewHolder.tvName.setText("" + documentModels.get(position).getDoc_name());
            viewHolder.tvDescription.setText(documentModels.get(position).getDescription());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    //Holder class for Adapter UI components.
    public static class ViewHolder {
        public TextView tvName;
        public TextView tvDescription;
        public LinearLayout layoutDescription;
    }
}
