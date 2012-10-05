package dk.illution.computer.info;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginSelectActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_select);
		final Button usernameAndPasswordButton = (Button) findViewById(R.id.username_and_password_button);
		final Button googleButton = (Button) findViewById(R.id.google_button);

		usernameAndPasswordButton
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						ComputerInfo.launchLogin(LoginSelectActivity.this);
					}
				});

		googleButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final Activity activity = LoginSelectActivity.this;
				final Context context = activity.getApplicationContext();

				AccountManager am = AccountManager.get(context);
				final Account[] accounts = am.getAccountsByType("com.google");
				CharSequence [] accountNames = new CharSequence[accounts.length];


				if (accounts.length <= 0) {
					Toast.makeText(context, "Sorry, no Google accounts were found on this phone. Please set one up before proceeding.", Toast.LENGTH_LONG).show();
					return;
				}

				for (int i = 0; i <= accounts.length - 1; i++) {
					accountNames[i] = accounts[i].name;
				}

				//Prepare the list dialog box
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				//Set its title
				builder.setTitle("Choose an account");
				//Set the list items and assign with the click listener
				builder.setItems(accountNames, new DialogInterface.OnClickListener() {

					// Click listener
					public void onClick(DialogInterface dialog, int item) {
						//
						AccountManager manager = AccountManager.get(activity);

						manager.getAuthToken(accounts[item], "oauth2:https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email", null, activity, new AccountManagerCallback<Bundle>() {
							public void run(AccountManagerFuture<Bundle> future) {
								try {
									// If the user has authorized your application to use the tasks API
									// a token is available.
									String token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
									Log.d("ComputerInfo", token);

									HttpResponse response = null;
									try {
											HttpClient client = new DefaultHttpClient();
											HttpGet request = new HttpGet();
											request.setURI(new URI("https://ci.illution.dk/login/device/google?access_token=" + token));
											response = client.execute(request);
										} catch (URISyntaxException e) {
											e.printStackTrace();
										} catch (ClientProtocolException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										Log.d("ComputerInfo", response.getEntity().getContent().toString());

									// Now you can use the Tasks API...
								} catch (OperationCanceledException e) {
									// TODO: The user has denied you access to the API, you should handle that
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}, null);

					}

				});

				AlertDialog alert = builder.create();
				//display dialog box
				alert.show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_login_select, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			ComputerInfo.launchPreferences(this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
