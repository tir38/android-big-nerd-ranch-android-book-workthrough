package com.bignerdranch.android.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;


// REMEMBER: this is a singleton
// ... just define constructor and get method that returns instance

public class CrimeLab {
	private static CrimeLab sCrimeLab;
	private Context mAppContext;
	private ArrayList<Crime> mCrimes;
	private static final String TAG = "CriminalIntent";	

	// constructor
	private CrimeLab(Context appContext){ 
		mAppContext = appContext;
		mCrimes = new ArrayList<Crime>();
	}

	// get crimeLab singleton
	public static CrimeLab get(Context c){
		// if crime lab instance doesn't *yet* exist...
		if (sCrimeLab == null){
			sCrimeLab = new CrimeLab(c.getApplicationContext()); // .. create 
			// note: the singleton is available application-wide because we pass it the application context, not an activity context
		}
		return sCrimeLab; // return
	}

	// return all crimes as arraylist
	public ArrayList<Crime> getCrimes() {
		return mCrimes;
	}	
	
	// return single crime, based on UUID
	public Crime getCrime(UUID id){
		for (int i = 0; i < mCrimes.size(); i++){
//			if (mCrimes.get(i).getID() == id){ // WHY WON"T THIS WORK???
			if (mCrimes.get(i).getID().equals(id)){
				return mCrimes.get(i);
			}
		}
		// if you get here, crime was not found
		Log.w(TAG, "crime not found!");
		return null;
	}
	
	// add a new Crime to array list
	public void  addCrime(Crime crime){
		Log.d(TAG, "adding crime");
		mCrimes.add(crime);
	}
	
}
