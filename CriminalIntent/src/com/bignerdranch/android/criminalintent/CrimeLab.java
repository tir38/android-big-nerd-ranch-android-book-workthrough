package com.bignerdranch.android.criminalintent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONException;

import android.content.Context;
import android.util.Log;


// REMEMBER: this is a singleton
// ... just define constructor and get method that returns instance

public class CrimeLab {
	// ==================== variables =================
	// member and static variables
	private static CrimeLab sCrimeLab;
	private Context mAppContext;
	private ArrayList<Crime> mCrimes;
	private CriminalIntentJSONSerializer mSerializer;
	
	// string constants
	private static final String TAG = "CriminalIntent";	
	private static final String FILENAME = "crimes.json";

	
	// ==================== methods =================
	// constructor
	private CrimeLab(Context appContext){ 
		mAppContext = appContext;				
		mSerializer = new CriminalIntentJSONSerializer(mAppContext, FILENAME); // define serializer
		// two approaches, different outcome:	
		// By using if/else, I risk that if mCrimes is not empty, it will try to load file. If load file then fails, I am left with nothing.
		// It won't then "go back" and create me an empty ArrayList.
		// However by using try/catch I guarantee that if load fails, I always get a new empty ArrayList.

//		// if mCrimes is empty
//		if (mCrimes.isEmpty()){
//			mCrimes = new ArrayList<Crime>();
//		}
//		else{
//			mCrimes = mSerializer.loadCrimes();
//		}
		
		try{
			mCrimes = mSerializer.loadCrimes();
		}
		catch(Exception e){
			mCrimes = new ArrayList<Crime>();
		}
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
		mCrimes.add(crime);
	}
	
	// save a crime to JSON serializer
	public boolean saveCrimes(){
		try{
			mSerializer.saveCrimes(mCrimes); // save crimes to serializer
			return true;
		}
		catch(Exception e){								// if try throws an error message,
			Log.e(TAG, "Error saving crimes: ", e);			// log message
			return false;				
		}
	}
	
	
	// build 
}
