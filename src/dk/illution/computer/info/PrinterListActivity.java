package dk.illution.computer.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class PrinterListActivity extends FragmentActivity
        implements PrinterListFragment.Callbacks {

    private boolean mTwoPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_list);

        if (findViewById(R.id.printer_detail_container) != null) {
            mTwoPane = true;
            ((PrinterListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.printer_list))
                    .setActivateOnItemClick(true);
        }
    }

    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(PrinterDetailFragment.ARG_ITEM_ID, id);
            PrinterDetailFragment fragment = new PrinterDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.printer_detail_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, PrinterDetailActivity.class);
            detailIntent.putExtra(PrinterDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
