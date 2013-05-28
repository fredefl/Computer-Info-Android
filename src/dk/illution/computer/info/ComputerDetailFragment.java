package dk.illution.computer.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fima.cardsui.views.CardUI;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.samskivert.mustache.Mustache;

import dk.illution.computer.info.ComputerList;
import dk.illution.computer.info.widget.AccordionView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ComputerDetailFragment extends Fragment {

	public static final String ARG_ITEM_ID = "item_id";
	public static Integer id;

	JSONObject computer;
    private CardUI mCardView;

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

		try {
			JSONArray processors = computer.getJSONArray("processors");

			int length = processors.length();
			for (int i = 0; i < length; ++i) {
				JSONObject processor = processors.getJSONObject(i);
				Log.d("ComputerInfo", processor.getString("name"));
			}

		} catch (Exception e) {}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		try {
			// Get the current computers id
			id = Integer.parseInt(getArguments().getString(ARG_ITEM_ID));

			// Check if the next computer exists, and remove the next arrow if it
			// doesn't
			if (!ComputerList.ITEM_MAP.containsKey(String.valueOf(id + 1))) {
				menu.findItem(R.id.navigation_next).setVisible(false);
			} else {
				menu.findItem(R.id.navigation_next).setVisible(true);
			}

			// Check if the previous computer exists, and remove the previous arrow
			// if it doesn't
			if (!ComputerList.ITEM_MAP.containsKey(String.valueOf(id - 1))) {
				menu.findItem(R.id.navigation_previous).setVisible(false);
			} else {
				menu.findItem(R.id.navigation_previous).setVisible(true);
			}
		} catch (Exception e) {

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

            mCardView = (CardUI) rootView.findViewById(R.id.cardsview);
            mCardView.setSwipeable(false);

            mCardView
                    .addCard(new MyPlayCard(
                            "Intel i7",
                            "2Ghz\n" +
                                    "4 Cores\n" +
                                    "8 Threads",
                            "#f2a400", "#9d36d0", false, false));

            mCardView
                    .addCard(new MyPlayCard(
                            "NVIDIA GeForce GTX 560 Ti",
                            "1024MB of RAM\n" +
                                    "1920x1080, 16:9",
                            "#33B5E5", "#222222", false, false));

            mCardView
                    .addCard(new MyPlayCard(
                            "8GB of RAM",
                            "4 slots used\n" +
                                    "4 slots empty",
                            "#4ac925", "#222222", false, false));

            mCardView
                    .addCard(new MyPlayCard(
                            "RAM Slot 1",
                            "2GB\n" +
                                    "DDR3\n" +
                                    "1333 Mhz",
                            "#4ac925", "#222222", false, false));

            for (int i = 2; i <= 4; i++) {
                mCardView
                        .addCardToLastStack(new MyPlayCard(
                                String.format("RAM Slot %s", i),
                                "2GB\n" +
                                        "DDR3\n" +
                                        "1333 Mhz",
                                "#4ac925", "#222222", false, false));
            }

            mCardView.refresh();

			// ************************************************* //
			// ******************* MEMORY ********************** //
			// ************************************************* //
			/*
			try {
				// Find the layout to insert the memory in
				LinearLayout memoryLayout = (LinearLayout) rootView.findViewById(R.id.section_computer_memory);
				
				// Get the memory information
				JSONObject memory = computer.getJSONObject("memory");
				//JSONArray memorySlots = memory.getJSONArray("slots");
				
				compileTemplate((LinearLayout) memoryLayout, toMap(memory));
			} catch (Exception e) {
				Log.e("ComputerInfo", "Error while creating memory view");
				e.printStackTrace();
			}

			// ************************************************* //
			// ***************** PROCESSOR ********************* //
			// ************************************************* //
			try {
				// Find the layout to insert the processor in
				LinearLayout processorLayout = (LinearLayout) rootView.findViewById(R.id.section_computer_processor);
				
				// Get the list of processors
				JSONArray processors = computer.getJSONArray("processors");

				// Count the total amount of processors
				int length = processors.length();
				
				// Loop though all of them
				for (int i = 0; i < length; ++i) {
					try {
						if(i > 0) {
							View ruler = new View(this.getActivity()); ruler.setMinimumHeight(10);
							processorLayout.addView(ruler,
									new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, 2));
						}
						
						// Grab the current processor
						JSONObject processor = processors.getJSONObject(i);
						
						// Create a new processor view
						View processorView = inflater.inflate(R.layout.computer_processor, null);

						// Set the current processor number
						((TextView) processorView.findViewById(R.id.computer_processor_number))
							.setText("#" + Integer.toString(i + 1));

						processorView.setTag(i);
						
						// And the newly created view to the layout
						processorLayout.addView(processorView);
						
						// Compile the template to insert to data
						compileTemplate((LinearLayout) processorLayout.findViewWithTag(i), toMap(processor));
						
					} catch (JSONException e1) {
						Log.e("ComputerInfo", "Error while creating a processor");
					}
				}
			} catch (Exception e) {
				Log.e("ComputerInfo", "Error while creating all processors");
			}*/
		}
		return rootView;
	}
	
	public void compileTemplate (LinearLayout layout, Map<String, Object> map) {
		// Loop through childs
		for (int i = 0; i < layout.getChildCount(); i++) {
			// Get current child
			View childView = layout.getChildAt(i);

			// If child is a TextView
			if (childView.getClass() == TextView.class) {
				// Get the current child as TextView
				TextView childTextView = (TextView) childView;
				try {
					// If current child has the tag "value"
					if (childTextView.getTag().toString().compareTo("value") == 0) {
						// Compile and execute the current child's text
						childTextView.setText(Mustache.compiler().compile(childTextView.getText().toString()).execute(map));
					}
				} catch (Exception e) {

				}
			}
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.navigation_next:
			((ComputerDetailActivity)getActivity()).viewComputer(String.valueOf(id + 1));
			return true;
		case R.id.navigation_previous:
			((ComputerDetailActivity)getActivity()).viewComputer(String.valueOf(id - 1));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public static Map<String, Object> toMap(JSONObject object) throws JSONException
	{
		Map<String, Object> map = new HashMap<String, Object>();
		Iterator keys = object.keys();
		while (keys.hasNext())
		{
			String key = (String) keys.next();
			map.put(key, fromJson(object.get(key)));
		}
		return map;
	}

	public static List toList(JSONArray array) throws JSONException
	{
		List list = new ArrayList();
		for (int i = 0; i < array.length(); i++)
		{
			list.add(fromJson(array.get(i)));
		}
		return list;
	}

	private static Object fromJson(Object json) throws JSONException
	{
		if (json instanceof JSONObject)
		{
			return toMap((JSONObject) json);
		} else if (json instanceof JSONArray)
		{
			return toList((JSONArray) json);
		} else
		{
			return json;
		}
	}

}
