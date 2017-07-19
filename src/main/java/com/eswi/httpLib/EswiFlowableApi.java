package com.eswi.httpLib;

import com.alibaba.fastjson.JSON;
import com.eswi.data.DBHelper;
import com.eswi.data.mapper.CurriculumInfo;
import com.eswi.data.mapper.NewsInfo;
import com.eswi.data.mapper.UserInfoDTO;
import com.eswi.data.upload.CosInfo;
import com.example.eswilib.Utils.DeviceUtils;
import com.example.eswilib.Utils.MD5Utils;
import com.example.eswilib.Utils.SPUtils;
import com.example.eswilib.Utils.StringUtils;
import com.example.eswilib.constant.LibConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.eswi.httpLib.HttpConstants.BASIC_URL_SHOT;

/**
 * Created by Terry on 2017/3/21.
 */

public class EswiFlowableApi {
    private EswiFloawableService mEswiFloawableService;
    static String USER = "user";
    static String TOKEN = "token";
    static String TIME = "time";
    static String ESWI = "eswi";
    static EswiFlowableApi eswiApi;
    public static final int K_学科_语文 = 1;
    public static final int K_学科_数学 = 2;
    public static final int K_学科_英语 = 3;
    public static final int K_学科_物理 = 4;
    public static final int K_学科_化学 = 5;
    public static final int K_学科_生物 = 6;
    public static final int K_学科_地理 = 7;
    public static final int K_学科_政治 = 8;
    public static final int K_学科_信息技术 = 9;
    public static final int K_学科_音乐 = 10;
    public static final int K_学科_品德 = 11;
    public static final int K_学科_美术 = 12;
    public static final int K_学科_历史 = 13;
    public static final int K_学科_体育 = 14;
    public static final int K_学科_科学 = 15;

    public String getBasicUrl() {
        return "http://" + BASIC_URL_SHOT + "/";
    }

    /**
     * 设置全局的判断本地服务器是否连接通畅，不用再接口里面反复判断接口返回判断是灰常耗时间的
     * 会用到这个属性的地方在：CourseActivity,UpdateVersionDialog StoreActivity 三个地方，只涉及到下载文件，其他都是走的外网！
     * 因此只在mainActivity中每15分钟去判断一次就可以
     */
    public static boolean isConnect;

    public static EswiFlowableApi getInstance() {

        if (eswiApi == null) {
            synchronized (EswiFlowableApi.class) {
                if (eswiApi == null) {
                    eswiApi = new EswiFlowableApi();
                }
            }
        }
        return eswiApi;
    }

