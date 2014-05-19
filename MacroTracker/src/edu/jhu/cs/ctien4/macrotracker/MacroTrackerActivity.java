package edu.jhu.cs.ctien4.macrotracker;

import java.util.Locale;

import edu.jhu.cs.ctien4.macrotracker.AddFoodFragment;
import edu.jhu.cs.ctien4.macrotracker.DeleteFoodFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MacroTrackerActivity extends ActionBarActivity implements
		ActionBar.TabListener, AddFoodFragment.onDatabaseUpdateListener, 
		DeleteFoodFragment.onDatabaseUpdateListener, FavoriteFoodFragment.onDatabaseUpdateListener {

	private static StatusFragment statusFragment;
	private static AddFoodFragment addFoodFragment;
	private static GraphFragment graphFragment;
	private static DeleteFoodFragment deleteFoodFragment;
	private static FavoriteFoodFragment favoriteFoodFragment;
	
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_macro_tracker);

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		// Setup DB
		LoggerDB loggerDB = new LoggerDB(this);
		
		
		// Set up Fragments
		statusFragment = new StatusFragment();
		addFoodFragment = new AddFoodFragment();
		graphFragment = new GraphFragment();
		deleteFoodFragment = new DeleteFoodFragment();
		favoriteFoodFragment = new FavoriteFoodFragment();
		
		// Pass a database reference to fragments
		statusFragment.setLoggerDb(loggerDB);
		addFoodFragment.setLoggerDb(loggerDB);
		graphFragment.setLoggerDb(loggerDB);
		deleteFoodFragment.setLoggerDB(loggerDB);
		favoriteFoodFragment.setLoggerDB(loggerDB);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.macro_tracker, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}
	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {			
			switch(position) {
			case 0:
				Log.d("debug", "RETURNING STATUS FRAGMENT");
				return statusFragment;
			case 1:
				return addFoodFragment;
			case 2:
				return deleteFoodFragment;
			case 3:
				return favoriteFoodFragment;
			case 4:
				return graphFragment;
			}
			
			return null;
		}

		@Override
		public int getCount() {
			// Show 4 total pages.
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.status_section).toUpperCase(l);
			case 1:
				return getString(R.string.food_section).toUpperCase(l);
			case 2:
				return getString(R.string.deletefood_section).toUpperCase(l);
			case 3:
				return getString(R.string.favoritefood_section).toUpperCase(l);
			case 4:
				return getString(R.string.graph_section).toUpperCase(l);
			}
			return null;
		}
	}

    /**
     * This is the universal callback function that will be called whenever we have written
     * to the database. Any child fragments that need to be updated will be updated here.
     */
	@Override
	public void onDatabaseUpdate() {
		statusFragment.updateCalorieField();
		statusFragment.updateFoodList();
		deleteFoodFragment.updateFoodList();
		favoriteFoodFragment.updateFoodList();
		graphFragment.genGraph();
	}
}
