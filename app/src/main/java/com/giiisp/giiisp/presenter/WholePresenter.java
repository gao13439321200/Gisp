package com.giiisp.giiisp.presenter;


import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.facebook.stetho.common.LogUtil;
import com.giiisp.giiisp.api.ApiStoreNew;
import com.giiisp.giiisp.base.BasePresenter;
import com.giiisp.giiisp.dto.AppInfoBean;
import com.giiisp.giiisp.dto.BaseBean;
import com.giiisp.giiisp.dto.CollectListBean;
import com.giiisp.giiisp.dto.CountryListBean;
import com.giiisp.giiisp.dto.CourseBean;
import com.giiisp.giiisp.dto.DownloadInfoBean;
import com.giiisp.giiisp.dto.DubbingBean;
import com.giiisp.giiisp.dto.DubbingListBean;
import com.giiisp.giiisp.dto.EditInfoBean;
import com.giiisp.giiisp.dto.FansBean;
import com.giiisp.giiisp.dto.FollowBean;
import com.giiisp.giiisp.dto.GroupInfoBean;
import com.giiisp.giiisp.dto.GroupListBean;
import com.giiisp.giiisp.dto.HeBean;
import com.giiisp.giiisp.dto.HeadImgBean;
import com.giiisp.giiisp.dto.HotImgBean;
import com.giiisp.giiisp.dto.ImgInfoBean;
import com.giiisp.giiisp.dto.LoginBean;
import com.giiisp.giiisp.dto.MIneInfoBean;
import com.giiisp.giiisp.dto.MajorBean;
import com.giiisp.giiisp.dto.MarkBean;
import com.giiisp.giiisp.dto.MsgNewBean;
import com.giiisp.giiisp.dto.MyAnswerBean;
import com.giiisp.giiisp.dto.PaperBean;
import com.giiisp.giiisp.dto.PaperEventBean;
import com.giiisp.giiisp.dto.PaperInfoBean;
import com.giiisp.giiisp.dto.PaperLiteratureBean;
import com.giiisp.giiisp.dto.PaperMainBean;
import com.giiisp.giiisp.dto.PaperQaBean;
import com.giiisp.giiisp.dto.PeopleBean;
import com.giiisp.giiisp.dto.PlayNoteBean;
import com.giiisp.giiisp.dto.ProfessionalListBean;
import com.giiisp.giiisp.dto.SchoolListBean;
import com.giiisp.giiisp.dto.StatisticsBean;
import com.giiisp.giiisp.dto.SubjectBean;
import com.giiisp.giiisp.dto.WordBean;
import com.giiisp.giiisp.entity.AnswerEntity;
import com.giiisp.giiisp.entity.AntistopEntity;
import com.giiisp.giiisp.entity.AttentionEntity;
import com.giiisp.giiisp.entity.BaseEntity;
import com.giiisp.giiisp.entity.FansConcernedEntity;
import com.giiisp.giiisp.entity.HomeEntity;
import com.giiisp.giiisp.entity.HomeSearchEntity;
import com.giiisp.giiisp.entity.LiteratureEntity;
import com.giiisp.giiisp.entity.MsgEntity;
import com.giiisp.giiisp.entity.MyScholarEntity;
import com.giiisp.giiisp.entity.PaperDatailEntity;
import com.giiisp.giiisp.entity.PhoneEntity;
import com.giiisp.giiisp.entity.QAEntity;
import com.giiisp.giiisp.entity.QuizEntity;
import com.giiisp.giiisp.entity.QuizHintEntity;
import com.giiisp.giiisp.entity.RecommendScholarEntity;
import com.giiisp.giiisp.entity.ScholarEntity;
import com.giiisp.giiisp.entity.SearchHistoryEntity;
import com.giiisp.giiisp.entity.SelectUser;
import com.giiisp.giiisp.entity.SubscribeEntity;
import com.giiisp.giiisp.entity.UpDateAppEntity;
import com.giiisp.giiisp.entity.UserInfoEntity;
import com.giiisp.giiisp.entity.WaitRecordPaperEntity;
import com.giiisp.giiisp.model.ModelFactory;
import com.giiisp.giiisp.utils.DESedeUtils;
import com.giiisp.giiisp.view.impl.BaseImpl;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.chad.library.adapter.base.listener.SimpleClickListener.TAG;
import static com.giiisp.giiisp.utils.ToolString.getUUID;
import static com.giiisp.giiisp.utils.ToolString.toJsonStr;


