package com.r.appinesstest.ui;

import android.app.Application;

import com.r.appinesstest.repo.RepositoryMain;
import com.r.appinesstest.repo.model.ResponseModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {
    private LiveData<List<ResponseModel>> responseListObservable;

    public MainViewModel(@NonNull Application application) {
        super(application);
        responseListObservable = RepositoryMain.getInstance().getListApi();
    }

    public LiveData<List<ResponseModel>> getResponseListObservable() {
        return responseListObservable;
    }

}
