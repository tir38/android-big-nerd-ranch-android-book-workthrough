package com.bignerdranch.android.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;

import java.util.Date;
import java.util.UUID;

//REMEMBER: this is a controller: controlling the view of a single crime!

public class CrimeFragment extends Fragment {

    // ================== variables ========================================================================
    // extras
    public static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";

    // dialog and request values
    public static final String DIALOG_DATE = "date";
    public static final int REQUEST_DATE = 0;
    public static final String DIALOG_TIME = "time";
    public static final int REQUEST_TIME = 1;
    public static final int REQUEST_PHOTO = 2;
    public static final String DIALOG_IMAGE = "image";
    public static final String TAG = "CriminalIntent";    // for debugging

    // member variables
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    //================== methods ======================================================
    // instantiate a new CrimeFragment based on a crime's ID
    public static CrimeFragment newInstance(UUID crimeID) {
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // call super method

        mCrime = new Crime();                // instantiate new Crime

        // get stuff from bundle
        UUID crimeID = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);

        // set crime ID from CrimeFragment's bundle
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeID);

        // tell fragment manager this fragment has an options menu
        setHasOptionsMenu(true);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup parent,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_crime, parent, false);    // inflate view

        // --- handle mTitleField
        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle()); // set text of crime' title
        mTitleField.addTextChangedListener(new TextWatcher() { // begin anonymous inner class, with three class methods
            public void onTextChanged(CharSequence c,
                                      int start,
                                      int before,
                                      int count) {
                mCrime.setTitle(c.toString());            // convert charSeq to string; set crime's title to string
            }

            public void beforeTextChanged(CharSequence c,
                                          int start,
                                          int before,
                                          int count) {
                // purposely left blank
            }

            public void afterTextChanged(Editable c) {
                // purposely left blank
            }
        } // ends anonymous inner class
        );

        // --- handle mDateButton
        mDateButton = (Button) v.findViewById(R.id.crime_date);
        updateDateText(mCrime.getDate());
        mDateButton.setOnClickListener(new View.OnClickListener() { // anonymous inner class
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity()
                        .getSupportFragmentManager();    // get fragManager of hosting activity
                DatePickerFragment dateDialog = DatePickerFragment.newInstance(mCrime.getDate()); // create new DatePickerFragment using crime date
                dateDialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dateDialog.show(fragmentManager, DIALOG_DATE);                            // show this fragment, and set its tag to the string contained in DIALOG_DATE

                // It might seem odd that we are setting the DatePickerFragment's Tag here, and not in DatePickerFragment.java.
                // But since we are instantiating DatePickerFragment here, we want to be able to set its tag here, too.
                // How could an instance of DatePickerFragment set its own tag anyways?

                // It might also seem odd that we are instantiating a DatePickerFragment here and not in the hosting activity.
                // But since we need a DatePickerFragment ONLY when we click on the date field, we create it here.
            }
        }
        );

        // --- handle mTimeButton
        mTimeButton = (Button) v.findViewById(R.id.crime_time); // get view
        updateTimeText(mCrime.getDate());
        mTimeButton.setOnClickListener(new View.OnClickListener()   // set time button's onClickListener
        {// anonymous inner class

            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity()
                        .getSupportFragmentManager(); // get fragManager of the hosting activity
                TimePickerFragment timeDialog = TimePickerFragment.newInstance(mCrime.getDate()); // instantiate a TimePickerFragment
                timeDialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);     // set TimePickerFragment's target fragment
                timeDialog.show(fragmentManager, DIALOG_TIME);
            }
        }// end anonymous inner class
        );

        // --- handle mSolvedCheckBox;
        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() { // anonymous inner class for listener; define single method
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // set the crime's solved variable
                mCrime.setSolved(isChecked);
            }
        } // end anonymous inner class for listener
        );

        // --- if high enough API, and if parent activity set, then set home button as up button
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (NavUtils.getParentActivityIntent(getActivity()) != null) {
//				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        // --- handle context menu
        LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.crime_entire_view);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            registerForContextMenu(linearLayout);// register this view to respond to longpress and display floating context menu
        } else {
            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {  // set onLongClickListener to launch action bar context menu
                // anonymous inner class
                @Override
                public boolean onLongClick(View v) {

                    // create instance of ActionMode
                    getActivity().startActionMode(new ActionMode.Callback() {
                        // double anonymous inner class
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            // inflate action-mode menu
                            MenuInflater inflater = mode.getMenuInflater();
                            inflater.inflate(R.menu.crime_context, menu);
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            // required, but not used
                            return false;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
                            // TODO DRY this up: can I just call onContextItemSelected(.) ?
                            switch (menuItem.getItemId()) {
                                case (R.id.menu_delete_crime):// if selected delete
                                    // get crimelab singleton, call delete on this crime
                                    CrimeLab.get(getActivity()).deleteCrime(mCrime);

                                    // navigate back up to CrimeListFragment
                                    if (NavUtils.getParentActivityIntent(getActivity()) != null) { // if parent activity is set ...
                                        NavUtils.navigateUpFromSameTask(getActivity()); // ... navigate up to parent
                                    }

                                    return true;

                                default:
                                    return false;
                            }
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {
                            // required, but not used
                        }
                    });

                    return false;
                }
            });

            // remember: unlike CrimeListFragment, I don't need to use multiple selection mode; I am only trying to delete ONE crime
        }

        // --- handle photo image button
        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_photo_button);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            // anonymous inner class
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CrimeCameraActivity.class);// start crime camera activity with intent
                startActivityForResult(intent, REQUEST_PHOTO);
            }
        });

        // if no camera is present on device, disable button
        PackageManager packageManager = getActivity().getPackageManager();
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) &&              // if has no Front AND no rear camera....
                !packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            Log.d(TAG, "front AND rear cameras not found");
            mPhotoButton.setEnabled(false);                                             // ... disable button
        }

        // ----- handle photo image view
        mPhotoView = (ImageView) v.findViewById(R.id.crime_imageView);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Photo photo = mCrime.getPhoto();
                if (photo == null) {
                    return;
                }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                String path = getActivity().getFileStreamPath(photo.getFilename()).getAbsolutePath();
                ImageFragment.newInstance(path).show(fragmentManager, DIALOG_IMAGE);
            }
        });

        return v;   // return view
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:

                // if parent activity is set, navigate up to parent
                if (NavUtils.getParentActivityIntent(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }

                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    // receive data from DatePickerFragment, TimePickerFragment, or CrimeCameraFragment
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;

        // if coming from DatePicker
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE); // get date from intent
            mCrime.setDate(date); // set crime's date
            updateDateText(mCrime.getDate());
        }

        // if coming from TimePicker
        if (requestCode == REQUEST_TIME) {
            Date date = (Date) data.
                    getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setDate(date);
            updateTimeText(mCrime.getDate());
        }

        // if coming from CrimeCameraFragment
        if (requestCode == REQUEST_PHOTO) {
            // create new Photo object and attach it to the Crime object
            String filename = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
            if (filename != null) {
                Photo photo = new Photo(filename);
                mCrime.setPhoto(photo);
                showPhoto();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();            // call to super method
        CrimeLab.get(getActivity()) // get crimelab singleton
                .saveCrimes();    // save crimes
    }

    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
    }

    @Override
    public void onStop() {
        super.onStop();
        PictureUtils.cleanImageView(mPhotoView);
    }

    // ------- context menu handlers -------
    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View view,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        getActivity()
                .getMenuInflater()
                .inflate(R.menu.crime_context, menu); // inflate menu
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case (R.id.menu_delete_crime):// if selected delete
                // get crimelab singleton, call delete on this crime
                CrimeLab.get(getActivity()).deleteCrime(mCrime);

                // navigate back up to CrimeListFragment
                if (NavUtils.getParentActivityIntent(getActivity()) != null) { // if parent activity is set ...
                    NavUtils.navigateUpFromSameTask(getActivity()); // ... navigate up to parent
                }

                return true;

            default:
                return super.onContextItemSelected(menuItem);
        }
    }

    // ------- helper methods -------
    public void updateDateText(Date date) {
        // decide to display "Today" or formatted date
        if (android.text.format.DateUtils.isToday(date.getTime())) {
            mDateButton.setText("Today");
        } else {
            mDateButton.setText(DateFormat.format("EEEE, MMM dd, yyyy", date).toString()); // format display text
        }
    }

    public void updateTimeText(Date date) {
        mTimeButton.setText(DateFormat.format("hh:mm aa", date).toString()); // format display text
    }

    private void showPhoto() {
        // (re)set the image button's image based on our photo
        Photo photo = mCrime.getPhoto();
        BitmapDrawable bitmapDrawable = null;
        if (photo != null) {
            String path = getActivity().getFileStreamPath(photo.getFilename()).getAbsolutePath();
            bitmapDrawable = PictureUtils.getScaledDrawable(getActivity(), path);
        }

        mPhotoView.setImageDrawable(bitmapDrawable);
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);

        return report;
    }
}
