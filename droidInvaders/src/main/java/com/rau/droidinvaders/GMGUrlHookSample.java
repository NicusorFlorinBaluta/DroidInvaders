package com.rau.droidinvaders;

import android.net.Uri;

public class GMGUrlHookSample implements GMGUrlHook
{
	public GMGUrlHookSample() {
		
	}
	
	public boolean overrideURL(String url) {
		Uri uri = Uri.parse(url);
		if(uri.getScheme().compareTo("tapjoy")==0){
			//play tapjoy activity
			return true;
		}
		//rest behaves as usual
		return false;
	}
}