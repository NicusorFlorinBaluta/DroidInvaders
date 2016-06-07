//clasa pentru setat background pentru meniu
package com.rau.droidinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Background {

	Bitmap bmp;
	Bitmap scaled;
	private int width;
	private int height;


	public Background(Bitmap bmp, int canvasWidth, int canvasHeight) {
		scaled = Bitmap.createBitmap(bmp);
		width =  canvasWidth;  
		height = canvasHeight;
	}
	public void paint(Canvas canvas) {
		Rect src = new Rect(0, 0, scaled.getWidth(), scaled.getHeight());
		Rect dst = new Rect(0, 0, width, height);
		//desenarea si scalarea imagini sursa
		canvas.drawBitmap(scaled, src, dst, null);
	}
}
