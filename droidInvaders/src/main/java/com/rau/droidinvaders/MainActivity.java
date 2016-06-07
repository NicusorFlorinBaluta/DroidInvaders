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
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

//import com.bda.controller.Controller;
//import com.bda.controller.ControllerListener;
//import com.bda.controller.MotionEvent;
//import com.bda.controller.StateEvent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.rau.droidinvaders.util.IabBroadcastReceiver;
import com.rau.droidinvaders.util.IabHelper;
import com.rau.droidinvaders.util.IabResult;
import com.rau.droidinvaders.util.Inventory;
import com.rau.droidinvaders.util.Purchase;
//import com.sponsorpay.sdk.android.publisher.UserId;

//import com.google.android.gms.appstate.AppStateManager;
//import com.google.android.gms.plus.PlusClient;
//import com.google.android.gms.plus.PlusClient.OnPeopleLoadedListener;

public class MainActivity extends Activity implements
//        ConnectionCallbacks, OnConnectionFailedListener,
//        ControllerListener,
//        RoomUpdateListener, RealTimeMessageReceivedListener, RoomStatusUpdateListener, OnInvitationReceivedListener,
		OnClickListener,
		IabBroadcastReceiver.IabBroadcastListener
//        ResultCallback<LoadPeopleResult>
{
	public static final String SKU_REMOVE_ADS = "remove_all_ads";
    public static final String SKU_PREMIUM_SHIP = "premium_ship";
    public static final String SKU_PREMIUM_SOUND = "premium_sound";
    public static final String SKU_EXTRA_LIFE = "extra_life";

	//google + client
//	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

//	private ProgressDialog mConnectionProgressDialog;
//	private PlusClient mPlusClient;
//	private ConnectionResult mConnectionResult;

	public static MainActivity instance;
	MainGamePanel mgp;
	private static final String TAG = MainActivity.class.getSimpleName();
	static final int RC_REQUEST = 10001;
	// The helper object
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

//	private static boolean hasBanner;

	//	boolean mExplicitSignOut = false;
//	boolean mInSignInFlow = false;
	RelativeLayout gameLayout;
	// set to true when you're in the middle of the
	// sign in flow, to know you should not attempt
	// to connect on onStart()
//	GoogleApiClient mClient;  // initialized in onCreate

	//moga
//	Controller mController = null;
//	PlusOneButton mPlusOneButton;

	//multiplayer stuff
	// This array lists everything that's clickable, so we can install click
	// event handlers.
//	final static int[] CLICKABLES = {
//		R.id.button_accept_popup_invitation,
//		R.id.sign_in_button, R.id.sign_out_button,
//		R.id.plus_one_button,
////		    	R.id.button_invite_players,
//		//            R.id.button_quick_game, R.id.button_see_invitations,
//		//            R.id.button_sign_out, R.id.button_click_me, R.id.button_single_player,
//		//            R.id.button_single_player_2
//	};



	@Override
	public void onCreate(Bundle savedInstanceState) {
		// requesting to turn the title OFF
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);

		MobileAds.initialize(getApplicationContext(), getString(R.string.ads_app_id));




		// create an instance of Google API client and specify the Play services
		// and scopes to use. In this example, we specify that the app wants
		// access to the Games, Plus, and Cloud Save services and scopes.
//	    GoogleApiClient.Builder builder =
//	        new GoogleApiClient.Builder(this, this, this);
//	    builder.addApi(Games.API)
//	           .addApi(Plus.API)
////	           .addApi(AppStateManager.API)
//	           .addScope(Games.SCOPE_GAMES)
//	           .addScope(Plus.SCOPE_PLUS_LOGIN);
////	           .addScope(AppStateManager.SCOPE_APP_STATE);
//	    mClient = builder.build();


		//google+ client
//		mPlusClient=new PlusClient.Builder(this, this, this).clearScopes()
//                .build();
//		//		 Progress bar to be displayed if the connection failure is not resolved.
//		mConnectionProgressDialog = new ProgressDialog(this);
//		mConnectionProgressDialog.setMessage("Signing in...");



		//moga
//		mController = Controller.getInstance(this);
//		mController.init();
//		mController.setListener(this, new Handler());

		//setContentView(R.layout.activity_main);
//		hasBanner = false;


//		String sponsorPayUserID = UserId.getAutoGenerated(this.getApplicationContext());
//
//		int[] AMA_BANNERS = { R.drawable.panic_flight,
//				R.drawable.animal_tycoon};
//		String[] AMA_LINKS = {"https://play.google.com/store/apps/details?id=com.Ama.PanicFlightFree",
//		"https://play.google.com/store/apps/details?id=softgames.at2free" };
//
//
//		/** please configure mobclix in manifest file:
//			<meta-data
//		    	android:name="com.mobclix.APPLICATION_ID"
//		    	android:value="2033F248-8CE5-4E92-AAE6-5D70C9B9B1FC"/>
//		 **/

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

				// Important: Dynamically register for broadcast messages about updated purchases.
				// We register the receiver here instead of as a <receiver> in the Manifest
				// because we always call getPurchases() at startup, so therefore we can ignore
				// any broadcasts sent while the app isn't running.
				// Note: registering this listener in an Activity is a bad idea, but is done here
				// because this is a SAMPLE. Regardless, the receiver must be registered after
				// IabHelper is setup, but before first call to getPurchases().
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
//			Purchase gasYearly = inventory.getPurchase(SKU_INFINITE_GAS_YEARLY);
//			if (gasMonthly != null && gasMonthly.isAutoRenewing()) {
//				mInfiniteGasSku = SKU_EXTRA_SHIP;
//				mAutoRenewEnabled = true;
//			} else if (gasYearly != null && gasYearly.isAutoRenewing()) {
//				mInfiniteGasSku = SKU_INFINITE_GAS_YEARLY;
//				mAutoRenewEnabled = true;
//			} else {
//				mInfiniteGasSku = "";
//				mAutoRenewEnabled = false;
//			}
//
//			// The user is subscribed if either subscription exists, even if neither is auto
//			// renewing
//			mSubscribedToInfiniteGas = (gasMonthly != null && verifyDeveloperPayload(gasMonthly))
//					|| (gasYearly != null && verifyDeveloperPayload(gasYearly));
//			Log.d(TAG, "User " + (mSubscribedToInfiniteGas ? "HAS" : "DOES NOT HAVE")
//					+ " infinite gas subscription.");
//			if (mSubscribedToInfiniteGas) mTank = TANK_MAX;
//
//			// Check for gas delivery -- if we own gas, we should fill up the tank immediately
//			Purchase gasPurchase = inventory.getPurchase(SKU_GAS);
//			if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
//				Log.d(TAG, "We have gas. Consuming it.");
//				try {
//					mHelper.consumeAsync(inventory.getPurchase(SKU_GAS), mConsumeFinishedListener);
//				} catch (IabAsyncInProgressException e) {
//					complain("Error consuming gas. Another async operation in progress.");
//				}
//				return;
//			}
//
//			updateUi();
//			setWaitScreen(false);
            init();
			Log.d(TAG, "Initial inventory query finished; enabling main UI.");
		}
	};


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
//		mPlusClient.connect();
//		  if (!mInSignInFlow && !mExplicitSignOut) {
//		        // auto sign in
//		        mClient.connect();
//		    }

	}


	@Override
	protected void onPause()
	{

//		//moga
//		if(mController != null)
//		{
//			mController.onPause();
//		}

		super.onPause();
	}

	//	// The request code must be 0 or greater. Google+1
