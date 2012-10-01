package dk.illution.computer.info;

import org.json.JSONException;
import org.json.JSONObject;

import dk.illution.computer.info.ComputerList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            id = Integer.parseInt(getArguments().getString(ARG_ITEM_ID));
            computer = ComputerList.ITEM_MAP.get(id.toString()).computer;
            
        }
        
        if (ComputerList.ITEM_MAP.containsKey(String.valueOf(id - 1))) {
            Log.d("ComputerInfo", "Can go back");
        }
        
        if (ComputerList.ITEM_MAP.containsKey(String.valueOf(id + 1))) {
            Log.d("ComputerInfo", "Can go forward");
        }
        
        try {
            getActivity().setTitle(computer.getString("identifier"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_computer_detail,
                container, false);
        if (computer != null) {
            
        }
        return rootView;
    }
}
