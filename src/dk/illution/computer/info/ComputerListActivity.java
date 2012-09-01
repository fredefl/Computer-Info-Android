package dk.illution.computer.info;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;

public class ComputerListActivity extends FragmentActivity
		implements ComputerListFragment.Callbacks, ActionBar.OnNavigationListener {

	private boolean mTwoPane;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_computer_list);

		if (findViewById(R.id.computer_detail_container) != null) {
			mTwoPane = true;
			((ComputerListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.computer_list))
					.setActivateOnItemClick(true);
		}

		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
				// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(
						actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1,
						new String[]{
								getString(R.string.computers),
								getString(R.string.printers),
								getString(R.string.units),
						}),
				this);
	}

	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			Bundle arguments = new Bundle();
			arguments.putString(ComputerDetailFragment.ARG_ITEM_ID, id);
			ComputerDetailFragment fragment = new ComputerDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.computer_detail_container, fragment)
					.commit();

		} else {
			Intent detailIntent = new Intent(this, ComputerDetailActivity.class);
			detailIntent.putExtra(ComputerDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}

	public boolean onNavigationItemSelected(int position, long id) {
		switch (position) {
			case 0:
				getActionBar().setLogo(R.drawable.computer);
				break;
			case 1:
				getActionBar().setLogo(R.drawable.printer);
				break;
			case 2:
				getActionBar().setLogo(R.drawable.camera);
				break;

		}
		return true;
	}
}
