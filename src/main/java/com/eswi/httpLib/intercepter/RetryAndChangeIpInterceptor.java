package com.eswi.httpLib.intercepter;

import android.text.TextUtils;

import com.eswi.data.DBHelper;
import com.eswi.httpLib.EswiFlowableApi;
import com.example.eswilib.Utils.NetworkUtils;

import java.io.IOException;
import java.net.ConnectException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Terry on 2017/3/29.
 */

public class RetryAndChangeIpInterceptor implements Interceptor {
    int RetryCount = 5;

    @Override
    public Response intercept(Chain chain) throws IOException {
        //网络不能用的时候直接返回
        if(!NetworkUtils.isNetworkAvailable()){
            throw new ConnectException("网络连接不可用,请检查网络!");
        }
        Request request = chain.request();
        Response response = doRequest(chain, request);
        int tryCount = 0;
        String url = request.url().toString();
        //在response为空或者返回在状态码不是200且 retry的次数小于5次的时候进行IP切换
//        Logger.d(((response == null || response != null && response.code() != 200) && tryCount <= RetryCount)+"######### response != null && response.code() #############");

        while ((response == null || response != null && response.code() != 200) && tryCount <= RetryCount) {
            //只有在下载文件的地址的时候才changIP,下载地址优先使用校本地址，拉取不到资源再 转换到外网
            if (url.contains("p/main/download_file/")) {
                url = changeIP(url) + url.substring(url.indexOf("p/main"));
            }
            Request newRequest = request.newBuilder().url(url).build();
            tryCount++;
            response = doRequest(chain, newRequest);
            if (response == null) {
                throw new IOException();
            }
        }
        return response;


    }

    private Response doRequest(Chain chain, Request request) {
        Response response = null;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
        }
        return response;
    }

    private String changeIP(String url) {
        if (url.contains(EswiFlowableApi.getInstance().getBasicUrl())) {
            //数据库中存取数据不存在，任然使用getBasicUrl()
            if (DBHelper.getStudent() != null && !TextUtils.isEmpty(DBHelper.getStudent().getResourceHost())&& EswiFlowableApi.isConnect) {
                return DBHelper.getStudent().getResourceHost();
            } else {
                return EswiFlowableApi.getInstance().getBasicUrl();
            }
        } else {
            return EswiFlowableApi.getInstance().getBasicUrl();
        }
    }
}
