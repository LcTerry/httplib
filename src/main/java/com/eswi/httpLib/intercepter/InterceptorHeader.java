package com.eswi.httpLib.intercepter;

import com.eswi.data.DBHelper;
import com.example.eswilib.Utils.StringUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Terry on 2017/5/4.
 */

public class InterceptorHeader implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request=chain.request();
        Request.Builder builder=request.newBuilder();
        if(DBHelper.getStudent()!=null) {
            if(!StringUtils.isEmpty(DBHelper.getStudent().getToken())){
                builder.addHeader("ESWI-AccessToken", DBHelper.getStudent().getToken());
            }
            builder.addHeader("ESWI-UA", "html;win;asjkhfsahdf;" + DBHelper.getStudent().getStudentId() + ";aaaa");
        }
        return chain.proceed(builder.build());

    }
}
