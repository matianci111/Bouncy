package com.example.templateproject;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

public class MainActivity extends Activity {
	private final GestureDetector.BaseListener mBaseListener = new GestureDetector.BaseListener() {
        @Override
        public boolean onGesture(Gesture gesture) {
            if (gesture == Gesture.TAP) {
                mAudioManager.playSoundEffect(Sounds.TAP);
                openOptionsMenu();
                return true;
            } else {
                return false;
            }
        }
    };
    private final Handler mHandler = new Handler();
    
    private AudioManager mAudioManager;

    private GestureDetector mGestureDetector;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mGestureDetector = new GestureDetector(this).setBaseListener(mBaseListener);
	}
	
	@Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mGestureDetector.onMotionEvent(event);
    }    
	
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.start_game, menu);
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The startXXX() methods start a new activity, and if we call them directly here then
        // the new activity will start without giving the menu a chance to slide back down first.
        // By posting the calls to a handler instead, they will be processed on an upcoming pass
        // through the message queue, after the animation has completed, which results in a
        // smoother transition between activities.
        switch (item.getItemId()) {
            case R.id.New_Game:
            	
                startActivity();
                

                return true;

            case R.id.Exit:
                finish();
                return true;

            default:
                return false;
        }
	}
	private void startActivity() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, PreviewActivity.class);
        startActivity(intent);
        finish();
	}
	
}