package com.rau.droidinvaders;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.view.Display;

public class CButton {
	Rect rect;
	String text;
	Paint mPaint;
	Paint painttext;
	Display display;
	int screenheight;
	Bitmap buttonB;
	
	CButton(Rect _rect, String _text, Align _align,Context context, Bitmap buttonBack)
	{
		display = ((Activity) context).getWindowManager().getDefaultDisplay();
		screenheight = display.getHeight();
		AssetManager mngr = context.getAssets();
		rect=_rect;
		text=_text;
		mPaint=new Paint();
		Typeface tf = Typeface.createFromAsset(mngr, "fonts/roboto.ttf");
		painttext=new Paint();
		painttext.setTypeface(tf);
		painttext.setTextSize(screenheight/32);
		painttext.setTextAlign(_align);
		painttext.setColor(Color.GRAY);
		mPaint.setColor(Color.GRAY);
		buttonB=buttonBack;
		
	}
	//desenarea butonului si textului
	public void paint(Canvas canvas)
	{
		canvas.drawBitmap(buttonB, null, rect, mPaint);
		if(text!=null)
		canvas.drawText(text, rect.centerX(), rect.centerY()+rect.height()/9, painttext);
	}
	public int width() {
		// TODO Auto-generated method stub
		return rect.width();
	}
	public int height() {
		// TODO Auto-generated method stub
		return rect.height();
	}
	
}
