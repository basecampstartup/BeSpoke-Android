//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 12/12/2016.
//===============================================================================
package com.bespoke.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bespoke.DocumentsCategoryActivity;
import com.bespoke.R;
import com.bespoke.ReportsActivity;
import com.bespoke.ViewIssuesActivity;
import com.bespoke.ViewRequestsActivity;

public class HomeFragment extends Fragment  implements View.OnClickListener {
    ImageView btnViewIssues,btnViewRequests,btnDocuments;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    /**
     *  Overridden method to handle clicks of UI components
     *  @param v
     */
    @Override
    public void onClick(View v) {

        //switch case
        switch (v.getId()) {

            case R.id.btnViewIssues:
                startActivity(new Intent(getActivity(), ViewIssuesActivity.class));
                break;
            case R.id.btnViewRequests:
                startActivity(new Intent(getActivity(), ViewRequestsActivity.class));
                break;
            case R.id.btnDocuments:
                startActivity(new Intent(getActivity(), DocumentsCategoryActivity.class));
                break;
            case R.id.btnCategory:
                startActivity(new Intent(getActivity(), ReportsActivity.class));
                break;
        }
    }
}
