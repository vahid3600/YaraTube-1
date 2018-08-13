package com.example.asus.yaratube.data.remote;

import com.example.asus.yaratube.data.model.Category;
import com.example.asus.yaratube.data.model.Store;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

import static com.example.asus.yaratube.util.Util.STORE_ID;

public interface ApiService {

    // get dashboard list
    @GET("/store/" + STORE_ID)
    Call<Store> getDashboard();

    // get category list
    @GET("/category/" + STORE_ID + "/463")
    Call<List<Category>> getCategories();

}
