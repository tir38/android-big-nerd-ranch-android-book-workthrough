package com.bignerdranch.android.criminalintent;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import java.util.ArrayList;

public class CrimeListFragment extends ListFragment {

    // ================= variables ====================
	// member variables
	private boolean mIsSubtitleVisible;
	private ArrayList<Crime> mCrimes;
	private ImageButton mAddNewCrimeButon;
	
	// extra string(s)
	public static final String EXTRA_IS_SUBTITLE_VISIBLE = "com.bignerdranch.android.criminalintent.is_subtitle_visible";

	// request code(s)
	private static final int crimeFragmentRequestCode = 0;
	
	// for debugging
	private static final String TAG = "CriminalIntent";	

    // ============= methods =========================
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
		
		// tell FragManager to call onCreateOptionsMenu()
		setHasOptionsMenu(true);
		
		// tell fragment manager to retain this instance (across rotation)
		setRetainInstance(true);
		mIsSubtitleVisible = false; // initialize to false
	}
	
	@TargetApi(16)
	@Override
	public View onCreateView(LayoutInflater inflater,
								ViewGroup parent,
								Bundle onSavedInstanceState){
//		View view = super.onCreateView(inflater, parent, onSavedInstanceState); // get the view of parent activity
		View view = inflater.inflate(R.layout.fragment_crime_list, parent, false);
		
		// if right SDK and is turned on, then display action bar subtitle
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
			if(mIsSubtitleVisible){
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
			}
		}
		
		// set the empty view's button action to start a new crime
		mAddNewCrimeButon = (ImageButton)view.findViewById(R.id.new_crime_button);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
			mAddNewCrimeButon.setBackground(null); // make backgroud transparent if SDK OK
		}
		mAddNewCrimeButon.setOnClickListener(new OnClickListener() 
			{ // anonymous inner class
				@Override
				public void onClick(View v) {
					startNewCrime(); 
				}
			}
		);

        // setup Context menu
        ListView listView = (ListView)view.findViewById(android.R.id.list);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
            // use floating context menus for pre-honeycomb versions
            registerForContextMenu(listView);
        }
        else{
            // use context action bar for honeycomb+ versions
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener( new AbsListView.MultiChoiceModeListener() {
                // anonymous inner class

                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                    // required, but not used
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    // inflate action-mode menu
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.crime_list_item_context, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                    // required, but not used
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    // switch based on what item is clicked
                    switch(item.getItemId()){
                        case R.id.menu_item_delete_crime: // if delete selected
                            CrimeAdapter adapter = (CrimeAdapter)getListAdapter();  // get list's adapter
                            CrimeLab crimeLab = CrimeLab.get(getActivity());        // get crimelab singleton
                            for(int i = adapter.getCount()-1; i >= 0; i--){         // for each crime (checked or not)
                                // work backwards so that index is preserved at each delete
                                if(getListView().isItemChecked(i)){
                                    crimeLab.deleteCrime(adapter.getItem(i));
                                }
                            }
                            mode.finish();
                            adapter.notifyDataSetChanged();
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
        }

		return view;
	}
						
	
	@Override
	public void onListItemClick(ListView l,
                                View v,
                                int position,
                                long id){
		Crime crime = ((CrimeAdapter)getListAdapter()).getItem(position); // get a crime using my custom adapter
		
		// create intent and start a CrimePagerActivity
		Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
		intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getID());
		startActivity(intent);
	}

	// update list any time the fragment resumes when returning from CrimeActivity
	@Override
	public void onResume(){
		super.onResume(); // call super class method
		((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	// ------ handle options menu ------
    @Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater); 				// call super class
		inflater.inflate(R.menu.fragment_crime_list, menu);		// inflate and populate menu

		MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);// change menu item text

		if(mIsSubtitleVisible && showSubtitle.getTitle() != null){ // if turned on AND currently showing title, display "hide"
			showSubtitle.setTitle(R.string.hide_subtitle);
		}
		// I don't need an "else" here because the default is to set subtitle as "show"
	}
	
	@TargetApi(11)
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem){
		switch (menuItem.getItemId()){
			case R.id.menu_item_new_crime:
//				Crime crime = new Crime();												// instantiate new crime
//				CrimeLab.get(getActivity()).addCrime(crime); 							// get crimelab singleton and add crime
//				Intent intent = new Intent(getActivity(), CrimePagerActivity.class); 	// start crime pager activity for new (blank) crime (which starts CrimeFragment)
//				intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getID());			// pass CrimeFragment which crime ID to display
//				startActivityForResult(intent, crimeFragmentRequestCode); 				// pass crimeFragment its results code
				startNewCrime();
				return true;
				
			case R.id.menu_item_show_subtitle:
				// check if subtitle is already displayed, show or don't show
				if(getActivity().getActionBar().getSubtitle() == null){				// if not displayed
					getActivity().getActionBar().setSubtitle(R.string.subtitle);	// display subtitle
					menuItem.setTitle(R.string.hide_subtitle);						// change menu item title to "hide subtitle"
					mIsSubtitleVisible = true;										// update member variable
				}
				else{																// else	
					getActivity().getActionBar().setSubtitle(null);					// remove subtitle
					menuItem.setTitle(R.string.show_subtitle); 						// change menu item title to "show subtitle"
					mIsSubtitleVisible = false;										// update member variable
					//TODO: this doesn't get preserved across rotation
				}
				return true;
				
			default:
				return super.onOptionsItemSelected(menuItem);
		}
	}

    // ------ handle context menu ------
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuItem.getMenuInfo(); // get context menu info
        int position = info.position; // get position in list
        CrimeAdapter adapter = (CrimeAdapter)getListAdapter(); // create new crime adapter, remember this is inner class
        Crime crime = adapter.getItem(position); // get crime from list

        // do specific task depending on which context item is selected
        switch (menuItem.getItemId()){
            case R.id.menu_item_delete_crime: // if delete selected
                CrimeLab.get(getActivity())       // get crimelab singleton
                        .deleteCrime(crime);        // and delete crime
                adapter.notifyDataSetChanged();
                return true;
        }

        Log.e(TAG, "Error: crime delete failed");
        return super.onContextItemSelected(menuItem); // TODO: what is this doing?
    }

	// there are several ways to trigger the add a new crime steps, so put them all together
	private void startNewCrime(){
		Crime crime = new Crime();												// instantiate new crime
		CrimeLab.get(getActivity()).addCrime(crime); 							// get crimelab singleton and add crime
		Intent intent = new Intent(getActivity(), CrimePagerActivity.class); 	// start crime pager activity for new (blank) crime (which starts CrimeFragment)
		intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getID());			// pass CrimeFragment which crime ID to display
		startActivityForResult(intent, crimeFragmentRequestCode); 				// pass crimeFragment its results code
	}
	
	// =================== inner class to handle Crime adapter =================== 
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
			dateTextView.setText(DateFormat.format("EEEE, MMM, dd, yyyy, hh:mm aa", crime.getDate()).toString()); 
			
			// handle solvedCheckBox
			CheckBox solvedCheckBox = (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
			solvedCheckBox.setChecked(crime.isSolved());
			
			return convertView;
		}
	} // end inner class
}
