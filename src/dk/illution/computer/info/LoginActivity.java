package dk.illution.computer.info;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends Activity {

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
				ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "",
						"Loading. Please wait...", true);
				dialog.setCancelable(true);
			}
		});

		signUpButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(context, MainActivity.class);
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
