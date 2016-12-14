package com.bespoke.servercommunication;

/**
 * Created by admin on 11/15/2016.
 */
public class APIUtils {
    //live url
    public static final String BASE_URL ="http://bespoke.rupeshpatil.com/BeSpokeAPI.php?";
    //public static final String BASE_URL ="http://10.0.12.10/bespoke/BeSpokeAPI.php?";
    public static final String METHOD_NAME = "method";

    public static final String METHOD_TYPE_POST = "post";
    public static final String METHOD_TYPE_GET = "get";

    public static final String METHOD_REGISTER_USER = "RegisterUser";
    public static final String METHOD_GET_USER = "GetUser";
    public static final String METHOD_GET_ALL_USER = "AllUser";
    public static final String METHOD_FORGOT_PASSWORD = "ForgotPassword";
    public static final String METHOD_LOGIN = "Login";
    public static final String METHOD_GET_USER_BY_TYPE = "GetUserByUserType";
    public static final String METHOD_GET_ALL_CATEGORY = "GetAllCategory";
    public static final String METHOD_GET_ALL_ISSUES = "GetAllIssue";
    public static final String METHOD_GET_ALL_SUB_CATEGORY = "GetAllSubCategory";
    public static final String METHOD_CREATE_TICKET = "CreateTicket";
    public static final String METHOD_UPDATE_TICKET_STATUS = "UpdateTicketStatus";
    public static final String METHOD_CREATE_CATEGORY = "CreateCategory";
    public static final String METHOD_CREATE_SUB_CATEGORY = "CreateSubCategory";
    public static final String METHOD_UPDATE_TICKET = "UpdateTicket";
    public static final String METHOD_GET_ALL_REQUEST = "GetAllRequest";


    public static final String METHOD_UPDATE_DEVICEID = "UpdateDeviceId";

    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_PASSWORD = "pswd";
    public static final String PARAM_NEW_PASSWORD = "newpswd";
    public static final String PARAM_USER_TYPE = "usertype";
    public static final String PARAM_USER_NAME = "UserName";
    public static final String PARAM_USER_ID = "user_id";

    public static final String USER_NAME_SMALL = "username";
    //usertype 1,2,3 (for admin, super admin, normal user)
    public static final String TICKET_STATUS = "ticketstatus";
    public static final String TICKET_ID = "ticket_id";
    public static final String TICKET_TYPE = "tickettype";
    public static final String CATEGORY_NAME = "category";
    public static final String SUB_CATEGORY_NAME = "subcategory";
    public static final String CATEGORY_ID = "cat_id";
    public static final String DEVICE_ID = "deviceid";
    public static final String DEVICE_TYPE = "devicetype";



}
