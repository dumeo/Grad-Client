package com.grad.util;

public class DefaultVals {
    public static final Integer UNDER_REGISTER = 0;
    public static final Integer REGISTERING = 1;
    public static final Integer REGISTERING_SUCCESS = 2;
    public static final Integer REGISTERING_FAILED = 3;
    public static final Integer REGISTER_UNFILL = 4;
    public static final String BASE_URL = "http://192.168.1.100:8080/";

    //-------------------SharedPreferences----------------------
    public static final String USER_INFO_DATABASE = "user";
    public static Integer POST_TYPE_TEXT = 0;
    public static Integer POST_TYPE_IMG = 1;

    public static final int FETCH_DATA_COMPLETED = 10;
    public static final int FETCH_DATA_FAILED= 11;
    public static final int REFETCH_DATA_COMPLETED = 12;
    public static final int LOAD_MORE_DATA_COMPLETED = 13;

    public static final int ADD_POST_TEXT_SUCCESS =  23;
    public static final int ADD_POST_SUCCESS =  24;
    public static final int ADD_IMG_SUCCESS =  25;
    public static final int ADD_IMG_FAILED =  26;
}
