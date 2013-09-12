package com.bignerdranch.android.criminalintent;
import java.util.Date;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

//REMEMBER: this is a controller: controlling the view of a single crime!

public class CrimeFragment extends Fragment {

	private Crime mCrime;
	private EditText mTitleField;
	private Button mDateButton;
	private CheckBox mSolvedCheckBox;
	public static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";
	public static final String DIALOG_DATE = "date";
	public static final int REQUEST_DATE = 0; 			// for target fragment
	private static final String TAG = "CriminalIntent";	// for debugging

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
	
	@Override	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState); // call super method
		
		mCrime = new Crime();				// instantiate new Crime
		
		// get crime ID from CrimeFragment's bundle
		UUID crimeID = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
		mCrime = CrimeLab.get(getActivity()).getCrime(crimeID);
	}
	
	@SuppressWarnings("static-access")
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
//		mDateButton.setText(DateFormat.format("EEEE, MMM dd, yyyy", mCrime.getDate()).toString()); // format display
		mDateButton.setOnClickListener(new View.OnClickListener() 
			{ // anonymous inner class
				public void onClick(View v){
					FragmentManager fragmentManager = getActivity()
														.getSupportFragmentManager(); 	// get fragManager of hosting activity
					DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate()); // create new DatePickerFragment using crime date
					dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
					dialog.show(fragmentManager, DIALOG_DATE); 							// show this fragment, and set its tag to the string contained in DIALOG_DATE
			
					// It might seem odd that we are setting the DatePickerFragment's Tag here, and not in DatePickerFragment.java.
					// But since we are instantiating DatePickerFragment here, we want to be able to set its tag here, too.
					// How could an instance of DatePickerFragment set its own tag anyways?
					
					// It might also seem odd that we are instantiating a DatePickerFragment here and not in the hosting activity.
					// But since we need a DatePickerFragment ONLY when we click on the date field, we create it here.
				}
			}
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
		
		return v;															// return view
	}

	// receive data from DatePickerFragment 
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode != Activity.RESULT_OK) return;
		if(requestCode == REQUEST_DATE) {
			Date date = (Date)data
								.getSerializableExtra(DatePickerFragment.EXTRA_DATE); // get date from intent
			mCrime.setDate(date); // set crime's date
//			mDateButton.setText(mCrime.getDate().toString()); // update date button's text TODO: replace with formatted date
			updateDateText(mCrime.getDate());
		}
	}

	public void updateDateText(Date date){
		mDateButton.setText(DateFormat.format("EEEE, MMM dd, yyyy", date).toString()); // format display
	}
	

}
