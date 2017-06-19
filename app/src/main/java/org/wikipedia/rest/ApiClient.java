package org.wikipedia.rest;



import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SwethaAmbati on 6/15/17.
 */

public class ApiClient {

    private static final String BASE_URL = "https://en.wikipedia.org/";

    public static ApiService getApi() {
        OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(new HeaderInterceptor()).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiService.class);
    }


}
