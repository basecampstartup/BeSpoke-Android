package com.bespoke.servercommunication;

/**
 * Created by admin on 11/15/2016.
 */
public class APIUtils {
    public static final String BASE_URL ="http://bespoke.rupeshpatil.com/BeSpokeAPI.php?";

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


    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_PASSWORD = "pswd";
    public static final String PARAM_NEW_PASSWORD = "newpswd";
    public static final String PARAM_USER_TYPE = "usertype";
    public static final String PARAM_USER_NAME = "UserName";
    public static final String PARAM_USER_ID = "user_id";


    //usertype 1,2,3 (for admin, super admin, normal user)
}
