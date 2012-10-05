package dk.illution.computer.info;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

public class ComputerInfo extends Application {

	/**
	 * Stores the app context
	 */
	private static Context context;
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

	/**
	 * Loads computers from the raw folder
	 *
	 * @return A JSONObject containing the list of computers
	 */
	public static JSONObject loadComputers() {
		String computers_json = "{}";
		try {
			Resources res = context.getResources();
			InputStream in_s = res.openRawResource(R.raw.computers);

			byte[] b = new byte[in_s.available()];
			in_s.read(b);
			computers_json = new String(b);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		JSONObject computers_data = null;

		try {
			computers_data = new JSONObject(computers_json);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return computers_data;
	}

	/**
	 * Gets the hash of the input string using the hash function type
	 *
	 * @param input
	 *            The input string you wish to hash
	 * @param type
	 *            The hash algorithm (For example SHA-512)
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

	public static void launchLogin (Activity activity) {
		Intent newIntent = new Intent(activity.getBaseContext(),
				LoginActivity.class);
		activity.startActivity(newIntent);
	}

	public static void launchLoginSelect (Activity activity) {
		Intent newIntent = new Intent(activity.getBaseContext(),
				LoginSelectActivity.class);
		activity.startActivity(newIntent);
	}

	public static void launchPreferences (Activity activity) {
		Intent newIntent = new Intent(activity.getBaseContext(),
				Preferences.class);
		activity.startActivity(newIntent);
	}

	public static void launchComputerList (Activity activity) {
		Intent newIntent = new Intent(activity.getBaseContext(),
				ComputerListActivity.class);
		newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		activity.startActivity(newIntent);
	}

	public static boolean parseUserTokenResponse (String response, Context context) {
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
			return false;
		}
		return false;
	}
}
