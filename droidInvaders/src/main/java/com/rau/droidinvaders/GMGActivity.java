package com.rau.droidinvaders;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class GMGActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gmg_activity);
		findViewById(R.id.sign_in_button).setOnClickListener(this);
		findViewById(R.id.sign_out_button).setOnClickListener(this);
	}

	public void onSignInFailed() {
		// TODO Auto-generated method stub
		
	}

	public void onSignInSucceeded() {
		// TODO Auto-generated method stub
		findViewById(R.id.sign_in_button).setVisibility(View.GONE);
	    findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
		
	}


	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view.getId() == R.id.sign_in_button) {
			//			// start the asynchronous sign in flow
//						beginUserInitiatedSignIn();
					}
		else if (view.getId() == R.id.sign_out_button) {
			//			// start the asynchronous sign in flow
//			signOut();
			findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
			findViewById(R.id.sign_out_button).setVisibility(View.GONE);
					}
		
	}

}
