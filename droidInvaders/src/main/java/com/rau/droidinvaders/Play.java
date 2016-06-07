package com.rau.droidinvaders;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class Play extends Thread{

	private SurfaceHolder surfaceHolder;
	private MainGamePanel gamePanel;
	private boolean mPaused;
    private boolean mFinished;
	boolean running;
	
	public void setRunning(boolean running) 
	{
		this.running = running;
	}
	
	public Play(SurfaceHolder surfaceHolder, MainGamePanel gamePanel) 
	{
		super();
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;
	}
	
	long lastUpdateTime;
	
	@Override
	public void run() 
	{
		Canvas canvas;
		mPaused=false;
		lastUpdateTime = System.currentTimeMillis();
		while (running) 
		{
			if(mPaused==false)
			{
				canvas = null;

				mFinished = false;
				
				long dt = System.currentTimeMillis() - lastUpdateTime;
				lastUpdateTime = System.currentTimeMillis();
				
				// try locking the canvas for exclusive pixel editing
				// in the surface
				try 
				{
					canvas = this.surfaceHolder.lockCanvas();

					this.gamePanel.update(dt);
					// render state to the screen
					// draws the canvas on the panel
					this.gamePanel.render(canvas);			


				} 
				catch (Exception e)
				{
					e.printStackTrace();
				}
				finally 
				{
					// in case of an exception the surface is not left in 
					// an inconsistent state
					if (canvas != null) 
					{
						surfaceHolder.unlockCanvasAndPost(canvas);
					}

				}	// end finally

			}
			else 
			{
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
		
	public void stopThread()
	{
		mPaused=true;
		//running = false;
	}
	
	public void resumeThread()
	{
		mPaused=false;
		//running = true;
	}
	
}
	
	
	
	
	


