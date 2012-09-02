package dk.illution.computer.info;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.net.ParseException;
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

class SignIn extends AsyncTask<String, Void, HttpResponse> {

	protected HttpResponse doInBackground(String... params) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://192.168.0.194/ci/login/device");

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("username", params[0]));
			nameValuePairs.add(new BasicNameValuePair("password", params[1]));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			return response;
		} catch (ClientProtocolException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	protected void onPostExecute(HttpResponse response) {
		String response_text = "";
		HttpEntity entity = null;
		try {
			entity = response.getEntity();
			response_text = _getResponseBody(entity);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			if (entity != null) {

					try {
							entity.consumeContent();

					} catch (IOException e1) {


					}
			}
		}
		LoginActivity.dialog.hide();
		Log.d("ComputerInfo", response_text);
	}


	public static String _getResponseBody(final HttpEntity entity) throws IOException, ParseException {

	if (entity == null) { throw new IllegalArgumentException("HTTP entity may not be null"); }

	InputStream instream = entity.getContent();

	if (instream == null) { return ""; }

	if (entity.getContentLength() > Integer.MAX_VALUE) { throw new IllegalArgumentException(

	"HTTP entity too large to be buffered in memory"); }

	String charset = getContentCharSet(entity);

	if (charset == null) {

	charset = HTTP.DEFAULT_CONTENT_CHARSET;

	}

	Reader reader = new InputStreamReader(instream, charset);

	StringBuilder buffer = new StringBuilder();

	try {

	char[] tmp = new char[1024];

	int l;

	while ((l = reader.read(tmp)) != -1) {

	buffer.append(tmp, 0, l);

	}

	} finally {

	reader.close();

	}

	return buffer.toString();

	}

	public static String getContentCharSet(final HttpEntity entity) throws ParseException {

	if (entity == null) { throw new IllegalArgumentException("HTTP entity may not be null"); }

	String charset = null;

	if (entity.getContentType() != null) {

	HeaderElement values[] = entity.getContentType().getElements();

	if (values.length > 0) {

	NameValuePair param = values[0].getParameterByName("charset");

	if (param != null) {

	charset = param.getValue();

	}

	}

	}

	return charset;

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
		final Button loginButton = (Button) findViewById(R.id.loginButton);
		final Button signUpButton = (Button) findViewById(R.id.signUpButton);
		final Context context = this;

		loginButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final TextView usernameBox = (TextView) findViewById(R.id.usernameBox);
				final TextView passwordBox = (TextView) findViewById(R.id.passwordBox);
				dialog = ProgressDialog.show(LoginActivity.this, "",
						"Loading. Please wait...", true);
				dialog.setCancelable(true);
				new SignIn().execute(usernameBox.getText().toString(), passwordBox.getText().toString());
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
				//
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
