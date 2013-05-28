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
			// TODO: Go go!
            // mCardView.addCard(new MyPlayCard("Test Card", "Yay", "#4ac925", "#222222", false, false));

            mCardView = (CardUI) rootView.findViewById(R.id.cardsview);
            mCardView.setSwipeable(false);

            // ************************************************* //
            // ***************** PROCESSOR ********************* //
            // ************************************************* //

            try {
                // Get the list of processors
                JSONArray processors = computer.getJSONArray("processors");

                // Count the total amount of processors
                int length = processors.length();

                // Loop though all of them
                for (int i = 0; i < length; ++i) {
                    try {
                        // Grab the current processor
                        JSONObject processor = processors.getJSONObject(i);
                        JSONObject processorModel = processor.getJSONObject("model");
                        mCardView
                                .addCard(new MyPlayCard(
                                        String.format("%s %s", processorModel.getJSONObject("manufacturer").getString("name"), processorModel.getString("name")),
                                        String.format("%s GHz\n%s %s\n%s %s",
                                                processorModel.getString("clock_rate"),
                                                processorModel.getString("cores"),
                                                "Cores",
                                                processorModel.getString("threads"),
                                                "Threads"),
                                        "#f2a400", "#9d36d0", false, false));

                    } catch (JSONException e1) {
                        Log.e("ComputerInfo", "Error while creating a processor");
                    }
                }
            } catch (Exception e) {
                Log.e("ComputerInfo", "Error while creating all processors");
            }

            // ************************************************* //
            // ******************* GRAPHICS ******************** //
            // ************************************************* //

            try {

                // Get the list of graphics cards
                JSONArray graphicsCards = computer.getJSONArray("graphics_cards");

                // Count the total amount of graphics cards
                int length = graphicsCards.length();

                // Loop though all of them
                for (int i = 0; i < length; ++i) {
                    try {
                        JSONObject graphicsCard = graphicsCards.getJSONObject(i);

                        mCardView
                                .addCard(new MyPlayCard(
                                        graphicsCard.getJSONObject("model").getString("caption").trim(),
                                        String.format("%s MB %s RAM\n%s, %s",
                                                graphicsCard.getString("ram_size"),
                                                "of",
                                                graphicsCard.getJSONObject("screen_size").getString("detection_string"),
                                                graphicsCard.getJSONObject("screen_size").getString("aspect_ratio")
                                        ),
                                        "#33B5E5", "#222222", false, false));


                    } catch (JSONException e1) {
                        Log.e("ComputerInfo", "Error while creating a graphics card");
                    }
                }

            } catch (Exception e) {
                Log.e("ComputerInfo", "Error while creating all graphics cards");
                e.printStackTrace();
            }

            // ************************************************* //
            // ******************* MEMORY ********************** //
            // ************************************************* //

            try {
                // Get the memory information
                JSONObject memory = computer.getJSONObject("memory");
                JSONArray memorySlots = memory.getJSONArray("slots");

                int usedMemorySlots = 0;
                int emptyMemorySlots = 0;

                for (int i = 0; i <= memorySlots.length() - 1; i++) {
                    if (memorySlots.getJSONObject(i).getBoolean("empty")) {
                        emptyMemorySlots++;
                    } else {
                        usedMemorySlots++;
                    }
                }

                mCardView
                        .addCard(new MyPlayCard(
                                String.format("%sGB of RAM", Math.round(memory.getDouble("total_physical_memory")/1024), 2),
                                String.format("%s %s\n%s %s",
                                        usedMemorySlots,
                                        "slots used",
                                        emptyMemorySlots,
                                        "slots empty"),
                                "#4ac925", "#222222", false, false));

                for (int i = 0; i <= memorySlots.length() - 1; i++) {
                    JSONObject memorySlot = memorySlots.getJSONObject(i);

                    String title = String.format("%s %s", "RAM Slot", i+1);
                    String description = "Empty";
                    if (!memorySlot.getBoolean("empty")) {
                        description = String.format("%s MB\n%s Mhz",
                                memorySlot.getInt("capacity"),
                                memorySlot.getInt("speed")
                        );
                    }

                    if (i == 0) {
                        mCardView.addCard(new MyPlayCard(title, description, "#4ac925", "#222222", false, false));
                    } else {
                        mCardView.addCardToLastStack(new MyPlayCard(title, description, "#4ac925", "#222222", false, false));
                    }
                }


            } catch (Exception e) {
                Log.e("ComputerInfo", "Error while creating memory view");
                e.printStackTrace();
            }

            mCardView.refresh();
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
