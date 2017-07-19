package com.eswi.httpLib;


import com.example.eswilib.Utils.NetworkUtils;

import io.reactivex.subscribers.ResourceSubscriber;


/**
 * Created by admin on 2017/2/8.
 * 自定义Subscriber，在网络请求的时候弹出一个Dialog，实现在数据返回之后取消Subscriber的订阅  rxjava2.0以下用Subscriber 以上用ResourceSubscriber
 *ResourceSubscriber：允许异步取消其订阅相关资源，节省内存而且是线程安全。
 *DisposableSubscriber：通过实现Desposable异步删除。此实现调用dispose()是无效的;
 *
 */

public abstract class NoProgressSubscriber<T> extends ResourceSubscriber<T> {
    @Override
    public void onComplete() {
        if (!this.isDisposed()) {
            this.dispose();
        }
    }
    @Override
    public void onError(Throwable e) {
        if (!this.isDisposed()) {
            this.dispose();
        }
        if (!NetworkUtils.isNetworkAvailable()) {
            _onError("网络连接不可用,请检查网络!");
        } else {
            if(e!=null&&e.getMessage()!=null) {
                _onError(e.getMessage().toString());
            }else {
                _onError("请求失败，请稍后再试");
            }
        }
    }


    protected abstract void _onError(String message);
//
}
