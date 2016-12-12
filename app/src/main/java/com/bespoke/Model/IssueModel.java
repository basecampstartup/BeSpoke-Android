package com.bespoke.Model;

/**
 * Created by admin on 11/25/2016.
 */

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
