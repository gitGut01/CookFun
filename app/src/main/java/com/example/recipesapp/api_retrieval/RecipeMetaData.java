package com.example.recipesapp.api_retrieval;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipeMetaData implements Parcelable {

    @SerializedName("publisher")
    @Expose
    private String publisher;
    @SerializedName("f2f_url")
    @Expose
    private String f2fUrl;
    @SerializedName("ingredients")
    @Expose
    private List<String> ingredients = null;
    @SerializedName("source_url")
    @Expose
    private String sourceUrl;
    @SerializedName("recipe_id")
    @Expose
    private String recipeId;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("social_rank")
    @Expose
    private double socialRank;
    @SerializedName("publisher_url")
    @Expose
    private String publisherUrl;
    @SerializedName("title")
    @Expose
    private String title;


    private int score;

    /**
     * No args constructor for use in serialization
     *
     */
    public RecipeMetaData() {
    }

    /**
     *
     * @param ingredients
     * @param socialRank
     * @param title
     * @param imageUrl
     * @param recipeId
     * @param sourceUrl
     * @param f2fUrl
     * @param publisherUrl
     * @param publisher
     */
    public RecipeMetaData(String publisher, String f2fUrl, List<String> ingredients, String sourceUrl, String recipeId, String imageUrl, double socialRank, String publisherUrl, String title) {
        super();
        this.publisher = publisher;
        this.f2fUrl = f2fUrl;
        this.ingredients = ingredients;
        this.sourceUrl = sourceUrl;
        this.recipeId = recipeId;
        this.imageUrl = imageUrl;
        this.socialRank = socialRank;
        this.publisherUrl = publisherUrl;
        this.title = title;
    }

    protected RecipeMetaData(Parcel in) {
        publisher = in.readString();
        f2fUrl = in.readString();
        ingredients = in.createStringArrayList();
        sourceUrl = in.readString();
        recipeId = in.readString();
        imageUrl = in.readString();
        socialRank = in.readDouble();
        publisherUrl = in.readString();
        title = in.readString();

        score = in.readInt();
    }

    public static final Creator<RecipeMetaData> CREATOR = new Creator<RecipeMetaData>() {
        @Override
        public RecipeMetaData createFromParcel(Parcel in) {
            return new RecipeMetaData(in);
        }

        @Override
        public RecipeMetaData[] newArray(int size) {
            return new RecipeMetaData[size];
        }
    };

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getF2fUrl() {
        return f2fUrl;
    }

    public void setF2fUrl(String f2fUrl) {
        this.f2fUrl = f2fUrl;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getSocialRank() {
        return socialRank;
    }

    public void setSocialRank(double socialRank) {
        this.socialRank = socialRank;
    }

    public String getPublisherUrl() {
        return publisherUrl;
    }

    public void setPublisherUrl(String publisherUrl) {
        this.publisherUrl = publisherUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }




    //Score for the lvl system
    public int getScore() {
        return score;
    }
    public void setScore(int i) {
        this.score = i;
    }





    //We make the received data parcelable
    //So we can save it and restore when change the screen orientation

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(publisher);
        dest.writeString(f2fUrl);
        dest.writeStringList(ingredients);
        dest.writeString(sourceUrl);
        dest.writeString(recipeId);
        dest.writeString(imageUrl);
        dest.writeDouble(socialRank);
        dest.writeString(publisherUrl);
        dest.writeString(title);

        dest.writeInt(score);
    }
}