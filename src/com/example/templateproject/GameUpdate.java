package com.example.templateproject;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;

import android.media.AudioManager;
import android.util.Log;

import com.example.templateproject.Ball;
import com.example.templateproject.Block;
import com.google.android.glass.media.Sounds;


public class GameUpdate {

	  private List<Block> blocks = new ArrayList<Block>();
	  private Plate plate;
	  private Ball ball;
	  private boolean gamelost;
	  private int resetcountdown;
	  private int score;
	  private AudioManager mAudioManager;
	  private boolean gameWin;
	  
	  
	public GameUpdate(Plate plate, Ball ball, List<Block> blocks) {
		this.blocks = blocks;
		this.ball = ball;
		this.plate = plate;
		gamelost = false;
		resetcountdown = 0;
		score = 0;
		gameWin = false;
	}

	public void updateAll(Point user) {
		Log.d("blablabla in updateAll", "x is " + user.x + " y is " + user.y);
		checkCollision(ball, plate);
		for (Block block : blocks) {
           	if(checkCollision(ball, block))
           	{
           		blocks.remove(block);
           		score++;
           		mAudioManager.playSoundEffect(Sounds.TAP);
           		if(blocks.isEmpty()){
           			gameWin = true;
           			mAudioManager.playSoundEffect(Sounds.SUCCESS);
           		}
           		break;
           	}
           		//blocks.remove(block);
        }
		ball.movement();
		plate.movement(user);
		if(ball.outside()){
			gamelost = true;
			mAudioManager.playSoundEffect(Sounds.ERROR);
		}
		// TODO Auto-generated method stub
		
	}

	private boolean checkCollision(Ball ball, Sprite sprite2) {
		boolean collided=true;
		ball.calculateNext();
		switch(CollisionDirection(ball, sprite2)){
		case 0:
			collided = false;
			break;
		case 1:
			ball.ySpeed = -ball.ySpeed;
			break;
		case 2:
			ball.xSpeed = -ball.xSpeed;
			break;
		case 3:
			ball.xSpeed = -ball.xSpeed;	
			ball.ySpeed = -ball.ySpeed;
			break;
		};
		return collided;
		// TODO Auto-generated method stub
		
	}

	private int CollisionDirection(Ball ball, Sprite sprite2) {
		int direction = 0;//nothing
		
		if(topleft(ball, sprite2)||topright(ball, sprite2)||botleft(ball, sprite2)|| botright(ball, sprite2))
			direction = 3;
		if((topleft(ball, sprite2)&&topright(ball, sprite2))||(botleft(ball, sprite2)&& botright(ball, sprite2)))
			direction = 1;
		if((topleft(ball, sprite2)&&botleft(ball, sprite2))||(topright(ball, sprite2)&& botright(ball, sprite2)))
			direction = 2;
		
		return direction;
			
	}

	private boolean topleft(Ball ball, Sprite sprite2) {
		return ball.nextX<sprite2.x+sprite2.width && ball.nextX>sprite2.x && ball.nextY > sprite2.y && ball.nextY < sprite2.y+sprite2.height;

	}

	private boolean topright(Ball ball, Sprite sprite2) {
		return ball.nextX+ball.width<sprite2.x+sprite2.width && ball.nextX+ball.width>sprite2.x && ball.nextY > sprite2.y && ball.nextY < sprite2.y+sprite2.height;
	}

	private boolean botleft(Ball ball, Sprite sprite2) {
		return ball.nextX<sprite2.x+sprite2.width && ball.nextX>sprite2.x && ball.nextY+ball.height > sprite2.y && ball.nextY+ball.height < sprite2.y+sprite2.height;
	}

	private boolean botright(Ball ball, Sprite sprite2) {
		return ball.nextX+ball.width<sprite2.x+sprite2.width && ball.nextX+ball.width>sprite2.x && ball.nextY+ball.height > sprite2.y && ball.nextY+ball.height < sprite2.y+sprite2.height;
	}

	public boolean lost() {
		// TODO Auto-generated method stub
		return gamelost;
	}

	public boolean waitrestart() {
		boolean reset = false;
		resetcountdown++;
		if(resetcountdown == 200){
			resetcountdown = 0;
			gamelost = false;
			reset = true;
			score = 0;
			gameWin = false;
		}
		return reset;	
		// TODO Auto-generated method stub

	}

	public int getScore() {
		// TODO Auto-generated method stub
		return score;
	}

	public void receiveAudio(AudioManager mAudioManager) {
		// TODO Auto-generated method stub
		this.mAudioManager = mAudioManager;
	}

	public boolean win() {
		// TODO Auto-generated method stub
		return gameWin;
	}

}
