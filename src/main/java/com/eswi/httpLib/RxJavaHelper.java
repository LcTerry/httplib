package com.eswi.httpLib;


import android.text.TextUtils;

import com.eswi.data.BaseResponseData;
import com.orhanobut.logger.Logger;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.internal.operators.flowable.FlowableError;
import io.reactivex.internal.operators.observable.ObservableError;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by admin on 2017/2/8.
 */

public class RxJavaHelper {

    private RxJavaHelper() {

    }

    public static RxJavaHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    //提前过滤好数据
    public  <T extends BaseResponseData> ObservableTransformer<T, T> handleResult() {
        return upstream -> {
            return upstream.flatMap(new Function<T, ObservableSource<T>>() {
                @Override
                public ObservableSource<T> apply(T t) throws Exception {
                    //这里过滤出返回正确的数据
                    Logger.d(t + "########tttttt####");
                    if (t.getCode() == 0) {
                        Logger.d(t.getCode() + "########t.getErrorCode()####");
                        return new Observable<T>() {
                            @Override
                            protected void subscribeActual(Observer<? super T> observer) {
                                try {
                                    Logger.d(t + "########t.getErrorCode()##tttttttttttttttttttttttttt##");

                                    observer.onNext(t);
                                    observer.onComplete();
                                } catch (Exception e) {
                                    observer.onError(e);
                                }

                            }
                        };
                    }
                    return ObservableError.error(new Throwable());
                }
            });
        };
    }

    public  <R extends BaseResponseData> FlowableTransformer<R, R> flowableHandleResult() {
        return upstream -> {
            return upstream.flatMap(new Function<R, Publisher<R>>() {
                @Override
                public Publisher<R> apply(R r) throws Exception {
                    if (r.getCode() == 0) {
                        return new Flowable<R>() {
                            @Override
                            protected void subscribeActual(Subscriber<? super R> s) {
                                try {

                                    s.onNext(r);
                                    s.onComplete();
                                } catch (Exception e) {
                                    s.onError(e);
                                }
                            }
                        };
                    } else {
                        String msg = null;
                        if(r!=null&&!TextUtils.isEmpty(r.getMsg())){
                            msg=r.getMsg();
                        }
                        return FlowableError.error(new Throwable(msg));
                    }
                }
            });
        };
    }

    /**
     * Transformer 把一个Observable整体转为另外一个Observable
     * 因此在这里放置Observable公共部分！这样避免破坏了rxjava响应式编程风格！
     *
     * @param <T>
     * @return
     */
    public <T> ObservableTransformer<T, T> getCommonObservable() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        //观察者执行的在主线程
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public <R> FlowableTransformer<R, R> getCommonFlowable() {
        return new FlowableTransformer<R, R>() {
            @Override
            public Publisher<R> apply(Flowable<R> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    private static class SingletonHolder {
        static final RxJavaHelper INSTANCE = new RxJavaHelper();
    }

}
