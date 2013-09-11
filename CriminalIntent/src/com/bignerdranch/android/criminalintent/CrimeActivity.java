package com.bignerdranch.android.criminalintent;

import java.util.UUID;

import android.support.v4.app.Fragment;
	
// NOTE: rewrite to use abstract SingleFragmentActivity class ...	
public class CrimeActivity extends SingleFragmentActivity{

	@Override
	protected Fragment createFragment() {
//		return new CrimeFragment(); 
		// no longer instantiating CrimeFragment instances here..
		// ... they are now instantiated in CrimeFragment.newInstance();
		
		// The activity still gets the intent from CrimeListFragment
		// which has the crime ID as an argument
		// then it creates a fragment of its own.
		// but it does this by letting CrimeFragment do "its own work" by calling the static method ...
		// CrimeFragment.newInstance()
		UUID crimeId = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		return CrimeFragment.newInstance(crimeId);
	}
}
