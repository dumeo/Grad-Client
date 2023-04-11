package com.grad.util;

import com.grad.constants.DefaultVals;

public class HttpUtil {
    public static int checkCode(int httpCode){
        if(httpCode >= 400 && httpCode < 500) return DefaultVals.HTTP_CODE_CLIENT_ERROR;
        else if(httpCode >= 500 && httpCode < 600) return DefaultVals.HTTP_CODE_SERVER_ERROR;
        return DefaultVals.HTTP_CODE_OK;
    }
}
