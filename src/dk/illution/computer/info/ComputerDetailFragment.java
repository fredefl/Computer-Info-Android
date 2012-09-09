package dk.illution.computer.info;

import org.json.JSONException;

import dk.illution.computer.info.ComputerList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ComputerDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    ComputerList.Computer mItem;

    public ComputerDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = ComputerList.ITEM_MAP.get(getArguments().getString(
                    ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_computer_detail,
                container, false);
        if (mItem != null) {
        	/*
            try {
                ((TextView) rootView.findViewById(R.id.computer_detail))
                        .setText(mItem.computer.getString("serial"));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/
        }
        return rootView;
    }
}
