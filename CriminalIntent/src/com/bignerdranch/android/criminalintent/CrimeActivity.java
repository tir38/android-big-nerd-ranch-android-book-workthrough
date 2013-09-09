package com.bignerdranch.android.criminalintent;

import android.support.v4.app.Fragment;

//public class CrimeActivitiy extends FragmentActivity {

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);			// call super class method
//		setContentView(R.layout.activity_fragment);	// set view
//		
//		FragmentManager fragmentManager = getSupportFragmentManager();// get fragment manager
//		Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer); // get a fragment by its ID
//		
//		// if a fragment isn't declared, create a new one and add to fragManager
//		if (fragment == null){
//			fragment = new CrimeFragment();
//			fragmentManager.beginTransaction()				// this is a fragment transaction... creates a transaction
//				.add(R.id.fragmentContainer, fragment)		// ... specifies the action of the transaction...
//				.commit();									// ... then commits the transaction.
//		}
//	}
	
// NOTE: rewrite to use abstract SingleFragmentActivity class ...	
public class CrimeActivity extends SingleFragmentActivity{

	@Override
	protected Fragment createFragment() {
		return new CrimeFragment();
	}
}
