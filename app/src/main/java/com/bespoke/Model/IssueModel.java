package com.bespoke.Model;

/**
 * Created by admin on 11/25/2016.
 */

public class IssueModel {
    private String issueId="";
    private String description="";
    private String caterogy="";
    private String affectedArea="";
    private String user="";
    private String openDate="";
    private String assignTo="";
    private String status="";

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCaterogy() {
        return caterogy;
    }

    public void setCaterogy(String caterogy) {
        this.caterogy = caterogy;
    }

    public String getAffectedArea() {
        return affectedArea;
    }

    public void setAffectedArea(String affectedArea) {
        this.affectedArea = affectedArea;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(String assignTo) {
        this.assignTo = assignTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
