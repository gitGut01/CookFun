package com.example.recipesapp.api_retrieval;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recipe {

    @SerializedName("recipe")
    @Expose
    private RecipeMetaData recipe;

    /**
     * No args constructor for use in serialization
     *
     */
    public Recipe() {
    }

    /**
     *
     * @param recipe
     */
    public Recipe(RecipeMetaData recipe) {
        super();
        this.recipe = recipe;
    }

    public RecipeMetaData getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeMetaData recipe) {
        this.recipe = recipe;
    }

}