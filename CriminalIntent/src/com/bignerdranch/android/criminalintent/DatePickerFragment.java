package com.bignerdranch.android.criminalintent;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;


public class DatePickerFragment extends DialogFragment {
	public static final String EXTRA_DATE = "com.bignerdranch.android.criminalintent.date";
	private Date mDate;
	
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
		
		// create calendar got get day, month, year from Date
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		// create DatePicker widget
		DatePicker datePicker = (DatePicker)view.findViewById(R.id.dialog_date_datePicker);
		datePicker.init(year, month, day, new OnDateChangedListener() // this listener updates mDate and date argument EVERY time the date changes (i.e. as user scroll through day, month, or year)
			{ // anonymous inner class
				public void onDateChanged(DatePicker view, int year, int month, int day){
					// translate year, month, day into Date object
					mDate = new GregorianCalendar(year, month, day).getTime();
					
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

	// return results back to CrimeFragment
	private void sendResult(int resultCode){
		if (getTargetFragment() == null){
			return;
		}
		// create new intent; add date as extra
		Intent i = new Intent();
		i.putExtra(EXTRA_DATE, mDate);
		
		// pass intent back to target fragment (i.e. CrimeFragment)
		getTargetFragment()
			.onActivityResult(getTargetRequestCode(), resultCode, i);
	}
	
}
