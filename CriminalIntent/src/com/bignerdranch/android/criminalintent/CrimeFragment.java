package com.bignerdranch.android.criminalintent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState); // call super method
		mCrime = new Crime();				// instantiate new Crime
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
							 ViewGroup parent, 
							 Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.fragment_crime, parent, false); 	// inflate view
		
		// handle mTitleField

		mTitleField = (EditText)v.findViewById(R.id.crime_title);
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
		mDateButton.setText(mCrime.getDate().toString());
		mDateButton.setEnabled(false);
		
		// handle mSolvedCheckBox;
		mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
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
}