//	private static final int PLUS_ONE_REQUEST_CODE = 0;
//	private static final String URL="https://play.google.com/store/apps/details?id=com.mando.babelrising3dfreemium";
	@Override
	protected void onResume()
	{
		super.onResume();

//		if(getPlusClient()!=null)
//			mPlusOneButton.initialize(getPlusClient(), URL, PLUS_ONE_REQUEST_CODE);

//		RelativeLayout relativeLayoutAd = (RelativeLayout)findViewById(R.id.webview);
//		mgp.
		//moga
//		if(mController != null)
//		{
//			mController.onResume();
//		}
	}


//	protected static LayoutParams createSurfaceViewLayoutParams() {
//		final LayoutParams layoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
//		layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
//		return layoutParams;
//	}

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

		//moga
//		if(mController != null)
//		{
//			mController.exit();
//		}
	}

	@Override
	protected void onStop()
	{
		Log.d(TAG, "Stopping...");
		super.onStop();
		//		mPlusClient.disconnect();

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return mgp.keyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return mgp.keyUp(keyCode, event);
	}





//	public void onKeyEvent(com.bda.controller.KeyEvent arg0) {
//		// TODO Auto-generated method stub
//		mgp.keyEvent(arg0);
//	}
//
//
//	public void onMotionEvent(MotionEvent arg0) {
//		mgp.onMotionEvent(arg0);
//
//	}
//
//
//	public void onStateEvent(StateEvent arg0) {
//		// TODO Auto-generated method stub
//		Log.d(TAG, "onStateEvent start");
//
//	}

	//google+stuff

