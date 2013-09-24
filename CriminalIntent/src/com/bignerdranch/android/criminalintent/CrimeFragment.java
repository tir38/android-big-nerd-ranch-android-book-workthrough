package com.bignerdranch.android.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

//REMEMBER: this is a controller: controlling the view of a single crime!

public class CrimeFragment extends Fragment {

	// ================== variables ========================================================================
	// member variables
	private Crime mCrime;
	private EditText mTitleField;
	private Button mDateButton;
	private Button mTimeButton;
	private CheckBox mSolvedCheckBox;
	
	// extras
	public static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";
	
	// dialog and request values
	public static final String DIALOG_DATE = "date";
	public static final int REQUEST_DATE = 0;
	
	public static final String DIALOG_TIME = "time";
	public static final int REQUEST_TIME = 1;
	
	private static final String TAG = "CriminalIntent";	// for debugging

	//================== methods ======================================================
	// instantiate a new CrimeFragment based on a crime's ID
	public static CrimeFragment newInstance(UUID crimeID){
		// create a bundle for this instance
		Bundle arguments = new Bundle();
		arguments.putSerializable(EXTRA_CRIME_ID, crimeID);

		// create a new CrimeFragment and set its bundle
		CrimeFragment crimeFragment = new CrimeFragment();
		crimeFragment.setArguments(arguments);
		
		return crimeFragment;
	}
	
	// ------- on state change methods -------
	@Override	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState); // call super method
		
		mCrime = new Crime();				// instantiate new Crime
		
		// get stuff from bundle
		UUID crimeID 		= (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);

		// set crime ID from CrimeFragment's bundle
		mCrime = CrimeLab.get(getActivity()).getCrime(crimeID);

		// tell fragment manager this fragment has an options menu
		setHasOptionsMenu(true);
	}
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, 
							 ViewGroup parent, 
							 Bundle savedInstanceState){
		
		View v = inflater.inflate(R.layout.fragment_crime, parent, false); 	// inflate view
		
		// handle mTitleField
		mTitleField = (EditText)v.findViewById(R.id.crime_title);		
		mTitleField.setText(mCrime.getTitle()); // set text of crime' title
		mTitleField.addTextChangedListener(new TextWatcher() 
			{ // begin anonymous inner class, with three class methods
				public void onTextChanged(CharSequence c,
											int start,
											int before,
											int count){
					mCrime.setTitle(c.toString());			// convert charSeq to string; set crime's title to string
				}
				
				public void beforeTextChanged(CharSequence c,
												int start,
												int before,
												int count){
					// purposely left blank
				}
	
				public void afterTextChanged(Editable c){
					// purposely left blank
				}
			} // ends anonymous inner class
		);
		
		// handle mDateButton
		mDateButton = (Button)v.findViewById(R.id.crime_date);
		updateDateText(mCrime.getDate());
		mDateButton.setOnClickListener(new View.OnClickListener() 
			{ // anonymous inner class
				public void onClick(View v){
					FragmentManager fragmentManager = getActivity()
														.getSupportFragmentManager(); 	// get fragManager of hosting activity
					DatePickerFragment dateDialog = DatePickerFragment.newInstance(mCrime.getDate()); // create new DatePickerFragment using crime date
					dateDialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
					dateDialog.show(fragmentManager, DIALOG_DATE); 							// show this fragment, and set its tag to the string contained in DIALOG_DATE
			
					// It might seem odd that we are setting the DatePickerFragment's Tag here, and not in DatePickerFragment.java.
					// But since we are instantiating DatePickerFragment here, we want to be able to set its tag here, too.
					// How could an instance of DatePickerFragment set its own tag anyways?
					
					// It might also seem odd that we are instantiating a DatePickerFragment here and not in the hosting activity.
					// But since we need a DatePickerFragment ONLY when we click on the date field, we create it here.
				}
			}
		);
		
		// handle mTimeButton
		mTimeButton = (Button)v.findViewById(R.id.crime_time); // get view
		updateTimeText(mCrime.getDate());
		mTimeButton.setOnClickListener(new View.OnClickListener()   // set time button's onClickListener
			{// anonymous inner class
			
				@Override
				public void onClick(View v) {
					FragmentManager fragmentManager = getActivity()
														.getSupportFragmentManager(); // get fragManager of the hosting activity
					TimePickerFragment timeDialog = TimePickerFragment.newInstance(mCrime.getDate()); // instantiate a TimePickerFragment
					timeDialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME); 	 // set TimePickerFragment's target fragment
					timeDialog.show(fragmentManager, DIALOG_TIME);
				}
			}// end anonymous inner class
		);
		
		// handle mSolvedCheckBox;
		mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
		mSolvedCheckBox.setChecked(mCrime.isSolved());
		mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
			{ // anonymous inner class for listener; define single method
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
					// set the crime's solved variable
					mCrime.setSolved(isChecked);
				}
			} // end anonymous inner class for listener
		);
		
		// if high enough API, and if parent activity set, then set home button as up button
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){ 
			if(NavUtils.getParentActivityIntent(getActivity()) != null){
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		
		return v;															// return view
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem){
		switch( menuItem.getItemId()){
			case android.R.id.home:

				// if parent activity is set, navigate up to parent
				if(NavUtils.getParentActivityIntent(getActivity()) != null){
					NavUtils.navigateUpFromSameTask(getActivity());
				}
				
				return true;
				
			default:
				return super.onOptionsItemSelected(menuItem);
		}
	}
	
	// receive data from DatePickerFragment and TimePickerFragment
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode != Activity.RESULT_OK) return;
		
		// if coming from DatePicker
		if(requestCode == REQUEST_DATE) {
			Date date = (Date)data
								.getSerializableExtra(DatePickerFragment.EXTRA_DATE); // get date from intent
			mCrime.setDate(date); // set crime's date
			updateDateText(mCrime.getDate());
		}
		
		// if coming from TimePicker
		if(requestCode == REQUEST_TIME){
			Date date = (Date)data.
								getSerializableExtra(TimePickerFragment.EXTRA_TIME);
			mCrime.setDate(date);
			updateTimeText(mCrime.getDate());
		}
	}
	
	@Override
	public void onPause(){
		super.onPause(); 			// call to super method
		CrimeLab.get(getActivity()) // get crimelab singleton
					.saveCrimes(); 	// save crimes
	}
	
	// ------- helper methods -------
	public void updateDateText(Date date){
		// decide to display "Today" or formatted date
		if(android.text.format.DateUtils.isToday(date.getTime())){
			mDateButton.setText("Today");
		}
		else{
			mDateButton.setText(DateFormat.format("EEEE, MMM dd, yyyy", date).toString()); // format display text
		}
	}
	
	public void updateTimeText(Date date){
		mTimeButton.setText(DateFormat.format("hh:mm aa", date).toString()); // format display text
	}

}
