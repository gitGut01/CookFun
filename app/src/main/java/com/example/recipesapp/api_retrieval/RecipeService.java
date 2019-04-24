package com.example.recipesapp.api_retrieval;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeService {

    //https://www.food2fork.com/api/get?key=5008128790b6a519bde84c27cf72c491&rId=35382

    @GET("api/get?")

    Call<Recipe> getAnswers(@Query("key") String s, @Query("rId") int id);


}
