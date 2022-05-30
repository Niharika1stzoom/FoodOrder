package com.zoom.happiestplaces.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.zoom.happiestplaces.model.Issue;
import com.zoom.happiestplaces.model.RateData;
import com.zoom.happiestplaces.model.Topic;
import com.zoom.happiestplaces.network.RestaurantApi;
import com.zoom.happiestplaces.util.AppConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IssueRepository {
    private RestaurantApi mApiInterface;;
    public IssueRepository(RestaurantApi apiInterface) {
        mApiInterface=apiInterface;
    }

    public void getTopics(MutableLiveData<Topic> liveData) {
        Call<Topic> call = mApiInterface.getTopics();
        call.enqueue(new Callback<Topic>() {
            @Override
            public void onResponse(@NonNull Call<Topic> call,
                                   @NonNull Response<Topic> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());

                } else {
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Topic> call, @NonNull Throwable t) {
                Log.d(AppConstants.TAG, "Topics fetching issue" + t.getLocalizedMessage());
                liveData.postValue(null);
            }
        });
    }

    public void sendIssue(Issue issue, MutableLiveData<Issue> liveData) {
        Call<Issue> call = mApiInterface.sendIssue(issue);
        call.enqueue(new Callback<Issue>() {
            @Override
            public void onResponse(@NonNull Call<Issue> call,
                                   @NonNull Response<Issue> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());

                } else {
                    liveData.postValue(null);
                    Log.d(AppConstants.TAG, "Issue Posting issue response" + response.message());
                }
            }
            @Override
            public void onFailure(@NonNull Call<Issue> call, @NonNull Throwable t) {
                Log.d(AppConstants.TAG, "Issue Posting issue" + t.getLocalizedMessage());
                liveData.postValue(null);
            }
        });
    }
}
