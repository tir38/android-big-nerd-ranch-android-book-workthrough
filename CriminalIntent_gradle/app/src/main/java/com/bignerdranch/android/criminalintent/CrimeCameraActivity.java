package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created with IntelliJ IDEA.
 * User: jason
 * Create Date: 9/27/13
 */
public class CrimeCameraActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){

        return new CrimeCameraFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){

        requestWindowFeature(Window.FEATURE_NO_TITLE);                      // hide title window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);   // hid status bar and other OS-level chrome
        // these need to be set BEFORE view is created (in super class)....

        super.onCreate(savedInstanceState); // call super class method
    }
}