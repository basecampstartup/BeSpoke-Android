package com.bespoke.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bespoke.Model.IssueModel;
import com.bespoke.Model.TicketModel;
import com.bespoke.R;
import java.util.ArrayList;

/**
 * Created by admin on 11/9/2016.
 */

public class IssueListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<TicketModel> ticketModels;
    private LayoutInflater lInflater;

    public IssueListAdapter(Context mContext, ArrayList<TicketModel> issueModels) {
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
            convertView = lInflater.inflate(R.layout.issue_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvIssueID = (TextView) convertView.findViewById(R.id.tvIDValue);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tvShortDescValue);
            viewHolder.tvCategory = (TextView) convertView.findViewById(R.id.tvCategoryValue);
            viewHolder.tvAffectedArea = (TextView) convertView.findViewById(R.id.tvAffectedAreaValue);
            viewHolder.tvUser = (TextView) convertView.findViewById(R.id.tvUserValue);
            viewHolder.tvOpenDate = (TextView) convertView.findViewById(R.id.tvIssueOpenDateValue);
            viewHolder.tvAssignedTo = (TextView) convertView.findViewById(R.id.tvAssignedToValue);
            viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatusValue);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvIssueID.setText(ticketModels.get(position).getTicket_id());
        viewHolder.tvDescription.setText(ticketModels.get(position).getShortdesc());
        viewHolder.tvCategory.setText(ticketModels.get(position).getCategoryName());
        viewHolder.tvAffectedArea.setText(ticketModels.get(position).getCategoryName());
        viewHolder.tvUser.setText(ticketModels.get(position).getTicket_id());
        viewHolder.tvOpenDate.setText(ticketModels.get(position).getTicketopendate());
        viewHolder.tvAssignedTo.setText(ticketModels.get(position).getAssignedto());
        viewHolder.tvStatus.setText(ticketModels.get(position).getAssignedto());

        return convertView;
    }

    public static class ViewHolder {
        public TextView tvIssueID;
        public TextView tvDescription;
        public TextView tvCategory;
        public TextView tvAffectedArea;
        public TextView tvUser;
        public TextView tvOpenDate;
        public TextView tvAssignedTo;
        public TextView tvStatus;
    }
}
