package dk.illution.computer.info;

import org.json.JSONObject;

import dk.illution.computer.info.ComputerList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

public class ComputerDetailFragment extends Fragment {

	public static final String ARG_ITEM_ID = "item_id";
	public static Integer id;

	JSONObject computer;

	public ComputerDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Make sure to say that we want influence on the menu
		setHasOptionsMenu(true);

		// Get the computers id and the computer object
		if (getArguments().containsKey(ARG_ITEM_ID)) {
			id = Integer.parseInt(getArguments().getString(ARG_ITEM_ID));
			computer = ComputerList.ITEM_MAP.get(id.toString()).computer;
		}

		// Get the identifier of the computer
		try {
			getActivity().setTitle(computer.getString("identifier"));
		} catch (Exception e) {
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Get the current computers id
		id = Integer.parseInt(getArguments().getString(ARG_ITEM_ID));

		// Check if the next computer exists, and remove the next arrow if it
		// doesn't
		if (!ComputerList.ITEM_MAP.containsKey(String.valueOf(id + 1))) {
			menu.findItem(R.id.navigation_next).setVisible(false);
		}

		// Check if the previous computer exists, and remove the previous arrow
		// if it doesn't
		if (!ComputerList.ITEM_MAP.containsKey(String.valueOf(id - 1))) {
			menu.findItem(R.id.navigation_previous).setVisible(false);
		}

		// Get the menu inflated
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_computer_detail,
				container, false);
		// Make sure the computer isn't null
		if (computer != null) {
			// TODO: Gogo!
		}
		return rootView;
	}

}
