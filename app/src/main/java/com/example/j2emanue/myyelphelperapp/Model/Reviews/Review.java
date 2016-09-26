package com.example.j2emanue.myyelphelperapp.Model.Reviews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("time_created")
    @Expose
    private String timeCreated;

    /**
     *
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return
     *     The user
     */
    public User getUser() {
        return user;
    }

    /**
     *
     * @param user
     *     The user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     *
     * @return
     *     The rating
     */
    public Integer getRating() {
        return rating;
    }

    /**
     *
     * @param rating
     *     The rating
     */
    public void setRating(Integer rating) {
        this.rating = rating;
    }

    /**
     *
     * @return
     *     The text
     */
    public String getText() {
        return text;
    }

    /**
     *
     * @param text
     *     The text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @return
     *     The timeCreated
     */
    public String getTimeCreated() {
        return timeCreated;
    }

    /**
     *
     * @param timeCreated
     *     The time_created
     */
    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

}