//	boolean signInSucceeded=false;
//	public void onSignInFailed() {
//		Log.d(TAG, "onSignInFailed start");
//		// TODO Auto-generated method stub
//		// Sign in has failed. So show the user the sign-in button.
//		findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
//		findViewById(R.id.sign_out_button).setVisibility(View.GONE);
////		signInSucceeded=false;
//		Log.d(TAG, "onSignInFailed exit");
//	}
//
//	public void signInStart()
//	{
//		beginUserInitiatedSignIn();
//	}
//
//	public void signOutStart()
//	{
//		signOut();
//	}

//	public boolean getSignInStatus()
//	{
//		return isSignedIn();
//	}
//	public void setAchievement(int id)
//	{
//		Games.Achievements.unlock(getApiClient(), getString(id));
//	}
//	public void setHighscore(int score)
//	{
//		 Games.Leaderboards.submitScore(getApiClient(),getString(R.string.lead_random), score);
//	}


//	public void onConnectionFailed(ConnectionResult result) {
//		Log.d(TAG, "onConnectionFailed start");
//		// TODO Auto-generated method stub
////		if (result.hasResolution()) {
////			// The user clicked the sign-in button already. Start to resolve
////			// connection errors. Wait until onConnected() to dismiss the
////			// connection dialog.
////			if (result.hasResolution()) {
////				try {
////					result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
////				} catch (SendIntentException e) {
////					mPlusClient.connect();
////				}
////			}
////		}
//
//		// Save the intent so that we can start an activity when the user clicks
//		// the sign-in button.
//		mConnectionResult = result;
//		Log.d(TAG, "onConnectionFailed exit");
//	}


//	public void onConnected(Bundle arg0) {
//		Log.d(TAG, "onConnected start");
//		// TODO Auto-generated method stub
//		String accountName = mPlusClient.getAccountName();
//		Toast.makeText(this, accountName + " is connected.", Toast.LENGTH_LONG).show();
//		// We've resolved any connection errors.
//		mConnectionProgressDialog.dismiss();
//		Log.d(TAG, "onConnected exit");
//	}

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
//                setWaitScreen(false);
                return;
            }
//            if (!verifyDeveloperPayload(purchase)) {
//                complain("Error purchasing. Authenticity verification failed.");
//                setWaitScreen(false);
//                return;
//            }

            Log.d(TAG, "Purchase successful.");


//                // bought 1/4 tank of gas. So consume it.
//                Log.d(TAG, "Purchase is gas. Starting gas consumption.");
                try {
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error consuming gas. Another async operation in progress.");
//                    setWaitScreen(false);
//                    return;
                }
//            }
//            else if (purchase.getSku().equals(SKU_PREMIUM)) {
//                // bought the premium upgrade!
//                Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
//                alert("Thank you for upgrading to premium!");
//                mIsPremium = true;
//                updateUi();
//                setWaitScreen(false);
//            }
//            else if (purchase.getSku().equals(SKU_INFINITE_GAS_MONTHLY)
//                    || purchase.getSku().equals(SKU_INFINITE_GAS_YEARLY)) {
//                // bought the infinite gas subscription
//                Log.d(TAG, "Infinite gas subscription purchased.");
//                alert("Thank you for subscribing to infinite gas!");
//                mSubscribedToInfiniteGas = true;
//                mAutoRenewEnabled = purchase.isAutoRenewing();
//                mInfiniteGasSku = purchase.getSku();
//                mTank = TANK_MAX;
//                updateUi();
//                setWaitScreen(false);
//            }
        }
    };

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
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

//                mTank = mTank == TANK_MAX ? TANK_MAX : mTank + 1;
//                saveData();
//                alert("You filled 1/4 tank. Your tank is now " + String.valueOf(mTank) + "/4 full!");
            }
            else {
                complain("Error while consuming: " + result);
            }
//            updateUi();
//            setWaitScreen(false);
            Log.d(TAG, "End consumption flow.");
        }
    };

//	public void onDisconnected() {
//		// TODO Auto-generated method stub
//		Log.d(TAG, "disconnected");
//	}
	// request codes we use when invoking an external activity
