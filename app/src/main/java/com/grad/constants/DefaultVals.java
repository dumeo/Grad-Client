package com.grad.constants;

public class DefaultVals {
    public static final int HTTP_CODE_OK = 200;
    public static final int HTTP_CODE_SERVER_ERROR = 500;
    public static final int HTTP_CODE_CLIENT_ERROR = 400;
    public static final Integer UNDER_REGISTER = 0;
    public static final Integer REGISTERING = 1;
    public static final Integer REGISTERING_SUCCESS = 2;
    public static final Integer REGISTERING_FAILED = 3;
    public static final Integer REGISTER_UNFILL = 4;
    public static final String BASE_URL = "http://192.168.1.106:8080/";

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

    public static final int GET_POST_SUCCESS = 31;
    public static final int GET_POST_FAILED = 32;
    public static final int ADD_COMMENT_FAILED = 33;
    public static final int ADD_COMMENT_SUCCESS = 34;
    public static final int LOAD_COMMENTS_FAILED = 35;
    public static final int LOAD_COMMENTS_SUCCESS = 36;
    public static final int GET_COMMENTCNT_SUCCESS = 37;
    public static final int GET_COMMENTCNT_FAILED = 38;
    public static final int REQUEST_ADD_COMMENT = 39;

    public static final int RELOAD_COMMENTS_SUCCESS = 40;
    public static final int RELOAD_COMMENTS_FAILED = 41;
    public static final int SET_LIKE_STATUS_SUCCESS = 42;
    public static final int SET_LIKE_STATUS_FAILED = 43;
    public static final int LIKE_COMMENT_SUCCESS = 44;
    public static final int LIKE_COMMENT_FAILED = 45;
    public static final String STATUS_OK = "ok";
    public static final String STATUS_FAILED = "failed";

    public static final int CHECK_LIKE_STATUS_SUCCESS = 56;
    public static final int CHECK_LIKE_STATUS_FAILED = 57;
    public static final int LIKE_STATUS_DISLIKED = 0;
    public static final int LIKE_STATUS_LIKED = 1;
    public static final int LIKE_STATUS_NOSTATUS = 2;

    public static final int LIKED_TO_DISLIKE = 50;
    public static final int DISLIKED_TO_LIKE = 51;
    public static final int LIKED_TO_NOSTATUS = 52;
    public static final int DISLIKED_TO_NOSTATUS = 53;
    public static final int NOSTATUS_TO_DISLIKE = 54;
    public static final int NOSTATUS_TO_LIKE = 55;

    public static final int ADD_COLLECT_SUCCESS = 60;
    public static final int ADD_COLLECT_FAILED = 61;
    public static final int COLLECT_POST = 62;
    public static final int UNCOLLECT_POST = 63;

    public static final int CHECK_USER_SUCCESS = 67;
    public static final String MSG_USER_NOT_EXISTS = "User not exists";
    public static final String MSG_USER_EXISTS = "User exists";
    public static final String MSG_SERVER_ERROR = "Server error";


}
