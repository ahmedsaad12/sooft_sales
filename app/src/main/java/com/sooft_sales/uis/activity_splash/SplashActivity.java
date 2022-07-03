package com.sooft_sales.uis.activity_splash;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.sooft_sales.R;
import com.sooft_sales.databinding.ActivitySplashBinding;
import com.sooft_sales.model.SettingModel;
import com.sooft_sales.preferences.Preferences;
import com.sooft_sales.uis.activity_base.BaseActivity;
import com.sooft_sales.uis.activity_home.HomeActivity;
import com.sooft_sales.uis.activity_login.LoginActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends BaseActivity {
    private ActivitySplashBinding binding;
    private CompositeDisposable disposable = new CompositeDisposable();
    private Animation animation;

    private Preferences preferences;
    private SettingModel settingDataModel;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        initView();

    }

    private void initView() {
        preferences = Preferences.getInstance();
        if (preferences.getUserAdditionalSettings(this) != null) {
            settingDataModel = preferences.getUserAdditionalSettings(this);
        }
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        Observable.timer(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        animateMethod();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void animateMethod() {
        animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.lanuch);


        binding.cons.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (getUserModel() == null) {
                    navigateToLoginActivity();
                } else {
                    if (settingDataModel != null && settingDataModel.getEnd_at().equals(dateFormat.format(new Date(System.currentTimeMillis())))) {
                        binding.logo.setVisibility(View.GONE);
                        binding.tv.setVisibility(View.VISIBLE);
                    } else {
                        navigateToHomeActivity();
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    private void navigateToLoginActivity() {
        Intent intent;


        intent = new Intent(this, LoginActivity.class);

        startActivity(intent);
        finish();
    }


    private void navigateToHomeActivity() {
        Intent intent;


        intent = new Intent(this, HomeActivity.class);

        startActivity(intent);
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}