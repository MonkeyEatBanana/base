package co.tton.android.base.demo.api;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import co.tton.android.base.api.ApiResult;
import co.tton.android.base.api.ApiResultException;
import co.tton.android.base.demo.R;
import co.tton.android.base.demo.model.UploadImageResultBean;
import co.tton.android.base.view.ToastUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ApiUtils {

    public static MultipartBody.Part convertToPartBody(String key, File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        return MultipartBody.Part.createFormData(key, file.getName(), requestFile);
    }

    public static Observable<List<UploadImageResultBean>> uploadImage(List<String> paths) {
        return Observable.from(paths)
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return !TextUtils.isEmpty(s) && new File(s).exists();
                    }
                })
                .map(new Func1<String, MultipartBody.Part>() {
                    @Override
                    public MultipartBody.Part call(String path) {
                        File file = new File(path);
                        return convertToPartBody("media", file);
                    }
                })
                .flatMap(new Func1<MultipartBody.Part, Observable<ApiResult<UploadImageResultBean>>>() {
                    @Override
                    public Observable<ApiResult<UploadImageResultBean>> call(MultipartBody.Part part) {
                        return Api.get().uploadImage(part);
                    }
                })
                .map(new Func1<ApiResult<UploadImageResultBean>, UploadImageResultBean>() {
                    @Override
                    public UploadImageResultBean call(ApiResult<UploadImageResultBean> apiResult) {
                        if (apiResult.isOk()) {
                            return apiResult.mData;
                        }
                        throw new ApiResultException("99", "upload image failed");
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<UploadImageResultBean> uploadImage(String path) {
        List<String> list = new ArrayList<>();
        list.add(path);
        return uploadImage(list)
                .flatMap(new Func1<List<UploadImageResultBean>, Observable<UploadImageResultBean>>() {
                    @Override
                    public Observable<UploadImageResultBean> call(List<UploadImageResultBean> list1) {
                        return Observable.just(list1.get(0));
                    }
                });
    }

    public static void toastError(Context context, String message, Throwable e) {
        if (e != null && e.getCause() instanceof ApiResultException) {
            if (message == null) {
                String str = ((ApiResultException) e.getCause()).mMessage;
                ToastUtils.showShort(context, str);
            } else {
                ToastUtils.showShort(context, message);
            }
        } else {
            ToastUtils.showShort(context, context.getString(R.string.common_connection_error));
        }
    }

    public static void toastSuccess(Context context, String message) {
        ToastUtils.showShort(context, message);
    }

}
