package com.eswi.httpLib;

import com.eswi.data.mapper.CurriculumInfo;
import com.eswi.data.mapper.NewsInfo;
import com.eswi.data.mapper.UserInfoDTO;
import com.eswi.data.upload.CosInfo;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Terry on 2017/3/21.
 */

public interface EswiFloawableService {
    String CONTENT_TYPE = "Content-Type: application/json;charset=UTF-8";

    @Headers({CONTENT_TYPE})
    @POST("student/login")
    Flowable<UserInfoDTO> login(@Body RequestBody code);








    @POST("file/cos/signature")
    Flowable<CosInfo> getCosSignature(
            @Body Map<String, Object> params);







    @POST("curriculum/current")
    Flowable<CurriculumInfo> getCurriculum();
    @POST("homework/unfinish/count")
    Flowable<NewsInfo> getHomeworkNews();
    @POST("wrong/exercises/new")
    Flowable<NewsInfo> getErrorCollectionNews();
}