//	final int RC_RESOLVE = 5000, RC_UNUSED = 5001;
//	public void openAchievements()
//	{
//		startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), RC_UNUSED);
//	}
//	public void openLeaderboards()
//	{
//		startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(getApiClient()), RC_UNUSED);
//	}
//	public void openInvitationInbox()
//	{
//		Log.d(TAG, "openInvitationInbox start");
//		// request code (can be any number, as long as it's unique)
//		final int RC_INVITATION_INBOX = 10001;
//
//		// launch the intent to show the invitation inbox screen
//		Intent intent =  Games.Invitations.getInvitationInboxIntent(getApiClient());
//		this.startActivityForResult(intent, RC_INVITATION_INBOX);
//		Log.d(TAG, "openInvitationInbox exit");
//	}
//	public void openInviteFriend()
//	{
//		Log.d(TAG, "openInviteFriend start");
//		// request code for the "select players" UI
//		// can be any number as long as it's unique
//		final int RC_SELECT_PLAYERS = 10000;
//
//		// launch the player selection screen
//		// minimum: 1 other player; maximum: 3 other players
//		Intent intent = Games.RealTimeMultiplayer.getSelectOpponentsIntent(getApiClient(),1, 3);
//		startActivityForResult(intent, RC_SELECT_PLAYERS);
//		Log.d(TAG, "openInviteFriend exit");
//	}
//
//	//multiplayer
//
//	// Request codes for the UIs that we show with startActivityForResult:
//	final static int RC_SELECT_PLAYERS = 10000;
//	final static int RC_INVITATION_INBOX = 10001;
//	final static int RC_WAITING_ROOM = 10002;

	// Room ID where the currently active game is taking place; null if we're
	// not playing.
//	String mRoomId = null;
//
//	// Are we playing in multiplayer mode?
//	boolean mMultiplayer = false;
//
//	// The participants in the currently active game
//	ArrayList<Participant> mParticipants = null;

