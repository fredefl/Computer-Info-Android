package dk.illution.computer.info;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

class SignIn extends AsyncTask<String, Void, String> {

	private Context appContext;

	public SignIn (Activity activity) {
		appContext = activity.getApplicationContext();
	}

	protected String doInBackground (String... params) {
		URL url;
		HttpURLConnection connection;

		try{
			String response= "";

			// Set the URL
			url=new URL(appContext.getString(R.string.base_url) + "/login/device");

			// Set parameters
			String param="username=" + URLEncoder.encode(params[0],"UTF-8")+
			"&password="+URLEncoder.encode(params[1],"UTF-8");

			// Open connectionection
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
		LoginActivity.dialog.hide();
		Log.d("ComputerInfo", response);
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
		final Context context = this;

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
				Intent intent = new Intent(context, ComputerListActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, LoginSelectActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.menu_settings:
			Intent settingsActivity = new Intent(getBaseContext(),
					Preferences.class);
			startActivity(settingsActivity);
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
