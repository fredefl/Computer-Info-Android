package dk.illution.computer.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class ComputerDetailActivity extends FragmentActivity {

	public Menu menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_computer_detail);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			arguments.putString(ComputerDetailFragment.ARG_ITEM_ID, getIntent()
					.getStringExtra(ComputerDetailFragment.ARG_ITEM_ID));
			ComputerDetailFragment fragment = new ComputerDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.computer_detail_container, fragment).commit();
		}
	}

	public void viewComputer (String id) {
		Bundle arguments = new Bundle();
		arguments.putString(ComputerDetailFragment.ARG_ITEM_ID, id);
		ComputerDetailFragment fragment = new ComputerDetailFragment();
		fragment.setArguments(arguments);
		getSupportFragmentManager().beginTransaction()
				.add(R.id.computer_detail_container, fragment).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_computer_detail, menu);
		Log.d("ComputerInfo", "Menu has been created");
		this.menu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			Intent parentActivityIntent = new Intent(this, ComputerListActivity.class);
            parentActivityIntent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(parentActivityIntent);
            finish();
            return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
