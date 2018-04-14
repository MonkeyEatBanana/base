package co.tton.android.base.demo.api;

import co.tton.android.base.api.ApiResult;
import co.tton.android.base.api.RxApiClient;
import co.tton.android.base.demo.api.params.LoginParams;
import co.tton.android.base.demo.model.UploadImageResultBean;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

public class Api {

    private static volatile Api sInstance;

    private InternalApi mApi;

    private Api() {
        // TODO: set api endpoint
//        RxApiClient.setBaseUrl(Const.API_ENDPOINT);

        mApi = RxApiClient.get().createApi(InternalApi.class);
    }

    public static InternalApi get() {
        if (sInstance == null) {
            synchronized (Api.class) {
                if (sInstance == null) {
                    sInstance = new Api();
                }
            }
        }
        return sInstance.mApi;
    }

    public interface InternalApi {

        @POST("v1/login")
        Observable<ApiResult<String>> login(@Body LoginParams params);

        @Multipart
        @POST("v1/orders/picture")
        Observable<ApiResult<UploadImageResultBean>> uploadImage(@Part MultipartBody.Part file);
    }
}
