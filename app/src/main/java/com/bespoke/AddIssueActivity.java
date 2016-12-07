package com.bespoke;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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
import com.bespoke.utils.DateStyleEnum;
import com.bespoke.utils.Utils;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class AddIssueActivity extends AppCompatActivity implements APIRequestCallback, View.OnClickListener, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    private Toolbar mToolbar;
    private Context mContext;
    String[] arrIssueTypeLabel = {"Open", "Inprogress", "Closed"};
    String[] arrIssueTypeValue = {"1", "2", "3"};
    private ProgressDialog loader = null;
    ArrayList<Category> categoryList;
    ArrayList<SubCategoryModel> subCategoryList;
    ArrayList<SubCategoryModel> activeSubCategoryList;
    ArrayList<UserModel> userList;
    EditText edtShortDescription, edtDescription, edtUserName, edtAssignedTo;
    TextView tvSelectCategory, tvSelectCategoryLbl, tvSelectAffectedArea, tvSelectAffectedAreaLbl, tvTicketStatusLbl, tvTicketStatusValue;
    TextView tvAssignedTo, tvAssignedToLbl, tvSelectIssueOpenDateLbl, tvSelectIssueOpenDateVal;
    Button btnSubmitTicket, btnEmail;
    private int d = 0, mon = 0, y = 0;
    private static String DISPLAY_DATE_FORMAT = "MMM d, yyyy";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_issue);
        mContext = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.AddIssue));
        initializeComponents();
        setCurrentDataTimeFields();
        loader = new ProgressDialog(this);
        loader.setMessage(getString(R.string.MessagePleaseWait));
        if (CheckNetwork.isInternetAvailable(mContext)) {
            loader.show();
            //Call API Request after check internet connection
            new CommunicatorNew(mContext, Request.Method.GET, APIUtils.METHOD_GET_ALL_CATEGORY, new HashMap<String, String>());
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
        tvSelectIssueOpenDateLbl = (TextView) findViewById(R.id.tvSelectIssueOpenDateLbl);
        tvSelectIssueOpenDateVal = (TextView) findViewById(R.id.tvIssueOpenDate);
        tvSelectCategory.setOnClickListener(this);
        tvSelectCategoryLbl.setOnClickListener(this);
        tvSelectAffectedArea.setOnClickListener(this);
        tvSelectAffectedAreaLbl.setOnClickListener(this);
        tvTicketStatusLbl.setOnClickListener(this);
        tvTicketStatusValue.setOnClickListener(this);
        tvAssignedTo.setOnClickListener(this);
        tvAssignedToLbl.setOnClickListener(this);
        tvSelectIssueOpenDateLbl.setOnClickListener(this);
        tvSelectIssueOpenDateVal.setOnClickListener(this);
        edtShortDescription = (EditText) findViewById(R.id.edtShortDescription);
        edtDescription = (EditText) findViewById(R.id.edtShortDescription);
        edtUserName = (EditText) findViewById(R.id.edtUserName);

        btnSubmitTicket = (Button) findViewById(R.id.btnSubmitTicket);
        btnEmail = (Button) findViewById(R.id.btnEmail);

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
                            new CommunicatorNew(mContext, Request.Method.GET, APIUtils.METHOD_GET_ALL_SUB_CATEGORY, new HashMap<String, String>());

                        }
                    });
                } else {
                    // In case of error occured.
                    final String message = responseObject.optString("message");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
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
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(APIUtils.PARAM_USER_TYPE, String.valueOf(Commons.TWO));
                            new CommunicatorNew(mContext, Request.Method.GET, APIUtils.METHOD_GET_USER_BY_TYPE, map);
                        }
                    });
                } else {
                    // In case of error occured.
                    final String message = responseObject.optString("message");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
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
                            userList = ResponseParser.parseUserResponse(responseObject);
                        }
                    });


                } else {
                    // error msg here
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String message = responseObject.optString("message");
                            Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
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
    public ArrayList<SubCategoryModel> getSubCategoryListByCategory(int categoryId) {
        ArrayList<SubCategoryModel> subCategoryModelList = new ArrayList<>();
        for (int i = 0; i < subCategoryList.size(); i++) {
            if (String.valueOf(subCategoryList.get(i).getCat_id()).equalsIgnoreCase(String.valueOf(categoryId))) {
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
                final Category selectedCategory = categoryList.get(position);
                selectedCategoryId = selectedCategory.getCat_id();
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
                //  Toast.makeText(mContext,"btnEmail click",Toast.LENGTH_LONG).show();
                break;
            case R.id.btnSubmitTicket:
            //    Toast.makeText(mContext, "btnSubmitTicket click", Toast.LENGTH_LONG).show();
                break;
            case R.id.tvSelectCategory:
                //  Toast.makeText(mContext,"tvCategory click",Toast.LENGTH_LONG).show();
                showCategoryDialog();
                break;
            case R.id.tvSelectCategoryLbl:
                // Toast.makeText(mContext,"tvSelectCategoryLbl click",Toast.LENGTH_LONG).show();
                showCategoryDialog();
                break;
            case R.id.tvSelectAffectedAreaLbl:
                //  Toast.makeText(mContext,"tvCategoryLbl click",Toast.LENGTH_LONG).show();
                showSubCategoryDialog();
                break;
            case R.id.tvSelectAffectedArea:
                //  Toast.makeText(mContext,"tvSelectSubCategory click",Toast.LENGTH_LONG).show();
                showSubCategoryDialog();
                break;

            case R.id.tvTicketStatusLbl:
                //  Toast.makeText(mContext,"tvTicketStatusLbl click",Toast.LENGTH_LONG).show();
                showTicketStatusDialog();
                break;
            case R.id.tvTicketStatusValue:
                //  Toast.makeText(mContext,"tvTicketStatusValue click",Toast.LENGTH_LONG).show();
                showTicketStatusDialog();
                break;
            case R.id.tvAssignedToLbl:
                showUserListDialog();
                // Toast.makeText(mContext,"tvAssignedToLbl click",Toast.LENGTH_LONG).show();
                break;
            case R.id.tvAssignedTo:
                showUserListDialog();
                // Toast.makeText(mContext,"tvAssignedTo click",Toast.LENGTH_LONG).show();
                break;
            case R.id.tvSelectIssueOpenDateLbl:
                openDatePicker();
                break;
            case R.id.tvIssueOpenDate:
                openDatePicker();
                break;

            default:
        }
    }

    int selectedSubCategoryId;

    public void showSubCategoryDialog() {
        activeSubCategoryList = getSubCategoryListByCategory(selectedCategoryId);
        if (activeSubCategoryList.size() <= 0)
            Toast.makeText(mContext, "No Subcategory available", Toast.LENGTH_LONG).show();
        final Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.list_dialog_layout, null);
        ListView lv = (ListView) view.findViewById(R.id.lstCategory);
        SubCategoryListDialogAdapter applianceListDialogAdapter = new SubCategoryListDialogAdapter(AddIssueActivity.this, activeSubCategoryList);
        lv.setAdapter(applianceListDialogAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubCategoryModel selectedSubCategory = subCategoryList.get(position);
                selectedSubCategoryId = selectedSubCategory.getSub_cat_id();
                tvSelectAffectedArea.setText(selectedSubCategory.getSubcategory());
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    String selectedTicketStatusId;

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
                String status = getTicketStatusLabelList().get(position);
                tvTicketStatusValue.setText(status);
                selectedTicketStatusId = getTicketStatusIdList().get(position);
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
                UserModel user = userList.get(position);
                tvAssignedTo.setText(user.getUserName());
                selectedUserId = userList.get(position).getUser_id();
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    public ArrayList<String> getTicketStatusLabelList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Open");
        list.add("Inprogress");
        list.add("Completed");
        return list;
    }

    public ArrayList<String> getTicketStatusIdList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        return list;
    }

    public void callAPI() {

    }

    /**
     * This method validate all the required fields.
     *
     * @return
     */
    public boolean validate() {
        boolean valid = true;
        String shortDescription = edtShortDescription.getText().toString();
        String description = edtDescription.getText().toString();
        String strSelectedAffectedArea = tvSelectAffectedArea.getText().toString();
        String strTicketStatus = tvTicketStatusValue.getText().toString();
        String strAssignedTo = tvAssignedTo.getText().toString();


        if (shortDescription.isEmpty()) {
            edtShortDescription.setError(getString(R.string.EnterShortDescription));
            valid = false;
        } else {
            edtShortDescription.setError(null);
        }
        if (description.isEmpty()) {
            edtDescription.setError(getString(R.string.EnterDescription));
            valid = false;
        } else {
            edtDescription.setError(null);
        }
        return valid;
    }

    public boolean validateDropDowns()
    {
        String strSelectedAffectedArea = tvSelectAffectedArea.getText().toString();
        String strTicketStatus = tvTicketStatusValue.getText().toString();
        String strAssignedTo = tvAssignedTo.getText().toString();
        if(strSelectedAffectedArea.equalsIgnoreCase(getResources().getString(R.string.AffectedArea)))
        {

        }
        return  true;
    }

    /**
     * method to open date picker
     */
    private void openDatePicker() {
        // open date picker for start date
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(AddIssueActivity.this, y, mon, d);
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }


    /**
     * To set current date and time in date-time text fields
     */
    private void setCurrentDataTimeFields() {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getDefault());
        y = now.get(Calendar.YEAR);
        mon = now.get(Calendar.MONTH);
        d = now.get(Calendar.DAY_OF_MONTH);
        Log.v("Calendar11", "At setCurrentDataTimeFields: Time Value Start:" + y + ":" + mon + ":" + d);
        Date startDate = getDateByTime(y, mon, d);
        tvSelectIssueOpenDateVal.setText(Utils.formatDate(this, startDate, DateStyleEnum.StyleType.MEDIUM));
        tvSelectIssueOpenDateVal.setTag(getCustomDateFormat(startDate));
    }

    /**
     * To get the date as per all the parameters
     *
     * @param y   : year
     * @param mon :month
     * @param d   :day
     * @return Date
     */
    private Date getDateByTime(int y, int mon, int d) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, y);
        calendar.set(Calendar.MONTH, mon);
        calendar.set(Calendar.DAY_OF_MONTH, d);
        return calendar.getTime();
    }

    /**
     * method to return formatted date with US locale
     *
     * @param mDate
     * @return
     */
    private String getCustomDateFormat(Date mDate) {
        DateFormat fmt = new SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.US);
        return fmt.format(mDate);
    }
    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
        try {
            SimpleDateFormat mdyFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            Date dateObj = mdyFormat.parse(date);
            tvSelectIssueOpenDateVal.setText(Utils.formatDate(this, dateObj, DateStyleEnum.StyleType.MEDIUM));
         //   previousStartDate = textStartDate.getTag().toString() + " " + textStartTime.getTag().toString();

                setDatePickerDate(dayOfMonth, monthOfYear, year);


        } catch (Exception e) {

        }

    }

    @Override
    public void onDateCancel() {

    }
    /**
     * To set Date in Picker which was selected
     */
    private void setDatePickerDate( int day, int month, int year) {
            d = day;
            mon = month;
            y = year;
    }



}


