//clasa pentru crearea, desenarea, miscarea, reciclarea si distrugerea de proiectile
package com.rau.droidinvaders;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class CAmmunition extends CEntity{
	Bitmap lasers;
	Paint mPaint;
	private static ArrayList<CAmmunition> pool = new ArrayList<CAmmunition>();
	
	public static CAmmunition createAmmunition (Rect bounds, Bitmap laser)
	{
		//creare de proiectil nou in cazul in care nu exista unul dispus pentru reciclare
		if (pool.size() <= 0)
		{
			return new CAmmunition(bounds, laser);
		}
		//folosirea unui proiectil deja existent
		else
		{
			CAmmunition a = pool.remove(0);
			a.bounds.set(bounds);
			a.lasers = laser;
			return a;
		}
	}
	//reciclarea de proiectil
	public static void recycle (CAmmunition a)
	{
		pool.add (a);
	}
	//distrugerea tuturor proiectilelor din pool
	public static void clearPool()
	{
		for(int i=0;i<pool.size();i++)
			pool.get(i).dispose();
	}
	
	private CAmmunition (Rect bounds, Bitmap laser)
	{
		super (bounds);
		mPaint = new Paint ();
		mPaint.setColor(Color.BLUE);
		lasers=laser;
	}
	//desenarea proiectilelor
	void paint (Canvas canvas)
	{
		canvas.drawBitmap(lasers, null, bounds, mPaint);
		
	}
	//miscarea lor pe ecran
	void update (int x)
	{
			bounds.offset(0, x);
	}
	
	//distrugerea proiectilelor
	@Override
	void dispose() 
	{
		super.dispose();
	}
}
