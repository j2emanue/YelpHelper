package com.example.j2emanue.myyelphelperapp.Model.Business;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BusinessesModel implements Parcelable {

    private static final BusinessesModel INSTANCE = new BusinessesModel();

    @SerializedName("businesses")
    @Expose
    private List<Business> businesses = new ArrayList<Business>();
    @SerializedName("total")
    @Expose
    private Integer total;



    private BusinessesModel(){}//lock the constructor of our model class



    public static BusinessesModel getInstance() {
        return BusinessesModel.INSTANCE;
    }

    /**
     *
     * @return
     *     The businesses
     */
    public List<Business> getBusinesses() {
        return businesses;
    }

    /**
     *
     * @param businesses
     *     The businesses
     */
    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }

    /**
     *
     * @return
     *     The total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     *
     * @param total
     *     The total
     */
    public void setTotal(Integer total) {
        this.total = total;
    }


    protected BusinessesModel(Parcel in) {
        if (in.readByte() == 0x01) {
            businesses = new ArrayList<Business>();
            in.readList(businesses, Business.class.getClassLoader());
        } else {
            businesses = null;
        }
        total = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (businesses == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(businesses);
        }
        if (total == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(total);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BusinessesModel> CREATOR = new Parcelable.Creator<BusinessesModel>() {
        @Override
        public BusinessesModel createFromParcel(Parcel in) {
            return new BusinessesModel(in);
        }

        @Override
        public BusinessesModel[] newArray(int size) {
            return new BusinessesModel[size];
        }
    };
}