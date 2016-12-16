//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 29/11/2016.
//===============================================================================
package com.bespoke.Model;

public class IssueModel extends TicketModel{

    private String Assignedto;

    @Override
    public String getAssignedto() {
        return Assignedto;
    }

    @Override
    public void setAssignedto(String assignedto) {
        Assignedto = assignedto;
    }
}
