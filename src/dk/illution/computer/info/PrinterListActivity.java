package dk.illution.computer.info;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class PrinterListActivity extends FragmentActivity implements
		PrinterListFragment.Callbacks, OnNavigationListener {

	private boolean mTwoPane;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_printer_list);
		getWindow().setWindowAnimations(0);
		

		if (findViewById(R.id.printer_detail_container) != null) {
			mTwoPane = true;
			((PrinterListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.printer_list))
					.setActivateOnItemClick(true);
		}
		
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		ComputerInfo.addDeviceSpinner(actionBar, this, "printers");
	}

	public void onItemSelected(String id) {
		if (mTwoPane) {
			Bundle arguments = new Bundle();
			arguments.putString(PrinterDetailFragment.ARG_ITEM_ID, id);
			PrinterDetailFragment fragment = new PrinterDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.printer_detail_container, fragment).commit();

		} else {
			Intent detailIntent = new Intent(this, PrinterDetailActivity.class);
			detailIntent.putExtra(PrinterDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}

	public boolean onNavigationItemSelected(int arg0, long arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
