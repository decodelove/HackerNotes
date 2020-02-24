package com.logincore.hackernotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.logincore.hackernotes.utils.AppManager;
import com.logincore.hackernotes.utils.SysUtil;

public abstract class BaseActivity extends AppCompatActivity {

    public static UtilsDataHelper utilsDataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getRequestWindowFeature());
        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        setContentView(setContentViewLayout());
        AppManager.getInstance().addActivity(this);
        initView();

        utilsDataHelper = new UtilsDataHelper(this);
        initBeforeData();
    }

    protected abstract int getRequestWindowFeature();

    protected abstract void initBeforeData();

    protected abstract void initView();

    protected abstract int setContentViewLayout();
}
