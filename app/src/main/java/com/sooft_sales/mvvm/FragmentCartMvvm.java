package com.sooft_sales.mvvm;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sooft_sales.remote.Api;
import com.sooft_sales.tags.Tags;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentCartMvvm extends AndroidViewModel {
    private static final String TAG = "FragmentHomeMvvm";
    private Context context;

    private CompositeDisposable disposable = new CompositeDisposable();



    public FragmentCartMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }








    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
