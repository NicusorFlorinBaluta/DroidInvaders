package com.rau.droidinvaders;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class CEnemy extends CEntity {

	int tempbounds;
	long fireRate;
	long lastUpdateTime = 0;
	
	ArrayList<Point> mPath;
	ArrayList<Integer> mPathTime, mPathInitTime;
	int mPathCurrentPos;
	int mPathCurrentTime;
	int mPathUpdate;
	int EnemyHitPoints;
	Bitmap enemyBm;
	boolean mPathLoop;
	boolean noNewPoints=true;
	
	Paint mPaint;
	CEnemy (Rect bounds, Bitmap enemyBitmap, int EnemyHP)
	{
		super (bounds);
		mPath = new ArrayList<Point>();
		mPathTime = new ArrayList<Integer>();
		mPathInitTime = new ArrayList<Integer>();
		mPathUpdate = 50;
		mPathCurrentPos = 0;
		mPathCurrentTime = 0;
		mPathLoop = false;
		enemyBm=enemyBitmap;
		EnemyHitPoints=EnemyHP;
		
		mPaint= new Paint();
		mPaint.setColor(Color.RED);
	}
	//resetarea pathului
	void clearPath ()
	{
		mPath.clear();
		mPathTime.clear();
		mPathInitTime.clear();
		mPathCurrentPos = 0;
	}
	//adaugarea punctelor de miscare al navei in vector
	void setPath (ArrayList<Point> path, ArrayList<Integer> pathTimes,  boolean loop)
	{
		clearPath();
		mPath.addAll(path);
		mPathTime.addAll(pathTimes);
		mPathInitTime.addAll(pathTimes);
		mPathLoop = loop;
	}
	//adaugarea unui singur punct de miscare al navei in vector
	void addPathPoint (Point point, int dt)
	{
		mPath.add(point);
		mPathTime.add(dt);
		mPathInitTime.add(dt);
	}
	
	@Deprecated
	void setPathUpdate(int dt)
	{
		mPathUpdate = dt;
	}
	//desenarea navei 
	void paint (Canvas canvas)
	{
		canvas.drawBitmap(enemyBm,null, bounds, mPaint);
	}
	//updatarea pozitiei navei 	
	void update (long dt)
	{
		if (mPathCurrentPos < mPath.size())
		{
			
			int offsetX, offsetY;
			
			if (dt >= mPathTime.get(mPathCurrentPos).intValue())
			{
				offsetX = (mPath.get(mPathCurrentPos).x - bounds.left);
				offsetY = (mPath.get(mPathCurrentPos).y - bounds.top);
				mPathTime.set(mPathCurrentPos, 0);
			}
			else
			{
				offsetX = (mPath.get(mPathCurrentPos).x - bounds.left) * (int)dt / mPathTime.get(mPathCurrentPos).intValue();
				offsetY = (mPath.get(mPathCurrentPos).y - bounds.top) * (int)dt / mPathTime.get(mPathCurrentPos).intValue();
				mPathTime.set(mPathCurrentPos, mPathTime.get(mPathCurrentPos).intValue() - (int)dt);
			}
			
			bounds.offset(offsetX, offsetY);
				
			if (mPathTime.get(mPathCurrentPos).intValue() <= 0)
			{
				mPathCurrentPos++;
			}
		}
		else
		{
			if (mPathLoop)
			{
				mPathCurrentPos = 0;
				int idx = 0;
				for (Integer i: mPathInitTime)
				{
					mPathTime.set(idx++, i);
				}
			}
		}
	}
	
	//verfica daca a ajuns la sfarsitul pathului
	public boolean finishedPath ()
	{
		if (mPathLoop)
			return false;
		
		if (mPathCurrentPos >= mPath.size())
			return true;
		else
			return false;
	}
	//seteaza cat de des trage nava
	public void setFireRate (long fireRate)
	{
		this.fireRate = fireRate;
	}
	//returneaza frecventa de trage a navei
	public long getFireRate()
	{
		return fireRate;
	}
	
	@Override
	void dispose() 
	{
		super.dispose();
	}
}
