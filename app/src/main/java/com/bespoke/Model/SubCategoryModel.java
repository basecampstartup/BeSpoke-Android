package com.bespoke.Model;

/**
 * Created by admin on 12/6/2016.
 */

public class SubCategoryModel {


    private int cat_id;
    private String subcategory;
    private int sub_cat_id;

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public int getSub_cat_id() {
        return sub_cat_id;
    }

    public void setSub_cat_id(int sub_cat_id) {
        this.sub_cat_id = sub_cat_id;
    }
}
