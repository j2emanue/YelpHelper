package com.example.j2emanue.myyelphelperapp.Model.Business;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location implements Parcelable {

    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("address2")
    @Expose
    private Object address2;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("zip_code")
    @Expose
    private String zipCode;
    @SerializedName("address3")
    @Expose
    private String address3;
    @SerializedName("address1")
    @Expose
    private String address1;
    @SerializedName("state")
    @Expose
    private String state;

    /**
     *
     * @return
     *     The city
     */
    public String getCity() {
        return city;
    }

    /**
     *
     * @param city
     *     The city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     *
     * @return
     *     The address2
     */
    public Object getAddress2() {
        return address2;
    }

    /**
     *
     * @param address2
     *     The address2
     */
    public void setAddress2(Object address2) {
        this.address2 = address2;
    }

    /**
     *
     * @return
     *     The country
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country
     *     The country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     *
     * @return
     *     The zipCode
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     *
     * @param zipCode
     *     The zip_code
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     *
     * @return
     *     The address3
     */
    public String getAddress3() {
        return address3;
    }

    /**
     *
     * @param address3
     *     The address3
     */
    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    /**
     *
     * @return
     *     The address1
     */
    public String getAddress1() {
        return address1;
    }

    /**
     *
     * @param address1
     *     The address1
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /**
     *
     * @return
     *     The state
     */
    public String getState() {
        return state;
    }

    /**
     *
     * @param state
     *     The state
     */
    public void setState(String state) {
        this.state = state;
    }


    protected Location(Parcel in) {
        city = in.readString();
        address2 = (Object) in.readValue(Object.class.getClassLoader());
        country = in.readString();
        zipCode = in.readString();
        address3 = in.readString();
        address1 = in.readString();
        state = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeValue(address2);
        dest.writeString(country);
        dest.writeString(zipCode);
        dest.writeString(address3);
        dest.writeString(address1);
        dest.writeString(state);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}