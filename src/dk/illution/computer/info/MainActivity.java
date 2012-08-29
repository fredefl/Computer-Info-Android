package dk.illution.computer.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.content.Context;
//import android.app.FragmentTransaction;
//import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
//import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

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

		ExpandableListView mEntries = (ExpandableListView) findViewById(R.id.computerList);
		ExpandableListAdapter adapter = new MyExpandableListAdapter(this);
		mEntries.setAdapter(adapter);
	}

	public class MyExpandableListAdapter extends BaseExpandableListAdapter {
		private String[] groups = {"BUF323542U", "UUF534256U"};
		private String[][] children = {
			{"Memory", "Processor"},
			{"In", "Ruh"}
		};

		private Context cxt;

		public MyExpandableListAdapter(Context cxt) {
			this.cxt = cxt;
		}

		@Override
		public Object getChild(int groupPos, int childPos) {
			return children[groupPos][childPos];
		}

		@Override
		public long getChildId(int groupPos, int childPos) {
			return childPos;
		}

		@Override
		public View getChildView(int groupPos, int childPos,
				boolean isLastChild, View convertView, ViewGroup parent) {
			TextView tv = getGenericView();
			tv.setText(getChild(groupPos, childPos).toString());
			return tv;
		}

		@Override
		public int getChildrenCount(int groupPos) {
			return children[groupPos].length;
		}

		@Override
		public Object getGroup(int groupPos) {
			return groups[groupPos];
		}

		@Override
		public int getGroupCount() {
			return groups.length;
		}

		@Override
		public long getGroupId(int groupPos) {
			return groupPos;
		}

		public TextView getGenericView() {
			// Layout parameters for the ExpandableListView
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, 64);

			TextView tv = new TextView(this.cxt);
			tv.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
			tv.setLayoutParams(lp);
			tv.setTextAppearance(this.cxt, android.R.attr.textAppearanceMedium);
			// Center the text vertically
			tv.setGravity(Gravity.CENTER_VERTICAL);
			// Set the text starting position
			tv.setPadding(70, 0, 0, 0);
			return tv;
		}

		@Override
		public View getGroupView(int groupPos, boolean isExpanded, View convertView,
				ViewGroup parent) {
			TextView tv = getGenericView();
			tv.setText(getGroup(groupPos).toString());
			tv.setTextAppearance(this.cxt, android.R.attr.textAppearanceMedium);
			return tv;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPos, int childPos) {
			return true;
		}

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
