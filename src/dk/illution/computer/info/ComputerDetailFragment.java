package dk.illution.computer.info;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
			try {
				((TextView) rootView.findViewById(R.id.test)).setText(computer.getString("serial"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			try {
				JSONArray processors = computer.getJSONArray("processors");
				
				int length = processors.length();
		        for (int i = 0; i < length; ++i) {
					JSONObject processor = processors.getJSONObject(i);
		            LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.section_computer_processor);
					View processorView = inflater.inflate(R.layout.computer_processor, null);
					
					((TextView) processorView.findViewById(R.id.computer_processor_number))
						.setText("#" + Integer.toString(i + 1));
					
					try {
						((TextView) processorView.findViewById(R.id.computer_processor_clock_speed))
							.setText(processor.getString("clock_rate") + " Ghz");
					} catch (Exception e) {};
					
					try {
						((TextView) processorView.findViewById(R.id.computer_processor_cores))
							.setText(processor.getString("cores"));
					} catch (Exception e) {};
					
					try {
						((TextView) processorView.findViewById(R.id.computer_processor_threads))
							.setText(processor.getString("threads"));
					} catch (Exception e) {};
					
					try {
						((TextView) processorView.findViewById(R.id.computer_processor_manufacturer))
							.setText(processor.getJSONObject("manufacturer").getString("name"));
					} catch (Exception e) {};
					
					try {
						((TextView) processorView.findViewById(R.id.computer_processor_model_number))
							.setText(processor.getString("model_number"));
					} catch (Exception e) {};
					
					ll.addView(processorView);
		        }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
			

			
			
			//AccordionView view = (AccordionView) rootView.findViewById(R.id.accordion_view);
			//View processor = inflater.inflate(R.layout.computer_processor, null);
			//view.addView(processor);
		}
		return rootView;
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

}
