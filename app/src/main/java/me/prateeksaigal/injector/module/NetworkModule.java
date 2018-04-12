package me.prateeksaigal.injector.module;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import me.prateeksaigal.injector.JsonExclusionStrategy;
import me.prateeksaigal.injector.scope.PerApplication;
import me.prateeksaigal.network.Repository;
import me.prateeksaigal.network.RetrofitRepository;
import me.prateeksaigal.ocotocoffee.BuildConfig;
import me.prateeksaigal.utils.Constans;
import me.prateeksaigal.utils.PreferenceUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static me.prateeksaigal.utils.Constans.API_TIMEOUT_IN_SECOND;


/**
 *
 */
@Module
public class NetworkModule {

    public static final String API_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ";

    @Provides
    @PerApplication
    Repository provideRepository(Retrofit retrofit) {
        return new RetrofitRepository(retrofit);
    }

    @Provides
    @PerApplication
    Retrofit provideRetrofit() {
        Gson gson = new GsonBuilder()
                .setDateFormat(API_DATE_FORMAT)
                .addSerializationExclusionStrategy(new JsonExclusionStrategy())
                .addDeserializationExclusionStrategy(new JsonExclusionStrategy())
                .create();
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(API_TIMEOUT_IN_SECOND, TimeUnit.SECONDS);
        httpClient.readTimeout(API_TIMEOUT_IN_SECOND, TimeUnit.SECONDS);
        httpClient.writeTimeout(API_TIMEOUT_IN_SECOND, TimeUnit.SECONDS);
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            String accessToken = PreferenceUtils.getAuthToken();
            //String accessToken ="3DXMjtt0DroaFtgHAtBVnDnoFTuDb837";
            if(accessToken!=null){
                Request.Builder requestBuilder = original.newBuilder()
                        .header(Constans.AUTHORIZATION, "Bearer "+accessToken)
                        .method(original.method(), original.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            } else {
                return chain.proceed(original);
            }
        });
        httpClient.addInterceptor(logging);
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(httpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .build();
    }

}
