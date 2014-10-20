package com.example.templateproject;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;


public class Sprite {

    protected int x;
    protected int y;
    protected InputStream bitmapstring;
    protected Bitmap bitmap; 
    
    private String fileName;
    protected screen Screen;

    protected int width;
    protected int height;
    private Rect src;
    private Rect dst;

    public Sprite(screen Screen, String fileName, int x, int y) {
          this.Screen = Screen;
          this.fileName = fileName;
         
          this.x = x;
          this.y = y;
          src = new Rect();
          dst = new Rect();
          try {
				bitmapstring = Screen.getContext().getAssets().open(fileName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          bitmap = BitmapFactory.decodeStream(bitmapstring);
         width = bitmap.getWidth();
         height = bitmap.getHeight();

    }

    private void update() {
 	   	
    }

    public void onDraw(Canvas canvas) {
          update();
          src.set(0, 0, width, height);
          dst.set(x, y, x+width, y+height);
          canvas.drawBitmap(bitmap, src, dst, null);

    }
    

}
