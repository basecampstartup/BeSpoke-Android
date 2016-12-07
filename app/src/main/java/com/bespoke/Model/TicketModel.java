package com.bespoke.Model;

import java.io.Serializable;

/**
 * Created by admin on 11/29/2016.
 */

public class TicketModel implements Serializable {

    private String ticket_id;
    private String shortdesc;
    private String description;
    private String user_id;
    private String ticketopendate;
    private String assignedto;
    private int ticketstatus;
    private String tickettype;
    private String cat_id;
    private String subcat_id;
    private String IsDeleted;
    private String createdby;
    private String Createddate;
    private String modifiedby;
    private String modifieddate;
    private String CategoryName;
    private String SubCategoryName;
    private String UserName;
    private String AssignedToName;

    public String getTicket_id() {
        if (ticket_id == null || ticket_id.equalsIgnoreCase("null")) ticket_id = "";
        return ticket_id;
    }

    public void setTicket_id(String ticket_id) {

        this.ticket_id = ticket_id;
    }

    public String getShortdesc() {
        if (shortdesc == null || shortdesc.equalsIgnoreCase("null")) shortdesc = "";
        return shortdesc;
    }

    public void setShortdesc(String shortdesc) {

        this.shortdesc = shortdesc;
    }

    public String getDescription() {
        if (description == null || description.equalsIgnoreCase("null")) description = "";
        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public String getUser_id() {
        if (user_id == null || user_id.equalsIgnoreCase("null")) user_id = "";
        return user_id;
    }

    public void setUser_id(String user_id) {

        this.user_id = user_id;
    }

    public String getTicketopendate() {
        if (ticketopendate == null || ticketopendate.equalsIgnoreCase("null")) ticketopendate = "";
        return ticketopendate;
    }

    public void setTicketopendate(String ticketopendate) {

        this.ticketopendate = ticketopendate;
    }

    public String getAssignedto() {
        if (assignedto == null || assignedto.equalsIgnoreCase("null")) assignedto = "";
        return assignedto;
    }

    public void setAssignedto(String assignedto) {

        this.assignedto = assignedto;
    }

    public int getTicketstatus() {
        return ticketstatus;
    }

    public void setTicketstatus(int ticketstatus) {

        this.ticketstatus = ticketstatus;
    }

    public String getTickettype() {
        if (tickettype == null || tickettype.equalsIgnoreCase("null")) tickettype = "";
        return tickettype;
    }

    public void setTickettype(String tickettype) {

        this.tickettype = tickettype;
    }

    public String getCat_id() {
        if (cat_id == null || cat_id.equalsIgnoreCase("null")) cat_id = "";
        return cat_id;
    }

    public void setCat_id(String cat_id) {

        this.cat_id = cat_id;
    }

    public String getSubcat_id() {
        if (subcat_id == null || subcat_id.equalsIgnoreCase("null")) subcat_id = "";
        return subcat_id;
    }

    public void setSubcat_id(String subcat_id) {

        this.subcat_id = subcat_id;
    }

    public String getIsDeleted() {
        if (IsDeleted == null || IsDeleted.equalsIgnoreCase("null")) IsDeleted = "";
        return IsDeleted;
    }

    public void setIsDeleted(String isDeleted) {

        IsDeleted = isDeleted;
    }

    public String getCreatedby() {
        if (createdby == null || createdby.equalsIgnoreCase("null")) createdby = "";
        return createdby;
    }

    public void setCreatedby(String createdby) {

        this.createdby = createdby;
    }

    public String getCreateddate() {
        if (Createddate == null || Createddate.equalsIgnoreCase("null")) Createddate = "";
        return Createddate;
    }

    public void setCreateddate(String createddate) {

        Createddate = createddate;
    }

    public String getModifiedby() {
        if (modifiedby == null || modifiedby.equalsIgnoreCase("null")) modifiedby = "";
        return modifiedby;
    }

    public void setModifiedby(String modifiedby) {

        this.modifiedby = modifiedby;
    }

    public String getModifieddate() {
        if (modifieddate == null || modifieddate.equalsIgnoreCase("null")) modifieddate = "";
        return modifieddate;
    }

    public void setModifieddate(String modifieddate) {

        this.modifieddate = modifieddate;
    }

    public String getCategoryName() {
        if (CategoryName == null || CategoryName.equalsIgnoreCase("null")) CategoryName = "";
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {

        CategoryName = categoryName;
    }

    public String getSubCategoryName() {
        if (SubCategoryName == null || SubCategoryName.equalsIgnoreCase("null"))
            SubCategoryName = "";
        return SubCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {

        SubCategoryName = subCategoryName;
    }

    public String getUserName() {
        if (UserName == null || UserName.equalsIgnoreCase("null")) UserName = "";
        return UserName;
    }

    public void setUserName(String userName) {

        UserName = userName;
    }

    public String getAssignedToName() {
        if (AssignedToName == null || AssignedToName.equalsIgnoreCase("null")) AssignedToName = "";
        return AssignedToName;
    }

    public void setAssignedToName(String assignedToName) {
        AssignedToName = assignedToName;
    }
}
