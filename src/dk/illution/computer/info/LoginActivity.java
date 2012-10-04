package dk.illution.computer.info;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.Credentials;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

class SignIn extends AsyncTask<String, Void, String> {

	private Activity activity;
	private Context appContext;

	public SignIn (Activity activity) {
		appContext = activity.getApplicationContext();
		this.activity = activity;
	}

	protected String doInBackground (String... params) {
		URL url;
		HttpURLConnection connection;

		try {
			String response= "";

			// Set the URL
			url=new URL(appContext.getString(R.string.base_url) + "/login/device");

			// Set parameters
			String param="username=" + URLEncoder.encode(params[0],"UTF-8")+
			"&password="+URLEncoder.encode(params[1],"UTF-8");

			// Open connection
			connection=(HttpURLConnection)url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");

			// Set headers
			connection.setFixedLengthStreamingMode(param.getBytes().length);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			// Send request
			PrintWriter out = new PrintWriter(connection.getOutputStream());
			out.print(param);
			out.close();

			// Get stream
			Scanner inStream = new Scanner(connection.getInputStream());

			// Loop through stream and add get the response
			while(inStream.hasNextLine())
				response+=(inStream.nextLine());

			// Return the response
			return response;
		}
		//catch some error
		catch(MalformedURLException ex){
			Log.d("ComputerInfo", "Malformed URL error");
			return null;
		}
		// and some more
		catch(IOException ex){
			Log.d("ComputerInfo", "IO Exception");
			return null;
		}
	}

	protected void onPostExecute(String response) {
		if (response != null) {
			try {
				JSONObject credentials = new JSONObject(response);
				Log.d("ComputerInfo", response);
				if (credentials.getString("status") == "FAIL") {
					LoginActivity.dialog.hide();
					Toast.makeText(appContext, "Wrong email or password, please try again.", Toast.LENGTH_LONG).show();
					return;
				}
				if (credentials)
				String token = credentials.getJSONObject("token").getString("token");
				if (token.length() > 0) {
					Log.d("ComputerInfo", "Successfully logged in with token: " + token);
				}
				// Huge success
				LoginActivity.dialog.hide();
				ComputerInfo.launchComputerList(activity);
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(appContext, "There was an in the servers response, please try again.", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(appContext, "There was an error while connecting to the server, please try again.", Toast.LENGTH_LONG).show();
		}
	}
}

public class LoginActivity extends Activity {

	public static ProgressDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		final Button loginButton = (Button) findViewById(R.id.login_button);
		final Button signUpButton = (Button) findViewById(R.id.sign_up_button);

		loginButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final TextView usernameBox = (TextView) findViewById(R.id.username_box);
				final TextView passwordBox = (TextView) findViewById(R.id.password_box);
				dialog = ProgressDialog.show(LoginActivity.this, "",
						"Loading. Please wait...", true);
				dialog.setCancelable(true);
				new SignIn(LoginActivity.this).execute(usernameBox.getText().toString(),
						passwordBox.getText().toString());
			}
		});

		signUpButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ComputerInfo.launchComputerList(LoginActivity.this);
			}
		});
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			ComputerInfo.launchLoginSelect(LoginActivity.this);
			return true;
		case R.id.menu_settings:
			ComputerInfo.launchPreferences(this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}
}
