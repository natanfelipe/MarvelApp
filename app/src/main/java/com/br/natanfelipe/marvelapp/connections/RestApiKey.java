package com.br.natanfelipe.marvelapp.connections;

import com.br.natanfelipe.marvelapp.BuildConfig;
import com.br.natanfelipe.marvelapp.interfaces.ServerResponseConnector;
import com.br.natanfelipe.marvelapp.model.Comics;
import com.br.natanfelipe.marvelapp.model.Data;
import com.br.natanfelipe.marvelapp.model.Info;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiKey {

    String baseUrl = BuildConfig.BASE_URL;
    Retrofit retrofit;
    ServerResponseConnector connector;
    ApiInterface apiInterface;
    OkHttpClient okHttpClient;


    public void configure() {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiInterface = retrofit.create(ApiInterface.class);
    }

    public OkHttpClient getOkHttpClient() {
        if (okHttpClient != null)
            return okHttpClient;

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                return chain.proceed(request);
            }
        });

        clientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        clientBuilder.connectTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS);

        okHttpClient = clientBuilder.build();

        return okHttpClient;
    }

    public void setServerResponseConnector(ServerResponseConnector connector) {
        this.connector = connector;
    }


    public void heroesList(String ts, String key, String hash) {

        configure();

        try {
            if(ts != null && !ts.isEmpty() && key != null && !key.isEmpty() && hash != null && !hash.isEmpty()) {
                retrofit2.Response<Info> characterResponse;
                characterResponse = apiInterface.charactersList(ts, key, hash).execute();

                if (characterResponse.code() > 300) {
                    Converter<ResponseBody, Info> responseBodyCategoriesConverter = retrofit.responseBodyConverter(Info.class, new Annotation[0]);
                    Info error = responseBodyCategoriesConverter.convert(characterResponse.errorBody());
                    connector.onConnectionResult(characterResponse.code(), error);
                } else {
                    connector.onConnectionResult(characterResponse.code(), characterResponse.body());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMoreHeroes(int offset, String ts, String key, String hash) {

        configure();

        try {
            if(ts != null && !ts.isEmpty() && key != null && !key.isEmpty() && hash != null && !hash.isEmpty()) {
                retrofit2.Response<Info> characterResponse;
                characterResponse = apiInterface.moreCharactersList(offset,ts, key, hash).execute();

                if (characterResponse.code() > 300) {
                    Converter<ResponseBody, Info> responseBodyCategoriesConverter = retrofit.responseBodyConverter(Info.class, new Annotation[0]);
                    Info error = responseBodyCategoriesConverter.convert(characterResponse.errorBody());
                    connector.onConnectionResult(characterResponse.code(), error);
                } else {
                    connector.onConnectionResult(characterResponse.code(), characterResponse.body());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void comicsList(int id,String ts, String key, String hash) {

        configure();

        try {
            if(ts != null && !ts.isEmpty() && key != null && !key.isEmpty() && hash != null && !hash.isEmpty()) {
                retrofit2.Response<Info> characterResponse;
                characterResponse = apiInterface.comicsList(id,ts, key, hash).execute();

                if (characterResponse.code() > 300) {
                    Converter<ResponseBody, Info> responseBodyCategoriesConverter = retrofit.responseBodyConverter(Info.class, new Annotation[0]);
                    Info error = responseBodyCategoriesConverter.convert(characterResponse.errorBody());
                    connector.onConnectionResult(characterResponse.code(), error);
                } else {
                    connector.onConnectionResult(characterResponse.code(), characterResponse.body());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
