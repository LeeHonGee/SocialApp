package com.sharemob.tinchat.lib.common;

import java.io.File;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.component.CircleBitmapDisplayer;
import com.sharemob.tinchat.lib.MyApplication;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;

public class GlobalParams {

	private static GlobalParams instance = null;
	private Context context = null;

	public static GlobalParams getInstance() {
		if (instance == null) {
			instance = new GlobalParams();
		}
		return instance;
	}

	private GlobalParams() {
	}
	public static final String URL_UploadMultipart="http://IP/upload".replace("IP", CacheManager.IP);
	public Typeface getLocalTypeface() {
		if (localFontTypeface == null) {
			this.init(context);
		}
		return this.localFontTypeface;
	}

	public void init(Context context) {
		this.context = context;
		this.localFontTypeface = Typeface.createFromAsset(MyApplication
				.getContext().getAssets(), "fonts/font_gothic_china.TTF");
		String voiceDirectory=String.format("%s/%s/%s",Environment.getExternalStorageDirectory(), "TinChat","voice");
		String videoDirectory=String.format("%s/%s/%s",Environment.getExternalStorageDirectory(), "TinChat","video");
		File voiceFile=new File (voiceDirectory);
		if(!voiceFile.exists()){
			voiceFile.mkdir();
		}
		File videoFile=new File (videoDirectory);
		if(!videoFile.exists()){
			videoFile.mkdir();
		}
	}

	public Typeface localFontTypeface = null;

	// public static final String IP_PORT = "172.16.4.37:8443";
	// public static final String POST_REGISTER_ADDR =
	// "https://ip:port/android/service".replace("ip:port", IP_PORT);
	public DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
			.bitmapConfig(Config.RGB_565)
//			.cacheInMemory(false)
			.resetViewBeforeLoading(true)
//			.showImageForEmptyUri(R.drawable.empty_photo)
			.showImageOnFail(R.drawable.empty_photo)
			.displayer(new FadeInBitmapDisplayer(1500))
			.delayBeforeLoading(500)
			.imageScaleType(ImageScaleType.EXACTLY)
			.cacheOnDisc(true)
			.build();

	public DisplayImageOptions round_options = new DisplayImageOptions.Builder()
//			.showImageOnLoading(R.drawable.avatar_normal)
//			.showImageOnFail(R.drawable.avatar_normal)
			.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
			.cacheInMemory(false) // 内存缓存
			.cacheOnDisc(true)
			.displayer(new RoundedBitmapDisplayer(10))
			.bitmapConfig(Config.RGB_565)
			.build();

	public DisplayImageOptions circle_options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.public_default_head)
			.showImageOnFail(R.drawable.public_default_head)
			.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
			.cacheInMemory(false)
			.cacheOnDisc(true)
			.displayer(new CircleBitmapDisplayer(5))
			.bitmapConfig(Config.RGB_565)
			.build();

	public void ImageLoaderConfiguration() {
		File cacheDir = StorageUtils.getOwnCacheDirectory(
				MyApplication.context, "Tinchat/Cache");

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				MyApplication.context)
				.defaultDisplayImageOptions(defaultOptions)
				.memoryCacheExtraOptions(480, 800)
				// Max Width, Max Height，即保存的每个缓存文件的最大长宽
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.threadPoolSize(ImageLoaderConfiguration.Builder.DEFAULT_THREAD_POOL_SIZE)
				.denyCacheImageMultipleSizesInMemory()
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// .discCache(new UnlimitedDiscCache(cacheDir))
//				.discCache(new TotalSizeLimitedDiscCache(cacheDir,200 * 1024 * 1024))
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				// .memoryCache(new UsingFreqLimitedMemoryCache(2000000))
				// .discCacheFileNameGenerator(new Md5FileNameGenerator())
				.discCacheSize(200 * 1024 * 1024) // 20M
				.discCacheFileCount(100)
//				.writeDebugLogs()
				.build();
		ImageLoader.getInstance().init(config);

		GlobalParams.getInstance().init(this.context);
	}
}
