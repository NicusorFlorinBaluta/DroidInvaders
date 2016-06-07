package com.rau.droidinvaders;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class Touchpad extends CEntity{

	public static boolean fireBool=false;
	int screenh;
	int offsetX ;
	int offsetY;
	int pointX ;
	int pointY;
	int xi = -1, yi = -1;
	int scrW, scrH;
	boolean moveShip;
	Paint mPaint;

	Rect ship;

	Touchpad (Rect bounds, int screenWidth, int screenHeight)
	{
		ship=bounds;
		mPaint= new Paint();
		mPaint.setColor(Color.RED);
		scrW=screenWidth;
		scrH=screenHeight;

	}

	public void setBounds (Rect bounds)
	{
		ship = bounds;
	}

	//desenarea joystickului
	void paint (Canvas canvas)
	{
		//		mPaint.setColor(Color.RED);
		//		canvas.drawRect(ship, mPaint);
		//		canvas.drawLine(ship.centerX(), ship.centerY(), xi, yi, mPaint);
	}
	void update ()
	{
		//		if(moveShip)

	}

	public void onTouch (MotionEvent event)
	{
		super.onTouch(event);  
		int action = event.getAction();
		switch(action){
		case MotionEvent.ACTION_DOWN:
			xi = (int)event.getX();
			yi = (int)event.getY();
			offsetX=0;
			offsetY=0;
			if(ship.contains((int)event.getX(), (int)event.getY()))
			{
				moveShip=true;
				fireBool=true;
				ship.offset(0, -50);
			}	
			break;

		case MotionEvent.ACTION_MOVE:
			//			if(ship.contains((int)event.getX(), (int)event.getY()))
			//			{
			if(moveShip)
			{
				if (xi != -1 && yi != -1)
				{
					int offsetX = (int)event.getX() - xi;
					int offsetY = (int)event.getY() - yi;
					xi = (int)event.getX();
					yi = (int)event.getY();
					//possible coords
					if(ship.top+offsetY>=0
							&& ship.right + offsetX <= scrW
							&& ship.left + offsetX >= 0
							&& ship.bottom + offsetY <= scrH)
						ship.offset(offsetX, offsetY);
				}
			}
			break;

		case MotionEvent.ACTION_OUTSIDE:
			offsetX=0;
			offsetY=0;
			moveShip=false;
			fireBool=false;
			break;
		case MotionEvent.ACTION_CANCEL:
			offsetX=0;
			offsetY=0;
			moveShip=false;
			fireBool=false;
			break;
		case MotionEvent.ACTION_UP:
			offsetX=0;
			offsetY=0;
			moveShip=false;
			fireBool=false;
			break;
		default:
			break;
		}
	}
}
