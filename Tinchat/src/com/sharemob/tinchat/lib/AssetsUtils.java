package com.sharemob.tinchat.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;

public class AssetsUtils {  
	
	/** 
	 *  从assets目录中复制整个文件夹内容 
	 *  @param  context  Context 使用CopyFiles类的Activity
	 *  @param  oldPath  String  原文件路径  如：/aa 
	 *  @param  newPath  String  复制后路径  如：xx:/bb/cc 
	 */ 
	public static void copyFilesFassets(Context context,String oldPath,String newPath) {                    
	         try {
	        String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
	        if (fileNames.length > 0) {//如果是目录
	            File file = new File(newPath);
	            file.mkdirs();//如果文件夹不存在，则递归
	            for (String fileName : fileNames) {
	               copyFilesFassets(context,oldPath + File.separator + fileName,newPath+File.separator+fileName);
	            }
	        } else {
	        	//如果是文件
	            InputStream is = context.getAssets().open(oldPath);
	          //获取文件的字节数
	            int lenght = is.available();
	            //创建byte数组
	            FileOutputStream fos = new FileOutputStream(new File(newPath));
	            byte[] buffer = new byte[lenght];
	            int byteCount=0;               
	            while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节        
	                fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
	            }
	            fos.flush();//刷新缓冲区
	            is.close();
	            fos.close();
	        }
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	        //如果捕捉到错误则通知UI线程
	    }                           
	}
	
    /** 
     * 解压Assets中的文件 
     * @param context上下文对象 
     * @param assetName压缩包文件名 
     * @param outputDirectory输出目录 
     * @throws IOException 
     */
    public static void unZip(Context context, String assetName,String outputDirectory) throws IOException {  
        //创建解压目标目录  
        File file = new File(outputDirectory);  
        //如果目标目录不存在，则创建  
        if (!file.exists()) {  
            file.mkdirs();  
        }  
        InputStream inputStream = null;  
        //打开压缩文件  
        inputStream = context.getAssets().open(assetName);  
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);  
        //读取一个进入点  
        ZipEntry zipEntry = zipInputStream.getNextEntry();  
        //使用1Mbuffer  
        byte[] buffer = new byte[1024 * 1024];  
        //解压时字节计数  
        int count = 0;  
        //如果进入点为空说明已经遍历完所有压缩包中文件和目录  
        while (zipEntry != null) {  
            //如果是一个目录  
            if (zipEntry.isDirectory()) {  
                //String name = zipEntry.getName();  
                //name = name.substring(0, name.length() - 1);  
                file = new File(outputDirectory + File.separator + zipEntry.getName());  
                file.mkdir();  
            } else {  
                //如果是文件  
                file = new File(outputDirectory + File.separator  
                        + zipEntry.getName());  
                //创建该文件  
                file.createNewFile();  
                FileOutputStream fileOutputStream = new FileOutputStream(file);  
                while ((count = zipInputStream.read(buffer)) > 0) {  
                    fileOutputStream.write(buffer, 0, count);  
                }  
                fileOutputStream.close();  
            }  
            //定位到下一个文件入口  
            zipEntry = zipInputStream.getNextEntry();  
        }  
        zipInputStream.close();  
    }  
}