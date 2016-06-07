package com.rau.droidinvaders;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

public class CEntity 
{
	Rect bounds;
	
	CEntity ()
	{
		this.bounds = new Rect();
	}
	
	CEntity (Rect bounds)
	{
		this.bounds = new Rect(bounds);
	}
	
	void paint (Canvas canvas)
	{
		
	}
	
	void update (long dt)
	{
		
	}

	public void onTouch(MotionEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	void dispose()
	{
		
	}
}
