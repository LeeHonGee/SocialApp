package com.sharemob.tinchat.base;

import java.util.ArrayList;
import java.util.List;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

public abstract class BaseActivityGroup extends FragmentActivity {

	public List<Bitmap> bmp = new ArrayList<Bitmap>();
	public List<String> drr = new ArrayList<String>();
	
	 private List<Fragment> fragments=new ArrayList<Fragment>();
	 public FragmentManager fragmentManager;  
	 public FragmentTransaction fragmentTransaction;
	 public void addFragmentManager(Fragment object){
		 fragments.add(object);
	 }
	 
	 public void hideAllFragment(){
		 fragmentTransaction = fragmentManager.beginTransaction();
		 for(Fragment fragment:fragments){
			 fragmentTransaction.hide(fragment);
		 }
	 }
	 
	 public void showFragment(int index){
		 hideAllFragment();
		 fragmentTransaction.show(fragments.get(index));
		 fragmentTransaction.commit();
	 }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        
    }
    
}
