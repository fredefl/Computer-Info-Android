package dk.illution.computer.info;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

/**
 * Demonstration of PreferenceFragment, showing a single fragment in an
 * activity.
 */
public class Preferences extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new PrefsFragment()).commit();
        
        getActionBar().setHomeButtonEnabled(true);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    
	public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	   case android.R.id.home:
    	      finish();
    	      return true;
    	}
    	return false;
	}


    public static class PrefsFragment extends PreferenceFragment {
    	


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
    }

}