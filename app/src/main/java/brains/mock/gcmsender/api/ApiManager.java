package brains.mock.gcmsender.api;

import android.content.Context;

import brains.mock.gcmsender.Const;
import brains.mock.gcmsender.api.model.Data;
import brains.mock.gcmsender.api.model.Push;
import brains.mock.gcmsender.api.model.Result;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ApiManager {

    private ApiInterface apiInterface;

    public ApiManager(Context context) {
        ApiModule apiModule = new ApiModule(context, Const.API_BASE_URL, ApiInterface.class, Const.DISK_CACHE_SIZE, Const.REQUEST_TIMEOUT);
        apiInterface = apiModule.provideApiInterface();
    }

    public Observable<Result> sendPushNotification(String apiKey, String message, String to) {
        Data data = new Data(message);
        Push push = new Push(data, to);
        String authorizationParam = Const.KEY + apiKey;
        return apiInterface.sendPushNotification(authorizationParam, push)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
