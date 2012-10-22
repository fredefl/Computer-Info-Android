package dk.illution.computer.info;

import android.app.ActionBar;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class ComputerListActivity extends FragmentActivity implements
		ComputerListFragment.Callbacks {

	private boolean mTwoPane;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_computer_list);
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		getWindow().setWindowAnimations(0);
		
		try {
			ComputerInfo.mainDatabase.selectCredential("token");
		} catch (SQLiteException e) {
			ComputerInfo.launchLoginSelect(this);
		}

		if (findViewById(R.id.computer_detail_container) != null) {
			mTwoPane = true;
			((ComputerListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.computer_list))
					.setActivateOnItemClick(true);
		}

		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		ComputerInfo.addDeviceSpinner(actionBar, this, "computers");
	}

	public void onItemSelected(String id) {
		if (mTwoPane) {
			Bundle arguments = new Bundle();
			arguments.putString(ComputerDetailFragment.ARG_ITEM_ID, id);
			ComputerDetailFragment fragment = new ComputerDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.computer_detail_container, fragment).commit();

		} else {
			Intent detailIntent = new Intent(this, ComputerDetailActivity.class);
			detailIntent.putExtra(ComputerDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_preferences:
				ComputerInfo.launchPreferences(this);
				return true;
			case R.id.menu_logout:
				MainDatabase database = new MainDatabase(this.getApplicationContext());
				database.deleteCredential("token");
				ComputerInfo.launchLoginSelect(ComputerListActivity.this);
				this.finish();
				return true;
			case R.id.menu_about:
				ComputerInfo.launchAbout(this);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
