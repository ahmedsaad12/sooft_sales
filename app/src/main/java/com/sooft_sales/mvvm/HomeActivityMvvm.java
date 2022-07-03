package com.sooft_sales.mvvm;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.disposables.CompositeDisposable;

public class HomeActivityMvvm extends AndroidViewModel {
    private Context context;

    private CompositeDisposable disposable = new CompositeDisposable();

    public HomeActivityMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
