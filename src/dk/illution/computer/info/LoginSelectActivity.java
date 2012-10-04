package dk.illution.computer.info;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class LoginSelectActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_select);
		final Button usernameAndPasswordButton = (Button) findViewById(R.id.username_and_password_button);
		final Button googleButton = (Button) findViewById(R.id.google_button);
		final Context context = this;
		usernameAndPasswordButton
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(context, LoginActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
				});

		googleButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ComputerInfo.launchComputerList(LoginSelectActivity.this);
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
