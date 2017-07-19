package com.eswi.httpLib.intercepter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

/**
 * Created by Terry on 2017/3/29.
 * Glide 全部配置使用OkHttp作为下载引擎
 * 在主工程清单文件中加入：
   <meta-data
 android:name="包名.OkHttpGlideModule"
 android:value="GlideModule" />
 *
 *
 */

public class OkHttpGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Do nothing.

    }

    @Override
    public void registerComponents(Context context, Glide glide) {


        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }
}
