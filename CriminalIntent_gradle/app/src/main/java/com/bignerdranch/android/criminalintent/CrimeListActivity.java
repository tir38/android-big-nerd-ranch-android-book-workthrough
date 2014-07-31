package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks{

	@Override
	protected Fragment createFragment() {
		return new CrimeListFragment();
	}


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detailFragmentContainer) == null) {
            // start CrimePagerActivity
            Intent intent = new Intent(this, CrimePagerActivity.class);
            intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getID());
            startActivity(intent);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


            Fragment oldFragment = fragmentManager.findFragmentById(R.id.detailFragmentContainer);
            Fragment newFragment = CrimeFragment.newInstance(crime.getID());

            if (oldFragment != null) {
                fragmentTransaction.remove(oldFragment);
            }

            fragmentTransaction.add(R.id.detailFragmentContainer, newFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        CrimeListFragment listFragment = (CrimeListFragment)fragmentManager.findFragmentById(R.id.fragmentContainer);
        listFragment.updateUI();
    }
}
