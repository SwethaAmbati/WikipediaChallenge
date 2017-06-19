package org.wikipedia.rest;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by SwethaAmbati on 6/15/17.
 */

public interface ApiService {


    @GET("w/api.php")
    Call<JsonObject> getWikiResponse(@Query("action") String query, @Query("prop") String pageimages,
                                     @Query("format") String json, @Query("piprop") String thumbnail,
                                     @Query("pithumbsize") int pithumbsize, @Query("pilimit") int pilimit,
                                     @Query("generator") String prefixsearch, @Query("gpssearch") String gpssearch,
                                     @Query("gpslimit") int gpslimit);


}

