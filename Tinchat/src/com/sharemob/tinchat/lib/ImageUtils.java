/**
 *  文件名:ImageUtils.java
 *  修改人:lihangjie
 *  创建时间:2015-10-13 下午2:46:36
 */
package com.sharemob.tinchat.lib;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.photoview.ImagePagerActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.widget.ImageView;

/**
 * 
 * <一句话功能简述>
 * 
 * @author lihangjie version [版本号,2015-10-13 下午2:46:36]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 * 
 */
public final class ImageUtils {

	public static Drawable BitmapToDrawable(Bitmap bmp) {
		Drawable drawable = new BitmapDrawable(bmp);
		return drawable;
	}

	public static Bitmap getBitMapFromRescourceID(Context context, int id) {
		Bitmap bitmap = BitmapFactory
				.decodeResource(context.getResources(), id);
		return bitmap;
	}

	public static Bitmap getBitmap(InputStream is) {
		Bitmap bitmap = BitmapFactory.decodeStream(is);
		return bitmap;
	}

	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 75, baos);
		return baos.toByteArray();
	}

	public static Bitmap Bytes2Bimap(byte[] data) {
		if (data.length != 0) {
			return BitmapFactory.decodeByteArray(data, 0, data.length);
		} else {
			return null;
		}
	}

	/**
	 * 将图片按照某个角度进行旋转
	 * 
	 * @param bm
	 *            需要旋转的图片
	 * @param degree
	 *            旋转角度
	 * @return 旋转后的图片
	 */
	public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
		Bitmap returnBm = null;

		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		try {
			// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
			returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
					bm.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {
		}
		if (returnBm == null) {
			returnBm = bm;
		}
		if (bm != returnBm) {
			bm.recycle();
		}
		return returnBm;
	}

	// 把bitmap转换成String
	public static String bitmapToString(String filePath) {
		Bitmap bm = compressImageFromFile(filePath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		byte[] b = baos.toByteArray();
		return Base64.encodeToString(b, Base64.DEFAULT);
	}

	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			options -= 10;// 每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		
		return bitmap;
	}
	
	public static InputStream compressBitmap(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int quality = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			quality -= 10;// 每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, quality, baos);// 这里压缩options%，把压缩后的数据存放到baos中
		}
		return new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
	}

	public static InputStream compressBitmapFromFile(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int width = newOpts.outWidth;
		int height = newOpts.outHeight;
		final float Height = 800.0f;//
		final float Width = 480.0f;//
		int be = 1;
		if (width > height && width > Width) {
			be = (int) (newOpts.outWidth / Width);
		} else if (width < height && height > Height) {
			be = (int) (newOpts.outHeight / Height);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置采样率

		newOpts.inPreferredConfig = Config.RGB_565;// 该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		 return compressBitmap(bitmap);//原来的方法调用了这个方法企图进行二次压缩
		// 其实是无效的,大家尽管尝试
//		return bitmap;
	}
	public static Bitmap compressImageFromFile(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 800f;//
		float ww = 480f;//
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置采样率

		newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		 return compressImage(bitmap);//原来的方法调用了这个方法企图进行二次压缩
		// 其实是无效的,大家尽管尝试
//		return bitmap;
	}
	public void saveBitmapFile(String path,Bitmap bitmap){
        File file=new File(path);//将要保存图片的路径
        try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
        } catch (IOException e) {
                e.printStackTrace();
        }
}

	public static int reckonThumbnail(int oldWidth, int oldHeight,
			int newWidth, int newHeight) {
		if ((oldHeight > newHeight && oldWidth > newWidth)
				|| (oldHeight <= newHeight && oldWidth > newWidth)) {
			int be = (int) (oldWidth / (float) newWidth);
			if (be <= 1)
				be = 1;
			return be;
		} else if (oldHeight > newHeight && oldWidth <= newWidth) {
			int be = (int) (oldHeight / (float) newHeight);
			if (be <= 1)
				be = 1;
			return be;
		}
		return 1;
	}

	public static Bitmap PicZoom(Bitmap bmp, int width, int height) {
		int bmpWidth = bmp.getWidth();
		int bmpHeght = bmp.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale((float) width / bmpWidth, (float) height / bmpHeght);

		return Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeght, matrix, true);
	}

	public static void ImageByteSaveFile(byte[] data, String filePath) {

		File tempFile = new File(filePath);
		FileOutputStream outSteam = null;
		if (tempFile.exists()) {
			tempFile.delete();
		}
		try {
			tempFile.createNewFile();
			outSteam = new FileOutputStream(tempFile);
			outSteam.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outSteam.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void loaderImageForCache(String url, ImageView imageView,
			boolean display_empty) {
		DisplayImageOptions options = null;
		if (display_empty) {
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.empty_photo) // 加载中显示的默认图片
					.showImageOnFail(R.drawable.empty_photo) // 设置加载失败的默认图片
					.cacheOnDisc(true) // sdcard缓存
					.bitmapConfig(Config.RGB_565)// 设置最低配置
					.build();
		} else {
			options = new DisplayImageOptions.Builder().cacheOnDisc(true)
					.bitmapConfig(Config.RGB_565).build();
		}
		ImageLoader.getInstance().displayImage(url, imageView, options);
	}

	/**
	 * 打开图片查看器
	 * 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
	 * @param position
	 * @param urls2
	 */
	public static void imageBrower(Context mContext, int position, ArrayList<CharSequence> urls) {
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		intent.putCharSequenceArrayListExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		mContext.startActivity(intent);
	}

	public static String[] toArray(List<String> list) {
		int size = list.size();
		String[] args = new String[list.size()];
		for (int i = 0; i < size; i++) {
			args[i] = list.get(i);
		}
		return args;
	}

	/**
	 * 从内容提提供者中抓取图片
	 */
	public static void displayFromContent(String uri, ImageView imageView) {
		// String imageUri = "content://media/external/audio/albumart/13"; //
		// from content provider
		ImageLoader.getInstance().displayImage("content://" + uri, imageView);
	}

	/**
	 * 从内存卡中异步加载本地图片
	 * 
	 * @param uri
	 * @param imageView
	 */
	public static void displayFromSDCard(String uri, ImageView imageView) {
		// String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
		ImageLoader.getInstance().displayImage("file://" + uri, imageView);
	}

	/**
	 * 从assets文件夹中异步加载图片
	 * 
	 * @param imageName
	 *            图片名称，带后缀的，例如：1.png
	 * @param imageView
	 */
	public static void dispalyFromAssets(String imageName, ImageView imageView) {
		// String imageUri = "assets://image.png"; // from assets
//		System.out.println("assets://" + imageName);
		ImageLoader.getInstance().displayImage("assets://" + imageName,imageView);
	}

	/**
	 * 从drawable中异步加载本地图片
	 * 
	 * @param imageId
	 * @param imageView
	 */
	public static void displayFromDrawable(int imageId, ImageView imageView) {
		// String imageUri = "drawable://" + R.drawable.image; // from drawables
		// (only images, non-9patch)
		ImageLoader.getInstance().displayImage("drawable://" + imageId,imageView);
	}
}
