package com.example.templateproject;

import org.opencv.core.Point;


public class Plate extends Sprite{
	public int xSpeed;
	
	public Plate(screen Screen, String string, int i, int j) {
		super(Screen, string, i, j);
		xSpeed = 0;
		// TODO Auto-generated constructor stub
	}

	public void movement(Point user) {
		xSpeed = (int) (user.x - this.x);
		if(xSpeed < 5 && xSpeed >-5 ){
			xSpeed = 0;
		}
		if(this.x+xSpeed < 20 || this.x+width+xSpeed > this.Screen.getWidth()-20){
			xSpeed = 0;
			//do nothing
		}
		else{
			this.x = this.x + xSpeed;		
		}
		// TODO Auto-generated method stub
		
	
		
	}
}
