package com.example.j2emanue.myyelphelperapp.Model.Reviews;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeParcelable(this.user, flags);
        dest.writeValue(this.rating);
        dest.writeString(this.text);
        dest.writeString(this.timeCreated);
    }

    public Review() {
    }

    protected Review(Parcel in) {
        this.url = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.rating = (Integer) in.readValue(Integer.class.getClassLoader());
        this.text = in.readString();
        this.timeCreated = in.readString();
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}