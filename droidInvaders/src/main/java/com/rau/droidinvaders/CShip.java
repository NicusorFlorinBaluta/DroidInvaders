package com.rau.droidinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class CShip extends CEntity 
{
	float mAxisX = 0.0f;
	float mAxisY = 0.0f;
	float mX =  0.0f;
	float mY =  0.0f;
	long fireRate;
	int height;
	int width;
	int speed;
	Rect shipRect;
	Bitmap shipBm;
	Paint mPaint;

	CShip (Rect bounds,Bitmap shipBitmap, int scrheight, int scrwidth)
	{
		super (bounds);
		shipRect=bounds;
		shipBm=shipBitmap;
		height=scrheight;
		width=scrwidth;
		speed=width/10 -1;
		mPaint= new Paint();
		mPaint.setColor(Color.BLUE);
	}
	//deseneaza nava
	void paint (Canvas canvas)
	{

		canvas.drawBitmap(shipBm,null, bounds, mPaint);
	}
	//misca nava in functie de directia trimisa de joystick
	void update (int dir)
	{
		switch (dir)
		{
		case CGamepad.DIR_N:
			if(bounds.top-speed>=0)
				bounds.offset(0, -speed);
			break;
		case CGamepad.DIR_S:
			if(bounds.bottom+speed<=height)
				bounds.offset(0, speed);
			break;
		case CGamepad.DIR_V:
			if(bounds.left -speed>=0)
				bounds.offset(-speed, 0);
			break;
		case CGamepad.DIR_E:
			if(bounds.right+speed<=width)
				bounds.offset(speed, 0);
			break;
		case CGamepad.DIR_NE:
			if(bounds.right+speed<=width&&bounds.top>=0)
				bounds.offset(speed, -speed);
			break;
		case CGamepad.DIR_NV:
			if(bounds.left-speed>=0&&bounds.top-speed>=0)
				bounds.offset(-speed, -speed);
			break;
		case CGamepad.DIR_SV:
			if(bounds.left-speed>=0&&bounds.bottom+speed<=height)
				bounds.offset(-speed, speed);
			break;
		case CGamepad.DIR_SE:
			if(bounds.right+speed<=width&&bounds.bottom+speed<=height)
				bounds.offset(speed, speed);
			break;
		case CGamepad.DIR_NONE:
			bounds.offset(0, 0);
			break;
		}

	}
	//seteaza frecventa de tragere a navei
	public void setFireRate (long fireRate)
	{
		this.fireRate = fireRate;
	}
	//returneaza frecventa de tragere a navei
	public long getFireRate()
	{
		return fireRate;
	}
	//distruge obiectul(nava)
	void dispose() 
	{
		super.dispose();
	}
}
