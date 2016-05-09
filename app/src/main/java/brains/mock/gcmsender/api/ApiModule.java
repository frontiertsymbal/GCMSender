package brains.mock.gcmsender.api;

import android.content.Context;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiModule {

    private static final String CACHE_FOLDER_NAME = "cached";

    private Context context;
    private int diskCacheSize;
    private int requestTimeout;
    private String apiBaseUrl;
    private Class<ApiInterface> apiInterface;

    public ApiModule(Context context, String apiBaseUrl, Class<ApiInterface> apiInterface, int diskCacheSize, int requestTimeout) {
        this.context = context;
        this.apiBaseUrl = apiBaseUrl;
        this.apiInterface = apiInterface;
        this.diskCacheSize = diskCacheSize;
        this.requestTimeout = requestTimeout;
    }

    public ApiInterface provideApiInterface() {
        Retrofit retrofit = provideRetrofit(provideConverter(), provideHttpClient(context));
        return provideApiInterface(retrofit);
    }

    private ApiInterface provideApiInterface(Retrofit retrofit) {
        return retrofit.create(apiInterface);
    }

    private Retrofit provideRetrofit(GsonConverterFactory converter, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(apiBaseUrl)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(converter)
                .build();
    }

    private GsonConverterFactory provideConverter() {
        return GsonConverterFactory.create();
    }

    private OkHttpClient provideHttpClient(Context context) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        //HTTP logging init
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        //Cache init
        File cacheDir = new File(context.getCacheDir(), CACHE_FOLDER_NAME);
        Cache cache = new Cache(cacheDir, diskCacheSize);

        httpClient.connectTimeout(requestTimeout, TimeUnit.SECONDS);
        httpClient.readTimeout(requestTimeout, TimeUnit.SECONDS);
        httpClient.writeTimeout(requestTimeout, TimeUnit.SECONDS);
        httpClient.addInterceptor(logging);

        httpClient.cache(cache);

        return httpClient.build();
    }
}
