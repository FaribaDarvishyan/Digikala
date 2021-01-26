package com.example.digikala.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.digikala.data.model.product.Product;
import com.example.digikala.data.remote.NetworkParams;
import com.example.digikala.data.remote.retrofit.RetrofitInstance;
import com.example.digikala.data.remote.retrofit.WooCommerceAPI;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {
    public static final String TAG = "Product Repository";
    private static ProductRepository sInstance;

    private final MutableLiveData<List<Product>> mAllProductsLiveData;
    private final MutableLiveData<List<Product>> mOnSaleProductsLiveData;
    private final MutableLiveData<List<Product>> mLatestProductsLiveData;
    private final MutableLiveData<List<Product>> mTopRatedProductsLiveData;
    private final MutableLiveData<List<Product>> mPopularProductsLiveData;

    private WooCommerceAPI mWooCommerceAPI;


    public static synchronized ProductRepository getInstance() {
        if (sInstance == null)
            sInstance = new ProductRepository();
        return sInstance;
    }

    private ProductRepository() {
        mWooCommerceAPI = RetrofitInstance.getInstance().create(WooCommerceAPI.class);

        mAllProductsLiveData = new MutableLiveData<>();
        mOnSaleProductsLiveData = new MutableLiveData<>();
        mLatestProductsLiveData = new MutableLiveData<>();
        mTopRatedProductsLiveData = new MutableLiveData<>();
        mPopularProductsLiveData = new MutableLiveData<>();

    }

    public MutableLiveData<List<Product>> getAllProductsLiveData() {
        return mAllProductsLiveData;
    }

    public MutableLiveData<List<Product>> getOnSaleProductsLiveData() {
        return mOnSaleProductsLiveData;
    }

    public MutableLiveData<List<Product>> getLatestProductsLiveData() {
        return mLatestProductsLiveData;
    }

    public MutableLiveData<List<Product>> getTopRatedProductsLiveData() {
        return mTopRatedProductsLiveData;
    }

    public MutableLiveData<List<Product>> getPopularProductsLiveData() {
        return mPopularProductsLiveData;
    }

    public void setAllProductsLiveData() {
        mWooCommerceAPI.getAllProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                mAllProductsLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });
    }
    public void setLatestProductsLiveData(){
        Log.d(TAG,"request for latest data");
        mWooCommerceAPI.getProducts(NetworkParams.getProducts(10, 1, "date"))                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        mLatestProductsLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {

                    }
                });
    }
    public void setPopularProductsLiveData() {
        mWooCommerceAPI.getProducts(NetworkParams.getProducts(10, 1, "popularity"))
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        mPopularProductsLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {

                    }
                });
    }
    public void setTopRatedProductsLiveData(){
        mWooCommerceAPI.getProducts(NetworkParams.getProducts(10,1,"rating"))
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        mTopRatedProductsLiveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {

                    }
                });
    }
}