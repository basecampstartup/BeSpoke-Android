//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 22/11/2016.
//===============================================================================
package com.bespoke.servercommunication;

import com.bespoke.Model.Category;
import com.bespoke.Model.DocumentModel;
import com.bespoke.Model.IssueModel;
import com.bespoke.Model.SubCategoryModel;
import com.bespoke.Model.UserModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * This class will parse the webservice json responses.
 */
public class ResponseParser {

    /**
     * Method for parse login response.
     * @param object
     * @return
     */
    public static UserModel parseLoginResponse(Object object) {
        JSONObject jsonObj = null;
        JSONObject jsonObj1 = null;
        UserModel model = new UserModel();
        try {
            jsonObj = new JSONObject(object.toString());
            jsonObj1 = jsonObj.getJSONObject("user");
            model.setUser_id(jsonObj1.getString(APIUtils.PARAM_USER_ID));
            model.setUserName(jsonObj1.getString(APIUtils.PARAM_USER_NAME));
            model.setUsertype(jsonObj1.getString(APIUtils.PARAM_USER_TYPE));
            model.setEmail(jsonObj1.getString(APIUtils.PARAM_EMAIL));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }

    /**
     * Method for parse login response.
     * @param object
     * @return
     */
    public static ArrayList<Category> parseCategoryResponse(Object object) {
        JSONArray categoriesArray = null;
        ArrayList<Category> cataegoryList = new ArrayList<>();
        try {
            JSONObject responseJsonObject = new JSONObject(object.toString());
            categoriesArray = responseJsonObject.getJSONArray("categories");
            for (int i = 0; i < categoriesArray.length(); i++) {
                JSONObject categoryObject = categoriesArray.getJSONObject(i);
                JSONObject categoryObject1 = categoryObject.getJSONObject("category");
                int catId = categoryObject1.getInt("cat_id");
                String catName = categoryObject1.getString("category");
                Category category = new Category();
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
     * Method for parse ticket response.
     * @param object
     * @return
     */
    public static ArrayList<IssueModel> parseTicketsResponse(Object object) {
        JSONArray ticketArray = null;
        ArrayList<IssueModel> ticketList = new ArrayList<>();
        try {
            JSONObject responseJsonObject = new JSONObject(object.toString());
            ticketArray = responseJsonObject.getJSONArray("tickets");
            for (int i = 0; i < ticketArray.length(); i++) {
                JSONObject ticketObject = ticketArray.getJSONObject(i);
                JSONObject ticketObject1 = ticketObject.getJSONObject("ticket");
                IssueModel ticketModel = new IssueModel();
                ticketModel.setTicket_id(ticketObject1.getString("ticket_id"));
                ticketModel.setShortdesc(ticketObject1.getString("shortdesc"));
                ticketModel.setDescription(ticketObject1.getString("description"));
                ticketModel.setUser_id(ticketObject1.getString("user_id"));
                ticketModel.setTicketopendate(ticketObject1.getString("ticketopendate"));
                ticketModel.setAssignedto(ticketObject1.getString("assignedto"));
                ticketModel.setTicketstatus(ticketObject1.getInt("ticketstatus"));
                ticketModel.setTickettype(ticketObject1.getInt("tickettype"));
                ticketModel.setCat_id(ticketObject1.getInt("cat_id"));
                ticketModel.setSubcat_id(ticketObject1.getInt("subcat_id"));
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

    /**
     * Method for parse Subcategory response.
     * @param object
     * @return
     */
    public static ArrayList<SubCategoryModel> parseSubCategoryResponse(Object object) {
        JSONArray subCategoriesArray = null;
        ArrayList<SubCategoryModel> subCategoryList = new ArrayList<>();
        try {
            JSONObject responseJsonObject = new JSONObject(object.toString());
            subCategoriesArray = responseJsonObject.getJSONArray("subcategories");
            for (int i = 0; i < subCategoriesArray.length(); i++) {
                JSONObject categoryObject = subCategoriesArray.getJSONObject(i);
                JSONObject subCategoryObject1 = categoryObject.getJSONObject("subcategory");
                int catId = subCategoryObject1.getInt("cat_id");
                int subCatId = subCategoryObject1.getInt("sub_cat_id");
                String catName = subCategoryObject1.getString("subcategory");
                SubCategoryModel subCategoryModel = new SubCategoryModel();
                subCategoryModel.setCat_id(catId);
                subCategoryModel.setSub_cat_id(subCatId);
                subCategoryModel.setSubcategory(catName);
                subCategoryList.add(subCategoryModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return subCategoryList;
    }

    /**
     * Method for parse login response.
     * @param object
     * @return
     */
    public static ArrayList<UserModel> parseUserResponse(Object object) {
        JSONArray usersArray = null;
        JSONObject jsonObj = null;
        UserModel model = new UserModel();
        ArrayList<UserModel> userList = new ArrayList<>();
        try {
            JSONObject responseJsonObject = new JSONObject(object.toString());
            usersArray = responseJsonObject.getJSONArray("users");

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userObject = usersArray.getJSONObject(i);
                JSONObject userObject1 = userObject.getJSONObject("user");
                String userId = userObject1.getString("user_id");
                String userType = userObject1.getString("usertype");
                String email = userObject1.getString("email");
                String username = userObject1.getString("username");
                UserModel userModel = new UserModel();
                userModel.setUser_id(userId);
                userModel.setUsertype(userType);
                userModel.setEmail(email);
                userModel.setUserName(username);
                userList.add(userModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userList;
    }

    /**
     * Method for parse login response.
     * @param object
     * @return
     */
    public static ArrayList<DocumentModel> parseDocumentResponse(Object object) {
        JSONArray documentArray = null;
        JSONObject jsonObj = null;
        DocumentModel model = new DocumentModel();
        ArrayList<DocumentModel> documentList = new ArrayList<>();
        try {
            JSONObject responseJsonObject = new JSONObject(object.toString());
            documentArray = responseJsonObject.getJSONArray("documents");

            for (int i = 0; i < documentArray.length(); i++) {
                JSONObject jsonObject = documentArray.getJSONObject(i);
                JSONObject jsonObject1 = jsonObject.getJSONObject("document");
                String doc_id = jsonObject1.getString("doc_id");
                String doc_name = jsonObject1.getString("doc_name");
                String path = jsonObject1.getString("path");
                String createdby = jsonObject1.getString("createdby");
                String Createddate = jsonObject1.getString("Createddate");
                String modifieddate = jsonObject1.getString("modifieddate");
                String description = jsonObject1.getString("description");
              /*  Gson gson = new Gson();
                DocumentModel documentModel = gson.fromJson(jsonObject.toString(), DocumentModel.class);*/
                DocumentModel documentModel = new DocumentModel();
                documentModel.setDoc_id(Integer.parseInt(doc_id));
                documentModel.setDoc_name(doc_name);
                documentModel.setPath(path);
                documentModel.setCreatedby(createdby);
                documentModel.setCreateddate(Createddate);
                documentModel.setModifieddate(modifieddate);
                documentModel.setDescription(description);
                documentList.add(documentModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return documentList;
    }
}
