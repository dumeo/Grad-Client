package com.grad.util;

public class DefaultVals {
    public static final Integer UNDER_REGISTER = 0;
    public static final Integer REGISTERING = 1;
    public static final Integer REGISTERING_SUCCESS = 2;
    public static final Integer REGISTERING_FAILED = 3;
    public static final Integer REGISTER_UNFILL = 4;
    public static final String BASE_URL = "http://192.168.1.101:8080/";

    //-------------------SharedPreferences----------------------
    public static final String USER_INFO_DATABASE = "user";
    public static Integer POST_TYPE_TEXT = 0;
    public static Integer POST_TYPE_IMG = 1;

    public static final int FETCH_DATA_COMPLETED = 1;
    public static final int REFETCH_DATA_COMPLETED = 2;

    public static final int ADD_POST_EDITING = 1;
    public static final int ADD_POST_EDIT_COMPLETED = 2;
    public static final int ADD_POST_POSTING = 3;
    public static final int ADD_POST_SUCCESS = 4;
    public static final int ADD_POST_FAILED =  5;
}
