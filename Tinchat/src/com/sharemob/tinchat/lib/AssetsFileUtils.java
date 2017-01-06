package com.sharemob.tinchat.lib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import android.content.Context;

public class AssetsFileUtils{
	
	//正在搜索文件
	private final static int STATE_SEARCHING=1;
	//正在复制文件
	private final static int STATE_COPYING=2;
	//操作结束
	private final static int STATE_FINISHED=3;
	//当前状态
	private int state=0;
	private Map<String,String> filePaths=new HashMap<String,String>();
	private Context context;
	private String res_path=null;
	private String src_path=null;
	private int folderSize=0;

	public int getFileSize(){
		return filePaths.size();
	}
	public int getState(){
		return state;
	}
	public String getResPath() {
		return res_path;
	}
	public String getSrcPath() {
		return src_path;
	}
	public AssetsFileUtils(Context context) {
		 this.context=context;
	     res_path=String.format("%s%s%s", this.context.getFilesDir(),File.separator,"res");
	     src_path=String.format("%s%s%s", this.context.getFilesDir(),File.separator,"src");
	     File res_file=new File(res_path);
	     if(!res_file.exists()){
	    	 res_file.mkdirs();
	     }
	     File src_file=new File(src_path);
	     if(!src_file.exists()){
	    	 src_file.mkdirs();
	     }
	}
	protected void CopyingFileData(String oldPath,String newPath){
 	   	//如果是文件
		this.state=STATE_COPYING;
//		System.out.println(folderSize+"--"+getStateDescription()+"--->"+newPath);
        InputStream is=null;
        FileOutputStream fos =null;
		try {
			File file=new File(newPath);
			if(file.getName().contains(".")){
				file.createNewFile();
				is = context.getAssets().open(oldPath);
			    //获取文件的字节数
		        int lenght = is.available();
		        //创建byte数组
		        fos = new FileOutputStream(new File(newPath));
		        byte[] buffer = new byte[lenght];
		        int byteCount=0;               
		        while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节        
		            fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
		        }
				fos.flush();
			}else{
				file.mkdirs();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			 try {
				if(is!=null)
			    is.close();
			    if(fos!=null)
			    fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void searchingFileSize(final String oldPath, final String newPath) {
		this.state=STATE_SEARCHING;
		try {
			// 获取assets目录下的所有文件及目录名
			String fileNames[] = context.getAssets().list(oldPath);
			for(String filename:fileNames){
				String toPath = newPath + File.separator + filename;
				String resPath = oldPath + File.separator + filename;
				filePaths.put(resPath, toPath);
				this.folderSize=filePaths.size();
//				System.out.println("--"+getStateDescription()+"--->" + resPath);
				if (!filename.contains(".")) {
					File file=new File(toPath);
					file.mkdirs();
					searchingFileSize(resPath, toPath);
					file=null;
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getStateDescription(){
		int key=state;
		switch (key) {
		
		case STATE_SEARCHING:
				return "searching";
		case STATE_COPYING:
			return "copying";
		case STATE_FINISHED:
			return "finished";
		default:
			break;
		}
		return "readying";
	}
	
	public void CopyingAssetsFile(){
		Thread thread=new Thread(new Runnable() {
			public void run() {
				for (String key : filePaths.keySet()) { 
					String newPath=filePaths.get(key);
					CopyingFileData(key, newPath);
					folderSize--;
					if(folderSize==0){
						state=STATE_FINISHED;
					}
				}
//				System.out.println("--->"+getStateDescription());
			}
		});
		thread.start();
	
	}
}