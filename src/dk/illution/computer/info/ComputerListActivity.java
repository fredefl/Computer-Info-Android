package dk.illution.computer.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class ComputerListActivity extends FragmentActivity
        implements ComputerListFragment.Callbacks {

    private boolean mTwoPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer_list);

        if (findViewById(R.id.computer_detail_container) != null) {
            mTwoPane = true;
            ((ComputerListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.computer_list))
                    .setActivateOnItemClick(true);
        }
    }

    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(ComputerDetailFragment.ARG_ITEM_ID, id);
            ComputerDetailFragment fragment = new ComputerDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.computer_detail_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, ComputerDetailActivity.class);
            detailIntent.putExtra(ComputerDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