public class WholePresenter extends BasePresenter<BaseImpl> {
    private BaseImpl impl = null;
    private String pid = "";

    public WholePresenter(BaseImpl impl) {
        this.impl = impl;
    }

    public void getSendCodeData(String mobile, String codeType) {
        Callback<BaseEntity> callback = new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    BaseEntity entity = response.body();
                    if (entity != null) {
                        impl.onSuccess(entity);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        };
        ModelFactory.getBaseModel().getCodeData(mobile, codeType, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    BaseEntity entity = response.body();
                    if (entity != null) {
                        impl.onSuccess(entity);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void getPhoneData(String mobile) {
        ModelFactory.getBaseModel().getPhoneData(mobile, new Callback<PhoneEntity>() {
            @Override
            public void onResponse(Call<PhoneEntity> call, Response<PhoneEntity> response) {
                if (response.isSuccessful()) {
                    PhoneEntity entity = response.body();
                    if (entity != null) {
                        impl.onSuccess(entity);
                    }
                }
            }

            @Override
            public void onFailure(Call<PhoneEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void getEnrollData(ArrayMap<String, Object> token) {
        ModelFactory.getBaseModel().getEnrollData(token, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    BaseEntity entity = response.body();
                    if (entity != null /*&& entity.getResult() == 1*/) {
                        impl.onSuccess(entity);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void saveClientTypeData(ArrayMap<String, Object> token) {
        ModelFactory.getBaseModel().saveClientTypeData(token, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    BaseEntity entity = response.body();
                    if (entity != null /*&& entity.getResult() == 1*/) {
                        impl.onSuccess(entity);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void getUpdatePortraitData(String uid, MultipartBody.Part filePart) {
        ModelFactory.getBaseModel().getUpdatePortraitData(uid, filePart, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    BaseEntity entity = response.body();
                    if (entity != null /*&& entity.getResult() == 1*/) {
                        impl.onSuccess(entity);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void getLoginData(String mobile, String pwd) {
        ModelFactory.getBaseModel().getLoginData(mobile, pwd, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    BaseEntity entity = response.body();
                    if (entity != null /*&& entity.getResult() == 1*/) {
                        impl.onSuccess(entity);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void getRetrieveData(ArrayMap<String, Object> token) {
        ModelFactory.getBaseModel().getRetrieveData(token, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    BaseEntity entity = response.body();
                    if (entity != null /*&& entity.getResult() == 1*/) {
                        impl.onSuccess(entity);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void getUpdateMobileData(ArrayMap<String, Object> token) {
        ModelFactory.getBaseModel().getUpdateMobileData(token, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    BaseEntity entity = response.body();
                    if (entity != null /*&& entity.getResult() == 1*/) {
                        impl.onSuccess(entity);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void getUserInfoData(ArrayMap<String, Object> map) {
        ModelFactory.getBaseModel().getUserInfoData(map, new Callback<UserInfoEntity>() {
            @Override
            public void onResponse(Call<UserInfoEntity> call, Response<UserInfoEntity> response) {
                if (response.isSuccessful()) {
                    UserInfoEntity entity = response.body();
                    if (entity != null /*&& entity.getResult() == 1*/) {
                        impl.onSuccess(entity);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserInfoEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void getUpdateUserInfoData(ArrayMap<String, Object> mobile) {
        ModelFactory.getBaseModel().getUpdateUserInfoData(mobile, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    BaseEntity entity = response.body();
                    if (entity != null) {
                        impl.onSuccess(entity);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void getUserNumsData(String uid, String mobile) {
        ModelFactory.getBaseModel().getUserNumsData(uid, mobile, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    BaseEntity entity = response.body();
                    if (entity != null) {
                        impl.onSuccess(entity);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void getResetPwdData(ArrayMap<String, Object> mobile) {
        ModelFactory.getBaseModel().getResetPwdData(mobile, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    BaseEntity entity = response.body();
                    if (entity != null) {
                        impl.onSuccess(entity);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void getFeedbackData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getFeedbackData(options, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    BaseEntity entity = response.body();
                    if (entity != null /*&& entity.getResult() == 1*/) {
                        impl.onSuccess(entity);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void getSaveFollowTypeData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getSaveFollowTypeData(options, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    BaseEntity entity = response.body();
                    if (entity != null /*&& entity.getResult() == 1*/) {
                        impl.onSuccess(entity);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void getCancelFollowTypeInfo(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getCancelFollowTypeInfo(options, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    BaseEntity entity = response.body();
                    if (entity != null /*&& entity.getResult() == 1*/) {
                        impl.onSuccess(entity);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void getSubscribeFieldData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getSubscribeFieldData(options, new Callback<AttentionEntity>() {
            @Override
            public void onResponse(Call<AttentionEntity> call, Response<AttentionEntity> response) {
                if (response.isSuccessful()) {
                    AttentionEntity entity = response.body();
                    if (entity != null /*&& entity.getResult() == 1*/) {
                        impl.onSuccess(entity);
                    }
                }
            }

            @Override
            public void onFailure(Call<AttentionEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void getScholarsRecommendData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getScholarsRecommendData(options, new Callback<RecommendScholarEntity>() {
            @Override
            public void onResponse(Call<RecommendScholarEntity> call, Response<RecommendScholarEntity> response) {
                if (response.isSuccessful()) {
                    BaseEntity entity = response.body();
                    if (entity != null /*&& entity.getResult() == 1*/) {
                        impl.onSuccess(entity);
                    }
                }
            }

            @Override
            public void onFailure(Call<RecommendScholarEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void getScholarsFollowedData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getScholarsFollowedData(options, new Callback<MyScholarEntity>() {
            @Override
            public void onResponse(Call<MyScholarEntity> call, Response<MyScholarEntity> response) {
                if (response.isSuccessful()) {
                    MyScholarEntity entity = response.body();
                    if (entity != null /*&& entity.getResult() == 1*/) {
                        impl.onSuccess(entity);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyScholarEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void getPortraitData(String options) {
        ModelFactory.getBaseModel().getPortraitData(options, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    try {
                        String result = response.body().string().trim();
                        BaseEntity entity = new BaseEntity();
                        entity.setInfo("result");
                        impl.onSuccess(entity);
                        Log.i("--->>", "onResponse: " + result);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void downloadFileWithDynamicUrlSync() {
   /*     new Thread(new Runnable() {
            @Override
            public void run() {*/
        Log.i("--->>", "downloadFileWithDynamicUrlSync: " + Thread.currentThread().getName());
        ModelFactory.getBaseModel().downloadFileWithDynamicUrlSync(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    boolean writtenToDisk = writeResponseBodyToDisk(response.body());
                    Log.i("--->>", "onResponse: " + writtenToDisk);
                    // response.body() 返回 ResponseBody
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
        //            }

        //        });

    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            File futureStudioIconFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "Giiisp/" + System.currentTimeMillis() + ".mp3");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                int len;
                while ((len = inputStream.read(fileReader)) != -1) {
                    outputStream.write(fileReader, 0, len);
                    fileSizeDownloaded += len;

                    Log.d("--->>", "file download: " + fileSizeDownloaded + " of " + fileSize);

                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }


    public void getListUserFollowedData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getListUserFollowedData(options, new Callback<FansConcernedEntity>() {
            @Override
            public void onResponse(Call<FansConcernedEntity> call, Response<FansConcernedEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<FansConcernedEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getListUserFollowData(ArrayMap<String, Object> options) {
        Log.i("--->>", "getListUserFollowData:" + options.toString());
        ModelFactory.getBaseModel().getListUserFollowData(options, new Callback<FansConcernedEntity>() {
            @Override
            public void onResponse(Call<FansConcernedEntity> call, Response<FansConcernedEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<FansConcernedEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getSaveFollowUserData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getSaveFollowUserData(options, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getCancelFollowUserData(ArrayMap<String, Object> options) { // getCancelFollowUserData
        ModelFactory.getBaseModel().getCancelFollowUserData(options, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getSaveFollowPaperPictureData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getSaveFollowPaperPictureData(options, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void getCancelFollowPaperPictureData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getCancelFollowPaperPictureData(options, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void getSaveQuizData(ArrayMap<String, Object> options, MultipartBody.Part filePrta) {
        ModelFactory.getBaseModel().getSaveQuizData(options, filePrta, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getSaveAnswerData(ArrayMap<String, Object> options, MultipartBody.Part filePrta) {
        ModelFactory.getBaseModel().getSaveAnswerData(options, filePrta, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void getListScholarData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getListScholarData(options, new Callback<ScholarEntity>() {
            @Override
            public void onResponse(Call<ScholarEntity> call, Response<ScholarEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<ScholarEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getListSummarizeData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getListSummarizeData(options, new Callback<SubscribeEntity>() {
            @Override
            public void onResponse(Call<SubscribeEntity> call, Response<SubscribeEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<SubscribeEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getListNewPaperData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getListNewPaperData(options, new Callback<SubscribeEntity>() {
            @Override
            public void onResponse(Call<SubscribeEntity> call, Response<SubscribeEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<SubscribeEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getWaitRecordPaperListData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getWaitRecordPaperListData(options, new Callback<WaitRecordPaperEntity>() {
            @Override
            public void onResponse(Call<WaitRecordPaperEntity> call, Response<WaitRecordPaperEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<WaitRecordPaperEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getPaperBaseByIdData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getPaperBaseByIdData(options, new Callback<PaperDatailEntity>() {
            @Override
            public void onResponse(Call<PaperDatailEntity> call, Response<PaperDatailEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<PaperDatailEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getListOfLiteratureData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getListOfLiteratureData(options, new Callback<LiteratureEntity>() {
            @Override
            public void onResponse(Call<LiteratureEntity> call, Response<LiteratureEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<LiteratureEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getListOfAntistopData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getListOfAntistopData(options, new Callback<AntistopEntity>() {
            @Override
            public void onResponse(Call<AntistopEntity> call, Response<AntistopEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<AntistopEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getlistFollowPaperData(ArrayMap<String, Object> options) {
//        ModelFactory.getBaseModel().getlistFollowPaperData(options, new Callback<SubscribeEntity>() {
//            @Override
//            public void onResponse(Call<SubscribeEntity> call, Response<SubscribeEntity> response) {
//                if (response.isSuccessful()) {
//                    // response.body() 返回 ResponseBody
//                    BaseEntity entity = response.body();
//                    impl.onSuccess(entity);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SubscribeEntity> call, Throwable t) {
//                impl.onFailure(call + "", (Exception) t);
//            }
//
//
//        });
    }

    public void getListOfQuizAndAnswerData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getListOfQuizAndAnswerData(options, new Callback<QAEntity>() {
            @Override
            public void onResponse(Call<QAEntity> call, Response<QAEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<QAEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getListFollowedPaperData(ArrayMap<String, Object> options) {
//        ModelFactory.getBaseModel().getListFollowedPaperData(options, new Callback<SubscribeEntity>() {
//            @Override
//            public void onResponse(Call<SubscribeEntity> call, Response<SubscribeEntity> response) {
//                if (response.isSuccessful()) {
//                    // response.body() 返回 ResponseBody
//                    BaseEntity entity = response.body();
//                    impl.onSuccess(entity);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SubscribeEntity> call, Throwable t) {
//                impl.onFailure(call + "", (Exception) t);
//            }
//
//
//        });
    }

    public void getSaveRecordData(ArrayMap<String, Object> options, MultipartBody.Part part) {
        ModelFactory.getBaseModel().getSaveRecordData(options, part, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response != null && response.body() != null && response.body().getResult() == 1) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    BaseBean baseBean = new BaseBean();
                    baseBean.setRid(response.body().getRid());
                    impl.onSuccessNew("sendData", baseBean);
                } else {
                    impl.onFailNew("sendData", "发送失败");
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void gethomePageData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().gethomePageData(options, new Callback<HomeEntity>() {
            @Override
            public void onResponse(Call<HomeEntity> call, Response<HomeEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<HomeEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getUserRecordData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getUserRecordData(options, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getUserLogOutData() {
        ModelFactory.getBaseModel().getUserLogOutData(new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getAppInfoData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getAppInfoData(options, new Callback<UpDateAppEntity>() {
            @Override
            public void onResponse(Call<UpDateAppEntity> call, Response<UpDateAppEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<UpDateAppEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getAuthenUserlData(String email, String uid,MultipartBody.Part imageBodyPart) {
        ModelFactory.getBaseModel().getAuthenUserlData(email, uid,imageBodyPart, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
                Log.e("Presenter", "onFailure: " + t.toString());
            }


        });
    }

    public void getHomeSearchData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getHomeSearchData(options, new Callback<HomeSearchEntity>() {
            @Override
            public void onResponse(Call<HomeSearchEntity> call, Response<HomeSearchEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<HomeSearchEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getUpdatePaperData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getUpdatePaperData(options, new Callback<HomeSearchEntity>() {
            @Override
            public void onResponse(Call<HomeSearchEntity> call, Response<HomeSearchEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<HomeSearchEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getMsgListData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getMsgListData(options, new Callback<MsgEntity>() {
            @Override
            public void onResponse(Call<MsgEntity> call, Response<MsgEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<MsgEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getMsgDelData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getMsgDelData(options, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getQuizHintListData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getQuizHintListData(options, new Callback<QuizHintEntity>() {
            @Override
            public void onResponse(Call<QuizHintEntity> call, Response<QuizHintEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<QuizHintEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getSearchHistoryData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getSearchHistoryData(options, new Callback<SearchHistoryEntity>() {
            @Override
            public void onResponse(Call<SearchHistoryEntity> call, Response<SearchHistoryEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<SearchHistoryEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getCleanHistoryData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getCleanHistoryData(options, new Callback<QuizHintEntity>() {
            @Override
            public void onResponse(Call<QuizHintEntity> call, Response<QuizHintEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<QuizHintEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getSaveShareData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getSaveShareData(options, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getQNTokenData(String options) {
        ModelFactory.getBaseModel().getQNTokenData(options, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getWith3Data(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getWith3Data(options, new Callback<PhoneEntity>() {
            @Override
            public void onResponse(Call<PhoneEntity> call, Response<PhoneEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    PhoneEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<PhoneEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getListAnswerData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getListAnswerData(options, new Callback<AnswerEntity>() {
            @Override
            public void onResponse(Call<AnswerEntity> call, Response<AnswerEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    AnswerEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<AnswerEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }


        });
    }

    public void getListQuizData(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().getListQuizData(options, new Callback<QuizEntity>() {
            @Override
            public void onResponse(Call<QuizEntity> call, Response<QuizEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    QuizEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<QuizEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void addIntroduction(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().addIntroduction(options, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void updateIntroduction(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().updateIntroduction(options, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void deleteIntroduction(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().deleteIntroduction(options, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void userAuthen(List<MultipartBody.Part> parts) {
        ModelFactory.getBaseModel().userAuthen(parts, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public void selectUser(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().selectUser(options, new Callback<SelectUser>() {
            @Override
            public void onResponse(Call<SelectUser> call, Response<SelectUser> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    SelectUser entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<SelectUser> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    public static List<MultipartBody.Part> filesToMultipartBodyParts(List<File> files) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {
            //  16-4-2  这里为了简单起见，没有判断file的类型
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }


    // 验证论文密码
    public void checkPaperPwd(ArrayMap<String, Object> options) {
        ModelFactory.getBaseModel().checkPaperPwd(options, new Callback<BaseEntity>() {
            @Override
            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                if (response.isSuccessful()) {
                    // response.body() 返回 ResponseBody
                    BaseEntity entity = response.body();
                    impl.onSuccess(entity);
                }
            }

            @Override
            public void onFailure(Call<BaseEntity> call, Throwable t) {
                impl.onFailure(call + "", (Exception) t);
            }
        });
    }

    // 普通回调，无特殊数据
    public void getDataAll(String por, HashMap<String, Object> options) {
        LogUtils.v("okHttp---por:" + por + " 回调参数：" + options);
        ApiStoreNew.getInstance().getApiService().getDataString(getHashMap(por, options))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.body() != null) {
                            BaseBean entity = stringToBody(por, response.body());
                            if (1 == entity.getStatusCode() || 0 == entity.getStatusCode()) {
                                impl.onSuccessNew(por, entity);
                            } else {
                                impl.onFailNew(por, entity.getMessage());
                            }
                        } else {
                            LogUtils.v("接口：" + por + " 回调okHttp异常信息：回调为空");
                            impl.onFailNew(por, "");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        LogUtils.v("接口：" + por + " 回调okHttp异常信息：" + t);
                        impl.onFailNew(por, "");
                    }
                });
    }

    // 普通回调，无特殊数据
    public void getDataAll(String por, HashMap<String, Object> options, MultipartBody.Part part) {
        LogUtils.v("okHttp---por:" + por + " 回调参数：" + options);
        ApiStoreNew.getInstance().getApiService().getDataStringFile(getHashMap(por, options), part)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.body() != null) {
                            BaseBean entity = stringToBody(por, response.body());
                            if (1 == entity.getStatusCode() || 0 == entity.getStatusCode()) {
                                impl.onSuccessNew(por, entity);
                            } else {
                                impl.onFailNew(por, entity.getMessage());
                            }
                        } else {
                            LogUtils.v("接口：" + por + " 回调okHttp异常信息：回调为空");
                            impl.onFailNew(por, "");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        LogUtils.v("接口：" + por + " 回调okHttp异常信息：" + t);
                        impl.onFailNew(por, "信息获取失败");
                    }
                });
    }


    //参数统一处理
    private String getHashMap(String por, HashMap dMap) {
        String cipher = DESedeUtils.getDesede(toJsonStr(dMap), getUUID());
        LogUtil.d(TAG, "data:cipher；" + cipher);
        HashMap<String, Object> map = new HashMap<>();
        map.put("por", por);   // 请求接口
        map.put("pid", getUUID()); // 设备唯一码
        map.put("cipher", cipher); // c参数密文
        LogUtils.v("okHttp:" + toJsonStr(map));
        return toJsonStr(map);
    }

    private BaseBean stringToBody(String url, String result) {
//        LogUtils.v("接口：" + url + ",okHttp回调未解密：" + result);
        String cipher = DESedeUtils.getdeCrypt(result, getUUID());
        LogUtils.v("接口：" + url + ",okHttp回调解密：" + cipher);
        BaseBean baseEntity;
        switch (url) {
            case "102":
            case "104":
                baseEntity = new Gson().fromJson(cipher, LoginBean.class);
                break;
            case "109":
                baseEntity = new Gson().fromJson(cipher, SubjectBean.class);
                break;
            case "110":
            case "115":
                baseEntity = new Gson().fromJson(cipher, MajorBean.class);
                break;
            case "112":
            case "113":
                baseEntity = new Gson().fromJson(cipher, WordBean.class);
                break;
            case "116":
            case "117":
                baseEntity = new Gson().fromJson(cipher, PeopleBean.class);
                break;
            case "119":
                baseEntity = new Gson().fromJson(cipher, AppInfoBean.class);
                break;
            case "201":
                baseEntity = new Gson().fromJson(cipher, HeadImgBean.class);
                break;
            case "202":
                baseEntity = new Gson().fromJson(cipher, HotImgBean.class);
                break;
            case "203":
                baseEntity = new Gson().fromJson(cipher, PaperBean.class);
                break;
            case "204":
                baseEntity = new Gson().fromJson(cipher, PaperInfoBean.class);
                break;
            case "205":
                baseEntity = new Gson().fromJson(cipher, ImgInfoBean.class);
                break;
            case "206":
                baseEntity = new Gson().fromJson(cipher, PaperQaBean.class);
                break;
            case "208":
                baseEntity = new Gson().fromJson(cipher, PaperLiteratureBean.class);
                break;
            case "212":
                baseEntity = new Gson().fromJson(cipher, CollectListBean.class);
                break;
            case "209":
            case "312":
                baseEntity = new Gson().fromJson(cipher, PaperMainBean.class);
                break;
            case "214":
                baseEntity = new Gson().fromJson(cipher, PlayNoteBean.class);
                break;
            case "216":
                baseEntity = new Gson().fromJson(cipher, DownloadInfoBean.class);
                break;
            case "218":
                baseEntity = new Gson().fromJson(cipher, PeopleBean.class);
                break;
            case "219":
                baseEntity = new Gson().fromJson(cipher, PaperMainBean.class);
                break;
            case "220":
                baseEntity = new Gson().fromJson(cipher, StatisticsBean.class);
                break;
            case "222":
                baseEntity = new Gson().fromJson(cipher, HeBean.class);
                break;
            case "305":
                baseEntity = new Gson().fromJson(cipher, PaperEventBean.class);
                break;
            case "306":
                baseEntity = new Gson().fromJson(cipher, MIneInfoBean.class);
                break;
            case "307":
                baseEntity = new Gson().fromJson(cipher, FansBean.class);
                break;
            case "308":
                baseEntity = new Gson().fromJson(cipher, FollowBean.class);
                break;
            case "313":
            case "314":
                baseEntity = new Gson().fromJson(cipher, MyAnswerBean.class);
                break;
            case "316":
                baseEntity = new Gson().fromJson(cipher, DubbingListBean.class);
                break;
            case "317":
                baseEntity = new Gson().fromJson(cipher, DubbingBean.class);
                break;
            case "321":
                baseEntity = new Gson().fromJson(cipher, CourseBean.class);
                break;
            case "322":
                baseEntity = new Gson().fromJson(cipher, CountryListBean.class);
                break;
            case "323":
            case "324":
            case "325":
                baseEntity = new Gson().fromJson(cipher, SchoolListBean.class);
                break;
            case "326":
                baseEntity = new Gson().fromJson(cipher, EditInfoBean.class);
                break;
            case "330":
                baseEntity = new Gson().fromJson(cipher, ProfessionalListBean.class);
                break;
            case "337":
                baseEntity = new Gson().fromJson(cipher, GroupListBean.class);
                break;
            case "338":
                baseEntity = new Gson().fromJson(cipher, GroupInfoBean.class);
                break;
            case "342":
                baseEntity = new Gson().fromJson(cipher, MsgNewBean.class);
                break;
            case "343":
            case "344":
                baseEntity = new Gson().fromJson(cipher, MarkBean.class);
                break;
            default://101、105、111、304、318、215、327、328、329
                baseEntity = new Gson().fromJson(cipher, BaseBean.class);
                break;
        }
        return baseEntity;
    }
}
