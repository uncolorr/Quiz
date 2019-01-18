package com.sap.uncolor.quiz.apis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private static Api api;
    private static ApiSource apiSource;
    private Retrofit retrofit;
    private static final String BASE_URL = "http://92.53.67.78:8080/";

    private Api() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        Gson gson = new GsonBuilder()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
        apiSource = retrofit.create(ApiSource.class);
    }

    public static void init() {
        if (api == null) {
            api = new Api();
        }
    }

    public static ApiSource getSource() {
        return apiSource;
    }
}
