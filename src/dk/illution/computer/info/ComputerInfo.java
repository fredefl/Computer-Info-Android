package dk.illution.computer.info;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

public class ComputerInfo extends Application {

	private static Context context;

	public void onCreate(){
		super.onCreate();
		ComputerInfo.context = getApplicationContext();
	}

	public static Context getAppContext() {
		return ComputerInfo.context;
	}

	public static JSONObject loadComputers () {
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

	public static String getHash (String input, String type) {
		byte[] bytesOfMessage;
		try {
			bytesOfMessage = input.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] thedigest = md.digest(bytesOfMessage);
			return String.format("%0128x", new BigInteger(1, thedigest));
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
}