//	// My participant ID in the currently active game
//	String mMyId = null;
//
//	// If non-null, this is the id of the invitation we received via the
//	// invitation listener
//	String mIncomingInvitationId = null;
//
//	// Message buffer for sending messages
//	byte[] mMsgBuf = new byte[2];
//
//	// flag indicating whether we're dismissing the waiting room because the
//	// game is starting
//	boolean mWaitRoomDismissedFromCode = false;
//
//	private RoomConfig.Builder makeBasicRoomConfigBuilder() {
//		return RoomConfig.builder(this)
//				.setMessageReceivedListener(this)
//				.setRoomStatusUpdateListener(this);
//	}
//
//	public void onSignInSucceeded() {
//		Log.d(TAG, "onSignInSucceeded start");
//		// ...
//
//		// show sign-out button, hide the sign-in button
//		findViewById(R.id.sign_in_button).setVisibility(View.GONE);
//		findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
//		mPlusOneButton.initialize(URL, PLUS_ONE_REQUEST_CODE);
////				mPlusOneButton.setVisibility(View.VISIBLE);
//
////		signInSucceeded=true;
//		if (getInvitationId() != null) {
//			Builder roomConfigBuilder =
//					makeBasicRoomConfigBuilder();
//			roomConfigBuilder.setInvitationIdToAccept(getInvitationId());
//			Games.RealTimeMultiplayer.join(getApiClient(), roomConfigBuilder.build());
//
//			// prevent screen from sleeping during handshake
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
//			// go to game screen
//		}
//		 Games.Invitations.registerInvitationListener(getApiClient(),this);
//
//		 Plus.PeopleApi.loadVisible(getApiClient(), null)
//	      .setResultCallback(this);
//
////		getPlusClient().loadPeople(this, Person.Collection.VISIBLE);
//		Log.d(TAG, "onSignInSucceeded exit");
//	}
//
//	public void onJoinedRoom(int statusCode, Room room) {
//		Log.d(TAG, "onJoinedRoom start");
//		// TODO Auto-generated method stub
//		Log.d(TAG, "onRoomConnected(" + statusCode + ", " + room + ")");
//		if (statusCode != GamesStatusCodes.STATUS_OK) {
//			Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
//			return;
//		}
//		Log.d(TAG, "onJoinedRoom exit");
//	}
//
//
//	public void onLeftRoom(int arg0, String arg1) {
//		// TODO Auto-generated method stub
//		Log.d(TAG, "onLeftRoom start");
//	}
//
//
//	public void onRoomConnected(int statusCode, Room room) {
//		Log.d(TAG, "onRoomConnected start");
//		// TODO Auto-generated method stub
//		Log.d(TAG, "onRoomConnected(" + statusCode + ", " + room + ")");
//		if (statusCode != GamesStatusCodes.STATUS_OK) {
//			Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
//			return;
//		}
//		Log.d(TAG, "onRoomConnected exit");
//	}
//
//
//	public void onRoomCreated(int statusCode, Room room) {
//		Log.d(TAG, "onRoomCreated start");
//		// TODO Auto-generated method stub
//		Log.d(TAG, "onRoomConnected(" + statusCode + ", " + room + ")");
//		if (statusCode != GamesStatusCodes.STATUS_OK) {
//			Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
//			return;
//		}
//		Log.d(TAG, "onRoomCreated exit");
//	}
//
//
//	public void onRealTimeMessageReceived(RealTimeMessage arg0) {
//		// TODO Auto-generated method stub
//		Log.d(TAG, "onRealTimeMessageReceived start");
//
//	}
//
//
//	public void onConnectedToRoom(Room arg0) {
//		Log.d(TAG, "onConnectedToRoom start");
//		// TODO Auto-generated method stub
//		Log.d(TAG, "onConnectedToRoom exit");
//	}
//
//
//	public void onDisconnectedFromRoom(Room arg0) {
//		// TODO Auto-generated method stub
//		Log.d(TAG, "onDisconnectedFromRoom start");
//
//	}
//
//
//	public void onPeerDeclined(Room arg0, List<String> arg1) {
//		// TODO Auto-generated method stub
//		Log.d(TAG, "onPeerDeclined start");
//
//	}
//
//
//	public void onPeerInvitedToRoom(Room arg0, List<String> arg1) {
//		// TODO Auto-generated method stub
//		Log.d(TAG, "onPeerInvitedToRoom start");
//
//	}
//
//
//	public void onPeerJoined(Room arg0, List<String> arg1) {
//		// TODO Auto-generated method stub
//		Log.d(TAG, "onPeerJoined start");
//
//	}
//
//
//	public void onPeerLeft(Room arg0, List<String> arg1) {
//		// TODO Auto-generated method stub
//		Log.d(TAG, "onPeerLeft start");
//	}
//
//
//	public void onPeersConnected(Room arg0, List<String> arg1) {
//		// TODO Auto-generated method stub
//		Log.d(TAG, "onPeersConnected start");
//	}
//
//
//	public void onPeersDisconnected(Room arg0, List<String> arg1) {
//		// TODO Auto-generated method stub
//		Log.d(TAG, "onPeersDisconnected start");
//	}
//
//
//	public void onRoomAutoMatching(Room arg0) {
//		// TODO Auto-generated method stub
//		Log.d(TAG, "onRoomAutoMatching start");
//	}
//
//
//	public void onRoomConnecting(Room arg0) {
//		// TODO Auto-generated method stub
//		Log.d(TAG, "onRoomConnecting start");
//
//	}
//
//
//	public void onInvitationReceived(Invitation invitation) {
//		Log.d(TAG, "onInvitationReceived start");
//		// TODO Auto-generated method stub
//		// show in-game popup to let user know of pending invitation
//
//
//		// store invitation for use when player accepts this invitation
//		mIncomingInvitationId = invitation.getInvitationId();
//		 ((TextView) findViewById(R.id.incoming_invitation_text)).setText(
//	                invitation.getInviter().getDisplayName() + " " +
//	                        getString(R.string.is_inviting_you));
//		 findViewById(R.id.invitation_popup).setVisibility(View.VISIBLE);
//		Log.d(TAG, "onInvitationReceived exit");
//
//	}


	public void onClick(View view) {
		Intent intent;

		//		if (view.getId() == R.id.sign_in_button && !mPlusClient.isConnected()) {
		//			// start the asynchronous sign in flow
		//			beginUserInitiatedSignIn();
		//		}
		//		else {
		//            try {
		//                mConnectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
		//            } catch (SendIntentException e) {
		//                // Try connecting again.
		//                mConnectionResult = null;
		//                mPlusClient.connect();
		//            }
		//        }
		//		if (view.getId() == R.id.sign_out_button) {
		//			// sign out.
		//			signOut();
		//
		//			// show sign-in button, hide the sign-out button
		//			findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
		//			findViewById(R.id.sign_out_button).setVisibility(View.GONE);
		//		}


		switch (view.getId()) {
			case R.id.sign_in_button:
			{

				intent = new Intent(this, GMGActivity.class);
				startActivity(intent);
				// start the asynchronous sign in flow
//			beginUserInitiatedSignIn();
//			if(!mPlusClient.isConnected()) {
//				if (mConnectionResult == null) {
//					mConnectionProgressDialog.show();
//				} else {
//					try {
//						mConnectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
//					} catch (SendIntentException e) {
//						// Try connecting again.
//						mConnectionResult = null;
//						mPlusClient.connect();
//					}
//				}
//			}

				break;
			}

			case R.id.sign_out_button:
			{
//			if (mPlusClient.isConnected()) {
//				mPlusClient.clearDefaultAccount();
//				mPlusClient.disconnect();
//				mPlusClient.connect();
//			}
				// sign out.
//			  mExplicitSignOut = true;
//		        mClient.disconnect();
//			signOut();
				//			mPlusOneButton.setVisibility(View.GONE);
				// show sign-in button, hide the sign-out button
				findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
				findViewById(R.id.sign_out_button).setVisibility(View.GONE);
				break;
			}
			//
			//            case R.id.button_sign_in:
			//                // user wants to sign in
			//                if (!verifyPlaceholderIdsReplaced()) {
			//                    showAlert("Error", "Sample not set up correctly. Please see README.");
			//                    return;
			//                }
			//                beginUserInitiatedSignIn();
			//                break;
			//            case R.id.button_sign_out:
			//                signOut();
			//                switchToScreen(R.id.screen_sign_in);
			//                break;
			//            case R.id.button_invite_players:
			//                // show list of invitable players
			//                intent = getGamesClient().getSelectPlayersIntent(1, 3);
			//                switchToScreen(R.id.screen_wait);
			//                startActivityForResult(intent, RC_SELECT_PLAYERS);
			//                break;
			//            case R.id.button_see_invitations:
			//                // show list of pending invitations
			//                intent = getGamesClient().getInvitationInboxIntent();
			//                switchToScreen(R.id.screen_wait);
			//                startActivityForResult(intent, RC_INVITATION_INBOX);
			//                break;
//		case R.id.button_accept_popup_invitation:
//		{
//			// user wants to accept the invitation shown on the invitation
//			// popup
//			// (the one we got through the OnInvitationReceivedListener).
////			acceptInviteToRoom(mIncomingInvitationId);
////			mIncomingInvitationId = null;
//			break;
//		}
			default:
				break;
			//            case R.id.button_quick_game:
			//                // user wants to play against a random opponent right now
			//                startQuickGame();
			//                break;
			//            case R.id.button_click_me:
			//                // (gameplay) user clicked the "click me" button
			//                scoreOnePoint();
			//                break;
		}
		// TODO Auto-generated method stub

	}

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
//	void keepScreenOn() {
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//	}
	// Accept the given invitation.
