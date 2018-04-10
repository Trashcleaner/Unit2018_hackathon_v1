package com.CtuiInPrague.Hackathon;

import com.unity3d.player.*;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class UnityPlayerActivity extends Activity {
    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code

    // Setup activity layout
    //Declare a FrameLayout object
    FrameLayout fl_forUnity;

    //Declare the buttons
    Button bt_event1;
    Button bt_event2;

    ImageView img;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy

        mUnityPlayer = new UnityPlayer(this);
        if (mUnityPlayer.getSettings().getBoolean("hide_status_bar", true)) {
            setTheme(android.R.style.Theme_NoTitleBar_Fullscreen);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        //setContentView(mUnityPlayer);
        //Set the content to main
        setContentView(R.layout.activity_main_layout);

        //Inflate the frame layout from XML
        this.fl_forUnity = (FrameLayout) findViewById(R.id.fl_forUnity);

        //Add the mUnityPlayer view to the FrameLayout, and set it to fill all the area available
        this.fl_forUnity.addView(mUnityPlayer.getView(),
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        //Initialize the buttons from the XML file
        this.bt_event1 = (Button) findViewById(R.id.bt_event1);
        this.bt_event2 = (Button) findViewById(R.id.bt_event2);

        /*Send a brodcast a message to the 'Main Camera' game object, passing L, R according to the
        pressed buttons.*/
        this.bt_event1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //UnityPlayer.UnitySendMessage("Main Camera", "ReceiveRotDir", "L");
                receiveStr(Constants.obj1);
            }
        });

        this.bt_event2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //UnityPlayer.UnitySendMessage("Main Camera", "ReceiveRotDir", " ");
                receiveStr(Constants.obj2);
            }
        });

        mUnityPlayer.requestFocus();

    }

    public void receiveStr(String str){
        switch (str){
            case Constants.obj1:
                Log.e("Ev", "event1");

                textView=(TextView)findViewById(R.id.tv_info_text);
                textView.setText(getString(R.string.description1));

                img=(ImageView)findViewById(R.id.iv_info_image);
                Drawable myDrawable1 = getResources().getDrawable(R.drawable.mrs3);
                img.setImageDrawable(myDrawable1);

                break;
            case Constants.obj2:
                Log.e("Ev", "event2");

                textView=(TextView)findViewById(R.id.tv_info_text);
                textView.setText(getString(R.string.description2));

                img=(ImageView)findViewById(R.id.iv_info_image);
                Drawable myDrawable2 = getResources().getDrawable(R.drawable.mrs16);
                img.setImageDrawable(myDrawable2);

                break;
            default:
                break;

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        setIntent(intent);
    }

    // Quit Unity
    @Override
    protected void onDestroy() {
        mUnityPlayer.quit();
        super.onDestroy();
    }

    // Pause Unity
    @Override
    protected void onPause() {
        super.onPause();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override
    protected void onResume() {
        super.onResume();
        mUnityPlayer.resume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUnityPlayer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mUnityPlayer.stop();
    }

    // Low Memory Unity
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL) {
            mUnityPlayer.lowMemory();
        }
    }

    // This ensures the layout will be correct.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    /*API12*/
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }
}
