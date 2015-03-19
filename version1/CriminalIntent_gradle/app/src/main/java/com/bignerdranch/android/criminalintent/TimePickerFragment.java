package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import java.util.Calendar;
import java.util.Date;


public class TimePickerFragment extends DialogFragment {
	private Date mDate;
	private int mHour;
	private int mMinute;
	private int mDay;
	private int mMonth;
	private int mYear;
	
	public static final String EXTRA_TIME = "com.bignerdranch.android.criminalIntent.time";
	private static final String TAG = "CriminalIntent";	// for debugging
	
	public static TimePickerFragment newInstance(Date date){
		Bundle arguments = new Bundle(); 				// create bundle for this fragment
		arguments.putSerializable(EXTRA_TIME, date); 	// add time argument to bundle
	
		TimePickerFragment timePickerFragment = new TimePickerFragment();
		timePickerFragment.setArguments(arguments);
		
		return timePickerFragment;
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState){
		// this is called by the fragment manager of the host activity
		
		// inflate the dialog view
		View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_time, null);
		
		// get date from bundle
		mDate = (Date)getArguments().getSerializable(EXTRA_TIME); // get from bundle

		burstDate(); // split date into components
		
		// create TimePicker widget, set its change listener
		TimePicker timePicker = (TimePicker)view.findViewById(R.id.dialog_date_datePicker);		
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener()
			{// anonymous inner class
				public void onTimeChanged(TimePicker view, int hourOfDay, int minute){
					// update both the dialog and the TimePickerFragment's member variables
					// WTF: this is automatically preserving across rotations, why?
					
					// set hour/minute from alert dialog
					mHour = hourOfDay;
					mMinute = minute;
					rebuildDate();
					
					// update argument to preserved selected value on rotation
					getArguments().putSerializable(EXTRA_TIME, mDate);
				}
			}
		);
		
		// handle AlertDialog
		return new AlertDialog.Builder(getActivity())				// construct AlertDialog from host activity
				.setView(view) 										// set its view
				.setTitle(R.string.time_picker_title)				// set its title
				.setPositiveButton(									// set ok button
						android.R.string.ok,							// set onclick listener for ok button
						new DialogInterface.OnClickListener()
							{ // anonymous inner class
								public void onClick(DialogInterface dialog, int which){
									sendResult(Activity.RESULT_OK);						 	// send result back to target fragment
								}
							}
						)
				.create();											// create the whole thing
	}
	
	// send result back to target fragment
	private void sendResult(int resultCode){
		// check if target fragment is set
		if (getTargetFragment() == null){
			return;
		}
		
		// create new intent and add time as extra
		Intent intent = new Intent();
		intent.putExtra(EXTRA_TIME, mDate);

		// send intent back to target fragment
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
	}
	
	private void burstDate(){
		// burst date into year/month/day/hour/minute, using Calendar object
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		mHour = calendar.get(Calendar.HOUR_OF_DAY);
		mMinute = calendar.get(Calendar.MINUTE);
		mDay = calendar.get(Calendar.DAY_OF_MONTH);
		mMonth = calendar.get(Calendar.MONTH);
		mYear = calendar.get(Calendar.YEAR);
	}
	
	private void rebuildDate(){
		Calendar calender = Calendar.getInstance();
		calender.set(mYear, mMonth, mDay, mHour, mMinute, 0);
		mDate = calender.getTime(); // this is a poorly named method; it is getting the date+time, basically it is just converting Calender -> Date
	}
}


