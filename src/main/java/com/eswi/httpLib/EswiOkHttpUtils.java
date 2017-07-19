package com.eswi.httpLib;

import com.eswi.httpLib.intercepter.InterceptorHeader;
import com.eswi.httpLib.intercepter.RetryAndChangeIpInterceptor;
import com.example.eswilib.Utils.AppUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Terry on 2017/3/30.
 * 配置OkHttp 方便整个工程进行使用
 */

public class EswiOkHttpUtils {
    static OkHttpClient.Builder builder;

    public static void init() {
        //http://192.168.0.214:6001/   http://192.168.119.12:6002/
        builder = new OkHttpClient.Builder();
        //debug时候输出日志信息
        if (AppUtils.isAppDebug()) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        builder  //添加超时
                .connectTimeout(HttpConstants.CONNECTTIMEOUT, TimeUnit.SECONDS)
                //写入超时
                .writeTimeout(HttpConstants.WRITETIMEOUT, TimeUnit.SECONDS)
                //读写超时
                .readTimeout(HttpConstants.READTIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new RetryAndChangeIpInterceptor())
                .addInterceptor(new InterceptorHeader())
                //重连  自己实现重连机制
                .retryOnConnectionFailure(false);
    }

    private final static class HolderClass {
        private final static EswiOkHttpUtils INSTANCE = new EswiOkHttpUtils();
    }
    private EswiOkHttpUtils(){}

    public static EswiOkHttpUtils getInstance() {
        return HolderClass.INSTANCE;
    }

    public  OkHttpClient.Builder getBuilder() {
        return builder;
    }
}
