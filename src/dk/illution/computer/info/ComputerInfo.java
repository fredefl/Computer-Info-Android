package dk.illution.computer.info;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ComputerInfo extends Application implements OnNavigationListener {

    /**
     * Stores the app context
     */
    private static Context context;
    private static Activity activity;
    private static String[] deviceList;
    public static MainDatabase mainDatabase;

    public void onCreate() {
        super.onCreate();
        ComputerInfo.context = getApplicationContext();
        ComputerInfo.mainDatabase = new MainDatabase(context);
    }

    /**
     * Gets the app context
     *
     * @return The app context
     */
    public static Context getAppContext() {
        return ComputerInfo.context;
    }

    public static void addDeviceSpinner(ActionBar actionBar, Activity activity, String device) {
        ComputerInfo.activity = activity;
        deviceList = new String[3];
        if (device == "computers") {
            deviceList[0] = activity.getString(R.string.computers);
            deviceList[1] = activity.getString(R.string.printers);
            deviceList[2] = activity.getString(R.string.units);
        } else if (device == "printers") {
            deviceList[0] = activity.getString(R.string.printers);
            deviceList[1] = activity.getString(R.string.computers);
            deviceList[2] = activity.getString(R.string.units);
        } else if (device == "units") {
            deviceList[0] = activity.getString(R.string.units);
            deviceList[1] = activity.getString(R.string.computers);
            deviceList[2] = activity.getString(R.string.printers);
        }
        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter<String>(actionBar.getThemedContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1, deviceList), ComputerInfo.onNavigationItemSelected);
    }

    private static OnNavigationListener onNavigationItemSelected = new OnNavigationListener() {
        @SuppressLint("NewApi")
        public boolean onNavigationItemSelected(int position, long id) {
            Log.d("ComputerInfo", "Switched to " + deviceList[position]);
            if (position == 0) {
                return true;
            }
            if (deviceList[position].equalsIgnoreCase(activity.getString(R.string.computers))) {
                activity.getActionBar().setLogo(R.drawable.computer);
                launchComputerList(activity);
                activity.finish();
            } else if (deviceList[position].equalsIgnoreCase(activity.getString(R.string.printers))) {
                activity.getActionBar().setLogo(R.drawable.printer);
                launchPrinterList(activity);
                //activity.finish();
            } else if (deviceList[position].equalsIgnoreCase(activity.getString(R.string.units))) {
                activity.getActionBar().setLogo(R.drawable.camera);
            }
            return true;
        }
    };

    /**
     * Loads computers from the raw folder
     *
     * @return A JSONObject containing the list of computers
     */
    public static JSONObject loadComputers() {
        String computersString = "{}";
        try {
            Resources res = context.getResources();
            InputStream in_s = res.openRawResource(R.raw.computers);

            byte[] b = new byte[in_s.available()];
            in_s.read(b);
            computersString = new String(b);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        JSONObject computersObject = null;

        try {
            computersObject = new JSONObject(computersString);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return computersObject;
    }

    /**
     * Gets the hash of the input string using the hash function type
     *
     * @param input The input string you wish to hash
     * @param type  The hash algorithm (For example SHA-512)
     * @return The resulting hash
     */
    public static String getHash(String input, String type) {
        byte[] bytesOfMessage;
        try {
            bytesOfMessage = input.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance(type);
            byte[] thedigest = md.digest(bytesOfMessage);
            return String.format("%0128x", new BigInteger(1, thedigest));
        } catch (UnsupportedEncodingException e) {
            return null;
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static void launchLogin(Activity activity) {
        Intent newIntent = new Intent(activity.getBaseContext(),
                LoginActivity.class);
        activity.startActivity(newIntent);
    }

    public static void launchLoginSelect(Activity activity) {
        Intent newIntent = new Intent(activity.getBaseContext(),
                LoginSelectActivity.class);
        activity.startActivity(newIntent);
    }

    public static void launchPreferences(Activity activity) {
        Intent newIntent = new Intent(activity.getBaseContext(),
                Preferences.class);
        activity.startActivity(newIntent);
    }

    public static void launchComputerList(Activity activity) {
        Intent newIntent = new Intent(activity.getBaseContext(),
                ComputerListActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(newIntent);
    }

    public static void launchPrinterList(Activity activity) {
        Intent newIntent = new Intent(activity, PrinterListActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(newIntent);
    }

    public static void launchAbout(Activity activity) {
        ((TextView) new AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.about))
                .setIcon(R.drawable.action_about)
                .setMessage(Html.fromHtml("" +
                        activity.getString(R.string.about_title) +
                        activity.getString(R.string.about_made_by) +
                        activity.getString(R.string.about_open_source) +
                        activity.getString(R.string.about_icons_wireframesketcher_studio)))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show()
                .findViewById(android.R.id.message))
                .setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static boolean parseUserTokenResponse(String response, Context context) {
        try {
            JSONObject credentials = new JSONObject(response);
            Log.d("ComputerInfo", "Login status: " + credentials.getString("status"));
            if (credentials.getString("status").equals("FAIL")) {
                return false;
            }
            if (credentials.getString("status").equals("OK")) {
                String token = credentials.getJSONObject("token").getString("token");

                if (token.length() > 0) {
                    // Create database link and create an SQL statement
                    ComputerInfo.mainDatabase.insertCredential("token", token);
                    return true;
                } else {
                    return false;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("ComputerInfo", "ComputerInfo.java:204");
            return false;
        }
        return false;
    }

    public boolean onNavigationItemSelected(int arg0, long arg1) {
        // TODO Auto-generated method stub
        return false;
    }
}
