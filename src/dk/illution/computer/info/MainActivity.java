package dk.illution.computer.info;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

public class MainActivity extends FragmentActivity implements ActionBar.OnNavigationListener {

	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		String computers_json = "{}";
		try {
			Resources res = getResources();
			InputStream in_s = res.openRawResource(R.raw.computers);

			byte[] b = new byte[in_s.available()];
			in_s.read(b);
			computers_json = new String(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject computers_data = null;

		try {
			computers_data = new JSONObject(computers_json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Log.d("count", computers_data.getString("count"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JSONArray computers_array = computers_data.getJSONArray("Computers");
			for (int i = 0; i <= computers_array.length(); i++) {
				JSONObject computer = computers_array.getJSONObject(i);
				computers.add(computer.getString("identifier"));
				List<String> computerList = new ArrayList<String>();

				computerList.add("Ip");
				computerList.add(computer.getString("ip"));
				computerList.add("LAN MAC");
				computerList.add(computer.getString("lan_mac"));

				singleComputer.add(computerList);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Set up the action bar.
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

		ExpandableListView list = (ExpandableListView) findViewById(R.id.computerList);
		SimpleExpandableListAdapter expListAdapter =
				new SimpleExpandableListAdapter(
					this,
					createGroupList(),	// groupData describes the first-level entries
					R.layout.base_group_row,	// Layout for the first-level entries
					new String[] { "name" },	// Key in the groupData maps to display
					new int[] { R.id.groupname },		// Data under "colorName" key goes into this TextView
					createChildList(),	// childData describes second-level entries
					R.layout.computer_child_row,	// Layout for second-level entries
					new String[] { "title", "value" },	// Keys in childData maps to display
					new int[] { R.id.title, R.id.value }	// Data under the keys above go into these TextViews
				);
		list.setAdapter( expListAdapter );
	}

	Random rand = new Random();

	public int getRandomNumber (int min, int max) {
		return rand.nextInt(max - min + 1) + min;
	}

	public List<String> computers = new ArrayList<String>();

	public List<List<String>> singleComputer = new ArrayList<List<String>>();

	public List<HashMap<String, String>> createGroupList() {
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		for(String name : computers) {
			HashMap<String, String> m = new HashMap<String, String>();
			m.put("name", name);
			result.add(m);
		}
		return (List<HashMap<String, String>>) result;
	}

	public List<ArrayList<HashMap<String, String>>> createChildList() {
		ArrayList<ArrayList<HashMap<String, String>>> result = new ArrayList<ArrayList<HashMap<String, String>>>();
		for(List<String> computer : singleComputer) {
	// Second-level lists
		ArrayList<HashMap<String, String>> secList = new ArrayList<HashMap<String, String>>();
		for(int n = 0 ; n < computer.size() ; n += 2) {
			HashMap<String, String> child = new HashMap<String, String>();
			child.put( "title", computer.get(n) );
			child.put( "value", computer.get(n+1) );
			secList.add( child );
		}
		result.add( secList );
		}
		return result;
	}


	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
				getActionBar().getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public boolean onNavigationItemSelected(int position, long id) {
		// When the given tab is selected, show the tab contents in the container
		Fragment fragment = new DummySectionFragment();
		Bundle args = new Bundle();
		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
		fragment.setArguments(args);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment)
				.commit();
		return true;
	}


	/**
	 * A dummy fragment representing a section of the app, but that simply displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		public DummySectionFragment() {
		}

		public static final String ARG_SECTION_NUMBER = "section_number";
	}
}
