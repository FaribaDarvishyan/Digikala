package com.example.OnlineMarket.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.OnlineMarket.data.model.customer.Customer;
import com.example.OnlineMarket.data.repository.CustomerRepository;
import com.example.OnlineMarket.view.fragment.ProfileFragment;
import com.example.OnlineMarket.utils.QueryPreferences;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileViewModel extends AndroidViewModel {
    private CustomerRepository mRepository;
    private MutableLiveData<Customer> mCustomerLiveData;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        mRepository = CustomerRepository.getCustomer();
        mCustomerLiveData = new MutableLiveData<>();

    }

    public LiveData<Customer> getCustomerLiveData() {
        return mCustomerLiveData;
    }

    public void setCustomerLiveData(String email) {
        mRepository.setCustomerLiveData(email)
                .enqueue(new Callback<List<Customer>>() {
        @Override
        public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
//                        Log.d(ProfileFragment.TAG, "onResponse: size " + response.body().size());
//                        Log.d(ProfileFragment.TAG, "onResponse: email " + response.body().get(0).getEmail());
            if (response.body() != null && response.body().size() > 0)
                mCustomerLiveData.setValue(response.body().get(0));
            else
                registerCustomer(email);
        }
                    public void registerCustomer(String email) {
                        Customer customer = new Customer();
                        customer.setEmail(email);
                        mRepository.registerCustomer(customer)
                                .enqueue(new Callback<Customer>() {
                                    @Override
                                    public void onResponse(Call<Customer> call, Response<Customer> response) {
                                        Log.d(ProfileFragment.TAG, "onResponse: Register Customer " + response.isSuccessful());
                                        Log.d(ProfileFragment.TAG, "onResponse: Register Customer " + response.message());
                                        if (response.isSuccessful())
                                            setCustomerLiveData(email);
                                    }

                                    @Override
                                    public void onFailure(Call<Customer> call, Throwable t) {

                                    }
                                });
                    }

        @Override
        public void onFailure(Call<List<Customer>> call, Throwable t) {

        }
    });
    }

    public boolean isCustomerReady() {

        return QueryPreferences.getCustomerEmail(getApplication()) == null ? false : true;
//        return mCustomerLiveData.getValue() == null ? false : true;
    }
    public void signOut() {
        mCustomerLiveData.setValue(null);
        QueryPreferences.setCustomerId(getApplication(), -1);
    }
}
