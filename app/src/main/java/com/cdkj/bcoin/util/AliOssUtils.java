package com.cdkj.bcoin.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.model.AliTokenModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

/**
 * @updateDts 2018/10/31
 */

public class AliOssUtils {
    private static final String ANDROID = "ANDROID";
    static String size = "";
    static String imageWidth = "";
    static String imageHeight = "";

    public static final String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
    public static final String bucketName = "kkkotc";
    private Context context;

    public AliOssUtils(Context context) {
        this.context = context;

    }

    /**
     * 获取七牛url  同步的
     *
     * @param callBack
     */
    public void getAliURL(final AliUpLoadBack callBack, final String data) {
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).showLoadingDialog();
        }
        getAliToeknRequest().enqueue(new BaseResponseModelCallBack<AliTokenModel>(context) {
            @Override
            protected void onSuccess(AliTokenModel mo, String SucMessage) {
                if (mo == null || TextUtils.isEmpty(mo.getSecurityToken())) {
                    return;
                }
                String accessKeyId = mo.getAccessKeyId();
                String accessKeySecret = mo.getAccessKeySecret();
                String securityToken = mo.getSecurityToken();

                try {
                    String key = MyConfig.IMGURL_ALI_HEAD + ANDROID + timestamp() + getImageWidthHeight(data) + ".jpg";
                    build2(accessKeyId, accessKeySecret, securityToken, data, key, callBack);
//                    uploadSingle(callBack,data);
                } catch (Exception e) {
                    if (callBack != null) {
                        callBack.onFal("图片上传失败,请选择正确的图片");
                    }
                }
            }

            @Override
            protected void onBuinessFailure(String code, String error) {
                callBack.onFal("图片上传失败,请选择正确的图片");
            }

            @Override
            protected void onReqFailure(int errorCode, String errorMessage) {
                callBack.onFal("图片上传失败,请选择正确的图片");
            }

            @Override
            protected void onNull() {
                callBack.onFal("图片上传失败,请选择正确的图片");
            }

            @Override
            protected void onFinish() {
                if (context instanceof BaseActivity) {
                    ((BaseActivity) context).disMissLoading();
                }
            }
        });

    }

    /**
     * 获取七牛url  异步的
     *
     * @param callBack
     */
    public void getAliURLAsync(final AliUpLoadBack callBack, final String data) {
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).showLoadingDialog();
        }
        getAliToeknRequest().enqueue(new BaseResponseModelCallBack<AliTokenModel>(context) {
            @Override
            protected void onSuccess(AliTokenModel mo, String SucMessage) {
                if (mo == null || TextUtils.isEmpty(mo.getSecurityToken())) {
                    return;
                }
                String accessKeyId = mo.getAccessKeyId();
                String accessKeySecret = mo.getAccessKeySecret();
                String securityToken = mo.getSecurityToken();

                try {
                    String key = ANDROID + timestamp() + getImageWidthHeight(data) + ".jpg";
                    build(accessKeyId, accessKeySecret, securityToken, data, key, callBack);
//                    uploadSingle(callBack,data);
                } catch (Exception e) {
                    if (callBack != null) {
                        callBack.onFal("图片上传失败,请选择正确的图片");
                    }
                }
            }

            @Override
            protected void onBuinessFailure(String code, String error) {
                callBack.onFal("图片上传失败,请选择正确的图片");
            }

            @Override
            protected void onReqFailure(int errorCode, String errorMessage) {
                callBack.onFal("图片上传失败,请选择正确的图片");
            }

            @Override
            protected void onNull() {
                callBack.onFal("图片上传失败,请选择正确的图片");
            }

            @Override
            protected void onFinish() {
                if (context instanceof BaseActivity) {
                    ((BaseActivity) context).disMissLoading();
                }
            }
        });

    }


    public void build(String AccessKeyId, String AccessKeySecret, String SecurityToken, String path, String name, AliUpLoadBack callBack) {
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).showLoadingDialog();
        }
        getAliToeknRequest().enqueue(new BaseResponseModelCallBack<AliTokenModel>(context) {
            @Override
            protected void onSuccess(AliTokenModel mo, String SucMessage) {
                if (mo == null || TextUtils.isEmpty(mo.getSecurityToken())) {
                    return;
                }
                String accessKeyId = mo.getAccessKeyId();
                String accessKeySecret = mo.getAccessKeySecret();
                String securityToken = mo.getSecurityToken();

                try {
                    String key = ANDROID + timestamp() + getImageWidthHeight(path) + ".jpg";
                    build2(accessKeyId, accessKeySecret, securityToken, path, key, callBack);
//                    uploadSingle(callBack,data);
                } catch (Exception e) {
                    if (callBack != null) {
                        callBack.onFal("图片上传失败,请选择正确的图片");
                    }
                }
            }

            @Override
            protected void onBuinessFailure(String code, String error) {
                callBack.onFal("图片上传失败,请选择正确的图片");
            }

            @Override
            protected void onReqFailure(int errorCode, String errorMessage) {
                callBack.onFal("图片上传失败,请选择正确的图片");
            }

            @Override
            protected void onNull() {
                callBack.onFal("图片上传失败,请选择正确的图片");
            }

            @Override
            protected void onFinish() {
                if (context instanceof BaseActivity) {
                    ((BaseActivity) context).disMissLoading();
                }
            }
        });


