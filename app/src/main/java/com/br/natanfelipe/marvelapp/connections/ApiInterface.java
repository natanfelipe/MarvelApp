package com.br.natanfelipe.marvelapp.connections;

import com.br.natanfelipe.marvelapp.model.Comics;
import com.br.natanfelipe.marvelapp.model.Data;
import com.br.natanfelipe.marvelapp.model.Info;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("characters")
    Call<Info> charactersList(@Query("ts") String ts,
                              @Query("apikey") String key,
                              @Query("hash") String hash);

    @GET("characters")
    Call<Info> moreCharactersList(@Query("offset") int offset,
                                  @Query("ts") String ts,
                                  @Query("apikey") String key,
                                  @Query("hash") String hash);

    @GET("characters/{id}/comics")
    Call<Info> comicsList(@Path("id") int id,
                            @Query("ts") String ts,
                            @Query("apikey") String key,
                            @Query("hash") String hash);

    /*@GET("movie/{id}/videos")
    Call<TraillerCatalog> traillerCatalog(@Path("id") String id,
                                          @Query("api_key") String key,
                                          @Query("language") String language);*/
}
