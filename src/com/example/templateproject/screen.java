package com.example.templateproject;


import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class screen extends SurfaceView{
	   private SurfaceHolder holder;
	   private GameLoopThread gameLoopThread;
	   private boolean gameisRunning;
	   private List<Block> blocks = new ArrayList<Block>();
	   private Plate plate;
	   private Ball ball;
	   private GameUpdate gameUpdate;
	   protected Point user = new Point(100,100);;
	   private Paint paint = new Paint();
	   private AudioManager mAudioManager;
	
	public screen(Context context, AudioManager mAudioManager) {
		super(context);
		this.mAudioManager = mAudioManager;
		if(mAudioManager == null){
			Log.d("in Screen context constructor", "is null");
		}
		else{
			Log.d("blabla in screen context constructor", "is not null");
		}
		selfinit();
		// TODO Auto-generated constructor stub
	}

	

	public screen(Context context, AttributeSet attrs) {
	    super(context, attrs); // This should be first line of constructor
	    selfinit();
	}
	
	public screen(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        selfinit();
	}
	

    private void initializeLevel() {
    	ball = new Ball(this, "ball.png", this.getWidth()/2, this.getHeight()-90);
    	Log.d("in initialize", this.getWidth()+ " and " + this.getHeight());
    	//load the plate
		plate = new Plate(this, "plate.png", this.getWidth()/2-60, this.getHeight()-30);
    	//load all blocks
    	blocksinit();
    	
    	//load the ball
	}

    private void blocksinit() {
    	for(int i=0;i<2;i++)
    		for(int j=0;j<7;j++)
    			blocks.add(createBlock(i, j));
		
	}



	private Block createBlock(int i, int j) {
		String fileName = "block.png";
		Block block = new Block(this, fileName, 0, 0, i, j); 
		return block;
	}
    
	@Override
	protected void onDraw(Canvas canvas) {
		if(!gameUpdate.lost() && !gameUpdate.win()){
			paint.setColor(Color.GRAY);
	        paint.setStrokeWidth(2);
	        paint.setTextSize(100);
	        paint.setAlpha(50);
			gameUpdate.updateAll(user);			
	        canvas.drawColor(Color.BLACK);
	        canvas.drawText(""+gameUpdate.getScore(), this.getWidth()/2-20, this.getHeight()/2+30, paint);
	        for (Block block : blocks) {
	            block.onDraw(canvas);
	        }
	        ball.onDraw(canvas);
	        plate.onDraw(canvas);
		}
		else{
			if(gameUpdate.lost()){
				paint.setColor(Color.RED);
				paint.setAlpha(255);
				canvas.drawColor(Color.BLACK);
				canvas.drawText("You Lost", this.getWidth()/2-190, this.getHeight()/2+30, paint);
			}
			if(gameUpdate.win()){
				paint.setColor(Color.GREEN);
				paint.setAlpha(255);
				canvas.drawColor(Color.BLACK);
				canvas.drawText("You Win!", this.getWidth()/2-190, this.getHeight()/2+30, paint);
			}
			if(gameUpdate.waitrestart()){
				levelinit();
			}
		}

	}


	private void levelinit() {
		// TODO Auto-generated method stub
		blocks.clear();
		blocksinit();
		ball.x = this.getWidth()/2;
		ball.y = this.getHeight()/2;
		ball.xSpeed = 3;
		ball.ySpeed = -3;
		plate.x = this.getWidth()/2-60;
		plate.y = this.getHeight()-30;
		
	}



	public void pauseThread() {
		// TODO Auto-generated method stub
		gameLoopThread.setRunning(false);
		gameLoopThread.onPause();
		
	}

	public void resumeThread() {
		gameLoopThread.setRunning(true);
		// TODO Auto-generated method stub
		
	}
	
	private void selfinit() {
		// TODO Auto-generated method stub
		
		gameisRunning = false;
		gameLoopThread = new GameLoopThread(this);
		holder = getHolder();
		holder.addCallback(new SurfaceHolder.Callback() {
		
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				Log.d("thread", "surfacecreated");
				if(!gameisRunning){
					 initializeLevel();
					 gameUpdate = new GameUpdate(plate, ball, blocks);
					 if(mAudioManager == null){
							Log.d("in Surfacecreated", "is null");
						}
						else{
							Log.d("in surfacecreated", "is not null");
						}
					 gameUpdate.receiveAudio(mAudioManager);
					 gameLoopThread.setRunning(true);
					 gameLoopThread.start();
					 Log.d("thread", "thread started");
					 gameisRunning = true;
				}
           	 else{
           		 gameLoopThread.onResume();
           	 }
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				Log.d("thread", "why is this called?");
				
			}
		});
	}



	public void receiveAudio(AudioManager mAudioManager) {
		// TODO Auto-generated method stub
		this.mAudioManager = mAudioManager;
		if(mAudioManager == null){
			Log.d("in receiveAudio", "is null");
		}
		else{
			Log.d("in receiveAudio", "is not null");
		}
	}

}