//        //if null , default will be init
//        ClientConfiguration conf = new ClientConfiguration();
//        conf.setConnectionTimeout(15 * 1000); // connction time out default 15s
//        conf.setSocketTimeout(15 * 1000); // socket timeout，default 15s
//        conf.setMaxConcurrentRequest(5); // synchronous request number，default 5
//        conf.setMaxErrorRetry(2); // retry，default 2
//        OSSLog.enableLog(); //write local log file ,path is SDCard_path\OSSLog\logs.csv
//        String AccessKeyId = "STS.NHCqkbHmzfgqufGuR1EZMzAh1";
//        String AccessKeySecret = "GeZynyywkxJnTgpCqeCvttN4XKs9ejA9jxx19fMWuULU";
//        String SecurityToken = "CAISmwJ1q6Ft5B2yfSjIr4v2OtHWpbJb0aWad0D2kVJkSdVhlYTD0zz2IHpPf3lhBOEasvUznmBS7P8Ylrh+W4NIX0rNaY5t9ZlN9wqkbtITC18RafhW5qe+EE2/VjTJvqaLEdibIfrZfvCyESem8gZ43br9cxi7QlWhKufnoJV7b9MRLGbaAD1dH4UUXEgAzvUXLnzML/2gHwf3i27LdipStxF7lHl05NbYoKiV4QGMi0bhmK1H5dazAOD9MZI0bMwuCInsgLcuLfqf6kMKtUgWrpURpbdf5DLKsuuaB1Rs+BicO4LWiIY1d1QpN/VrR/IV8KKsz6ch4PagnoD22gtLOvpOTyPcSYavzc3JAuq1McwjcrL2K+5jcT4xuQOfGoABWsXikl6vs1o6JwuCvJmfnGppzWUDL6CMrIC2IHxloczlFUBSj66QADAcoZ9PVxcUQio7aLn5bojglVfsNK/xUu+4kSoxro9qoW7D1gj4yyHQerAMRSzGeh371wkIAx5OvW2qIShrHCjUGxiJFNJfqhvsTz+/GxtML2jUE+pWS1o=";
//
//        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(AccessKeyId, AccessKeySecret, SecurityToken);
//
//        OSS oss = new OSSClient(context, endpoint, credentialProvider, conf);
//
//        // Construct an upload request
//        //上传名称  token   文件地址
//        PutObjectRequest put = new PutObjectRequest("kkkotc", "abc.jpg", path);
//
//        // You can set progress callback during asynchronous upload
//        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
//            @Override
//            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
//                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
//            }
//        });
//
//        try {
//            PutObjectResult putObjectResult = oss.putObject(put);
//            if (callBack != null)
//                callBack.onSuccess("", putObjectResult.getETag(), putObjectResult.getRequestId());
//            Log.d("ppppppPutObject成功", putObjectResult.getETag());
//            Log.d("ppppppPutObject成功", putObjectResult.getRequestId());
//        } catch (Exception e) {
//            e.printStackTrace();
//            if (callBack != null)
//                callBack.onFal(e.getMessage());
//        }

