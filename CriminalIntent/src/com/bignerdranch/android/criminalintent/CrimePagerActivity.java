package com.bignerdranch.android.criminalintent;
import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class CrimePagerActivity extends FragmentActivity {
	
	private ViewPager mViewPager;
	private ArrayList<Crime> mCrimes;
	private static final String TAG = "CriminalIntent";	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState); // call to superclass
		mViewPager = new ViewPager(this); // set context = this activity
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		
		mCrimes = CrimeLab.get(this).getCrimes(); // get all crimes
		
		//WTF_isGoingOnHere: unlike CrimeListActivity' there is no ONE fragment for this view.
		// Ultimately we will create several fragments, each for a separate crime.
		// This makes sense because in the list we had a bunch of views at once on the screen. 
		// In the pager we have a bunch of "single" views, each "ready" to be displayed on the screen, one at a time.
		// So the differences here are:
		// 1. The adapter is an anonymous inner class, not just an inner class
		// 2. The adapter is creating ViewPager objects, not View objects
		// 3. The adapter is being defined here in the Activity, not in the Fragment
		// 4. We are using a FragmentStatePagerAdapter, not "manually" adding fragments with .beginTransaction(), .add(), and .commit()
		// 5. getItem() is returning a Fragment (to be added to activity), not using a getView() to return a view to be displayed
		FragmentManager fragmentManager = getSupportFragmentManager(); // get this activity's FragManager
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) 
			{ // anonymous inner class with two methods
				@Override
				public int getCount(){
					return mCrimes.size();
				}
				
				@Override
				public Fragment getItem(int position){
					Crime crime = mCrimes.get(position); // get a single crime
					return CrimeFragment.newInstance(crime.getID()); // create fragment for that crime
				}
			} // end anonymous inner class
		);
				
		// get the ID of the crime that was clicked to start this PagerActivity
		UUID currentID = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID); // get crime ID from the intent of this activity, which came from CrimeListFragment
		for( int i = 0; i < mCrimes.size(); i++){
			if(mCrimes.get(i).getID().equals(currentID)){
				mViewPager.setCurrentItem(i); // set view for current crime
				updateActivityTitle(i); // update activity title onCreate (not just when swiping between crimes)
				break;
			}
		}
		
		// implement ViewPager's OnPageChangeListener interface (to display crime title in action/title bar)
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			// anonymous inner class
			@Override
			public void onPageSelected(int position) {
				updateActivityTitle(position);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {} // do nothing
			
			@Override
			public void onPageScrollStateChanged(int arg0) {} // do nothing
		}); // finish anonymous inner class
	} // onCreate()
	
	public void updateActivityTitle(int position){
		Crime crime = mCrimes.get(position); // get crime
		if(crime.getTitle() != null){		// if title not null
			setTitle(crime.getTitle());			// set activity's title as crime's title
		}
	}
}
