package com.example.templateproject;


import android.graphics.Canvas;
import android.util.Log;

public class GameLoopThread extends Thread{
	static final long FPS = 30;
	private screen view;
	private Object pauselock;
	private boolean running;
	private boolean paused;
	
	
	public GameLoopThread(screen screen) {
		// TODO Auto-generated constructor stub
		this.view = screen;
		pauselock = new Object();
		paused = false;
		running = false;
	}

	public void setRunning(boolean run) {
		// TODO Auto-generated method stub
		running = run;
		
	}
	
	public void onPause(){
		synchronized(pauselock){
			paused = true;
		}
	}
	
	public void onResume(){
		synchronized(pauselock){
			paused = false;
			pauselock.notifyAll();
		}
	}
	
	@Override
	public void run() {
		long ticksPS = 1000 / FPS;
		long starttime;
		long sleeptime;
		Log.d("thread", "thread started running");
		
		while(running){
			Canvas c = null;
			starttime = System.currentTimeMillis();
			Log.d("thread", "thread is running");
				try{
					c = view.getHolder().lockCanvas();
					synchronized(view.getHolder()){
						view.onDraw(c);
					}
				} finally {
					if(c!=null){
						view.getHolder().unlockCanvasAndPost(c);
					}
				}
				sleeptime = ticksPS - (System.currentTimeMillis() - starttime);
				try{
					if(sleeptime > 0)
						sleep(sleeptime);
					else
						sleep(10);
				} catch (Exception e) {}
				
			synchronized(pauselock){
				while(paused){
					try{
						pauselock.wait();
					}catch(InterruptedException e){
					}
				}
			}
		}
	}

}