    public EswiFlowableApi() {
//        initOkHttp();
        SPUtils.putOpenShareString(LibConstants.BASIC_URL, BASIC_URL_SHOT);
        GsonBuilder gbuilder = new GsonBuilder();
        gbuilder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()));
        gbuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        final Gson gsonFormat = gbuilder.create();


        Retrofit retrofit = new Retrofit.Builder()
                .client(EswiOkHttpUtils.getInstance().getBuilder().build())
                .baseUrl(getBasicUrl())
                .addConverterFactory(GsonConverterFactory.create(gsonFormat))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mEswiFloawableService = retrofit.create(EswiFloawableService.class);
        courseDefaultPic.put(K_学科_语文, "yuwen.jpg");
        courseDefaultPic.put(K_学科_数学, "shuxue.jpg");
        courseDefaultPic.put(K_学科_英语, "yingyu.jpg");
        courseDefaultPic.put(K_学科_物理, "wuli.jpg");
        courseDefaultPic.put(K_学科_化学, "huaxue.jpg");
        courseDefaultPic.put(K_学科_生物, "shengwu.jpg");
        courseDefaultPic.put(K_学科_地理, "dili.jpg");
        courseDefaultPic.put(K_学科_政治, "zhengzhi.jpg");
        courseDefaultPic.put(K_学科_信息技术, "xinxijishu.jpg");
        courseDefaultPic.put(K_学科_历史, "lishi.jpg");
    }


    public void ResetIp() {
        SPUtils.putOpenShareString(LibConstants.BASIC_URL, BASIC_URL_SHOT);
        mEswiFloawableService = null;
        GsonBuilder gbuilder = new GsonBuilder();
        gbuilder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()));
        gbuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        final Gson gsonFormat = gbuilder.create();
        Retrofit retrofit = new Retrofit.Builder()
                .client(EswiOkHttpUtils.getInstance().getBuilder().build())
                .baseUrl(getBasicUrl())
                .addConverterFactory(GsonConverterFactory.create(gsonFormat))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mEswiFloawableService = retrofit.create(EswiFloawableService.class);
    }



    public Flowable<CosInfo> getCosSignature() {
        HashMap<String, Object> params = new HashMap<>();
        return mEswiFloawableService.getCosSignature(params);
    }




    public String getDownloadUrl(String id) {
        //当分配给学生的地址不为空，且可达的时候才去连接，否则就连基础的
        if (!StringUtils.isEmpty(DBHelper.getStudent().getResourceHost())) {
            return DBHelper.getStudent().getResourceHost() + "p/main/download_file/" + id;
        }
        return getBasicUrl() + "p/main/download_file/" + id;
    }

    public String getBasicUrl1() {
        return "http://" + BASIC_URL_SHOT;
    }

    public String getCourseCoverPicUrl(int id) {

        return getBasicUrl() + "image/course/" + courseDefaultPic.get(id);
    }

    public static LinkedHashMap<Integer, String> courseDefaultPic = new LinkedHashMap<>();

    /**
     * 上传图片获得MultipartBody
     *
     * @param path
     * @return
     */
    private MultipartBody.Part getMultipartBody(String path) {
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        }
        return MultipartBody.Part.createFormData("file", f.getName(), getMultRequetBody(f));
    }

    /**
     * 上传图片，添加表单属性
     *
     * @param file
     * @return
     */
    private RequestBody getMultRequetBody(File file) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), file);
    }

    /**
     * post上传内容
     *
     * @param bean
     * @return
     */
    private RequestBody getJsonRequestBody(BaseParamsBean bean) {
        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(bean));
    }

    private RequestBody getJsonRequestBody(Map bean) {
        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(bean));
    }

    public Flowable<UserInfoDTO> login(String userName, String passWord) {
        HashMap<String, String> params = getHttpRequestMap(userName, passWord);
        return mEswiFloawableService.login(getJsonRequestBody(params));
    }

    public Flowable<CurriculumInfo> getCurriculum() {
        return mEswiFloawableService.getCurriculum();
    }

    public Flowable<NewsInfo> getHomeworkNews() {
        return mEswiFloawableService.getHomeworkNews();
    }

    public Flowable<NewsInfo> getErrorCollectionNews() {
        return mEswiFloawableService.getErrorCollectionNews();
    }

    /**
     * base params need username and pwd
     *
     * @param
     * @return
     */
    public HashMap<String, String> getHttpRequestMap(String username, String pwd) {
        HashMap<String, String> map = new HashMap<String, String>();
        long time = System.currentTimeMillis();
        String token = username + MD5Utils.md5(pwd)
                .toUpperCase() + time + ESWI;
        token = MD5Utils.md5(token).toUpperCase();
//        map.put(USER, username);
//        map.put(TOKEN, token);http://192.168.0.36:8080/student/login?
//        map.put(TIME, time + "");
//        map.put("device_id", DeviceUtils.getDeviceId());
//        map.put("android_id", DeviceUtils.getAndroidId());
//        map.put("mac", DeviceUtils.getMac());
//        map.put("studentId", username);
        //String studentNo;
//        String password;
//        String deviceId;
//        String androidId;
//        String macAddress;
        map.put("studentNo", username);
        map.put("password", MD5Utils.md5(pwd));
        map.put("deviceId", DeviceUtils.getDeviceId());
        map.put("androidId", DeviceUtils.getAndroidId());
        map.put("macAddress", DeviceUtils.getMac());
        return map;
    }

    /**
     * have login get the default data
     *
     * @param
     * @return
     */
    public HashMap<String, String> getHttpRequestMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        String user = "";
        String pass = "";
        if (DBHelper.getUser() != null) {
            user = DBHelper.getUser().getName();
            pass = DBHelper.getUser().getPassword();
        }
        long studentId = -1;
        if (DBHelper.getStudent() != null && DBHelper.getStudent().getId() != null) {
            studentId = DBHelper.getStudent().getId();
        }
        map.put(USER, user);
        long time = System.currentTimeMillis();
        String token = user + MD5Utils.md5(pass)
                .toUpperCase() + time + ESWI;
        token = MD5Utils.md5(token).toUpperCase();
        map.put(TOKEN, token);
        map.put(TIME, time + "");
        map.put("device_id", DeviceUtils.getDeviceId());
        map.put("android_id", DeviceUtils.getAndroidId());
        map.put("mac", DeviceUtils.getMac());
        map.put("studentId", studentId + "");
        return map;
    }
}
