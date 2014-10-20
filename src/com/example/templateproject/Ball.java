package com.example.templateproject;

import com.example.templateproject.screen;
import com.example.templateproject.Sprite;


public class Ball extends Sprite{
	public int xSpeed;
	public int ySpeed;
	public int nextX;
	public int nextY;

	public Ball(screen Screen, String fileName, int x, int y) {
		super(Screen, fileName, x, y);
		xSpeed = 3;
		ySpeed = -3;
		
		
		// TODO Auto-generated constructor stub
	}

	public void movement() {
		if(this.x+xSpeed < 20 || this.x+width+xSpeed > this.Screen.getWidth()-20){
			xSpeed = -xSpeed;
		}
		else{
			this.x = this.x + xSpeed;
		}
		if(this.y+ySpeed < 0){
			ySpeed = -ySpeed;
		}
		else{
			this.y = this.y + ySpeed;
		}
		// TODO Auto-generated method stub
		
	
	}
	public void calculateNext(){
		nextX = x+xSpeed;
		nextY = y+ySpeed;
	}

	public boolean outside() {
		// TODO Auto-generated method stub
		return (this.y+height+ySpeed > this.Screen.getHeight());
	}
}
