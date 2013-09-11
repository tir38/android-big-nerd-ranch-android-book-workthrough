package com.bignerdranch.android.criminalintent;

import java.util.ArrayList;

import android.content.Intent;
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
	private static final String TAG = "CriminalIntent";	

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
		Crime crime = ((CrimeAdapter)getListAdapter()).getItem(position); // get a crime using my custom adapter
		
		// create intent and start a CrimePagerActivity
		Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
		intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getID());
		startActivity(intent);
	}

	// override onResume to update list any time the fragment resumes...
	// ... when returning from CrimeActivity
	@Override
	public void onResume(){
		super.onResume(); // call super class method
		((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	// inner class to handle Crime adapter
	// WTF_isGoingOnHere: this adapter juggles an ArrayList to provide views for each item in the list.
	// The fragment can't do this itself; The Adapter is especially good at the repetitive task
	// of creating a view for each item in the list.
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
			Crime crime = getItem(position);// get crime from arraylist using adapter
		
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
