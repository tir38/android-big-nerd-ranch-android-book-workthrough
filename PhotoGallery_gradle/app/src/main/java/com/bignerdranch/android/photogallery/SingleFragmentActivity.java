package com.bignerdranch.android.photogallery;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

// REMEMBER: this is an abstract class for any activity with a single fragment
public abstract class SingleFragmentActivity extends FragmentActivity {
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);			// call super class method
        setContentView(R.layout.activity_fragment);

        FragmentManager fragmentManager = getSupportFragmentManager();// get fragment manager
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer); // get a fragment by its ID

        // if a fragment isn't declared, create a new one and add to fragManager
        if (fragment == null){
            fragment = createFragment();
            fragmentManager.beginTransaction()				// this is a fragment transaction... creates a transaction
                    .add(R.id.fragmentContainer, fragment)		// ... specifies the action of the transaction...
                    .commit();									// ... then commits the transaction.
        }
    }
}