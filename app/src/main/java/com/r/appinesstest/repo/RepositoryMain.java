package com.r.appinesstest.repo;

import android.util.Log;

import com.r.appinesstest.repo.model.ResponseModel;
import com.r.appinesstest.repo.networking.ApiInterface;
import com.r.appinesstest.repo.networking.RetrofitService;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class RepositoryMain {
    private static final String TAG = "TaskRepository";

    private RepositoryMain() {
    }

    public static RepositoryMain getInstance() {
        return InstanceHelper.INSTANCE;
    }

    public LiveData<List<ResponseModel>> getListApi() {
        final MutableLiveData<List<ResponseModel>> taskList = new MutableLiveData<>();
        Observable<Response<List<ResponseModel>>> observable;
        observable = RetrofitService.getInstance().builder().create(ApiInterface.class).getDataFromApi();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<ResponseModel>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(Response<List<ResponseModel>> listResponse) {
                        taskList.setValue(listResponse.body());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });
        return taskList;
    }

    private static class InstanceHelper {
        private static RepositoryMain INSTANCE = new RepositoryMain();
    }
}
