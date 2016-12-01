package com.bespoke.servercommunication;

import com.bespoke.Model.Category;
import com.bespoke.Model.TicketModel;
import com.bespoke.Model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 11/15/2016.
 * This class will parse the webservice json responses.
 */

public class ResponseParser {

    /**
     * Method for parse login response.
     *
     * @param object
     * @return
     */
    public static UserModel parseLoginResponse(Object object) {
        JSONObject jsonObj = null;
        UserModel model = new UserModel();
        try {
            jsonObj = new JSONObject(object.toString());
            model.setUser_id(jsonObj.getString(APIUtils.PARAM_USER_ID));
            model.setUserName(jsonObj.getString(APIUtils.PARAM_USER_NAME));
            model.setUsertype(jsonObj.getString(APIUtils.PARAM_USER_TYPE));
            model.setEmail(jsonObj.getString(APIUtils.PARAM_EMAIL));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }

    /**
     * Method for parse login response.
     *
     * @param object
     * @return
     */
    public static ArrayList<Category> parseCategoryResponse(Object object) {
        JSONArray categoriesArray = null;
        ArrayList<Category> cataegoryList=new ArrayList<>();
        try {
            JSONObject responseJsonObject=new JSONObject(object.toString());
            categoriesArray = responseJsonObject.getJSONArray("categories");
            for(int i=0;i<categoriesArray.length();i++)
            {
                JSONObject categoryObject=categoriesArray.getJSONObject(i);
                JSONObject categoryObject1=categoryObject.getJSONObject("category");
                int catId=categoryObject1.getInt("cat_id");
                String catName=categoryObject1.getString("category");
                Category category=new Category();
                category.setCat_id(catId);
                category.setCategory(catName);
                cataegoryList.add(category);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cataegoryList;
    }


    /**
     * Method for parse login response.
     *
     * @param object
     * @return
     */
    public static ArrayList<TicketModel> parseTicketsResponse(Object object) {
        JSONArray ticketArray = null;
        ArrayList<TicketModel> ticketList=new ArrayList<>();
        try {
            JSONObject responseJsonObject=new JSONObject(object.toString());
            ticketArray = responseJsonObject.getJSONArray("tickets");
            for(int i=0;i<ticketArray.length();i++)
            {
                JSONObject ticketObject=ticketArray.getJSONObject(i);
                JSONObject ticketObject1=ticketObject.getJSONObject("ticket");
                TicketModel ticketModel=new TicketModel();
                ticketModel.setTicket_id(ticketObject1.getString("ticket_id"));
                ticketModel.setShortdesc(ticketObject1.getString("shortdesc"));
                ticketModel.setDescription(ticketObject1.getString("description"));
                ticketModel.setUser_id(ticketObject1.getString("user_id"));
                ticketModel.setTicketopendate(ticketObject1.getString("ticketopendate"));
                ticketModel.setAssignedto(ticketObject1.getString("assignedto"));
                ticketModel.setTicketstatus(ticketObject1.getString("ticketstatus"));
                ticketModel.setTickettype(ticketObject1.getString("tickettype"));
                ticketModel.setCat_id(ticketObject1.getString("cat_id"));
                ticketModel.setSubcat_id(ticketObject1.getString("subcat_id"));
                ticketModel.setIsDeleted(ticketObject1.getString("IsDeleted"));
                ticketModel.setCreatedby(ticketObject1.getString("createdby"));
                ticketModel.setCreateddate(ticketObject1.getString("Createddate"));
                ticketModel.setModifiedby(ticketObject1.getString("modifiedby"));
                ticketModel.setModifieddate(ticketObject1.getString("modifieddate"));
                ticketModel.setCategoryName(ticketObject1.getString("CategoryName"));
                ticketModel.setSubCategoryName(ticketObject1.getString("SubCategoryName"));
                ticketModel.setUserName(ticketObject1.getString("UserName"));
                ticketModel.setAssignedToName(ticketObject1.getString("AssignedToName"));
                ticketList.add(ticketModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ticketList;
    }
}
