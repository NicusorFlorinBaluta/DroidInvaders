package com.rau.droidinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Animation {
	int frames [];
	int framesTime [];
	int currentFrameIdx;
	long animationTime = 0;
	int frameCount = 4;
	boolean running;
	Rect bmpSrc;
	Bitmap img;

	int spriteWidth;
	int spriteHeight;

	int spritesInRow;
	int spritesInColumn;

	Rect destRect;
	Paint mPaint;

	public Animation(Rect destRect, Bitmap imgSprites, int spriteWidth, int spriteHeight) 
	{
		this.destRect = destRect;

		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;

		spritesInRow = imgSprites.getWidth() / spriteWidth;
		spritesInColumn = imgSprites.getHeight() / spriteHeight;

		frameCount = spritesInRow * spritesInColumn;
		currentFrameIdx = 0;
		bmpSrc = new Rect (0, 0, spriteWidth, spriteHeight);
		img = imgSprites;

		mPaint = new Paint();
	}

	public void startAnimation (int frames[], int framesTime[])
	{
		animationTime = 0;
		running = true;

		this.frames = new int [frames.length];
		System.arraycopy(frames, 0, this.frames, 0, frames.length);

		this.framesTime = new int [framesTime.length];
		System.arraycopy(framesTime, 0, this.framesTime, 0, framesTime.length);
	}

	public void updateAnimation (long dt)
	{
		if (running)
		{
			animationTime += dt;
			if (animationTime >= framesTime [0]*currentFrameIdx)
				currentFrameIdx++;

			if (currentFrameIdx >= frames.length)
				running = false;
		}
	}

	public void paint(Canvas canvas) 
	{
		if (running)
		{
			int frame = frames [currentFrameIdx];
			int y_src = (frame / spritesInRow) * spriteHeight;
			int x_src = (frame % spritesInRow) * spriteWidth;
			bmpSrc.offsetTo(x_src, y_src);
			canvas.drawBitmap(img, bmpSrc, destRect, mPaint);
		}
	}
}
