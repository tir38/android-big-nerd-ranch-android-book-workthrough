package com.bignerdranch.android.criminalintent;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class CrimeListFragment extends ListFragment {

	private ArrayList<Crime> mCrimes;
	private static final String TAG = "CrimeListFragment";	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState); // call super-class method
		getActivity().setTitle(R.string.crimes_title);
		
		mCrimes = CrimeLab.get(getActivity()).getCrimes(); // get list of crimes
		// this works by getting the singleton (i.e. the single instance of CrimeLab)...
		// ... and then getting that instance's crimeList (i.e. the ArrayList<Crime>)
		
		// declare our custom adapter
		CrimeAdapter adapter = new CrimeAdapter(mCrimes);
		setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		Crime c = ((CrimeAdapter)getListAdapter()).getItem(position); // get a crime using my custom adapter
		Log.d(TAG, c.getTitle() + " was called.");
	}
	
	// inner class to handle Crime adapter
	private class CrimeAdapter extends ArrayAdapter<Crime>{
		
		public CrimeAdapter(ArrayList<Crime> crimes){ // constructor
			super(getActivity(), 0, crimes);
		}
		
		// get view for single crime
		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			// if we aren't given a convertView, inflate one
			if (convertView == null){
				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_crime, null);
			}
			
			// configure view for specific crime
			Crime crime = getItem(position);// get crime from arraylist
//			Crime crime = mCrimes.get(position); // DON'T USE THIS. Instead rely on the adapter to handle interactions with the model. Thats what its there for!
		
			// handle titleTextView
			TextView titleTextView = (TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
			titleTextView.setText(crime.getTitle());
			
			// handle dateTextView
			TextView dateTextView = (TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
			dateTextView.setText(crime.getDate().toString()); // consider using formatter from CH8 challenge !!!!!!!
			
			// handle solvedCheckBox
			CheckBox solvedCheckBox = (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
			solvedCheckBox.setChecked(crime.isSolved());
			
			return convertView;
		}
	} // end inner class
}
