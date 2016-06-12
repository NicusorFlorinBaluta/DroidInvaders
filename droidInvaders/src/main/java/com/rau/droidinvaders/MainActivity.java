package com.rau.droidinvaders;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.rau.droidinvaders.util.IabBroadcastReceiver;
import com.rau.droidinvaders.util.IabHelper;
import com.rau.droidinvaders.util.IabResult;
import com.rau.droidinvaders.util.Inventory;
import com.rau.droidinvaders.util.Purchase;

public class MainActivity extends Activity implements
		IabBroadcastReceiver.IabBroadcastListener
{
	public static final String SKU_REMOVE_ADS = "remove_all_ads";
    public static final String SKU_PREMIUM_SHIP = "premium_ship";
    public static final String SKU_PREMIUM_SOUND = "premium_sound";
    public static final String SKU_EXTRA_LIFE = "extra_life";

	public static MainActivity instance;
	MainGamePanel mgp;
	private static final String TAG = MainActivity.class.getSimpleName();
	static final int RC_REQUEST = 10001;
	IabHelper mHelper;
	public boolean removeAds = false;
	public boolean premiumShip = false;
	public boolean premiumSound = false;
    public SharedPreferences iapSP;

	// Provides purchase notification while this app is running
	IabBroadcastReceiver mBroadcastReceiver;
	String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs9v8Ku7Qb69rifCPJjvO6" +
			"aarSoqUfTUWqVouZAruXz22w69KTUHWyI9D21iEGNZaRD2jB5g0HREmn2rSjWhpumNdt1lyJXrce0maVt5z0P+aGg3MMtAik5JwzZ" +
			"kT/ThFIFG8I4qVWbKJMoG1Yo4pnZFMPJXJZ5YenZzQBhnxAasJ0rrtxFosQdDSCsyun1S5h1V6dOU9T58pqZaTtYfK/CyXO6DDm" +
			"ZzWSnmFVcMlkAXNF5Vq13O1cIOvRCZCfKuk8vTJ9nfDOIGx7KRvCG0NTiJgUYq0khQd/eJJWbZTlN0Ki2U0M4cAdx+ZMUJK" +
			"TFJoEvbqFV3g2Shxbradjd7cDQIDAQAB";
	RelativeLayout gameLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// requesting to turn the title OFF
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);

		MobileAds.initialize(getApplicationContext(), getString(R.string.ads_app_id));
		mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
		mHelper.enableDebugLogging(true);
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished.");

				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
//					complain("Problem setting up in-app billing: " + result);
					return;
				}

				// Have we been disposed of in the meantime? If so, quit.
				if (mHelper == null) return;

				mBroadcastReceiver = new IabBroadcastReceiver(MainActivity.this);
				IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
				registerReceiver(mBroadcastReceiver, broadcastFilter);

				// IAB is fully set up. Now, let's get an inventory of stuff we own.
				Log.d(TAG, "Setup successful. Querying inventory.");
				try {
					mHelper.queryInventoryAsync(mGotInventoryListener);
				} catch (IabHelper.IabAsyncInProgressException e) {
//					complain("Error querying inventory. Another async operation in progress.");
				}
			}
		});

	}

	void init()
	{setContentView(/*mgp*/R.layout.activity_main);
		gameLayout = (RelativeLayout) findViewById(R.id.game);


		gameLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			public void onGlobalLayout() {

				gameLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

				// set our MainGamePanel as the View
				mgp = new MainGamePanel(MainActivity.this, gameLayout.getWidth(), gameLayout.getHeight());
				gameLayout.addView(mgp, 0);
				gameLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mgp.requestFocus();
                mgp.requestFocusFromTouch();
			}
		});
        iapSP = getSharedPreferences("iap", Context.MODE_PRIVATE);

		AdView mAdView = (AdView) findViewById(R.id.adView);
		if(iapSP.getBoolean(MainActivity.SKU_REMOVE_ADS, false))
		{
			mAdView.setVisibility(View.GONE);
		}
		else
		{
			AdRequest adRequest = new AdRequest.Builder().addTestDevice("C1D5E02BCC85B10E2360E490C686B09E")
					.build();
			mAdView.loadAd(adRequest);
			gameLayout.bringChildToFront(mAdView);
		}


		Log.d(TAG, "View added");
		instance = this;
	}

	void complain(String message) {
		Log.e(TAG, "**** TrivialDrive Error: " + message);
		alert("Error: " + message);
	}

	void alert(String message) {
		AlertDialog.Builder bld = new AlertDialog.Builder(this);
		bld.setMessage(message);
		bld.setNeutralButton("OK", null);
		Log.d(TAG, "Showing alert dialog: " + message);
		bld.create().show();
	}

	// Listener that's called when we finish querying the items and subscriptions we own
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
			Log.d(TAG, "Query inventory finished.");

			// Have we been disposed of in the meantime? If so, quit.
			if (mHelper == null) return;

			// Is it a failure?
			if (result.isFailure()) {
				complain("Failed to query inventory: " + result);
				return;
			}

			Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

			// Do we have the premium upgrade?
			Purchase premiumPurchase = inventory.getPurchase(SKU_REMOVE_ADS);
			removeAds = (premiumPurchase != null);

			// First find out which subscription is auto renewing
			Purchase premiumShipPurchase = inventory.getPurchase(SKU_PREMIUM_SHIP);
            premiumShip = (premiumShipPurchase != null);
            Purchase premiumSoundPurchase = inventory.getPurchase(SKU_PREMIUM_SOUND);
            premiumSound = (premiumSoundPurchase != null);
            init();
			Log.d(TAG, "Initial inventory query finished; enabling main UI.");
		}
	};
	@Override
	protected void onDestroy()
	{
		Log.d(TAG, "Destroying...");
		super.onDestroy();
		instance=null;

        // very important:
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
            mHelper = null;
        }

		mgp = null;
		System.gc();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return mgp.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return mgp.onKeyUp(keyCode, event);
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    public void buyItemSubscription(String sku)
    {
        try {
            mHelper.launchPurchaseFlow(this, sku, IabHelper.ITEM_TYPE_INAPP,
                    null, RC_REQUEST, mPurchaseFinishedListener, null);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error launching purchase flow. Another async operation in progress.");
//            setWaitScreen(false);
        }
    }

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                return;
            }

            Log.d(TAG, "Purchase successful.");
                try {
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error consuming gas. Another async operation in progress.");
                }
        }
    };

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            if (mHelper == null) return;

            if (result.isSuccess()) {
                Log.d(TAG, "Consumption successful. Provisioning.");
                SharedPreferences.Editor editor = iapSP.edit();
                if (purchase.getSku().equals(SKU_PREMIUM_SHIP))
                {
                    premiumShip = true;
                    editor.putBoolean(SKU_PREMIUM_SHIP, true);
                    editor.commit();
                }
                if (purchase.getSku().equals(SKU_PREMIUM_SOUND))
                {
                    premiumSound = true;
                    editor.putBoolean(SKU_PREMIUM_SOUND, true);
                    editor.commit();
                }
                if (purchase.getSku().equals(SKU_REMOVE_ADS))
                {
                    removeAds = true;
                    findViewById(R.id.adView).setVisibility(View.GONE);
                    editor.putBoolean(SKU_REMOVE_ADS, true);
                    editor.commit();
                }
            }
            else {
                complain("Error while consuming: " + result);
            }
            Log.d(TAG, "End consumption flow.");
        }
    };

	@Override
	public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error querying inventory. Another async operation in progress.");
        }
	}
}
