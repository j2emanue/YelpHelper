package com.example.j2emanue.myyelphelperapp.Services;

import com.example.j2emanue.myyelphelperapp.Model.Business.BusinessesModel;
import com.example.j2emanue.myyelphelperapp.Model.Reviews.Reviews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by j2emanue on 9/23/16.
 */

public interface YelpRestaurantService {


    @GET("v3/businesses/search")
    Call<BusinessesModel> getBusinesses(
            @Query("term") String term,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude);

    @GET("v3/businesses/{id}/reviews")
    Observable<Reviews> getReviews(
            @Path("id") String businessID);

    /* todo: i should have used this to get the photos as it has more then the reviews class
    @GET("v3/businesses/{id}")
    Observable<BusinessDetails> getBusinessDetails(
            @Path("id") String businessID);
*/


}
