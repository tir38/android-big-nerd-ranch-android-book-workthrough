package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

import java.util.Calendar;
import java.util.Date;


public class DatePickerFragment extends DialogFragment {
	private Date mDate;
	private int mHour;
	private int mMinute;
	private int mDay;
	private int mMonth;
	private int mYear;

	public static final String EXTRA_DATE = "com.bignerdranch.android.criminalintent.date";
	private static final String TAG = "CriminalIntent";	// for debugging

	public static DatePickerFragment newInstance(Date date){
		// create a bundle for this instance and add an argument
		Bundle arguments = new Bundle();
		arguments.putSerializable(EXTRA_DATE, date);

		// create a new DatePickerFragment and set its bundle
		DatePickerFragment datePickerFragment = new DatePickerFragment();
		datePickerFragment.setArguments(arguments);
		
		return datePickerFragment;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		// this is called by the fragment manager of the host activity
		
		// remember: you have to inflate a view before you can set it w/ setView(.)
		View view = getActivity().getLayoutInflater()
				.inflate(R.layout.dialog_date, null);

		// get date from bundle
		mDate = (Date)getArguments().getSerializable(EXTRA_DATE);
		
		burstDate(); // split date into components
		
		// create DatePicker widget
		DatePicker datePicker = (DatePicker)view.findViewById(R.id.dialog_date_datePicker);
		datePicker.init(mYear, mMonth, mDay, new OnDateChangedListener() // this listener updates mDate and date argument EVERY time the date changes (i.e. as user scroll through day, month, or year)
			{ // anonymous inner class
				public void onDateChanged(DatePicker view, int year, int month, int day){					
					mDay = day;
					mMonth = month;
					mYear = year;
					rebuildDate();

					// update argument to preserved selected value on rotation
					getArguments().putSerializable(EXTRA_DATE, mDate);
				}
			}// end anonymous inner class
		);
		
		// handle AlertDialog
		return new AlertDialog.Builder(getActivity())				// construct AlertDialog from host activity
				.setView(view) 										// set its view
				.setTitle(R.string.date_picker_title)				// set its title
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

	// return results back to target fragment
	private void sendResult(int resultCode){
		if (getTargetFragment() == null){
			return;
		}
		// create new intent; add date as extra
		Intent intent = new Intent();
		intent.putExtra(EXTRA_DATE, mDate);
		
		// pass intent back to target fragment (i.e. CrimeFragment)
		getTargetFragment()
			.onActivityResult(getTargetRequestCode(), resultCode, intent); // SUPER IMPORTANT: you are calling the target fragment's onActivityResult method
																			// i.e. CrimeFragment.onActivityResult()
																			// also, getTargetRequestCode() will return the unique Request code that the TargetFragment assigned THIS fragment
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