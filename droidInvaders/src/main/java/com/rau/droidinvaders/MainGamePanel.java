package com.rau.droidinvaders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Process;
import android.util.Log;
import android.view.Display;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;

public class MainGamePanel extends SurfaceView implements
        SurfaceHolder.Callback {

    public static Play thread;

    public static final String SAVE_NAME = "MySaves";
    private static final String TAG = MainGamePanel.class.getSimpleName();
    public static final String SOUNDBTN = "SOUNDBTN";
    public static final String PLAYBTNSWTCH = "PLAYBTNSWTCH";
    public static final String WAVENUMB = "WAVENUMB";
    public static final String SCORENUMB = "SCORENUMB";
    public static final String LIVES = "LIVES";
    public static final String SETTINGSBTNSWTCH = "SETTINGSBTNSWTCH";
    public static final String COMMUNITYBTNSWTCH = "COMMUNITYBTNSWTCH";

    public static final int SCRNHGHTPRC=12;
    public static final int SCRNWDTHPRC=5;
    public static final int BTNHGHT=10;
    public static final int TXTPOS=BTNHGHT/2;

    public final static float SCALE = 0.7f;

    final static int STATE_SPLASH = 0;
    final static int STATE_MAIN_MENU = 1;
    final static int STATE_PLAY=2;
    final static int STATE_SETTINGS=3;
    final static int STATE_HELP=5;
    final static int STATE_NEWGAME=6;
    final static int STATE_GAMEOVER=7;
    final static int STATE_WAVECLEARED=8;
    final static int STATE_GAMEFINISHED=9;
    final static int STATE_COMMUNITY=10;
    final static int STATE_SHOP=11;

    // Time constants
    final static int SPLASH_TIME = 3000;
    boolean isSoundOn;
    boolean startGmg = false;
    boolean shipDestroyed = false;
    boolean wavefinshed = false;
    boolean continueWave =false;
    boolean stateReady = false;
    boolean isSettingsOpened = false;
    boolean isCommunityOpened = false;
    boolean isPlayOpened = false;
    boolean btnPopped = false;

    Random randomNum = new Random();
    int randomNum1;
    int clr[];
    int	screenwidth;
    int screenheight;
    int currentState;
    int scrnBtnLeftMargin;
    int scrnBtnTopMargin;
    int scorenr;
    int prevScore;
    int waveNumber;
    int enemyOffsetCount=0;
    int livesNumber=3;
    int ammunitionSpeed=50;
    int enemyNumber;
    int btnwidth;
    int btnheight;


    //gameplay variables
    int shipW=screenwidth/15;
    int shipH=screenheight/14;
    int shipVOffSet=screenheight/20;
    int enemyH=screenheight/16;
    int enemyW=screenwidth/22;
    int enemyHSpacing=shipW/3;
    int enemyVSpacing=screenheight/35;
    int shipHOffSet;
    int EnemyHp;

    long startAppTime;
    long gameoverTime;
    long currentTime;
    long ammunitionTime;
    long enemyAmmunitionTime;
    long waveInitTime;
    long invicibilityLength=5000;

    Bitmap splashpic;
    Bitmap background;
    Bitmap bigCircle;
    Bitmap smallCircle;
    Bitmap shipBitmap;
    Bitmap enemyBitmap;
    Bitmap enemyL2Bitmap;
    Bitmap lasere;
    Bitmap lasers;
    Bitmap buttonBack;
    Bitmap explosionSprite;
    Bitmap signInImg;
    Bitmap signOutImg;

    Random rnd = new Random();

    Display display;

    Context maincontext;

    Paint paint;
    Paint painttext;

    Rect score;
    Rect splashrect;
    Rect play;
    Rect community;
    Rect settings;
    Rect help;
    Rect gmg;
    Rect newGame;
    Rect continueGame;
    Rect sounds;
    Rect soundOn;
    Rect soundOff;
    Rect resetGame;
    Rect signIn;

    //google+ game services
    Rect achievements;
    Rect premiumship;
    Rect premiumSoundPack;
    Rect removeAds;
    Rect extralife;
    Rect leaderboards;
    Rect invitePlayer;
    Rect invitationsBox;
    Rect quickGame;


    Rect firstSprite;
    Rect secondSprite;
    Rect thirdSprite;
    Rect fourthSprite;
    Rect currentSprite;
    Rect explosionRect;

    Rect invincibilityRect;

    Rect screenBounds;

    SharedPreferences sp;


    CButton btnStart;
    CButton btnCommunity;
    CButton btnSettings;
    CButton btnHelp;
    CButton btnGmg;
    CButton btnNewgame;
    CButton btnContinuegame;
    CButton btnSounds;
    CButton btnResetgame;
    CButton btnSignIn;
    CButton btnSignOut;
    CButton btnAchievements;
    CButton btnLeaderboards;
    CButton btnInvite;
    CButton btnInvitesBox;
    CButton btnRemoveAds;
    CButton btnPremiumShip;
    CButton btnPremiumSoundPack;
    CButton btnExtraLife;

    ArrayList<CButton> menuButtons;
    ArrayList<CButton> settingsButtons;
    ArrayList<CButton> startButtons;
    ArrayList<CButton> communityButtons;
    ArrayList<CButton> shopButtons;

    CEnemy e;

    MediaPlayer menuplayer;
    MediaPlayer gameplayer;
    MediaPlayer sfx;
    SoundPool soundSfx;
    int slaser;
    int explosion;
    int buttonClick;
    int maxVolume = 50;
    float volume;

    Background gameplayBackground;

    // Game objects
    CShip ship;
    CGamepad gamepad;
    Touchpad touchpad;
    CAmmunition a;
    ArrayList<CAmmunition> ammun;
    ArrayList<CAmmunition> enemyAmmun;
    ArrayList<CEnemy> enemy;
    ArrayList<Point> pathW2A;
    ArrayList<Point> pathIntro;

    //lives
    ArrayList<Rect> livesRect;
    ArrayList<Rect> bossLivesRect;

    ArrayList<Animation> animList;




    private static MainGamePanel instance;
    public static MainGamePanel GetInstance()
    {
        return instance;
    }
    public MainGamePanel(Context context, int screenWidth, int screenHeight) {
        super(context);
        instance = this;
        maincontext=context;
//        display = ((Activity)maincontext).getWindowManager().getDefaultDisplay();
        splashpic = BitmapFactory.decodeResource(getResources(), R.drawable.ura_logo);

        // retine width si heightul deviceului
        screenwidth = screenWidth;
        screenheight = screenHeight;

        gameoverTime=0;

        //set sharedprefs(creaza un fisier pentru retinerea setarilor de sunet si progresul de gameplay)
        sp = maincontext.getSharedPreferences(SAVE_NAME, Context.MODE_PRIVATE);

        //incarca resursele in variabile
        bigCircle= BitmapFactory.decodeResource(getResources(), R.drawable.bigcircle);
        smallCircle= BitmapFactory.decodeResource(getResources(), R.drawable.smallcircle);
        if(((MainActivity)maincontext).iapSP.getBoolean(MainActivity.SKU_PREMIUM_SHIP, false))
        {
            background = BitmapFactory.decodeResource(getResources(), R.drawable.space_premium);
            shipBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.premium_ship);
            enemyBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.premium_enemy);
            enemyL2Bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.premium_enemyl2);
            lasers= BitmapFactory.decodeResource(getResources(), R.drawable.premium_lasers);
            lasere= BitmapFactory.decodeResource(getResources(), R.drawable.premium_laser);
            explosionSprite=BitmapFactory.decodeResource(getResources(), R.drawable.explosion_sprite2);
        }
        else
        {
            background = BitmapFactory.decodeResource(getResources(), R.drawable.spacebackgroundland);
            shipBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.ship);
            enemyBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.enemy);
            enemyL2Bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.enemyl2);
            lasers= BitmapFactory.decodeResource(getResources(), R.drawable.lasers);
            lasere= BitmapFactory.decodeResource(getResources(), R.drawable.lasere);
            explosionSprite=BitmapFactory.decodeResource(getResources(), R.drawable.explosprite);
        }
        buttonBack=BitmapFactory.decodeResource(getResources(), R.drawable.buttonb);
        signInImg=BitmapFactory.decodeResource(getResources(), R.drawable.common_signin_btn_icon_normal);
        signOutImg=BitmapFactory.decodeResource(getResources(), R.drawable.common_signin_btn_icon_disabled);

        //creaza backgroundul folosit clasa Background si resursa de background
        gameplayBackground = new Background(background, screenwidth, screenheight);

        //		//create explosion sprites
        //		firstSprite=new Rect(0,0,explosionSprite.getWidth()/4,explosionSprite.getHeight());
        //		secondSprite=new Rect(explosionSprite.getWidth()/4,0,explosionSprite.getWidth()/2,explosionSprite.getHeight());
        //		thirdSprite=new Rect(explosionSprite.getWidth()/2,0,explosionSprite.getWidth()*3/4,explosionSprite.getHeight());
        //		fourthSprite=new Rect(explosionSprite.getWidth()*3/4,0,explosionSprite.getWidth(),explosionSprite.getHeight());

        //calculeaza marginile pentru butoane in functie de dimensiunile ecranului
        scrnBtnLeftMargin=screenwidth/12;
        scrnBtnTopMargin=screenheight/14;

        //create mediaplayers pentru muzica si sound effects
        if(((MainActivity)maincontext).iapSP.getBoolean(MainActivity.SKU_PREMIUM_SOUND, false))
        {
            menuplayer = MediaPlayer.create(context, R.raw.premium_gameplay_sound2);
            gameplayer = MediaPlayer.create(context, R.raw.premium_gameplay_sound);
        }
        else
        {
            menuplayer = MediaPlayer.create(context, R.raw.gameplaysound1);
            gameplayer = MediaPlayer.create(context, R.raw.gameplaysound2);
        }
        menuplayer.setLooping(true);
        gameplayer.setLooping(true);
        volume=(float)(Math.log(maxVolume-40)/Math.log(maxVolume));
        gameplayer.setVolume(volume, volume);
        soundSfx = new SoundPool(20, AudioManager.STREAM_MUSIC,0);
        buttonClick=soundSfx.load(context, R.raw.buttonclick,1);
        if(((MainActivity)maincontext).iapSP.getBoolean(MainActivity.SKU_PREMIUM_SOUND, false))
        {
            slaser = soundSfx.load(context, R.raw.premium_laser, 1);
            explosion=soundSfx.load(context, R.raw.premium_explosion, 1);
        }
        else
        {
            slaser = soundSfx.load(context, R.raw.laser, 1);
            explosion=soundSfx.load(context, R.raw.explosion, 1);
        }

        isSoundOn = sp.getBoolean(SOUNDBTN, false);

        //set splash screen
        int newWidth = (int) (splashpic.getWidth() * SCALE);
        int newHeight = (int) (splashpic.getHeight() * SCALE);
        int left = (screenwidth - newWidth) / 2;
        int top = (screenheight - newHeight) / 2;
        splashrect = new Rect (left, top, left + newWidth, top + newHeight);

        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // create the game loop thread
        thread = new Play(getHolder(), this);

        // make the GamePanel focusable so it can handle events
        setFocusable(true);

        //creaza obiectele de tip paint pentru desenarea pe ecran
        paint= new Paint();
        painttext = new Paint();

        //seteaza fontul folosind resursa de font
        Typeface tf = Typeface.createFromAsset(maincontext.getAssets(), "fonts/roboto.ttf");
        painttext.setTypeface(tf);

        //retine data la care a fost deschisa aplicatia
        startAppTime = System.currentTimeMillis();
        ammunitionTime = startAppTime;

        //seteaza ecranul curent
        currentState = STATE_SPLASH;

        screenBounds = new Rect (0, 0, screenwidth, screenheight);

        animList = new ArrayList<Animation>();
    }
    // 4	@Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }
    //	3 @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // at this point the surface is created and
        // we can safely play the game loop
        if (thread.getState() == Thread.State.TERMINATED)
        {
            thread = null;
            thread = new Play(getHolder(), this);
        }

        thread.setRunning(true);

        Process.setThreadPriority(Process.myTid(),
                Process.THREAD_PRIORITY_URGENT_DISPLAY);
        thread.start();

    }
    //	2 @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Surface is being destroyed");
        // tell the thread to shut down and wait for it to finish
        // this is a clean shutdown
        boolean retry = true;
        thread.setRunning(false);
        while (retry)
        {
            try
            {
                thread.join();
                retry = false;
            }
            catch (InterruptedException e)
            {
                // try again shutting down the thread
            }
        }
        Log.d(TAG, "Thread was shut down cleanly");
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        //daca nava a fost creata, trimite eventul de touch catre gamepad pentru a verifica daca joystickul contine eventul
        if(ship!=null)
            //			gamepad.onTouch(event);
            touchpad.onTouch(event);


        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            buttonPopUp(event);

            if(currentState==STATE_SPLASH)
            {
                return true;
            }
            else if (currentState==STATE_MAIN_MENU)
            {
                //daca butonul de help a fost apasat deschide ecranul de help
                if(help.contains((int)event.getX(), (int)event.getY()))
                {
                    transittostate(STATE_HELP);
                }
                else if(gmg.contains((int)event.getX(), (int)event.getY()))
                {
                    transittostate(STATE_SHOP);
                }
                //daca butonul de play a fost apasat deschide meniul de play
                else if (play.contains((int)event.getX(), (int)event.getY()))
                {
                    isPlayOpened = sp.getBoolean(PLAYBTNSWTCH, false);
                    sp.edit().putBoolean(PLAYBTNSWTCH, !isPlayOpened).commit();

                    if(isPlayOpened==false)
                    {
                        transittostate(STATE_PLAY);
                    }
                    else
                    {
                        transittostate(STATE_MAIN_MENU);
                    }
                }
                //daca butonul de settings a fost apasat deschide ecranul de settings
                else if (settings.contains((int)event.getX(), (int)event.getY()))
                {
                    isSettingsOpened = sp.getBoolean(SETTINGSBTNSWTCH, false);
                    if(isSettingsOpened==false)
                        transittostate(STATE_SETTINGS);
                    sp.edit().putBoolean(SETTINGSBTNSWTCH, !isSettingsOpened).commit();
                }
                else if(community.contains((int)event.getX(), (int)event.getY()))
                {
                    isCommunityOpened = sp.getBoolean(COMMUNITYBTNSWTCH, false);
                    sp.edit().putBoolean(COMMUNITYBTNSWTCH, !isCommunityOpened).commit();
                    transittostate(STATE_COMMUNITY);
                }
                else
                    //										if(signIn.contains((int)event.getX(), (int)event.getY()))
                    //										{
                    //											if(!((MainActivity) maincontext).getSignInStatus())
                    //											{
                    //												((MainActivity) maincontext).signInStart();
                    //											}
                    //											else
                    //											{
                    //												((MainActivity) maincontext).signOutStart();
                    //											}
                    //										}
                    //										else
                    transittostate(STATE_MAIN_MENU);
            }
            else if(currentState==STATE_COMMUNITY)
            {
                if (achievements != null && achievements.contains((int)event.getX(), (int)event.getY()))
                {
                    //							if(!((MainActivity) maincontext).getSignInStatus())
                    //							{
                    //								((MainActivity) maincontext).signInStart();
                    //							}
                    //							else

//					((MainActivity) maincontext).openAchievements();
                }
                else if (leaderboards != null && leaderboards.contains((int)event.getX(), (int)event.getY()))
                {
                    //							if(!((MainActivity) maincontext).getSignInStatus())
                    //							{
                    //								((MainActivity) maincontext).signInStart();
                    //							}
                    //							else

//					((MainActivity) maincontext).openLeaderboards();
                }
                else
                {
                    transittostate(STATE_MAIN_MENU);
                    isPlayOpened = sp.getBoolean(PLAYBTNSWTCH, false);
                    sp.edit().putBoolean(PLAYBTNSWTCH, !isPlayOpened).commit();
                }
            }
            else if(currentState==STATE_PLAY)
            {
                //daca butonul de newGame a fost apasat deschide un joc nou
                if (newGame != null && newGame.contains((int)event.getX(), (int)event.getY()))
                {
                    newGameInit();
                }
                //daca butonul de continueGame a fost apasat deschide jocul anterior
                else if (continueGame != null && continueGame.contains((int)event.getX(), (int)event.getY()))
                {
                    continueGameInit();
                }
                else if (invitationsBox != null && invitationsBox.contains((int)event.getX(), (int)event.getY()))
                {
//					((MainActivity) maincontext).openInvitationInbox();
                }
                else if (invitePlayer != null && invitePlayer.contains((int)event.getX(), (int)event.getY()))
                {
//					((MainActivity) maincontext).openInviteFriend();
                }
                else
                {
                    transittostate(STATE_MAIN_MENU);
                    isPlayOpened = sp.getBoolean(PLAYBTNSWTCH, false);
                    sp.edit().putBoolean(PLAYBTNSWTCH, !isPlayOpened).commit();
                }
            }
            //daca jocul a fost pierdut si ecranul este apasat se intoarce in meniu
            else if(currentState==STATE_GAMEOVER)
            {
                transittostate(STATE_MAIN_MENU);
            }
            //daca jocul a fost castigat si ecranul este apasat se intoarce in meniu
            else if(currentState==STATE_GAMEFINISHED)
            {
                transittostate(STATE_MAIN_MENU);
            }
            //daca nivelul a fost terminat se trece la nivelul urmator
            else if(currentState==STATE_WAVECLEARED)
            {
                transittostate(STATE_NEWGAME);
            }else if(currentState==STATE_SHOP)
            {
                for(int i=0;i<shopButtons.size();i++)
                {
                    if(shopButtons.get(i).rect.contains((int)event.getX(), (int)event.getY()))
                    {
                        switch (i){
                            case 0:
                                ((MainActivity) maincontext).buyItemSubscription(MainActivity.SKU_REMOVE_ADS);
                                break;
                            case 1:
                                ((MainActivity) maincontext).buyItemSubscription(MainActivity.SKU_PREMIUM_SHIP);
                                break;
                            case 2:
                                ((MainActivity) maincontext).buyItemSubscription(MainActivity.SKU_PREMIUM_SOUND);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            //daca buttonul de sunet a fost apasat se
            else if(currentState==STATE_SETTINGS)
            {
                if(sounds !=null && sounds.contains((int)event.getX(), (int)event.getY()))
                {
                    isSoundOn = sp.getBoolean(SOUNDBTN, false);
                    sp.edit().putBoolean(SOUNDBTN, !isSoundOn).commit();
                }
                else if(resetGame!=null && resetGame.contains((int)event.getX(), (int)event.getY()))
                {

                }
                else
                {
                    isSettingsOpened = sp.getBoolean(SETTINGSBTNSWTCH, false);
                    sp.edit().putBoolean(SETTINGSBTNSWTCH, !isSettingsOpened).commit();
                    transittostate(STATE_MAIN_MENU);
                }
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            buttonPopDown(event);
        }
        return true;
    }
    public boolean keyDown(int keyCode, KeyEvent event) {
        //daca back key este apasat se intoarce in ecranul anterior
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            switch (currentState)
            {
                case STATE_HELP:
                    transittostate(STATE_MAIN_MENU);
                    break;

                case STATE_SHOP:
                    transittostate(STATE_MAIN_MENU);
                    break;

                case STATE_PLAY:

                    isPlayOpened = sp.getBoolean(PLAYBTNSWTCH, false);
                    sp.edit().putBoolean(PLAYBTNSWTCH, !isPlayOpened).commit();
                    transittostate(STATE_MAIN_MENU);

                    break;
                case STATE_SETTINGS:
                    isSettingsOpened = sp.getBoolean(SETTINGSBTNSWTCH, false);
                    sp.edit().putBoolean(SETTINGSBTNSWTCH, !isSettingsOpened).commit();
                    transittostate(STATE_MAIN_MENU);
                    break;
                case STATE_COMMUNITY:
                    isCommunityOpened = sp.getBoolean(COMMUNITYBTNSWTCH, false);
                    sp.edit().putBoolean(COMMUNITYBTNSWTCH, !isCommunityOpened).commit();
                    transittostate(STATE_MAIN_MENU);
                    break;
                case STATE_NEWGAME:
                    isPlayOpened = sp.getBoolean(PLAYBTNSWTCH, false);
                    sp.edit().putBoolean(PLAYBTNSWTCH, !isPlayOpened).commit();
                    transittostate(STATE_MAIN_MENU);
                    break;
                //daca back key este apasat se inchide aplicatia
                case STATE_MAIN_MENU:
                    CAmmunition.clearPool();
                    if(menuplayer!=null)
                    {
                        menuplayer.stop();
                        menuplayer.release();
                    }
                    ((Activity) maincontext).finish();
                    break;
            }
            return true;
        }
        else
        {
            switch(event.getKeyCode())
            {
                case KeyEvent.KEYCODE_BUTTON_A:
                    if(currentState==STATE_GAMEOVER)
                    {
                        transittostate(STATE_MAIN_MENU);
                    }else
                    if(currentState==STATE_WAVECLEARED)
                    {
                        transittostate(STATE_NEWGAME);
                    }else
                    if(currentState==STATE_NEWGAME)
                    {
                        touchpad.fireBool=true;
                    }
                    else
                    if(currentState==STATE_MAIN_MENU)
                    {
                        for(int i=0;i<menuButtons.size();i++)
                        {
                            if(menuButtons.get(i).rect.width()!=btnwidth)
                            {
                                switch (i){
                                    case 0:
                                        isPlayOpened = sp.getBoolean(PLAYBTNSWTCH, false);
                                        sp.edit().putBoolean(PLAYBTNSWTCH, !isPlayOpened).commit();
                                        transittostate(STATE_PLAY);
                                        break;
                                    case 1:
                                        isCommunityOpened = sp.getBoolean(COMMUNITYBTNSWTCH, false);
                                        sp.edit().putBoolean(COMMUNITYBTNSWTCH, !isCommunityOpened).commit();
                                        transittostate(STATE_COMMUNITY);
                                        break;
                                    case 2:
                                        isSettingsOpened = sp.getBoolean(SETTINGSBTNSWTCH, false);
                                        sp.edit().putBoolean(SETTINGSBTNSWTCH, !isSettingsOpened).commit();
                                        transittostate(STATE_SETTINGS);
                                        break;
                                    case 3:
                                        transittostate(STATE_HELP);
                                        break;
                                    case 4:
                                        transittostate(STATE_SHOP);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                    else
                    if(currentState==STATE_SETTINGS)
                    {
                        for(int i=0;i<settingsButtons.size();i++)
                        {
                            if(settingsButtons.get(i).rect.width()!=btnwidth)
                            {
                                switch (i){
                                    case 0:
                                        isSoundOn = sp.getBoolean(SOUNDBTN, false);
                                        sp.edit().putBoolean(SOUNDBTN, !isSoundOn).commit();
                                        break;
                                    case 1:
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                    else
                    if(currentState==STATE_SHOP)
                    {
                        for(int i=0;i<shopButtons.size();i++)
                        {
                            if(shopButtons.get(i).rect.width()!=btnwidth)
                            {
                                switch (i){
                                    case 0:
                                        ((MainActivity) maincontext).buyItemSubscription(MainActivity.SKU_REMOVE_ADS);
                                        break;
                                    case 1:
                                        ((MainActivity) maincontext).buyItemSubscription(MainActivity.SKU_PREMIUM_SHIP);
                                        break;
                                    case 2:
                                        ((MainActivity) maincontext).buyItemSubscription(MainActivity.SKU_PREMIUM_SOUND);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                    else
                    if(currentState==STATE_PLAY)
                    {
                        for(int i=0;i<startButtons.size();i++)
                        {
                            if(startButtons.get(i).rect.width()!=btnwidth)
                            {
                                switch (i){
                                    case 0:
                                        newGameInit();
                                        break;
                                    case 1:
                                        continueGameInit();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                    break;
                case KeyEvent.KEYCODE_BUTTON_X:
                    switch (currentState)
                    {
                        case STATE_HELP:
                            transittostate(STATE_MAIN_MENU);
                            break;

                        case STATE_SHOP:
                            transittostate(STATE_MAIN_MENU);
                            break;

                        case STATE_PLAY:

                            isPlayOpened = sp.getBoolean(PLAYBTNSWTCH, false);
                            sp.edit().putBoolean(PLAYBTNSWTCH, !isPlayOpened).commit();
                            transittostate(STATE_MAIN_MENU);

                            break;
                        case STATE_SETTINGS:
                            isSettingsOpened = sp.getBoolean(SETTINGSBTNSWTCH, false);
                            sp.edit().putBoolean(SETTINGSBTNSWTCH, !isSettingsOpened).commit();
                            transittostate(STATE_MAIN_MENU);
                            break;
                        case STATE_NEWGAME:
                            isPlayOpened = sp.getBoolean(PLAYBTNSWTCH, false);
                            sp.edit().putBoolean(PLAYBTNSWTCH, !isPlayOpened).commit();
                            transittostate(STATE_MAIN_MENU);
                            break;
                        //daca back key este apasat se inchide aplicatia
                        case STATE_MAIN_MENU:
                            CAmmunition.clearPool();
                            if(menuplayer!=null)
                            {
                                menuplayer.stop();
                                menuplayer.release();
                            }
                            ((Activity) maincontext).finish();
                            break;
                    }

                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    if(event.getAction() == KeyEvent.ACTION_DOWN)
                    {
                        if(currentState==STATE_NEWGAME)
                        {
                            ship.mAxisY=0;
                        }
                        else
                        if(currentState==STATE_MAIN_MENU)
                        {
                            for(int i=0;i<menuButtons.size();i++)
                            {
                                if(menuButtons.get(i).rect.width()!=btnwidth)
                                {
                                    menuButtons.get(i).rect.inset(10, 10);
                                    if(i-1>=0)
                                        menuButtons.get(i-1).rect.inset(-10, -10);
                                    else
                                        menuButtons.get(menuButtons.size()-1).rect.inset(-10, -10);
                                    btnPopped=true;
                                    break;
                                }
                            }
                            if(btnPopped==false)
                            {
                                menuButtons.get(menuButtons.size()-1).rect.inset(-10, -10);
                                btnPopped=true;
                            }
                        }
                        else
                        if(currentState==STATE_SETTINGS)
                        {
                            for(int i=0;i<settingsButtons.size();i++)
                            {
                                if(settingsButtons.get(i).rect.width()!=btnwidth)
                                {
                                    settingsButtons.get(i).rect.inset(10, 10);
                                    if(i-1>=0)
                                        settingsButtons.get(i-1).rect.inset(-10, -10);
                                    else
                                        settingsButtons.get(settingsButtons.size()-1).rect.inset(-10, -10);
                                    btnPopped=true;
                                    break;
                                }
                            }
                            if(btnPopped==false)
                            {
                                settingsButtons.get(settingsButtons.size()-1).rect.inset(-10, -10);
                                btnPopped=true;
                            }
                        }
                        else
                        if(currentState==STATE_SHOP)
                        {
                            for(int i=0;i<shopButtons.size();i++)
                            {
                                if(shopButtons.get(i).rect.width()!=btnwidth)
                                {
                                    shopButtons.get(i).rect.inset(10, 10);
                                    if(i-1>=0)
                                        shopButtons.get(i-1).rect.inset(-10, -10);
                                    else
                                        shopButtons.get(shopButtons.size()-1).rect.inset(-10, -10);
                                    btnPopped=true;
                                    break;
                                }
                            }
                            if(btnPopped==false)
                            {
                                shopButtons.get(shopButtons.size()-1).rect.inset(-10, -10);
                                btnPopped=true;
                            }
                        }
                        else
                        if(currentState==STATE_PLAY)
                        {
                            for(int i=0;i<startButtons.size();i++)
                            {
                                if(startButtons.get(i).rect.width()!=btnwidth)
                                {
                                    startButtons.get(i).rect.inset(10, 10);
                                    if(i-1>=0)
                                        startButtons.get(i-1).rect.inset(-10, -10);
                                    else
                                        startButtons.get(startButtons.size()-1).rect.inset(-10, -10);
                                    btnPopped=true;
                                    break;
                                }
                            }
                            if(btnPopped==false)
                            {
                                startButtons.get(startButtons.size()-1).rect.inset(-10, -10);
                                btnPopped=true;
                            }
                        }
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:

                    if(event.getAction() == KeyEvent.ACTION_DOWN)
                    {
                        if(currentState==STATE_NEWGAME)
                        {
                            ship.mAxisY=0;
                        }
                        else
                        if(currentState==STATE_MAIN_MENU)
                        {
                            for(int i=0;i<menuButtons.size();i++)
                            {
                                if(menuButtons.get(i).rect.width()!=btnwidth)
                                {
                                    menuButtons.get(i).rect.inset(10, 10);
                                    if(i+1<menuButtons.size())
                                        menuButtons.get(i+1).rect.inset(-10, -10);
                                    else
                                        menuButtons.get(0).rect.inset(-10, -10);
                                    btnPopped=true;
                                    break;
                                }
                            }
                            if(btnPopped==false)
                            {
                                menuButtons.get(0).rect.inset(-10, -10);
                                btnPopped=true;
                            }
                        }
                        else
                        if(currentState==STATE_SETTINGS)
                        {
                            for(int i=0;i<settingsButtons.size();i++)
                            {
                                if(settingsButtons.get(i).rect.width()!=btnwidth)
                                {
                                    settingsButtons.get(i).rect.inset(10, 10);
                                    if(i+1<settingsButtons.size())
                                        settingsButtons.get(i+1).rect.inset(-10, -10);
                                    else
                                        settingsButtons.get(0).rect.inset(-10, -10);
                                    btnPopped=true;
                                    break;
                                }
                            }
                            if(btnPopped==false)
                            {
                                settingsButtons.get(0).rect.inset(-10, -10);
                                btnPopped=true;
                            }

                        }
                        else
                        if(currentState==STATE_SHOP)
                        {
                            for(int i=0;i<shopButtons.size();i++)
                            {
                                if(shopButtons.get(i).rect.width()!=btnwidth)
                                {
                                    shopButtons.get(i).rect.inset(10, 10);
                                    if(i+1<shopButtons.size())
                                        shopButtons.get(i+1).rect.inset(-10, -10);
                                    else
                                        shopButtons.get(0).rect.inset(-10, -10);
                                    btnPopped=true;
                                    break;
                                }
                            }
                            if(btnPopped==false)
                            {
                                shopButtons.get(0).rect.inset(-10, -10);
                                btnPopped=true;
                            }

                        }
                        else
                        if(currentState==STATE_PLAY)
                        {
                            for(int i=0;i<startButtons.size();i++)
                            {
                                if(startButtons.get(i).rect.width()!=btnwidth)
                                {
                                    startButtons.get(i).rect.inset(10, 10);
                                    if(i+1<startButtons.size())
                                        startButtons.get(i+1).rect.inset(-10, -10);
                                    else
                                        startButtons.get(0).rect.inset(-10, -10);
                                    btnPopped=true;
                                    break;
                                }
                            }
                            if(btnPopped==false)
                            {
                                startButtons.get(0).rect.inset(-10, -10);
                                btnPopped=true;
                            }

                        }
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if(event.getAction() == KeyEvent.ACTION_DOWN)
                    {
                        if(ship!=null)
                            ship.mAxisX=0;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if(event.getAction() == KeyEvent.ACTION_DOWN)
                    {
                        if(ship!=null)
                            ship.mAxisX=0;
                    }
                    break;



            }
        }
        return false;
    }
    public boolean keyUp(int keyCode, KeyEvent event)
    {
        switch(keyCode) {
            case KeyEvent.KEYCODE_BUTTON_A:
                if (currentState == STATE_NEWGAME) {
                    touchpad.fireBool = false;
                }
        }
        return true;
    }

    public void keyEvent (KeyEvent event)
    {
        switch(event.getKeyCode())
        {
            case KeyEvent.KEYCODE_BUTTON_A:
                if(currentState==STATE_GAMEOVER)
                {
                    transittostate(STATE_MAIN_MENU);
                }else
                if(currentState==STATE_WAVECLEARED)
                {
                    transittostate(STATE_NEWGAME);
                }else
                if(currentState==STATE_NEWGAME)
                {
                    if(event.getAction() == KeyEvent.ACTION_DOWN)
                    {
                        touchpad.fireBool=true;
                    }
                    else
                    {
                        touchpad.fireBool=false;
                    }
                }
                else
                if(currentState==STATE_MAIN_MENU)
                {
                    for(int i=0;i<menuButtons.size();i++)
                    {
                        if(menuButtons.get(i).rect.width()!=btnwidth)
                        {
                            switch (i){
                                case 0:
                                    isPlayOpened = sp.getBoolean(PLAYBTNSWTCH, false);
                                    sp.edit().putBoolean(PLAYBTNSWTCH, !isPlayOpened).commit();
                                    transittostate(STATE_PLAY);
                                    break;
                                case 1:
                                    isCommunityOpened = sp.getBoolean(COMMUNITYBTNSWTCH, false);
                                    sp.edit().putBoolean(COMMUNITYBTNSWTCH, !isCommunityOpened).commit();
                                    transittostate(STATE_COMMUNITY);
                                    break;
                                case 2:
                                    isSettingsOpened = sp.getBoolean(SETTINGSBTNSWTCH, false);
                                    sp.edit().putBoolean(SETTINGSBTNSWTCH, !isSettingsOpened).commit();
                                    transittostate(STATE_SETTINGS);
                                    break;
                                case 3:
                                    transittostate(STATE_HELP);
                                    break;
                                case 4:
                                    transittostate(STATE_SHOP);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
                else
                if(currentState==STATE_SETTINGS)
                {
                    for(int i=0;i<settingsButtons.size();i++)
                    {
                        if(settingsButtons.get(i).rect.width()!=btnwidth)
                        {
                            switch (i){
                                case 0:
                                    isSoundOn = sp.getBoolean(SOUNDBTN, false);
                                    sp.edit().putBoolean(SOUNDBTN, !isSoundOn).commit();
                                    break;
                                case 1:
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
                else
                if(currentState==STATE_PLAY)
                {
                    for(int i=0;i<startButtons.size();i++)
                    {
                        if(startButtons.get(i).rect.width()!=btnwidth)
                        {
                            switch (i){
                                case 0:
                                    newGameInit();
                                    break;
                                case 1:
                                    continueGameInit();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
                break;
            case KeyEvent.KEYCODE_BUTTON_X:
                switch (currentState)
                {
                    case STATE_HELP:
                        transittostate(STATE_MAIN_MENU);
                        break;

                    case STATE_SHOP:
                        transittostate(STATE_MAIN_MENU);
                        break;

                    case STATE_PLAY:

                        isPlayOpened = sp.getBoolean(PLAYBTNSWTCH, false);
                        sp.edit().putBoolean(PLAYBTNSWTCH, !isPlayOpened).commit();
                        transittostate(STATE_MAIN_MENU);

                        break;
                    case STATE_SETTINGS:
                        isSettingsOpened = sp.getBoolean(SETTINGSBTNSWTCH, false);
                        sp.edit().putBoolean(SETTINGSBTNSWTCH, !isSettingsOpened).commit();
                        transittostate(STATE_MAIN_MENU);
                        break;
                    case STATE_NEWGAME:
                        isPlayOpened = sp.getBoolean(PLAYBTNSWTCH, false);
                        sp.edit().putBoolean(PLAYBTNSWTCH, !isPlayOpened).commit();
                        transittostate(STATE_MAIN_MENU);
                        break;
                    //daca back key este apasat se inchide aplicatia
                    case STATE_MAIN_MENU:
                        CAmmunition.clearPool();
                        if(menuplayer!=null)
                        {
                            menuplayer.stop();
                            menuplayer.release();
                        }
                        ((Activity) maincontext).finish();
                        break;
                }

                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                if(event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    if(currentState==STATE_NEWGAME)
                    {
                        ship.mAxisY=0;
                    }
                    else
                    if(currentState==STATE_MAIN_MENU)
                    {
                        for(int i=0;i<menuButtons.size();i++)
                        {
                            if(menuButtons.get(i).rect.width()!=btnwidth)
                            {
                                menuButtons.get(i).rect.inset(10, 10);
                                if(i-1>=0)
                                    menuButtons.get(i-1).rect.inset(-10, -10);
                                else
                                    menuButtons.get(menuButtons.size()-1).rect.inset(-10, -10);
                                btnPopped=true;
                                break;
                            }
                        }
                        if(btnPopped==false)
                        {
                            menuButtons.get(menuButtons.size()-1).rect.inset(-10, -10);
                            btnPopped=true;
                        }
                    }
                    else
                    if(currentState==STATE_SETTINGS)
                    {
                        for(int i=0;i<settingsButtons.size();i++)
                        {
                            if(settingsButtons.get(i).rect.width()!=btnwidth)
                            {
                                settingsButtons.get(i).rect.inset(10, 10);
                                if(i-1>=0)
                                    settingsButtons.get(i-1).rect.inset(-10, -10);
                                else
                                    settingsButtons.get(settingsButtons.size()-1).rect.inset(-10, -10);
                                btnPopped=true;
                                break;
                            }
                        }
                        if(btnPopped==false)
                        {
                            settingsButtons.get(settingsButtons.size()-1).rect.inset(-10, -10);
                            btnPopped=true;
                        }
                    }
                    else
                    if(currentState==STATE_PLAY)
                    {
                        for(int i=0;i<startButtons.size();i++)
                        {
                            if(startButtons.get(i).rect.width()!=btnwidth)
                            {
                                startButtons.get(i).rect.inset(10, 10);
                                if(i-1>=0)
                                    startButtons.get(i-1).rect.inset(-10, -10);
                                else
                                    startButtons.get(startButtons.size()-1).rect.inset(-10, -10);
                                btnPopped=true;
                                break;
                            }
                        }
                        if(btnPopped==false)
                        {
                            startButtons.get(startButtons.size()-1).rect.inset(-10, -10);
                            btnPopped=true;
                        }
                    }
                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:

                if(event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    if(currentState==STATE_NEWGAME)
                    {
                        ship.mAxisY=0;
                    }
                    else
                    if(currentState==STATE_MAIN_MENU)
                    {
                        for(int i=0;i<menuButtons.size();i++)
                        {
                            if(menuButtons.get(i).rect.width()!=btnwidth)
                            {
                                menuButtons.get(i).rect.inset(10, 10);
                                if(i+1<menuButtons.size())
                                    menuButtons.get(i+1).rect.inset(-10, -10);
                                else
                                    menuButtons.get(0).rect.inset(-10, -10);
                                btnPopped=true;
                                break;
                            }
                        }
                        if(btnPopped==false)
                        {
                            menuButtons.get(0).rect.inset(-10, -10);
                            btnPopped=true;
                        }
                    }
                    else
                    if(currentState==STATE_SETTINGS)
                    {
                        for(int i=0;i<settingsButtons.size();i++)
                        {
                            if(settingsButtons.get(i).rect.width()!=btnwidth)
                            {
                                settingsButtons.get(i).rect.inset(10, 10);
                                if(i+1<settingsButtons.size())
                                    settingsButtons.get(i+1).rect.inset(-10, -10);
                                else
                                    settingsButtons.get(0).rect.inset(-10, -10);
                                btnPopped=true;
                                break;
                            }
                        }
                        if(btnPopped==false)
                        {
                            settingsButtons.get(0).rect.inset(-10, -10);
                            btnPopped=true;
                        }

                    }
                    else
                    if(currentState==STATE_PLAY)
                    {
                        for(int i=0;i<startButtons.size();i++)
                        {
                            if(startButtons.get(i).rect.width()!=btnwidth)
                            {
                                startButtons.get(i).rect.inset(10, 10);
                                if(i+1<startButtons.size())
                                    startButtons.get(i+1).rect.inset(-10, -10);
                                else
                                    startButtons.get(0).rect.inset(-10, -10);
                                btnPopped=true;
                                break;
                            }
                        }
                        if(btnPopped==false)
                        {
                            startButtons.get(0).rect.inset(-10, -10);
                            btnPopped=true;
                        }

                    }
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if(event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    ship.mAxisX=0;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if(event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    ship.mAxisX=0;
                }
                break;



        }
        // do stuff
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {

        // Check that the event came from a game controller
        if(currentState==STATE_NEWGAME) {
            if ((event.getSource() & InputDevice.SOURCE_JOYSTICK) ==
                    InputDevice.SOURCE_JOYSTICK &&
                    event.getAction() == MotionEvent.ACTION_MOVE) {

                if (ship != null) {
                    ship.mAxisX = event.getAxisValue(MotionEvent.AXIS_X)*2;
                    ship.mAxisY = event.getAxisValue(MotionEvent.AXIS_Y)*2;
                }

                // Process all historical movement samples in the batch
//            final int historySize = event.getHistorySize();
//
//            // Process the movements starting from the
//            // earliest historical position in the batch
//            for (int i = 0; i < historySize; i++) {
//                // Process the event at historical position i
//                processJoystickInput(event, i);
//            }
//
//            // Process the current movement sample in the batch (position -1)
//            processJoystickInput(event, -1);
                return true;
            }
        }
        return super.onGenericMotionEvent(event);
    }

    public void onMotionEvent(MotionEvent event)
    {

        if(ship!=null)
        {
            ship.mAxisX = event.getAxisValue(MotionEvent.AXIS_X);
            ship.mAxisY = event.getAxisValue(MotionEvent.AXIS_Y);
        }
        //		ship.mAxisZ = event.getAxisValue(com.bda.controller.MotionEvent.AXIS_Z);
        //		ship.mAxisRZ = event.getAxisValue(com.bda.controller.MotionEvent.AXIS_RZ);

    }


    public void render(Canvas canvas) {

        switch (currentState)
        {
            //deseneaza obiectele din ecran
            case STATE_SPLASH:
                // draw splash screen
                paint.setColor(Color.WHITE);
                canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(splashpic, null, splashrect, paint);
                break;
            //deseneaza obiectele din ecran
            case STATE_MAIN_MENU:

                paintBackground(canvas);
                paintMainmenu(canvas);

                break;
            //deseneaza obiectele din ecran si textul
            case STATE_HELP:
                paintBackground(canvas);
                painttext.setTextAlign(Align.CENTER);
                paintHelp(canvas, screenheight);
                break;

            case STATE_SHOP:
                paintBackground(canvas);
                painttext.setTextAlign(Align.CENTER);
                paintShop(canvas);
                break;
            //deseneaza obiectele din ecran
            case STATE_PLAY:
                paintBackground (canvas);
                paintStart(canvas);
                break;
            //deseneaza obiectele din ecran
            case STATE_NEWGAME:
                // draw splash screen
                paintBackground(canvas);

                if(stateReady==true)
                {
                    ship.paint(canvas);
                    paint.setColor(Color.BLUE);
                    paint.setStyle(Style.STROKE);
                    painttext.setTextSize(screenheight/35);
                    painttext.setColor(Color.RED);
                    painttext.setTextAlign(Align.CENTER);

                    canvas.drawText(String.valueOf(scorenr), score.centerX(), score.centerY()+score.height()/8, painttext);
                    canvas.drawRect(score, paint);

                    //deseneaza vietile ramase ale navei tale
                    if(livesNumber!=0)
                        for(int i=0;i<livesNumber;i++)
                        {
                            canvas.drawBitmap(shipBitmap, null, livesRect.get(i), paint);
                        }

                    if (touchpad != null)
                        touchpad.paint(canvas);

                    //deseneaza scutul
                    if(invincibilityRect!=null)
                        canvas.drawBitmap(smallCircle, null, invincibilityRect, paint);

                    paint.setStyle(Style.FILL);

                    //draw the explosion
                    if (animList!=null)
                        for (Animation a: animList)
                        {
                            a.paint(canvas);
                        }

                    //deseneaza inamici ramasi
                    for (int i = 0; i < enemy.size(); i++)
                    {
                        enemy.get(i).paint(canvas);
                    }

                    //deseneaza proiectilele inamicilor
                    if (enemyAmmun != null)
                    {
                        for(int i=0;i<enemyAmmun.size();i++)
                        {
                            if (enemyAmmun.get(i) != null)
                                enemyAmmun.get(i).paint(canvas);
                        }
                    }

                    //deseneaza numarul de vieti ramase ale bossului
                    if(bossLivesRect!=null)
                    {
                        paint.setStyle(Style.FILL);
                        for(int k=0;k<bossLivesRect.size();k++)
                            canvas.drawRect(bossLivesRect.get(k), paint);
                        paint.setStyle(Style.STROKE);
                    }

                    //deseneaza joystickul
                    //				gamepad.paint(canvas);

                    //deseneaza proiectilele navei tale
                    if (ammun != null)
                    {
                        for(int i=0;i<ammun.size();i++)
                        {
                            if (ammun.get(i) != null)
                                ammun.get(i).paint(canvas);
                        }
                    }
                }
                break;
            //deseneaza obiectele din ecran
            case STATE_WAVECLEARED:
            {
                paintBackground(canvas);
                painttext.setTextSize(screenheight/18);
                canvas.drawText(getResources().getString(R.string.wavecleared), screenwidth/2, screenheight/2, painttext);
                painttext.setTextSize(screenheight/35);

                //distruge nivelul anterior
                waveDispose();
                wavefinshed=true;
                stateReady=false;
                break;
            }
            //deseneaza obiectele din ecran
            case STATE_GAMEFINISHED:
            {
//			((MainActivity) maincontext).setHighscore(scorenr);
                paintBackground(canvas);
                painttext.setTextSize(screenheight/22);
                canvas.drawText(getResources().getString(R.string.gamecompleted), screenwidth/2, screenheight/2, painttext);
                painttext.setTextSize(screenheight/35);
                //distruge nivelul anterior
                waveDispose();
                wavefinshed=true;
                stateReady=false;
                break;
            }
            //deseneaza obiectele din ecran
            case STATE_GAMEOVER:
            {
//			((MainActivity) maincontext).setHighscore(scorenr);
                paintBackground(canvas);
                painttext.setTextSize(screenheight/15);
                canvas.drawText(getResources().getString(R.string.gameover), screenwidth/2, screenheight/2, painttext);
                painttext.setTextSize(screenheight/35);
                stateReady=false;
                break;
            }
            //deseneaza obiectele din ecran
            case STATE_COMMUNITY:
                paintBackground(canvas);
                paintMainmenu(canvas);
                paintCommunity(canvas);
                break;
            //deseneaza obiectele din ecran
            case STATE_SETTINGS:
                paintBackground(canvas);
                paintMainmenu(canvas);
                paintSettings(canvas);
                isSoundOn = sp.getBoolean(SOUNDBTN, false);
                paint=new Paint();
                paint.setColor(Color.GRAY);
                painttext.setColor(Color.BLUE);
                painttext.setTextSize(screenheight/32);
                painttext.setTextAlign(Align.LEFT);
                canvas.drawRect(sounds, paint);
                //			canvas.drawText(getResources().getString(R.string.sounds), sounds.left, sounds.centerY()+sounds.height()/9, painttext);
                checkSound(canvas);
                break;
        }


    }
    public void update(long dt)  {

        currentTime = System.currentTimeMillis();
        ((Activity)maincontext).runOnUiThread(new Runnable() {
            //		1	@Override
            public void run() {
                if (startGmg)
                {
                    //thread.stopThread();
                    startGmg = false;
                    Intent getMoreGamesIntent = new Intent(maincontext, GMGActivity.class);
                    getMoreGamesIntent.setClassName("com.rau.droidinvaders", "com.rau.droidinvaders.GMGActivity");
                    maincontext.startActivity(getMoreGamesIntent);
                }
            }
        });
        //		if(auxRect!=null && buttonPopTime+150<=System.currentTimeMillis()&&buttonPopped)
        //			btnSettings.rect.inset(10, 10);
        switch (currentState)
        {
            //trece in meniu dupa ce SPLASH_TIME a trecut
            case STATE_SPLASH:
                if (currentTime - startAppTime >= SPLASH_TIME)
                    transittostate(STATE_MAIN_MENU);
                break;

            case STATE_NEWGAME:
                if(stateReady==true)
                {
                    //ship fire si sfxul de fire este redat
                    if(Touchpad.fireBool)
                    {
                        if (currentTime - ammunitionTime >= ship.getFireRate ())
                        {
                            soundSfx.play(slaser, 1.0f, 1.0f, 0, 0, 1);
                            ammun.add( CAmmunition.createAmmunition(new Rect(ship.bounds.centerX()-10, ship.bounds.top, ship.bounds.centerX()+10,ship.bounds.top+30),lasers));
                            //						Touchpad.fireBool = false;
                            ammunitionTime = currentTime;
                        }
                    }
                    //scutul dispare dupa ce invicibilityLength a trecut de la inceputul nivelului
                    if(currentTime>=waveInitTime+invicibilityLength)
                    {
                        invincibilityRect=null;
                    }

                    //misca nava
                    //				if(ship!=null)
                    //				{
                    //					moveShip(ship);
                    //				}

                    //moga
                    final float scale = 7.5f;
                    ship.mX = ship.mAxisX  * scale;
                    ship.mY = ship.mAxisY  * scale;
                    if(ship!=null && ship.bounds.top+ship.mY>=0
                            && ship.bounds.right + ship.mX  <= screenwidth
                            && ship.bounds.left + ship.mX  >= 0
                            && ship.bounds.bottom + ship.mY <= screenheight)
                    {
                        ship.bounds.offset((int)ship.mX, (int)ship.mY);
                    }


                    if(invincibilityRect!=null&&ship!=null)
                        invincibilityRect.offsetTo(ship.bounds.left, ship.bounds.top);

                    //wave finished change to next level
                    if(enemy != null&&enemy.size()==0)
                    {
                        invincibilityRect= new Rect(ship.bounds);
                        if(ship!=null&&ship.bounds.top>=-50)
                        {
                            ship.bounds.offset(0, -10);
                        }
                        if(ship!=null&&ship.bounds.top<-50)
                            transittostate(STATE_WAVECLEARED);
                    }
                    switch (waveNumber)
                    {
                        case 0:
                            break;
                        case 1:
                            //updateaza pozitia inamicilor ramasi
                            if (enemy != null)
                            {
                                for(int i=0;i<enemy.size();i++)
                                {
                                    enemy.get(i).update(dt);
                                }
                            }
                            //daca pathul anterior sa terminat, introduce pathul din nou
                            for (CEnemy e: enemy)
                            {
                                if (e.finishedPath())
                                {
                                    e.clearPath();

                                    ArrayList<Point> path = new ArrayList<Point>();
                                    path.add(new Point(e.bounds.left + 50, e.bounds.top));
                                    path.add(new Point(e.bounds.left + 50, e.bounds.top + 50));
                                    path.add(new Point(e.bounds.left, e.bounds.top + 50));
                                    path.add(new Point(e.bounds.left, e.bounds.top));

                                    ArrayList<Integer> pathTime = new ArrayList<Integer>();
                                    pathTime.add(300);
                                    pathTime.add(300);
                                    pathTime.add(300);
                                    pathTime.add(300);

                                    e.setPath(path, pathTime, true);
                                }
                            }
                            break;
                        case 2:

                            //seteaza un inamic random sa traga
                            if(enemy.size()>(enemyNumber/2) && currentTime-enemyAmmunitionTime>enemy.get(0).getFireRate())
                            {
                                randomNum1=randomNum.nextInt(enemy.size());
                                enemyAmmun.add( CAmmunition.createAmmunition(new Rect(enemy.get(randomNum1).bounds.centerX()-10, enemy.get(randomNum1).bounds.bottom, enemy.get(randomNum1).bounds.centerX()+10,enemy.get(randomNum1).bounds.bottom+30),lasere));
                                enemyAmmunitionTime=currentTime;
                            }
                            else if (enemy.size()!=0&& currentTime-enemyAmmunitionTime>(enemy.get(0).getFireRate()*2))
                            {
                                randomNum1=randomNum.nextInt(enemy.size());
                                enemyAmmun.add( CAmmunition.createAmmunition(new Rect(enemy.get(randomNum1).bounds.centerX()-10, enemy.get(randomNum1).bounds.bottom, enemy.get(randomNum1).bounds.centerX()+10,enemy.get(randomNum1).bounds.bottom+30),lasere));
                                enemyAmmunitionTime=currentTime;
                            }
                            break;
                        case 3:
                            //updateaza pozitia inamicilor ramasi
                            for(int i=0;i<enemy.size();i++)
                            {
                                enemy.get(i).update(dt);
                            }
                            //seteaza un inamic random sa traga
                            if(enemy.size()>(enemyNumber/2) && currentTime-enemyAmmunitionTime>enemy.get(0).getFireRate())
                            {
                                randomNum1=randomNum.nextInt(enemy.size());
                                enemyAmmun.add( CAmmunition.createAmmunition(new Rect(enemy.get(randomNum1).bounds.centerX()-10, enemy.get(randomNum1).bounds.bottom, enemy.get(randomNum1).bounds.centerX()+10,enemy.get(randomNum1).bounds.bottom+30),lasere));
                                enemyAmmunitionTime=currentTime;
                            }
                            else if (enemy.size()!=0&& currentTime-enemyAmmunitionTime>enemy.get(0).getFireRate()*2)
                            {
                                randomNum1=randomNum.nextInt(enemy.size());
                                enemyAmmun.add( CAmmunition.createAmmunition(new Rect(enemy.get(randomNum1).bounds.centerX()-10, enemy.get(randomNum1).bounds.bottom, enemy.get(randomNum1).bounds.centerX()+10,enemy.get(randomNum1).bounds.bottom+30),lasere));
                                enemyAmmunitionTime=currentTime;
                            }
                            //daca pathul anterior sa terminat, introduce pathul din nou
                            for (CEnemy e: enemy)
                            {
                                if (e.finishedPath())
                                {
                                    e.clearPath();

                                    ArrayList<Point> path = new ArrayList<Point>();
                                    path.add(new Point(e.bounds.left + 50, e.bounds.top));
                                    path.add(new Point(e.bounds.left + 50, e.bounds.top + 50));
                                    path.add(new Point(e.bounds.left, e.bounds.top + 50));
                                    path.add(new Point(e.bounds.left, e.bounds.top));

                                    ArrayList<Integer> pathTime = new ArrayList<Integer>();
                                    pathTime.add(300);
                                    pathTime.add(300);
                                    pathTime.add(300);
                                    pathTime.add(300);

                                    e.setPath(path, pathTime, true);
                                }
                            }
                            break;
                        case 4:
                            //updateaza pozitia inamicilor ramasi
                            for(int i=0;i<enemy.size();i++)
                            {
                                enemy.get(i).update(dt);
                            }
                            //seteaza un inamic random sa traga
                            if(enemy.size()>(enemyNumber/2) && currentTime-enemyAmmunitionTime>enemy.get(0).getFireRate())
                            {
                                randomNum1=randomNum.nextInt(enemy.size());
                                enemyAmmun.add( CAmmunition.createAmmunition(new Rect(enemy.get(randomNum1).bounds.centerX()-10, enemy.get(randomNum1).bounds.bottom, enemy.get(randomNum1).bounds.centerX()+10,enemy.get(randomNum1).bounds.bottom+30),lasere));
                                enemyAmmunitionTime=currentTime;
                            }
                            else if (enemy.size()!=0&& currentTime-enemyAmmunitionTime>enemy.get(0).getFireRate()*2)
                            {
                                randomNum1=randomNum.nextInt(enemy.size());
                                enemyAmmun.add( CAmmunition.createAmmunition(new Rect(enemy.get(randomNum1).bounds.centerX()-10, enemy.get(randomNum1).bounds.bottom, enemy.get(randomNum1).bounds.centerX()+10,enemy.get(randomNum1).bounds.bottom+30),lasere));
                                enemyAmmunitionTime=currentTime;
                            }
                            //daca pathul anterior sa terminat, introduce pathul din nou
                            for(int i=0;i<enemy.size();i++)
                            {
                                e=enemy.get(i);
                                if (e.finishedPath())
                                {
                                    e.clearPath();

                                    ArrayList<Point> path = new ArrayList<Point>();
                                    path.add(new Point(screenwidth - enemyW, e.bounds.top));
                                    path.add(new Point(screenwidth - enemyW, e.bounds.height()*6));
                                    path.add(new Point(e.bounds.left, e.bounds.height()*6));
                                    path.add(new Point(e.bounds.left, e.bounds.top));

                                    ArrayList<Integer> pathTime = new ArrayList<Integer>();
                                    pathTime.add((int)(1500*1.75));
                                    pathTime.add(1500);
                                    pathTime.add((int)(1500*1.75));
                                    pathTime.add(1500);

                                    e.setPath(path, pathTime, true);
                                }
                            }

                            break;
                        case 5:
                            //updateaza pozitia inamicilor ramasi
                            if(enemy.size()>0)
                            {
                                for(int i=0;i<enemy.size();i++)
                                {
                                    enemy.get(i).update(dt);
                                }
                                //seteaza bossul sa traga
                                if(enemy!=null)
                                    for(int i=0;i<enemy.size();i++)
                                        for(int k=0;k<bossLivesRect.size();k++)
                                            bossLivesRect.get(k).offsetTo(enemy.get(i).bounds.left+2*k*bossLivesRect.get(k).width()+bossLivesRect.get(k).width(), enemy.get(i).bounds.top);
                                if(currentTime-enemyAmmunitionTime>enemy.get(0).getFireRate())
                                {
                                    randomNum1=randomNum.nextInt(enemy.size());
                                    enemyAmmun.add( CAmmunition.createAmmunition(new Rect(enemy.get(randomNum1).bounds.left-10, enemy.get(randomNum1).bounds.bottom-50, enemy.get(randomNum1).bounds.left+10,enemy.get(randomNum1).bounds.bottom-30),lasere));
                                    enemyAmmun.add( CAmmunition.createAmmunition(new Rect(enemy.get(randomNum1).bounds.right-10, enemy.get(randomNum1).bounds.bottom-50, enemy.get(randomNum1).bounds.right+10,enemy.get(randomNum1).bounds.bottom-30),lasere));
                                    enemyAmmunitionTime=currentTime;
                                }
                                //daca pathul anterior sa terminat, introduce pathul din nou
                                for (CEnemy e: enemy)
                                {
                                    if (e.finishedPath())
                                    {
                                        e.clearPath();

                                        ArrayList<Point> path = new ArrayList<Point>();
                                        path.add(new Point(e.bounds.left + (screenwidth-e.bounds.left-e.bounds.width())-shipW, e.bounds.top));
                                        path.add(new Point(e.bounds.left, e.bounds.top));
                                        path.add(new Point(e.bounds.left - e.bounds.left+shipW, e.bounds.top));
                                        path.add(new Point(e.bounds.left, e.bounds.top));

                                        ArrayList<Integer> pathTime = new ArrayList<Integer>();
                                        pathTime.add(900);
                                        pathTime.add(900);
                                        pathTime.add(900);
                                        pathTime.add(900);

                                        e.setPath(path, pathTime, true);
                                    }
                                }
                            }
                            break;
                        default:
                            transittostate(STATE_GAMEFINISHED);
                            break;
                    }

                    //updateaza pozitia proiectilelor
                    if(ammun!=null)
                    {
                        for(int i=0;i<ammun.size();i++)
                        {
                            CAmmunition a = ammun.get(i);
                            if (a != null)
                                a.update (-ammunitionSpeed);

                            //recicleaza proiectilele daca au iesit din ecran
                            if (a.bounds.bottom<0)
                            {
                                ammun.remove(a);
                                i--;
                                //a.dispose();
                                CAmmunition.recycle(a);
                            }
                        }
                        //distruge inamicii daca au fost loviti
                        for(int i=0;i<ammun.size();i++)
                        {
                            for(int j=0;j<enemy.size();j++)
                            {
                                if (Rect.intersects(ammun.get(i).bounds, enemy.get(j).bounds))
                                {
//								if(((MainActivity) maincontext).getSignInStatus())
//								((MainActivity) maincontext).setAchievement(R.string.achievement_random);

                                    a = ammun.remove(i);
                                    CAmmunition.recycle(a);
                                    a = null;
                                    --i;
                                    e=enemy.get(j);
                                    if(e.EnemyHitPoints>1)
                                    {
                                        e.EnemyHitPoints--;
                                        if(bossLivesRect!=null&&bossLivesRect.size()>0)
                                            bossLivesRect.remove(bossLivesRect.size()-1);
                                        if(e.enemyBm!=enemyBitmap)
                                            e.enemyBm=enemyBitmap;
                                        scorenr=sp.getInt(SCORENUMB, 0);
                                        sp.edit().putInt(SCORENUMB, ++scorenr).commit();
                                        scorenr=sp.getInt(SCORENUMB, 0);
                                    }
                                    else
                                        e.EnemyHitPoints--;
                                    if(e.EnemyHitPoints==0)
                                    {
                                        Rect explosionrect;
                                        explosionrect=e.bounds;
                                        Animation a;
                                        if(((MainActivity)maincontext).iapSP.getBoolean(MainActivity.SKU_PREMIUM_SHIP, false))
                                        {
                                             a = new Animation(explosionrect, explosionSprite, explosionSprite.getWidth()/5, explosionSprite.getHeight()/3);
                                        }
                                        else
                                        {
                                             a = new Animation(explosionrect, explosionSprite, explosionSprite.getWidth()/4, explosionSprite.getHeight()/4);
                                        }
                                        a.startAnimation(new int []{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, new int[] {50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50});
                                        animList.add(a);

                                        soundSfx.play(explosion, 1.0f, 1.0f, 0, 0, 1);
                                        e = enemy.remove(j);
                                        e.dispose();
                                        e = null;
                                        --j;
                                        scorenr=sp.getInt(SCORENUMB, 0);
                                        sp.edit().putInt(SCORENUMB, ++scorenr).commit();
                                        scorenr=sp.getInt(SCORENUMB, 0);
                                    }
                                    break;
                                }
                            }
                        }
                        //updateaza pozitia proiectilelor inamice si le distruge daca ies din ecran
                        if(enemyAmmun!=null)
                        {
                            for(int i=0;i<enemyAmmun.size();i++)
                            {
                                a = enemyAmmun.get(i);
                                if (a != null)
                                    a.update (+ammunitionSpeed);
                                if (!screenBounds.contains(a.bounds))
                                {
                                    enemyAmmun.remove(a);
                                    a.dispose();
                                }
                            }
                            //distruge nava ta daca ai intrat cu nava in inamic, reinitializeaza nava daca mai ai vieti si reseteaza scutul
                            for(int j=0;j<enemy.size();j++)
                            {
                                if(Rect.intersects(ship.bounds,enemy.get(j).bounds) && gameoverTime == 0 && invincibilityRect==null)
                                {
                                    livesNumber=sp.getInt(LIVES, 3);
                                    sp.edit().putInt(LIVES, --livesNumber).commit();
                                    livesNumber=sp.getInt(LIVES, 3);
                                    shipW=screenwidth/21;
                                    shipH=screenheight/11;
                                    ship = new CShip(new Rect (screenwidth / 2, screenheight * 3 / 4,screenwidth / 2 + shipW, screenheight * 3 / 4 + shipH),shipBitmap, screenheight, screenwidth);
                                    ship.setFireRate(450);
                                    invincibilityRect= new Rect(ship.bounds);
                                    waveInitTime=currentTime;
                                    touchpad =  new Touchpad(ship.bounds, screenwidth, screenheight);
                                }
                            }
                            //distruge o viata daca nava a fost lovita de proiectil inamic, reinitializeaza nava daca mai ai vieti si reseteaza scutul
                            for(int i=0;i<enemyAmmun.size();i++)
                            {
                                if (Rect.intersects(enemyAmmun.get(i).bounds, ship.bounds) && invincibilityRect==null)
                                {
                                    livesNumber=sp.getInt(LIVES, 3);
                                    sp.edit().putInt(LIVES, --livesNumber).commit();
                                    livesNumber=sp.getInt(LIVES, 3);
                                    shipW=screenwidth/21;
                                    shipH=screenheight/11;
                                    ship = new CShip(new Rect (screenwidth / 2, screenheight * 3 / 4,screenwidth / 2 + shipW, screenheight * 3 / 4 + shipH),shipBitmap, screenheight, screenwidth);
                                    ship.setFireRate(450);
                                    invincibilityRect= new Rect(ship.bounds);
                                    waveInitTime=currentTime;
                                    touchpad =  new Touchpad(ship.bounds, screenwidth, screenheight);
                                }
                            }
                        }
                        if(animList!=null)
                            for (Animation a: animList)
                            {
                                a.updateAnimation(dt);
                            }
                        //afiseaza ecranul de gameover daca ai pierdut toate vietile
                        if(sp.getInt(LIVES, 3)==0)
                            transittostate(STATE_GAMEOVER);
                    }
                }
                else
                    continueGameInit();
                break;
        }


    }
    //realizeaza trecerea dintrun ecran in altul
    public void transittostate(int nextState)
    {
        exitState (currentState);
        currentState=nextState;
        entryState(currentState);
    }
    //initializeaza ecranul in care se intra
    private void entryState (int state)
    {
        switch (state)
        {
            //creaza butoanele din ecranul de Main Menu
            case STATE_MAIN_MENU:
            {
                if (btnStart == null)
                {
                    createMainmenu(isSoundOn);
                }
                //opreste muzica de gameplay cand intri in main menu
                if(gameplayer!=null)
                {
                    gameplayer.stop();
                    try {
                        gameplayer.prepare();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                //daca muzica este on, porneste muzica de meniu
                isSoundOn = sp.getBoolean(SOUNDBTN, false);
                if(menuplayer!=null && isSoundOn)
                    menuplayer.start();
                break;
            }
            //creaza butoanele din ecranul de Play
            case STATE_PLAY:
            {
                if (btnStart == null)
                {
                    createMainmenu(isSoundOn);
                }
                if (btnNewgame == null)
                {
                    startButtons=new ArrayList<CButton>();
                    newGame= new Rect(scrnBtnLeftMargin*2+btnGmg.width(), scrnBtnTopMargin, scrnBtnLeftMargin*2+btnGmg.width()*2, scrnBtnTopMargin+screenheight/BTNHGHT);
                    btnNewgame = new CButton(newGame,getResources().getString(R.string.newgame), Align.CENTER, maincontext, buttonBack);
                    startButtons.add(btnNewgame);
                    continueGame= new Rect(scrnBtnLeftMargin*2+btnGmg.width(), scrnBtnTopMargin*2+btnGmg.height(), scrnBtnLeftMargin*2+btnGmg.width()*2, scrnBtnTopMargin*2+btnGmg.height()*2);
                    btnContinuegame = new CButton(continueGame,getResources().getString(R.string.continuegame), Align.CENTER, maincontext,buttonBack);
                    startButtons.add(btnContinuegame);
                    invitationsBox= new Rect(scrnBtnLeftMargin*2+btnGmg.width(), scrnBtnTopMargin*3+btnGmg.height()*2, scrnBtnLeftMargin*2+btnGmg.width()*2, scrnBtnTopMargin*3+btnGmg.height()*3);
                    btnInvitesBox = new CButton(invitationsBox,getResources().getString(R.string.see_invitations), Align.CENTER, maincontext,buttonBack);
                    startButtons.add(btnInvitesBox);
                    invitePlayer= new Rect(scrnBtnLeftMargin*2+btnGmg.width(), scrnBtnTopMargin*4+btnGmg.height()*3, scrnBtnLeftMargin*2+btnGmg.width()*2, scrnBtnTopMargin*4+btnGmg.height()*4);
                    btnInvite = new CButton(invitePlayer,getResources().getString(R.string.invite_players), Align.CENTER, maincontext,buttonBack);
                    startButtons.add(btnInvite);

                }
                break;
            }
            //creaza butoanele din ecranul de Settings
            case STATE_SETTINGS:
            {
                if (btnStart == null)
                {
                    createMainmenu(isSoundOn);
                }
                if (btnSounds == null)
                {
                    settingsButtons=new ArrayList<CButton>();
                    isSoundOn = sp.getBoolean(SOUNDBTN, false);
                    resetGame= new Rect(scrnBtnLeftMargin*2+play.width(), scrnBtnTopMargin*4+play.height()*3, scrnBtnLeftMargin*2+play.width()*2, scrnBtnTopMargin*4+play.height()*4);
                    btnResetgame = new CButton(resetGame,getResources().getString(R.string.resetgame), Align.CENTER, maincontext,buttonBack);
                    settingsButtons.add(btnResetgame);
                    sounds= new Rect(scrnBtnLeftMargin*2+play.width(), scrnBtnTopMargin*3+play.height()*2, scrnBtnLeftMargin*2+play.width()*2, scrnBtnTopMargin*3+play.height()*3);
                    //				if(isSoundOn)
                    btnSounds = new CButton(sounds,null, Align.CENTER, maincontext,buttonBack);
                    settingsButtons.add(btnSounds);
                    //				else
                    //				btnSounds = new CButton(sounds,getResources().getString(R.string.soundoff), Align.CENTER, maincontext,buttonBack);
                }
                break;
            }
            case STATE_COMMUNITY:
            {
                if (btnStart == null)
                {
                    createMainmenu(isSoundOn);
                }
                if(btnAchievements == null)
                {
                    communityButtons = new ArrayList<CButton>();
                    achievements= new Rect(scrnBtnLeftMargin*2+play.width(), scrnBtnTopMargin*2+play.height()*1, scrnBtnLeftMargin*2+play.width()*2, scrnBtnTopMargin*2+play.height()*2);
                    btnAchievements = new CButton(achievements,getResources().getString(R.string.achievements), Align.CENTER, maincontext,buttonBack);
                    communityButtons.add(btnAchievements);
                    leaderboards= new Rect(scrnBtnLeftMargin*2+play.width(), scrnBtnTopMargin*3+play.height()*2, scrnBtnLeftMargin*2+play.width()*2, scrnBtnTopMargin*3+play.height()*3);
                    btnLeaderboards = new CButton(leaderboards,getResources().getString(R.string.leaderboards), Align.CENTER, maincontext,buttonBack);
                    communityButtons.add(btnLeaderboards);
                }
                break;
            }
            case STATE_SHOP:
            {
                createshop();
//                if (btnStart == null)
//                {
//                    createMainmenu(isSoundOn);
//                }
//                if(btnRemoveAds == null)
//                {
//                    shopButtons = new ArrayList<>();
//                    premiumship= new Rect(scrnBtnLeftMargin*2+play.width(), scrnBtnTopMargin*2+play.height()*1, scrnBtnLeftMargin*2+play.width()*2, scrnBtnTopMargin*2+play.height()*2);
//                    btnPremiumShip = new CButton(achievements,getResources().getString(R.string.achievements), Align.CENTER, maincontext,buttonBack);
//                    communityButtons.add(btnAchievements);
//                    leaderboards= new Rect(scrnBtnLeftMargin*2+play.width(), scrnBtnTopMargin*3+play.height()*2, scrnBtnLeftMargin*2+play.width()*2, scrnBtnTopMargin*3+play.height()*3);
//                    btnLeaderboards = new CButton(leaderboards,getResources().getString(R.string.leaderboards), Align.CENTER, maincontext,buttonBack);
//                    communityButtons.add(btnLeaderboards);
//                }
                break;
            }
            case STATE_NEWGAME:
            {
                //opreste muzica de meniu cand intri in gameplay
                if(menuplayer!=null && isSoundOn)
                {
                    menuplayer.stop();
                    try {
                        menuplayer.prepare();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                //porneste muzica de gameplay
                if(gameplayer!=null && isSoundOn)
                {
                    gameplayer.start();

                }
                //preia numarul nivelului
                waveNumber = sp.getInt(WAVENUMB, -1);
                sp.edit().putInt(WAVENUMB, ++waveNumber).commit();
                waveNumber = sp.getInt(WAVENUMB, -1);
                //seteaza numarul standard de vieti a inamicilor
                EnemyHp=1;
                //get number of lives
                livesNumber = sp.getInt(LIVES, 3);
                livesRect=new ArrayList<Rect>();
                //creaza vectorul pentru proiectilele inamice
                enemyAmmun=new ArrayList<CAmmunition>();
                //reseteaza pozitia navei tale
                if(ship!=null)
                {
                    ship.bounds.offsetTo(screenwidth / 2, screenheight * 3 / 4);
                }
                switch (waveNumber)
                {
                    case 0:
                        //initializeaza nivelul
                        waveInit();
                        //seteaza numarul de vieti ale inamicilor
                        EnemyHp=1;
                        //seteaza pozitia pentru navele inamice in functie de matricile din GameConstants
                        for(int i=0;i<GameConstants.W0_COLS;i++)
                        {
                            for(int j=0;j<GameConstants.W0_ROWS;j++)
                            {
                                if(GameConstants.W0[i][j]==1)
                                {
                                    enemy.add(new CEnemy(new Rect (shipHOffSet, shipVOffSet,shipHOffSet+enemyW,shipVOffSet+enemyH),enemyBitmap,EnemyHp));
                                }
                                shipHOffSet=shipHOffSet+enemyHSpacing+enemyW;
                            }
                            shipHOffSet=(screenwidth - GameConstants.W0_ROWS*enemyW-enemyHSpacing*(GameConstants.W0_ROWS-1))/2;
                            shipVOffSet=shipVOffSet+enemyVSpacing+enemyH;
                        }
                        enemyNumber=enemy.size();
                        break;
                    case 1:
                        //initializeaza nivelul
                        waveInit();
                        //seteaza numarul de vieti ale inamicilor
                        EnemyHp=2;
                        ArrayList<Point> path = new ArrayList<Point>();
                        ArrayList<Integer> pathTime = new ArrayList<Integer>();
                        //seteaza pozitia pentru navele inamice in functie de matricile din GameConstants si seteaza pathul de intrare a navelor in ecran
                        for(int i=0;i<GameConstants.W1_COLS;i++)
                        {
                            for(int j=0;j<GameConstants.W1_ROWS;j++)
                            {
                                if(GameConstants.W1[i][j]==1)
                                {
                                    e = new CEnemy(new Rect (-shipHOffSet, shipVOffSet,-shipHOffSet+enemyW,shipVOffSet+enemyH),enemyL2Bitmap,EnemyHp);
                                    path.clear();
                                    pathTime.clear();
                                    path.add(new Point(shipHOffSet/3, shipVOffSet-100));
                                    path.add(new Point(shipHOffSet*2/3, shipVOffSet+100));
                                    path.add(new Point(shipHOffSet, shipVOffSet));
                                    pathTime.add(700);
                                    pathTime.add(700);
                                    pathTime.add(700);
                                    e.setPath(path, pathTime, false);

                                    enemy.add(e);
                                }
                                shipHOffSet=shipHOffSet+enemyHSpacing+enemyW;
                            }
                            shipHOffSet=(screenwidth - GameConstants.W1_ROWS*enemyW-enemyHSpacing*(GameConstants.W1_ROWS-1))/2;
                            shipVOffSet=shipVOffSet+enemyVSpacing+enemyH;
                        }
                        enemyNumber=enemy.size();
                        break;
                    case 2:
                        //initializeaza nivelul
                        waveInit();
                        //seteaza numarul de vieti ale inamicilor
                        EnemyHp=1;
                        //seteaza pozitia pentru navele inamice in functie de matricile din GameConstants
                        for(int i=0;i<GameConstants.W2_COLS;i++)
                        {
                            for(int j=0;j<GameConstants.W2_ROWS;j++)
                            {
                                if(GameConstants.W2[i][j]==1)
                                {
                                    e = new CEnemy(new Rect (shipHOffSet, shipVOffSet,shipHOffSet+enemyW,shipVOffSet+enemyH),enemyBitmap,EnemyHp);
                                    enemy.add(e);
                                }
                                shipHOffSet=shipHOffSet+enemyHSpacing+enemyW;
                            }
                            shipHOffSet=(screenwidth - GameConstants.W2_ROWS*enemyW-enemyHSpacing*(GameConstants.W2_ROWS-1))/2;
                            shipVOffSet=shipVOffSet+enemyVSpacing+enemyH;
                        }
                        //seteaza frecventa de tragere a inamicilor
                        for(int i=0;i<enemy.size();i++)
                        {
                            enemy.get(i).setFireRate(1500);
                        }
                        enemyNumber=enemy.size();
                        break;
                    case 3:
                        //initializeaza nivelul
                        waveInit();
                        //seteaza numarul de vieti ale inamicilor
                        EnemyHp=1;
                        //creaza vectorii pentru path si timpul alocat pathului
                        ArrayList<Point> path3 = new ArrayList<Point>();
                        ArrayList<Integer> pathTime3 = new ArrayList<Integer>();
                        //seteaza pozitia pentru navele inamice in functie de matricile din GameConstants si seteaza pathul de intrare a navelor in ecran
                        for(int i=0;i<GameConstants.W3_COLS;i++)
                        {
                            for(int j=0;j<GameConstants.W3_ROWS;j++)
                            {
                                if(GameConstants.W3[i][j]==1)
                                {
                                    e = new CEnemy(new Rect (-shipHOffSet, shipVOffSet,-shipHOffSet+enemyW,shipVOffSet+enemyH),enemyBitmap,EnemyHp);
                                    path3.clear();
                                    pathTime3.clear();

                                    path3.add(new Point(shipHOffSet/3, shipVOffSet-100));
                                    path3.add(new Point(shipHOffSet*2/3, shipVOffSet+100));
                                    path3.add(new Point(shipHOffSet, shipVOffSet));

                                    pathTime3.add(700);
                                    pathTime3.add(700);
                                    pathTime3.add(700);

                                    e.setPath(path3, pathTime3, false);

                                    if(enemy!=null)
                                        enemy.add(e);
                                }
                                shipHOffSet=shipHOffSet+enemyHSpacing+enemyW;
                            }
                            shipHOffSet=(screenwidth - GameConstants.W3_ROWS*enemyW-enemyHSpacing*(GameConstants.W3_ROWS-1))/2;
                            shipVOffSet=shipVOffSet+enemyVSpacing+enemyH;
                        }
                        //seteaza frecventa de tragere a inamicilor
                        for(int i=0;i<enemy.size();i++)
                        {
                            enemy.get(i).setFireRate(1500);
                        }
                        enemyNumber=enemy.size();
                        break;
                    case 4:
                        //initializeaza nivelul
                        waveInit();
                        //seteaza numarul de vieti ale inamicilor
                        EnemyHp=2;
                        //seteaza pozitia pentru navele inamice in functie de matricile din GameConstants si seteaza pathul de intrare a navelor in ecran
                        for(int i=0;i<GameConstants.W4_COLS;i++)
                        {
                            for(int j=0;j<GameConstants.W4_ROWS;j++)
                            {
                                if(GameConstants.W4[i][j]==1)
                                {
                                    e = new CEnemy(new Rect (-shipHOffSet, shipVOffSet,-shipHOffSet+enemyW,shipVOffSet+enemyH),enemyL2Bitmap,EnemyHp);
                                    e.addPathPoint(new Point(enemyW, shipVOffSet), 1000+j*(i+1)*500);
                                    enemy.add(e);
                                }
                                shipHOffSet=shipHOffSet+enemyHSpacing+enemyW;
                            }
                            shipHOffSet=(screenwidth - GameConstants.W4_ROWS*shipW-enemyHSpacing*(GameConstants.W4_ROWS-1))/2;
                            shipVOffSet=shipVOffSet+enemyVSpacing+enemyH;
                        }
                        //seteaza frecventa de tragere a inamicilor
                        for(int i=0;i<enemy.size();i++)
                        {
                            enemy.get(i).setFireRate(500);
                        }
                        enemyNumber=enemy.size();
                        break;
                    case 5:
                        //initializeaza nivelul
                        waveInit();
                        //seteaza numarul de vieti ale inamicilor
                        EnemyHp=10;
                        bossLivesRect= new ArrayList<Rect>();
                        //seteaza pozitia pentru navele inamice in functie de matricile din GameConstants si seteaza pathul de intrare a navelor in ecran
                        for(int i=0;i<GameConstants.W5_COLS;i++)
                        {
                            for(int j=0;j<GameConstants.W5_ROWS;j++)
                            {
                                if(GameConstants.W5[i][j]==1)
                                {
                                    e = new CEnemy(new Rect (screenwidth/2-enemyW*2, -shipVOffSet,screenwidth/2+enemyW*2,-shipVOffSet+enemyH*4),enemyBitmap,EnemyHp);
                                    e.addPathPoint(new Point(screenwidth/2-enemyW*2, shipVOffSet), 1000);
                                    for(int k=0;k<e.EnemyHitPoints;k++)
                                        bossLivesRect.add(new Rect(e.bounds.left+k*screenwidth/50,e.bounds.top-screenheight/200,e.bounds.left+k*screenwidth/50+screenwidth/50,e.bounds.top-screenheight/100));
                                    enemy.add(e);
                                }
                                shipHOffSet=shipHOffSet+enemyHSpacing+enemyW;
                            }
                            shipHOffSet=(screenwidth - GameConstants.W5_ROWS*shipW-enemyHSpacing*(GameConstants.W5_ROWS-1))/2;
                            shipVOffSet=shipVOffSet+enemyVSpacing+enemyH;
                        }
                        //seteaza frecventa de tragere a inamicilor
                        for(int i=0;i<enemy.size();i++)
                        {
                            enemy.get(i).setFireRate(500);
                        }
                        enemyNumber=enemy.size();
                        break;
                }
                stateReady=true;
                break;
            }
        }
    }
    private void exitState(int state)
    {
        switch(state)
        {
            case STATE_MAIN_MENU:
                btnPopped=false;
                break;
            case STATE_SETTINGS:
                btnPopped=false;
                break;
            case STATE_COMMUNITY:
                btnPopped=false;
                break;
            case STATE_PLAY:
                btnPopped=false;
                break;
        }
    }
    //functie pentru desenarea backgroundului
    public void paintBackground(Canvas canvas)
    {
        if(gameplayBackground!=null)
            gameplayBackground.paint(canvas);
    }
    //functie pentru desenarea ecranului de Start
    public void paintStart(Canvas canvas)
    {
        paintMainmenu(canvas);
        if (btnNewgame != null)
        {
            btnNewgame.paint(canvas);
            btnContinuegame.paint(canvas);
            btnInvitesBox.paint(canvas);
            btnInvite.paint(canvas);
        }
    }
    //functie pentru desenarea ecranului de Settings
    public void paintSettings(Canvas canvas)
    {
        paintMainmenu(canvas);
        btnResetgame.paint(canvas);
        btnSounds.paint(canvas);
    }
    //functie pentru desenarea ecranului de Community
    public void paintCommunity(Canvas canvas)
    {
        paintMainmenu(canvas);
        btnAchievements.paint(canvas);
        btnLeaderboards.paint(canvas);
    }

    //functie pentru desenarea ecranului de Community
    public void paintShop(Canvas canvas)
    {
//        paintMainmenu(canvas);
        btnRemoveAds.paint(canvas);
        btnExtraLife.paint(canvas);
        btnPremiumShip.paint(canvas);
        btnPremiumSoundPack.paint(canvas);
    }

    //functie pentru crearea butoanelor de Main menu si pentru pornirea melodiei de background daca Music este setat on
    public void createMainmenu(Boolean isSoundOff)
    {
        menuButtons=new ArrayList<CButton>();
        play= new Rect(scrnBtnLeftMargin,scrnBtnTopMargin,screenwidth/2-scrnBtnLeftMargin/6, scrnBtnTopMargin+screenheight/BTNHGHT);

        community= new Rect(scrnBtnLeftMargin, scrnBtnTopMargin*2+play.height(), screenwidth/2-scrnBtnLeftMargin/6,scrnBtnTopMargin*2+play.height()*2 );
        settings= new Rect(scrnBtnLeftMargin, scrnBtnTopMargin*3+play.height()*2, screenwidth/2-scrnBtnLeftMargin/6,scrnBtnTopMargin*3+play.height()*3 );
        help= new Rect(scrnBtnLeftMargin, scrnBtnTopMargin*4+play.height()*3, screenwidth/2-scrnBtnLeftMargin/6,scrnBtnTopMargin*4+play.height()*4 );
        gmg= new Rect(scrnBtnLeftMargin, scrnBtnTopMargin*5+play.height()*4, screenwidth/2-scrnBtnLeftMargin/6,scrnBtnTopMargin*5+play.height()*5 );
        //signIn= new Rect(play.width()*2+scrnBtnLeftMargin,scrnBtnTopMargin*5+play.height()*5,scrnBtnLeftMargin*2+play.width()*2,scrnBtnTopMargin*5+play.height()*6);

        btnStart = new CButton(play,getResources().getString(R.string.play), Align.CENTER, maincontext,buttonBack);
        menuButtons.add(btnStart);
        btnCommunity= new CButton(community,getResources().getString(R.string.community), Align.CENTER, maincontext,buttonBack);
        menuButtons.add(btnCommunity);
        btnSettings= new CButton(settings,getResources().getString(R.string.settings), Align.CENTER, maincontext,buttonBack);
        menuButtons.add(btnSettings);
        btnHelp= new CButton(help,getResources().getString(R.string.help), Align.CENTER, maincontext,buttonBack);
        menuButtons.add(btnHelp);
        btnGmg= new CButton(gmg,getResources().getString(R.string.GMG), Align.CENTER, maincontext,buttonBack);
        menuButtons.add(btnGmg);
        //	btnSignIn= new CButton(signIn, null, Align.CENTER, maincontext, signInImg);
        //	menuButtons.add(btnSignIn);
        //	btnSignOut= new CButton(signIn, null, Align.CENTER, maincontext, signOutImg);
        //	menuButtons.add(btnSignOut);

        btnwidth=btnStart.rect.width();
        btnheight=btnStart.rect.height();

        if (isSoundOff)
            if(menuplayer!=null)
                menuplayer.start();
            else
            if(menuplayer!=null)
            {
                menuplayer.stop();
                try {
                    menuplayer.prepare();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
    }

    //functie pentru crearea butoanelor de Main menu si pentru pornirea melodiei de background daca Music este setat on
    public void createshop()
    {
        shopButtons=new ArrayList<>();
        removeAds= new Rect(scrnBtnLeftMargin,scrnBtnTopMargin,screenwidth/2-scrnBtnLeftMargin/6, scrnBtnTopMargin+screenheight/BTNHGHT);

        premiumship= new Rect(scrnBtnLeftMargin, scrnBtnTopMargin*2+play.height(), screenwidth/2-scrnBtnLeftMargin/6,scrnBtnTopMargin*2+play.height()*2 );
        premiumSoundPack= new Rect(scrnBtnLeftMargin, scrnBtnTopMargin*3+play.height()*2, screenwidth/2-scrnBtnLeftMargin/6,scrnBtnTopMargin*3+play.height()*3 );
        extralife= new Rect(scrnBtnLeftMargin, scrnBtnTopMargin*4+play.height()*3, screenwidth/2-scrnBtnLeftMargin/6,scrnBtnTopMargin*4+play.height()*4 );
//        gmg= new Rect(scrnBtnLeftMargin, scrnBtnTopMargin*5+play.height()*4, screenwidth/2-scrnBtnLeftMargin/6,scrnBtnTopMargin*5+play.height()*5 );
        //signIn= new Rect(play.width()*2+scrnBtnLeftMargin,scrnBtnTopMargin*5+play.height()*5,scrnBtnLeftMargin*2+play.width()*2,scrnBtnTopMargin*5+play.height()*6);

        btnRemoveAds = new CButton(removeAds, "Remove Ads", Align.CENTER, maincontext, buttonBack);
        shopButtons.add(btnRemoveAds);
        btnPremiumShip= new CButton(premiumship, "Premium Ship", Align.CENTER, maincontext,buttonBack);
        shopButtons.add(btnPremiumShip);
        btnPremiumSoundPack= new CButton(premiumSoundPack, "Premium Sound Pack", Align.CENTER, maincontext,buttonBack);
        shopButtons.add(btnPremiumSoundPack);
        btnExtraLife= new CButton(extralife, "Extra Life", Align.CENTER, maincontext,buttonBack);
        shopButtons.add(btnExtraLife);
//        btnGmg= new CButton(gmg,getResources().getString(R.string.GMG), Align.CENTER, maincontext,buttonBack);
//        menuButtons.add(btnGmg);
        //	btnSignIn= new CButton(signIn, null, Align.CENTER, maincontext, signInImg);
        //	menuButtons.add(btnSignIn);
        //	btnSignOut= new CButton(signIn, null, Align.CENTER, maincontext, signOutImg);
        //	menuButtons.add(btnSignOut);

//        btnwidth=btnStart.rect.width();
//        btnheight=btnStart.rect.height();

//        if (isSoundOff)
//            if(menuplayer!=null)
//                menuplayer.start();
//            else
//            if(menuplayer!=null)
//            {
//                menuplayer.stop();
//                try {
//                    menuplayer.prepare();
//                } catch (IllegalStateException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
    }
    //functie pentru desenarea textului din ecranul de Help
    public void paintHelp(Canvas canvas, int screenheight)
    {
        painttext.setColor(0x7F33CC33);
        painttext.setTextSize(screenheight/40);
        Typeface tf = Typeface.createFromAsset(maincontext.getAssets(), "fonts/roboto.ttf");
        painttext.setTypeface(tf);
        painttext.setTextAlign(Align.CENTER);
        if(screenheight <=320)
        {
            String[] s = wrapText(getResources().getString(R.string.helptext), 30);
            for(int i=0;i<=24;i++)
                canvas.drawText(s[i], screenwidth/2, screenwidth/10+i*12, painttext);
        }
        else
        if(screenheight >=480&&screenheight<600)
        {
            String[] s = wrapText(getResources().getString(R.string.helptext), 30);
            for(int i=0;i<=24;i++)
                canvas.drawText(s[i], screenwidth/2, screenwidth/6+i*15, painttext);
        }
        else
        if(screenheight >=800&&screenheight<1200)
        {
            String[] s = wrapText(getResources().getString(R.string.helptext), 30);
            for(int i=0;i<=24;i++)
                canvas.drawText(s[i], screenwidth/2, screenwidth/8+i*30, painttext);
        }
        else
        if(screenheight >=900&&screenheight<1200)
        {
            String[] s = wrapText(getResources().getString(R.string.helptext), 30);
            for(int i=0;i<=24;i++)
                canvas.drawText(s[i], screenwidth/2, screenwidth/8+i*35, painttext);
        }
        else
        {
            String[] s = wrapText(getResources().getString(R.string.helptext), 30);
            for(int i=0;i<=24;i++)
                canvas.drawText(s[i], screenwidth/2, screenwidth/6+i*40, painttext);
        }
    }
    //functie pentru desenarea butoanelor din Main menu
    public void paintMainmenu(Canvas canvas)
    {
        btnStart.paint(canvas);
        btnCommunity.paint(canvas);
        btnSettings.paint(canvas);
        btnHelp.paint(canvas);
        btnGmg.paint(canvas);
        //		if(((MainActivity) maincontext).getSignInStatus())
        //		{
        //			btnSignOut.paint(canvas);
        //		}
        //		else
        //			btnSignIn.paint(canvas);
    }
    //functie pentru desenarea ecranului de settings in functie de setarea Music(on/off)
    public void checkSound(Canvas canvas)
    {
        if (isSoundOn)
        {
            btnSounds.paint(canvas);
            paint.setColor(Color.DKGRAY);
            painttext.setTextSize(screenheight/32);
            painttext.setColor(Color.GRAY);
            btnSounds.paint(canvas);
            canvas.drawText(getResources().getString(R.string.soundon), sounds.left+sounds.width()/4, sounds.centerY()+sounds.height()/9, painttext);
            if(menuplayer!=null && !menuplayer.isPlaying())
                menuplayer.start();
        }
        else
        {
            btnSounds.paint(canvas);
            painttext.setTextSize(screenheight/32);
            painttext.setColor(Color.GRAY);
            canvas.drawText(getResources().getString(R.string.soundoff), sounds.left+sounds.width()/4, sounds.centerY()+sounds.height()/9, painttext);
            if(menuplayer!=null && menuplayer.isPlaying())
            {
                menuplayer.stop();
                try {
                    menuplayer.prepare();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    public void pause ()
    {
        thread.stopThread();
    }
    public void resume ()
    {
        thread.resumeThread();
    }
    //functie pentru miscarea navei
    public void moveShip(CShip ship)
    {
        if(gamepad!=null)
        {
            ship.update(gamepad.getDir());
            if(invincibilityRect!=null)
                invincibilityRect.offsetTo(ship.bounds.left, ship.bounds.top);
        }
    }
    //functie pentru eliberarea memoriei si curatarea variabilelor dupa terminarea unui nivel
    public void waveDispose()
    {
        ship.dispose();
        gamepad.dispose();
        ammun=null;
        enemy=null;
        score=null;
        enemyAmmun=null;
    }
    //functie pentru initializarea unui joc nou
    public void newGameInit()
    {
        sp.edit().putInt(WAVENUMB, -1).commit();
        waveNumber = sp.getInt(WAVENUMB, -1);

        sp.edit().putInt(SCORENUMB, 0).commit();
        scorenr=sp.getInt(SCORENUMB, 0);

        sp.edit().putInt(LIVES, 3).commit();
        livesNumber=sp.getInt(LIVES, 3);

        continueWave=false;
        transittostate(STATE_NEWGAME);

    }
    //functie pentru initializarea unui joc preexistent
    public void continueGameInit()
    {
        waveNumber = sp.getInt(WAVENUMB, -1);
        sp.edit().putInt(WAVENUMB, --waveNumber).commit();
        waveNumber = sp.getInt(WAVENUMB, -1);

        scorenr=sp.getInt(SCORENUMB, 0);

        livesNumber=sp.getInt(LIVES, 3);
        continueWave=true;
        transittostate(STATE_NEWGAME);
    }
    //functie pentru initializarea unui nivel nou
    public void waveInit()
    {
        shipW=screenwidth/21;
        shipH=screenheight/11;
        shipVOffSet=screenheight/25;
        enemyH=screenheight/11;
        enemyW=screenwidth/23;
        enemyHSpacing=enemyW/2;
        enemyVSpacing=screenheight/35;
        waveInitTime=System.currentTimeMillis();
        shipHOffSet=(screenwidth - GameConstants.W0_ROWS*enemyW-enemyHSpacing*(GameConstants.W0_ROWS-1))/2;
        ship = new CShip(new Rect (screenwidth / 2, screenheight * 7 / 8,screenwidth / 2 + shipW, screenheight * 7 / 8 + shipH),shipBitmap, screenheight, screenwidth);
        ship.setFireRate(450);
        gamepad = new CGamepad(new Rect (screenwidth/100, screenheight*8/10, screenwidth/3, screenheight-10),smallCircle,smallCircle,screenwidth);
        ammun=new ArrayList<CAmmunition>();
        enemy = new ArrayList<CEnemy>();
        score= new Rect(screenwidth*4/5, screenheight/30,screenwidth-10,screenheight/30+50);
        invincibilityRect= new Rect(ship.bounds);
        touchpad =  new Touchpad(ship.bounds, screenwidth, screenheight);
        enemyAmmun.clear();
        //create lives
        livesRect.clear();
        for(int i=0;i<livesNumber;i++)
        {
            livesRect.add(new Rect(screenwidth/50+i*screenwidth/20, screenheight/100, screenwidth/50+i*screenwidth/20+screenwidth/20, screenheight/100+screenheight/35));
        }
    }
    //functie pentru impartirea textului din help pe linii
    public void buttonPopUp(MotionEvent event)
    {
        if(help.contains((int)event.getX(), (int)event.getY()))
        {
            btnHelp.rect.inset(-10, -10);
        }
        else
        if(gmg.contains((int)event.getX(), (int)event.getY()))
        //							startGmg = true;
        {
            btnGmg.rect.inset(-10, -10);
        }
        else
            //daca butonul de play a fost apasat deschide meniul de play
            if (play.contains((int)event.getX(), (int)event.getY()))
            {
                btnStart.rect.inset(-10, -10);
            }
            else
                //daca butonul de settings a fost apasat deschide ecranul de settings
                if (settings.contains((int)event.getX(), (int)event.getY()))
                {
                    btnSettings.rect.inset(-10, -10);
                }
                else
                if(community.contains((int)event.getX(), (int)event.getY()))
                {
                    btnCommunity.rect.inset(-10, -10);
                }
                else
                if (newGame != null && newGame.contains((int)event.getX(), (int)event.getY()))
                {
                    btnNewgame.rect.inset(-10, -10);
                }
                else
                if (continueGame != null && continueGame.contains((int)event.getX(), (int)event.getY()))
                {
                    btnContinuegame.rect.inset(-10, -10);
                }
                else
                if(sounds !=null && sounds.contains((int)event.getX(), (int)event.getY()))
                {
                    btnSounds.rect.inset(-10, -10);
                }
                else
                if(resetGame !=null && resetGame.contains((int)event.getX(), (int)event.getY()))
                {
                    btnResetgame.rect.inset(-10, -10);
                }
                else
                if(invitationsBox !=null && invitationsBox.contains((int)event.getX(), (int)event.getY()))
                {
                    btnInvitesBox.rect.inset(-10, -10);
                }
                else
                if(invitePlayer !=null && invitePlayer.contains((int)event.getX(), (int)event.getY()))
                {
                    btnInvite.rect.inset(-10, -10);
                }
    }
    public void buttonPopDown(MotionEvent event)
    {
        soundSfx.play(buttonClick, 1.0f, 1.0f, 1, 0, 1);

        if(help.contains((int)event.getX(), (int)event.getY()))
        {
            btnHelp.rect.inset(10, 10);
        }
        else
        if(!startGmg && gmg != null && gmg.contains((int)event.getX(), (int)event.getY()))
        //							startGmg = true;
        {
            btnGmg.rect.inset(10, 10);
        }
        else
            //daca butonul de play a fost apasat deschide meniul de play
            if (play.contains((int)event.getX(), (int)event.getY()))
            {
                btnStart.rect.inset(10, 10);
            }
            else
                //daca butonul de settings a fost apasat deschide ecranul de settings
                if (settings.contains((int)event.getX(), (int)event.getY()))
                {
                    btnSettings.rect.inset(10, 10);
                }
                else
                if(community.contains((int)event.getX(), (int)event.getY()))
                {
                    btnCommunity.rect.inset(10, 10);
                }
                else
                if (newGame != null && newGame.contains((int)event.getX(), (int)event.getY()))
                {
                    btnNewgame.rect.inset(10, 10);
                }
                else
                if (continueGame != null && continueGame.contains((int)event.getX(), (int)event.getY()))
                {
                    btnContinuegame.rect.inset(10, 10);
                }
                else
                if(sounds !=null && sounds.contains((int)event.getX(), (int)event.getY()))
                {
                    btnSounds.rect.inset(10, 10);
                }
                else
                if(resetGame !=null && resetGame.contains((int)event.getX(), (int)event.getY()))
                {
                    btnResetgame.rect.inset(10, 10);
                }
                else
                if(invitationsBox !=null && invitationsBox.contains((int)event.getX(), (int)event.getY()))
                {
                    btnInvitesBox.rect.inset(10, 10);
                }
                else
                if(invitePlayer !=null && invitePlayer.contains((int)event.getX(), (int)event.getY()))
                {
                    btnInvite.rect.inset(10, 10);
                }
    }

    //	public void startAnimation (Rect enemyShip)
    //	{
    //		explosionRect = enemyShip;
    //		animationTime = 0;
    //		animationRunning = true;
    //	}
    //
    //	long animationTime = 0;
    //	int animationFrameCount = 4;
    //	long animationFrameTime = 100;
    //	boolean animationRunning;
    //
    //	public void updateAnimation (long dt)
    //	{
    //		if (animationRunning)
    //		{
    //			animationTime += dt;
    //			Rect sprites[] = {firstSprite, secondSprite, thirdSprite, fourthSprite};
    //			for (int i = 0; i < animationFrameCount; i++)
    //			{
    //				if (animationTime >= i * animationFrameTime
    //						&& animationTime < (i + 1) * animationFrameTime)
    //					currentSprite = sprites [i];
    //			}
    //
    //			if (animationTime > animationFrameCount * animationFrameTime)
    //				animationRunning = false;
    //		}
    ////		if(System.currentTimeMillis()-spriteTime<=200)
    ////			currentSprite=firstSprite;
    ////		else
    ////			if(System.currentTimeMillis()-spriteTime<=400)
    ////				currentSprite=secondSprite;
    ////			else
    ////				if(System.currentTimeMillis()-spriteTime<=600)
    ////					currentSprite=thirdSprite;
    ////				else
    ////					if(System.currentTimeMillis()-spriteTime<=800)
    ////						currentSprite=fourthSprite;
    ////					else
    ////						if(System.currentTimeMillis()-spriteTime<=1000)
    ////							currentSprite=null;
    //	}
    //
    ////	public void explosion(Rect enemyShip, long spriteTime)
    ////	{
    ////		setExplosionRect(enemyShip);
    //////		long s=System.currentTimeMillis();
    //////		long c=spriteTime-s;
    ////		if(System.currentTimeMillis()-spriteTime<=200)
    ////			currentSprite=firstSprite;
    ////		else
    ////			if(System.currentTimeMillis()-spriteTime<=400)
    ////				currentSprite=secondSprite;
    ////			else
    ////				if(System.currentTimeMillis()-spriteTime<=600)
    ////					currentSprite=thirdSprite;
    ////				else
    ////					if(System.currentTimeMillis()-spriteTime<=800)
    ////						currentSprite=fourthSprite;
    ////					else
    ////						if(System.currentTimeMillis()-spriteTime<=1000)
    ////							currentSprite=null;
    ////	}
    //	public void setExplosionRect(Rect explosionRectangle)
    //	{
    //		explosionRect=explosionRectangle;
    //	}
    //	public Rect getExplosionRect()
    //	{
    //		return explosionRect;
    //	}
    //	public void explosionPaint(Canvas canvas, Rect enemyShip)
    //	{
    //		if(currentSprite!=null&& enemyShip!=null)
    //			canvas.drawBitmap(explosionSprite, currentSprite, enemyShip, null);
    //	}
    static String [] wrapText (String text, int len)
    {
        // return empty array for null text
        if (text == null)
            return new String [] {};

        // return text if len is zero or less
        if (len <= 0)
            return new String [] {text};

        // return text if less than length
        if (text.length() <= len)
            return new String [] {text};

        char [] chars = text.toCharArray();
        Vector lines = new Vector();
        StringBuffer line = new StringBuffer();
        StringBuffer word = new StringBuffer();

        for (int i = 0; i < chars.length; i++) {
            word.append(chars[i]);

            if (chars[i] == ' ') {
                if ((line.length() + word.length()) > len) {
                    lines.add(line.toString());
                    line.delete(0, line.length());
                }

                line.append(word);
                word.delete(0, word.length());
            }
        }

        // handle any extra chars in current word
        if (word.length() > 0) {
            if ((line.length() + word.length()) > len) {
                lines.add(line.toString());
                line.delete(0, line.length());
            }
            line.append(word);
        }
        // handle extra line
        if (line.length() > 0) {
            lines.add(line.toString());
        }
        String [] ret = new String[lines.size()];
        int c = 0; // counter
        for (Enumeration e = lines.elements(); e.hasMoreElements(); c++) {
            ret[c] = (String) e.nextElement();
        }

        return ret;
    }
}
