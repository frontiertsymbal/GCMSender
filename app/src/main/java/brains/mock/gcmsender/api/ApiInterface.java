package brains.mock.gcmsender.api;

import brains.mock.gcmsender.api.model.Push;
import brains.mock.gcmsender.api.model.Result;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

public interface ApiInterface {

    @Headers("Content-Type: application/json")
    @POST("/gcm/send")
    Observable<Result> sendPushNotification(@Header("Authorization") String authorization, @Body Push data);
}
