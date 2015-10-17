package com.vbstudio.twittersearch.dom.MovieResponse;

import com.vbstudio.twittersearch.Constants.ConstantKeys;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by vaibhav on 18/10/15.
 */
public class MovieBasicResponseImageData {

    private String imageUrl;
    private int width;
    private int height;

    public static MovieBasicResponseImageData convertResponseToObject(JSONObject response) {
        MovieBasicResponseImageData basicResponseImageData = new MovieBasicResponseImageData();
        JSONArray imageDataFromServer = response.optJSONArray(ConstantKeys.MOVIE_RESPONSE_BASIC_IMAGE_DETAILS);

        if (imageDataFromServer != null) {
            basicResponseImageData.setImageUrl(imageDataFromServer.optString(0));
            basicResponseImageData.setWidth(imageDataFromServer.optInt(1));
            basicResponseImageData.setHeight(imageDataFromServer.optInt(2));
        }
        return basicResponseImageData;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
