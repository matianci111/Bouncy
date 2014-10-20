package com.example.templateproject;

import com.example.templateproject.screen;
import com.example.templateproject.Sprite;


public class Block extends Sprite{
	public Block(screen Screen, String fileName, int x, int y, int i, int j){
		super(Screen, fileName, x, y);
		this.x = this.x + j*this.width+20 + 6*j;
		this.y = this.y + i*this.height;
		// TODO Auto-generated constructor stub
	}
}
