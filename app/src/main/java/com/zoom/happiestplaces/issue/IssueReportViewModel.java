package com.zoom.happiestplaces.issue;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zoom.happiestplaces.model.Issue;
import com.zoom.happiestplaces.model.Topic;
import com.zoom.happiestplaces.repository.IssueRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class IssueReportViewModel extends AndroidViewModel {
    MutableLiveData<Topic> topicMutableLiveData=new MutableLiveData<>();
    MutableLiveData<Issue> issueResponseLiveData=new MutableLiveData<>();
    @Inject
    IssueRepository issueRepository;
    @Inject
    public IssueReportViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Topic> getTopics() {
        issueRepository.getTopics(topicMutableLiveData);
        return topicMutableLiveData;
    }

    public LiveData<Issue> sendIssue(Issue issue) {
        issueRepository.sendIssue(issue,issueResponseLiveData);
        return issueResponseLiveData;
    }

}