//        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
//            @Override
//            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
//                Log.d("PutObject", "UploadSuccess");
//                Log.d("ppppppPutObject", "UploadSuccess");
//            }
//
//            @Override
//            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
//                Log.d("ppppppPutObject失败", request.getBucketName()+serviceException.getErrorCode()+serviceException.getRawMessage());
//                // Request exception
//                if (clientExcepion != null) {
//                    // Local exception, such as a network exception
//                    clientExcepion.printStackTrace();
//                }
//                if (serviceException != null) {
//                    // Service exception
//                    Log.e("ErrorCode", serviceException.getErrorCode());
//                    Log.e("RequestId", serviceException.getRequestId());
//                    Log.e("HostId", serviceException.getHostId());
//                    Log.e("RawMessage", serviceException.getRawMessage());
//                }
//            }
//
//        });

//        Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> e) throws Exception {
//
//            }
//        }).observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Observer<String>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
////开始
//                    }
//
//                    @Override
//                    public void onNext(String s) {
////成功
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
////异常
//                    }
//
//                    @Override
//                    public void onComplete() {
////完成 异常或者成功  最后都会走  完成接口
//                    }
//                });

    }

    public void build2(String AccessKeyId, String AccessKeySecret, String SecurityToken, String path, String name, AliUpLoadBack callBack) {
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).showLoadingDialog();
        }
        //if null , default will be init
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // connction time out default 15s
        conf.setSocketTimeout(15 * 1000); // socket timeout，default 15s
        conf.setMaxConcurrentRequest(5); // synchronous request number，default 5
        conf.setMaxErrorRetry(2); // retry，default 2
        OSSLog.enableLog(); //write local log file ,path is SDCard_path\OSSLog\logs.csv
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(AccessKeyId, AccessKeySecret, SecurityToken);
        OSS oss = new OSSClient(context, endpoint, credentialProvider, conf);

        // Construct an upload request
        //上传名称  token   文件地址
        PutObjectRequest put = new PutObjectRequest(bucketName, name, path);
        // You can set progress callback during asynchronous upload
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        try {
            PutObjectResult putObjectResult = oss.putObject(put);
            if (callBack != null)
                callBack.onSuccess(name, putObjectResult.getETag(), putObjectResult.getRequestId());
            if (context instanceof BaseActivity) {
                ((BaseActivity) context).disMissLoading();
            }
            Log.d("ppppppPutObject成功", putObjectResult.getETag());
            Log.d("ppppppPutObject成功", putObjectResult.getRequestId());
        } catch (Exception e) {
            e.printStackTrace();
            if (callBack != null)
                callBack.onFal(e.getMessage());
            if (context instanceof BaseActivity) {
                ((BaseActivity) context).disMissLoading();
            }
        }

    }

    public static void buildAsync(Context context, String path, AliUpLoadBack callBack) {

        //if null , default will be init
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // connction time out default 15s
        conf.setSocketTimeout(15 * 1000); // socket timeout，default 15s
        conf.setMaxConcurrentRequest(5); // synchronous request number，default 5
        conf.setMaxErrorRetry(2); // retry，default 2
        OSSLog.enableLog(); //write local log file ,path is SDCard_path\OSSLog\logs.csv
        String AccessKeyId = "STS.NHCqkbHmzfgqufGuR1EZMzAh1";
        String AccessKeySecret = "GeZynyywkxJnTgpCqeCvttN4XKs9ejA9jxx19fMWuULU";
        String SecurityToken = "CAISmwJ1q6Ft5B2yfSjIr4v2OtHWpbJb0aWad0D2kVJkSdVhlYTD0zz2IHpPf3lhBOEasvUznmBS7P8Ylrh+W4NIX0rNaY5t9ZlN9wqkbtITC18RafhW5qe+EE2/VjTJvqaLEdibIfrZfvCyESem8gZ43br9cxi7QlWhKufnoJV7b9MRLGbaAD1dH4UUXEgAzvUXLnzML/2gHwf3i27LdipStxF7lHl05NbYoKiV4QGMi0bhmK1H5dazAOD9MZI0bMwuCInsgLcuLfqf6kMKtUgWrpURpbdf5DLKsuuaB1Rs+BicO4LWiIY1d1QpN/VrR/IV8KKsz6ch4PagnoD22gtLOvpOTyPcSYavzc3JAuq1McwjcrL2K+5jcT4xuQOfGoABWsXikl6vs1o6JwuCvJmfnGppzWUDL6CMrIC2IHxloczlFUBSj66QADAcoZ9PVxcUQio7aLn5bojglVfsNK/xUu+4kSoxro9qoW7D1gj4yyHQerAMRSzGeh371wkIAx5OvW2qIShrHCjUGxiJFNJfqhvsTz+/GxtML2jUE+pWS1o=";
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(AccessKeyId, AccessKeySecret, SecurityToken);
//        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(AccessKeyId, AccessKeySecret, SecurityToken);


        OSS oss = new OSSClient(context, endpoint, credentialProvider, conf);

        // Construct an upload request
        //上传名称  token   文件地址
        PutObjectRequest put = new PutObjectRequest("kkkotc", "abc.jpg", path);

        // 进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });


        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");
                Log.d("ppppppPutObject", "UploadSuccess");
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                Log.d("ppppppPutObject失败", request.getBucketName() + serviceException.getErrorCode() + serviceException.getRawMessage());
                // Request exception
                if (clientExcepion != null) {
                    // Local exception, such as a network exception
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // Service exception
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }

        });

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
//开始
                    }

                    @Override
                    public void onNext(String s) {
//成功
                    }

                    @Override
                    public void onError(Throwable e) {
//异常
                    }

                    @Override
                    public void onComplete() {
//完成 异常或者成功  最后都会走  完成接口
                    }
                });

    }

    /**
     * 获取阿里osstoken
     *
     * @return
     */
    public Call<BaseResponseModel<AliTokenModel>> getAliToeknRequest() {
        Map<String, String> object = new HashMap<>();
        object.put("companyCode", MyConfig.COMPANYCODE);
        object.put("systemCode", MyConfig.SYSTEMCODE);
        return RetrofitUtils.getBaseAPiService().getAliTOken("805953", StringUtils.getJsonToString(object));
    }


    /**
     * 图片单张上传
     *
     * @param callBack
     * @param url
     */
    private void uploadSingle(final AliOssUtils.AliUpLoadBack callBack, String url) {
        String key = ANDROID + timestamp() + getImageWidthHeight(url) + ".jpg";
        LogUtil.E("图片");
    }

    public static String getImageWidthHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        imageWidth = options.outWidth + "";
        imageHeight = options.outHeight + "";
        size = "_" + imageWidth + "_" + imageHeight;

        System.out.print("size = _" + imageWidth + "_" + imageHeight);
        return size;
    }


    private static String timestamp() {
        String time = System.currentTimeMillis() + "";
        return "_" + time;
    }

    public interface AliUpLoadBack {
        void onSuccess(String name, String etag, String requestId);

        void onFal(String info);
    }
}