//	void acceptInviteToRoom(String invId) {
//		// accept the invitation
//		Log.d(TAG, "Accepting invitation: " + invId);
//		RoomConfig.Builder roomConfigBuilder = RoomConfig.builder(this);
//		roomConfigBuilder.setInvitationIdToAccept(invId)
//		.setMessageReceivedListener(this)
//		.setRoomStatusUpdateListener(this);
//		keepScreenOn();
//		 Games.RealTimeMultiplayer.join(getApiClient(),roomConfigBuilder.build());
//	}


//	public void onPeopleLoaded(ConnectionResult status, PersonBuffer personBuffer,
//			String arg2) {
//		// TODO Auto-generated method stub
//		 if (status.getErrorCode() == ConnectionResult.SUCCESS) {
//		        try {
//		            int count = personBuffer.getCount();
//		            for (int i = 0; i < count; i++) {
//		                Log.d(TAG, "Display Name: " + personBuffer.get(i).getDisplayName());
//		                personBuffer.get(i).getImage();
//		            }
//		        } finally {
//		            personBuffer.close();
//		        }
//		    } else {
//		        Log.e(TAG, "Error listing people: " + status.getErrorCode());
//		    }
//
//	}
//
//
//	public void onInvitationRemoved(String arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//
//	public void onP2PConnected(String arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//
//	public void onP2PDisconnected(String arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//
//	public void onResult(LoadPeopleResult arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//
//	public void onConnectionFailed(ConnectionResult arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//
//	public void onConnected(Bundle arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//
//	public void onDisconnected() {
//		// TODO Auto-generated method stub
//
//	}
//
//
//	public void onConnectionSuspended(int arg0) {
//		// TODO Auto-generated method stub
//
//	}

}
