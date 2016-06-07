package com.rau.droidinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class CGamepad extends CEntity{

	// Temporary solution
	Rect fireRect,bigCircleRect,smallCircleRect;

	Bitmap bigCircleBm;
	Bitmap smallCircleBm;
	Bitmap fireBm;

	public static boolean fireBool=false;
	static final int BTN_SIZE = 60;
	static final int BTN_OFFSET = 20;
	static final int DIR_NONE = 0;
	static final int DIR_N = 1;
	static final int DIR_NE = 2;
	static final int DIR_E = 3;
	static final int DIR_SE = 4;
	static final int DIR_S = 5;
	static final int DIR_SV = 6;
	static final int DIR_V = 7;
	static final int DIR_NV = 8;
	private int xi, yi;
	private int dir = 0;
	int multitouch=0;
	int screenh;
	int offsetX ;
	int offsetY;
	int pointX ;
	int pointY;
	int bigCircleRadius;
	Paint mPaint;

	CGamepad (Rect bounds,Bitmap bigCircle,
			Bitmap smallCircle, int screenWidth)
			{
		super (bounds);
		bigCircleRect= new Rect(bounds);
		smallCircleRect= new Rect(bounds.centerX() - smallCircle.getWidth() / 2, bounds.centerY() - smallCircle.getHeight() / 2, bounds.centerX() + smallCircle.getWidth() / 2, bounds.centerY() + smallCircle.getHeight() / 2);
		bigCircleBm=bigCircle;
		smallCircleBm=smallCircle;
		fireRect= new Rect(screenWidth-smallCircle.getWidth()*2-screenWidth/50, bounds.centerY()-smallCircle.getHeight()/2 ,screenWidth-screenWidth/50, bounds.centerY()-smallCircle.getHeight()/2 + smallCircle.getHeight()*2);
		fireBm=smallCircle;
		bigCircleRadius=bigCircleRect.width()/2;
		mPaint= new Paint();
		mPaint.setColor(Color.RED);
			}
	//desenarea joystickului
	void paint (Canvas canvas)
	{
		canvas.drawBitmap(bigCircleBm,null, bigCircleRect, mPaint);
		canvas.drawBitmap(smallCircleBm, null, smallCircleRect, mPaint);
		canvas.drawBitmap(fireBm, null, fireRect, mPaint);
	}
	void update ()
	{

	}

	public void onTouch (MotionEvent event)
	{
		super.onTouch(event);  
		int action = event.getAction();
		int pointerCount = event.getPointerCount();
		//verifica daca unul sau mai multe degete sunt pe ecran
		if(pointerCount>1)
			multitouch=1;
		else multitouch=0;

		switch(multitouch){
		case 0:
			//daca cercul mic nu este atins, reseteaza pozitia cercului
			if(!smallCircleRect.contains((int)event.getX(), (int)event.getY()))
				smallCircleRect.offsetTo(bounds.centerX() - smallCircleRect.width() / 2, bounds.centerY() - smallCircleRect.height() / 2);
			switch(action){
			case MotionEvent.ACTION_DOWN:
				dir=DIR_NONE;
				if(smallCircleRect.contains((int)event.getX(),(int)event.getY()))
				{
					xi = (int)event.getX();
					yi = (int)event.getY();
				}
				//muta cercul mic la pozitia in care ecranul este atins
				else
					if(event.getX()<bigCircleRect.centerX() + bigCircleRadius && event.getX() > bigCircleRect.centerX()-bigCircleRadius && event.getY()<bigCircleRect.centerY() + bigCircleRadius && event.getY() > bigCircleRect.centerY()-bigCircleRadius)
					{
						smallCircleRect.offsetTo((int)event.getX()+(smallCircleRect.left-smallCircleRect.centerX()),(int)event.getY()+(smallCircleRect.top-smallCircleRect.centerY()));
					}
				//daca buttonul de tragere este apasat returneaza true
				if(fireRect.contains((int)event.getX(), (int)event.getY()))
					fireBool=true;

				break;

			case MotionEvent.ACTION_MOVE:
				pointX  = (int)event.getX();
				pointY  = (int)event.getY();
				//updateaza pozitia cercului mic
				if(smallCircleRect.contains((int)event.getX(), (int)event.getY()))
				{
					offsetX = (int)event.getX() - xi;
					offsetY = (int)event.getY() - yi;
					xi = (int)event.getX();
					yi = (int)event.getY();
					//possible coords
					if(smallCircleRect.top+offsetY>=bigCircleRect.top&&smallCircleRect.right+offsetX<=bigCircleRect.right&&smallCircleRect.left+offsetX>=bigCircleRect.left&&smallCircleRect.bottom+offsetY<=bigCircleRect.bottom)
						smallCircleRect.offset(offsetX, offsetY);
				}
				//calculeaza directia in care nava este miscata
				if(bounds.contains(pointX, pointY))
				{
					int _offsetX, _offsetY;
					_offsetX = pointX - bounds.centerX();
					_offsetY = pointY - bounds.centerY();
					if (_offsetX != 0 || _offsetY != 0)
					{
						double d = Math.sqrt(Math.pow(_offsetX, 2) + Math.pow(_offsetY, 2));	
						if (d > 0)
						{
							double dsin = _offsetY / d;
							double alpha1 = Math.toDegrees(Math.asin(dsin));
							if (alpha1 < -67.5)
							{
								dir=DIR_N;
							}
							else if (alpha1 > -67.5 && alpha1 < -22.5)
							{
								if (_offsetX > 0)
									dir=DIR_NE;
								else
									dir=DIR_NV;
							}
							else if (alpha1 > -22.5 && alpha1 < 22.5)
							{
								if (_offsetX > 0)
									dir=DIR_E;
								else
									dir=DIR_V;
							}
							else if (alpha1 > 22.5 && alpha1 < 67.5)
							{
								if (_offsetX > 0)
									dir=DIR_SE;
								else
									dir=DIR_SV;
							}
							else if (alpha1 > 67.5)
							{
								dir=DIR_S;
							}
							else
							{
								dir = DIR_NONE;
							}
						}
					}
				}
				//returneaza true daca buttonul de tragere este apasat
				if(fireRect.contains((int)event.getX(), (int)event.getY()))
					fireBool=true;
				else
					fireBool=false;
				break;

				//reseteaza joystickul daca miscarea este in afara lui
			case MotionEvent.ACTION_OUTSIDE:
				dir = DIR_NONE;
				if(smallCircleRect.contains((int)event.getX(), (int)event.getY()))
				{
					smallCircleRect.offsetTo(bounds.centerX() - smallCircleRect.width() / 2, bounds.centerY() - smallCircleRect.height() / 2);

				}
				fireBool=false;
				break;
				//reseteaza joystickul daca eventul a fost anulat
			case MotionEvent.ACTION_CANCEL:
				dir = DIR_NONE;
				if(smallCircleRect.contains((int)event.getX(), (int)event.getY()))
				{
					smallCircleRect.offsetTo(bounds.centerX() - smallCircleRect.width() / 2, bounds.centerY() - smallCircleRect.height() / 2);
				}
				break;
				//reseteaza joystickul daca degetul a fost ridicat de pe ecran
			case MotionEvent.ACTION_UP:
				dir=DIR_NONE;
				smallCircleRect.offsetTo(bounds.centerX() - smallCircleRect.width() / 2, bounds.centerY() - smallCircleRect.height() / 2);
				offsetX=0;
				offsetY=0;
				if(fireRect.contains((int)event.getX(), (int)event.getY()))
					fireBool=false;
				break;
			default:
				smallCircleRect.offsetTo(bounds.centerX() - smallCircleRect.width() / 2, bounds.centerY() - smallCircleRect.height() / 2);
				dir=DIR_NONE;
				break;
			}
			break;
		case 1:
			switch(action){
			case MotionEvent.ACTION_DOWN:
				dir=DIR_NONE;
				if(smallCircleRect.contains((int)event.getX(),(int)event.getY()))
				{
					xi = (int)event.getX();
					yi = (int)event.getY();
				}
				//daca buttonul de tragere este apasat returneaza true
				if(fireRect.contains((int)event.getX(1), (int)event.getY(1)))
					fireBool=true;
				break;
			case MotionEvent.ACTION_MOVE:
				//misca cercul mic in functie de miscarea degetului pe ecran (odata daca primul deget atinge joystickul si odata pentru al doilea)
				if(smallCircleRect.contains((int)event.getX(0), (int)event.getY(0)))
				{
					offsetX = (int)event.getX(0) - xi;
					offsetY = (int)event.getY(0) - yi;
					pointX = xi = (int)event.getX(0);
					pointY = yi = (int)event.getY(0);
					//possible coords
					if(smallCircleRect.top+offsetY>=bigCircleRect.top&&smallCircleRect.right+offsetX<=bigCircleRect.right&&smallCircleRect.left+offsetX>=bigCircleRect.left&&smallCircleRect.bottom+offsetY<=bigCircleRect.bottom)
						smallCircleRect.offset(offsetX, offsetY);
				}
				else 
					if(smallCircleRect.contains((int)event.getX(1), (int)event.getY(1)))
					{
						offsetX = (int)event.getX(1) - xi;
						offsetY = (int)event.getY(1) - yi;
						pointX = xi = (int)event.getX(1);
						pointY = yi = (int)event.getY(1);
						//possible coords
						if(smallCircleRect.top+offsetY>=bigCircleRect.top&&smallCircleRect.right+offsetX<=bigCircleRect.right&&smallCircleRect.left+offsetX>=bigCircleRect.left&&smallCircleRect.bottom+offsetY<=bigCircleRect.bottom)
							smallCircleRect.offset(offsetX, offsetY);
					}
				//calculeaza directia in care nava este miscata
				if(bounds.contains(pointX, pointY))
				{
					int _offsetX, _offsetY;
					_offsetX = pointX - bounds.centerX();
					_offsetY = pointY - bounds.centerY();
					if (_offsetX != 0 || _offsetY != 0)
					{
						double d = Math.sqrt(Math.pow(_offsetX, 2) + Math.pow(_offsetY, 2));	
						if (d > 0)
						{
							double dsin = _offsetY / d;
							double alpha1 = Math.toDegrees(Math.asin(dsin));
							if (alpha1 < -67.5)
							{
								dir=DIR_N;
							}
							else if (alpha1 > -67.5 && alpha1 < -22.5)
							{
								if (_offsetX > 0)
									dir=DIR_NE;
								else
									dir=DIR_NV;
							}
							else if (alpha1 > -22.5 && alpha1 < 22.5)
							{
								if (_offsetX > 0)
									dir=DIR_E;
								else
									dir=DIR_V;
							}
							else if (alpha1 > 22.5 && alpha1 < 67.5)
							{
								if (_offsetX > 0)
									dir=DIR_SE;
								else
									dir=DIR_SV;
							}
							else if (alpha1 > 67.5)
							{
								dir=DIR_S;
							}
							else
							{
								dir = DIR_NONE;
							}
						}
					}
				}
				//daca buttonul de tragere este apasat returneaza true
				if(fireRect.contains((int)event.getX(0), (int)event.getY(0))||fireRect.contains((int)event.getX(1), (int)event.getY(1)))
					fireBool=true;
				else
					fireBool=false;
				break;
				//reseteaza joystickul daca degetul a fost ridicat de pe ecran
			case MotionEvent.ACTION_POINTER_UP:
				dir=DIR_NONE;
				if(smallCircleRect.contains((int)event.getX(event.getActionIndex()), (int)event.getY(event.getActionIndex())))
					smallCircleRect.offsetTo(bounds.centerX() - smallCircleRect.width() / 2, bounds.centerY() - smallCircleRect.height() / 2);
				offsetX=0;
				offsetY=0;
				if(fireRect.contains((int)event.getX(event.getActionIndex()), (int)event.getY(event.getActionIndex())))
					fireBool=false;
				break;
			}
			break;
		}
	}
	//returneaza directia in care se misca nava
	public int getDir()
	{
		return dir;
	}
}
