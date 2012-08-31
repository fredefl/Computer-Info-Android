package dk.illution.computer.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
		for (int i = 0; i <= 100; i++) {
			computers.add("BUF " + Integer.toString(getRandomNumber(10000,99999)) + "U");
		}
		
		ExpandableListView list = (ExpandableListView) findViewById(R.id.computerList);
		SimpleExpandableListAdapter expListAdapter =
				new SimpleExpandableListAdapter(
					this,
					createGroupList(),	// groupData describes the first-level entries
					R.layout.base_group_row,	// Layout for the first-level entries
					new String[] { "colorName" },	// Key in the groupData maps to display
					new int[] { R.id.groupname },		// Data under "colorName" key goes into this TextView
					createChildList(),	// childData describes second-level entries
					R.layout.computer_child_row,	// Layout for second-level entries
					new String[] { "shadeName", "rgb" },	// Keys in childData maps to display
					new int[] { R.id.childname, R.id.rgb }	// Data under the keys above go into these TextViews
				);
		list.setAdapter( expListAdapter );
	}
	Random rand = new Random();
	public int getRandomNumber (int min, int max) {
		return rand.nextInt(max - min + 1) + min;
	}

		public List<String> computers = new ArrayList<String>();

		final String shades[][] = {
	// Shades of grey
		{
			"lightgrey","#D3D3D3",
			"dimgray","#696969",
			"sgi gray 92","#EAEAEA"
		},
	// Shades of blue
		{
			"dodgerblue 2","#1C86EE",
			"steelblue 2","#5CACEE",
			"powderblue","#B0E0E6"
		},
	// Shades of yellow
		{
			"yellow 1","#FFFF00",
			"gold 1","#FFD700",
			"darkgoldenrod 1","	#FFB90F"
		},
	// Shades of red
		{
			"indianred 1","#FF6A6A",
			"firebrick 1","#FF3030",
			"maroon","#800000"
		}
		};

	/**
	 * Creates the group list out of the colors[] array according to
	 * the structure required by SimpleExpandableListAdapter. The resulting
	 * List contains Maps. Each Map contains one entry with key "colorName" and
	 * value of an entry in the colors[] array.
	 */
		private List<HashMap<String, String>> createGroupList() {
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		for(String name : computers) {
			HashMap<String, String> m = new HashMap<String, String>();
			m.put("colorName", name);
			result.add(m); 
		}
		return (List<HashMap<String, String>>) result;
		}

	/**
	 * Creates the child list out of the shades[] array according to the
	 * structure required by SimpleExpandableListAdapter. The resulting List
	 * contains one list for each group. Each such second-level group contains
	 * Maps. Each such Map contains two keys: "shadeName" is the name of the
	 * shade and "rgb" is the RGB value for the shade.
	 */
	private List<ArrayList<HashMap<String, String>>> createChildList() {
		ArrayList<ArrayList<HashMap<String, String>>> result = new ArrayList<ArrayList<HashMap<String, String>>>();
		for( int i = 0 ; i < shades.length ; ++i ) {
	// Second-level lists
		ArrayList<HashMap<String, String>> secList = new ArrayList<HashMap<String, String>>();
		for( int n = 0 ; n < shades[i].length ; n += 2 ) {
			HashMap<String, String> child = new HashMap<String, String>();
			child.put( "shadeName", shades[i][n] );
			child.put( "rgb", shades[i][n+1] );
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

		//@Override
		/*
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			Bundle args = getArguments();
			textView.setText(Integer.toString(args.getInt(ARG_SECTION_NUMBER)));
			return textView;
		}*/
	}
}
