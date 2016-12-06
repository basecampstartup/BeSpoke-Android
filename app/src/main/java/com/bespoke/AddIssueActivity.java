package com.bespoke;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bespoke.Model.Category;
import com.bespoke.Model.SubCategoryModel;
import com.bespoke.Model.UserModel;
import com.bespoke.adapter.CategoryListDialogAdapter;
import com.bespoke.adapter.SubCategoryListDialogAdapter;
import com.bespoke.adapter.UserListDialogAdapter;
import com.bespoke.callback.APIRequestCallback;
import com.bespoke.commons.Commons;
import com.bespoke.network.CheckNetwork;
import com.bespoke.servercommunication.APIUtils;
import com.bespoke.servercommunication.CommunicatorNew;
import com.bespoke.servercommunication.ResponseParser;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class AddIssueActivity extends AppCompatActivity implements APIRequestCallback, View.OnClickListener{
    private Toolbar mToolbar;
    private Context mContext;
    String[] arrIssueTypeLabel = {"Open", "Inprogress", "Closed"};
    String[] arrIssueTypeValue = {"1", "2", "3"};
    private ProgressDialog loader = null;
    ArrayList<Category> categoryList;
    ArrayList<SubCategoryModel> subCategoryList;
    ArrayList<SubCategoryModel> activeSubCategoryList;
    ArrayList<UserModel> userList;
    EditText edtShortDescription,edtDescription,edtUserName,edtIssueOpenDate,edtAssignedTo;
    TextView tvSelectCategory,tvSelectCategoryLbl,tvSelectAffectedArea,tvSelectAffectedAreaLbl,tvTicketStatusLbl,tvTicketStatusValue;
    TextView tvAssignedTo,tvAssignedToLbl;
    Button btnSubmitTicket,btnEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_issue);
        mContext=this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.AddIssue));
        initializeComponents();
        loader = new ProgressDialog(this);
        loader.setMessage(getString(R.string.MessagePleaseWait));
        if (CheckNetwork.isInternetAvailable(mContext)) {
            loader.show();
            //Call API Request after check internet connection
            new CommunicatorNew(mContext, Request.Method.GET, APIUtils.METHOD_GET_ALL_CATEGORY, new HashMap<String,String>());
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.MessageNoInternetConnection), Toast.LENGTH_LONG).show();
        }


    }

    /**
     * Initialize the UI components.
     */
    public void initializeComponents() {
        tvSelectCategory = (TextView) findViewById(R.id.tvSelectCategory);
        tvSelectCategoryLbl = (TextView) findViewById(R.id.tvSelectCategoryLbl);
        tvSelectAffectedArea = (TextView) findViewById(R.id.tvSelectAffectedArea);
        tvSelectAffectedAreaLbl = (TextView) findViewById(R.id.tvSelectAffectedAreaLbl);
        tvTicketStatusLbl = (TextView) findViewById(R.id.tvTicketStatusLbl);
        tvTicketStatusValue = (TextView) findViewById(R.id.tvTicketStatusValue);
        tvAssignedTo = (TextView) findViewById(R.id.tvAssignedTo);
        tvAssignedToLbl = (TextView) findViewById(R.id.tvAssignedToLbl);
        tvSelectCategory.setOnClickListener(this);
        tvSelectCategoryLbl.setOnClickListener(this);
        tvSelectAffectedArea.setOnClickListener(this);
        tvSelectAffectedAreaLbl.setOnClickListener(this);
        tvTicketStatusLbl.setOnClickListener(this);
        tvTicketStatusValue.setOnClickListener(this);
        tvAssignedTo.setOnClickListener(this);
        tvAssignedToLbl.setOnClickListener(this);

        edtShortDescription = (EditText) findViewById(R.id.edtShortDescription);
        edtDescription = (EditText) findViewById(R.id.edtShortDescription);
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtIssueOpenDate = (EditText) findViewById(R.id.edtIssueOpenDate);

        btnSubmitTicket=(Button)findViewById(R.id.btnSubmitTicket);
        btnEmail=(Button)findViewById(R.id.btnEmail);

        btnSubmitTicket.setOnClickListener(this);
        btnEmail.setOnClickListener(this);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSuccess(String name, Object object) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loader != null) {
                    loader.dismiss();
                }
            }
        });
        if (APIUtils.METHOD_GET_ALL_CATEGORY.equalsIgnoreCase(name)) {
            try {
                final JSONObject responseObject = new JSONObject(object.toString());
                String error = responseObject.optString("error");
                if (TextUtils.isEmpty(error)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            categoryList = ResponseParser.parseCategoryResponse(responseObject);
                            new CommunicatorNew(mContext, Request.Method.GET, APIUtils.METHOD_GET_ALL_SUB_CATEGORY, new HashMap<String,String>());

                        }
                    });
                }
                else {
                    // In case of error occured.
                    final String message = responseObject.optString("message");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, ""+message, Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        if (APIUtils.METHOD_GET_ALL_SUB_CATEGORY.equalsIgnoreCase(name)) {
            try {
                final JSONObject responseObject = new JSONObject(object.toString());
                String error = responseObject.optString("error");
                if (TextUtils.isEmpty(error)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            subCategoryList = ResponseParser.parseSubCategoryResponse(responseObject);
                            HashMap<String,String> map=new HashMap<String,String>();
                            map.put(APIUtils.PARAM_USER_TYPE,String.valueOf(Commons.TWO));
                            new CommunicatorNew(mContext, Request.Method.GET, APIUtils.METHOD_GET_USER_BY_TYPE,map);
                        }
                    });
                }
                else {
                    // In case of error occured.
                    final String message = responseObject.optString("message");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, ""+message, Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (APIUtils.METHOD_GET_USER_BY_TYPE.equalsIgnoreCase(name)) {
            try {
                final JSONObject responseObject = new JSONObject(object.toString());
                String error = responseObject.optString("error");
                if (TextUtils.isEmpty(error)) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            userList= ResponseParser.parseUserResponse(responseObject);
                        }
                    });


                } else {
                    // error msg here
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String message = responseObject.optString("message");
                            Toast.makeText(mContext, ""+message, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onFailure(String name, Object object) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loader != null) {
                    loader.dismiss();
                }
            }
        });
    }

    //getSubCategoryList by category ID
    public ArrayList<SubCategoryModel> getSubCategoryListByCategory(int categoryId)
    {
        ArrayList<SubCategoryModel> subCategoryModelList=new ArrayList<>();
        for(int i=0;i<subCategoryList.size();i++)
        {
            if(String.valueOf(subCategoryList.get(i).getCat_id()).equalsIgnoreCase(String.valueOf(categoryId)))
            {
                subCategoryModelList.add(subCategoryList.get(i));
            }
        }
        return subCategoryModelList;
    }


    int selectedCategoryId;
    int selectedCategory;
    public void showCategoryDialog() {

        final Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.list_dialog_layout, null);
        ListView lv = (ListView) view.findViewById(R.id.lstCategory);

        CategoryListDialogAdapter applianceListDialogAdapter = new CategoryListDialogAdapter(AddIssueActivity.this, categoryList);
        lv.setAdapter(applianceListDialogAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Category  selectedCategory = categoryList.get(position);
                selectedCategoryId=selectedCategory.getCat_id();
                //    Toast.makeText(AddScheduleTaskActivity.this, "Item :" + selectedAppliance, Toast.LENGTH_SHORT).show();
                tvSelectCategory.setText(selectedCategory.getCategory());
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnEmail:
                break;
            case R.id.btnSubmitTicket:
                break;
            case R.id.tvCategory:
                showCategoryDialog();
                break;
            case R.id.tvCategoryLbl:
                showSubCategoryDialog();
                break;
            case R.id.tvSelectSubCategory:
                showSubCategoryDialog();
                break;
            case R.id.tvSelectCategoryLbl:
                showSubCategoryDialog();
                break;
            case R.id.tvTicketStatusLbl:
                showTicketStatusDialog();
                break;
            case R.id.tvTicketStatusValue:
                showTicketStatusDialog();
                break;
            case R.id.tvAssignedToLbl:
                break;
            case R.id.tvAssignedTo:
                break;

            default:
        }
    }

    int selectedSubCategoryId;
    public void showSubCategoryDialog() {
        activeSubCategoryList=getSubCategoryListByCategory(selectedCategoryId);
        final Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.list_dialog_layout, null);
        ListView lv = (ListView) view.findViewById(R.id.lstCategory);
        SubCategoryListDialogAdapter applianceListDialogAdapter = new SubCategoryListDialogAdapter(AddIssueActivity.this, activeSubCategoryList);
        lv.setAdapter(applianceListDialogAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubCategoryModel  selectedSubCategory = subCategoryList.get(position);
                selectedSubCategoryId=selectedSubCategory.getSub_cat_id();
                tvSelectCategory.setText(selectedSubCategory.getSubcategory());
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    String selectedStatusId;
    public void showTicketStatusDialog() {

        final Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.list_dialog_layout, null);
        ListView lv = (ListView) view.findViewById(R.id.lstCategory);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, getTicketStatusLabelList());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String status=getTicketStatusLabelList().get(position);
                tvTicketStatusValue.setText(status);
                selectedStatusId=getTicketStatusIdList().get(position);
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    String selectedUserId;
    public void showUserListDialog() {

        final Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.list_dialog_layout, null);
        ListView lv = (ListView) view.findViewById(R.id.lstCategory);
        UserListDialogAdapter userListDialogAdapter = new UserListDialogAdapter(AddIssueActivity.this, userList);
        lv.setAdapter(userListDialogAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String status=getTicketStatusLabelList().get(position);
                tvAssignedTo.setText(status);
                selectedUserId =userList.get(position).getUser_id();
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }
    public ArrayList<String> getTicketStatusLabelList()
    {
        ArrayList<String> list=new ArrayList<>();
        list.add("Open");
        list.add("Inprogress");
        list.add("Completed");
        return  list;
    }
    public ArrayList<String> getTicketStatusIdList()
    {
        ArrayList<String> list=new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        return  list;
    }
    public void callAPI()
    {

    }

}
