/**
 *  文件名:DiscoverActivity.java
 *  修改人:lihangjie
 *  创建时间:2015-9-14 上午8:34:57
 */
package com.sharemob.tinchat.modules.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.base.BaseFragment;
import com.sharemob.tinchat.lib.Blur;
import com.sharemob.tinchat.lib.ImageUtils;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.modules.feeling.CardFragment;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2015-9-14 上午8:34:57]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public class FragmentFeeling extends BaseFragment {
    
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	     
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//        initImageLoader();
        if (savedInstanceState == null) {
        this.getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.feeling_container, new CardFragment())
                    .commitAllowingStateLoss();
        }
        LocalUtils.applyLocalFont(getActivity().findViewById(R.id.layout_feeling_fragment));

	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_feeling_tab, container,false);
	}
	
	 @SuppressWarnings("deprecation")
	    private void initImageLoader() {
	        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
	                this.getActivity())
	                .memoryCacheExtraOptions(480, 800)
	                        // default = device screen dimensions
	                .threadPoolSize(3)
	                        // default
	                .threadPriority(Thread.NORM_PRIORITY - 1)
	                        // default
	                .tasksProcessingOrder(QueueProcessingType.FIFO)
	                        // default
	                .denyCacheImageMultipleSizesInMemory()
	                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
	                .memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13) // default
	                .discCacheSize(50 * 1024 * 1024) // 缓冲大小
	                .discCacheFileCount(100) // 缓冲文件数目
	                .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
	                .imageDownloader(new BaseImageDownloader(this.getActivity())) // default
	                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
	                .writeDebugLogs().build();

	        // 2.单例ImageLoader类的初始化
	        ImageLoader imageLoader = ImageLoader.getInstance();
	        imageLoader.init(config);
	    }
}
