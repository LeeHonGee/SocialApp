package com.sharemob.tinchat.component;

import com.sharemob.tinchat.lib.common.ReleaseImageLoadingListener;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public abstract class LazyFragment extends Fragment {
	private boolean isPrepared;
    protected boolean isVisible;
    public Context mContext;
    protected View mRootView;
    public abstract void initData();
    private boolean isFirst = true;
    
    public LazyFragment() {}
    
	public ReleaseImageLoadingListener loadingListener=new ReleaseImageLoadingListener(){
		 public void onLoadingComplete(String imageUri, android.view.View view, android.graphics.Bitmap loadedImage) {
			 ImageView imageView=(ImageView)view;
			 imageView.setScaleType(ScaleType.CENTER_CROP);
			 imageView.setImageBitmap(loadedImage);   
         };
	};
    
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            lazyLoad();
            return;
        }
        isVisible = false;
        onInvisible();
    }
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
    }
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mRootView == null) {
            mRootView = initView(inflater, container);
        }
        return mRootView;
    }
    
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        lazyLoad();
    }
    
    protected void lazyLoad() {
        if((!isPrepared) || (!isVisible) || (!isFirst)) {
            return;
        }
        Log.d("TAG", "->initData()");
        initData();
        isFirst = false;
    }
    
    protected void onInvisible(){};
    protected abstract View initView(LayoutInflater inflater, ViewGroup container);
}