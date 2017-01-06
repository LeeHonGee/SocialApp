package com.sharemob.tinchat.lib.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

class AssetsPath{
	private String oldPath;
	private String newPath;
	
	public AssetsPath(String oldPath,String newPath) {
		this.oldPath=oldPath;
		this.newPath=newPath;
	}
	public String getOldPath() {
		return oldPath;
	}
	public void setOldPath(String oldPath) {
		this.oldPath = oldPath;
	}
	public String getNewPath() {
		return newPath;
	}
	public void setNewPath(String newPath) {
		this.newPath = newPath;
	}
	
}

public class AssetsFileUtils{
	
	//正在搜索文件
	private final static int STATE_SEARCHING=1;
	private final static int STATE_SEARCHING_FINISHED=2;
	//正在复制文件
	private final static int STATE_COPYING=3;
	//操作结束
	private final static int STATE_FINISHED=4;
	
	public final static int Type_RES=1;
	public final static int Type_SRC=2;
	private int Type_Copy=1;
	//当前状态
	private int state=0;
	private List<AssetsPath> assetsPaths=new ArrayList<AssetsPath>();
	private Context context;
	private String res_path=null;
	private String src_path=null;

	public int getState(){
		return state;
	}
	public String getResPath() {
		return res_path;
	}
	public String getSrcPath() {
		return src_path;
	}
	public AssetsFileUtils(Context context,int type) {
		 this.context=context;
		 this.Type_Copy=type;
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
        InputStream is=null;
        FileOutputStream fos =null;
		try {
			File file=new File(newPath);
			if(file.getName().contains(".")){
				if(!file.exists()){
					file.createNewFile();
				}
				is = context.getAssets().open(oldPath);
			    //获取文件的字节数
		        int lenght = is.available();
		        //创建byte数组
		        fos = new FileOutputStream(new File(newPath));
		        byte[] buffer = new byte[1024*1024];
		        int byteCount=0;               
		        while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节        
		            fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
		        }
				fos.flush();
			}else{
				if(!file.exists())
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
		try {
			// 获取assets目录下的所有文件及目录名
			String fileNames[] = context.getAssets().list(oldPath);
			for (int i = 0; i < fileNames.length; i++) {
				String toPath = newPath + File.separator + fileNames[i];
				String resPath = oldPath + File.separator + fileNames[i];
				assetsPaths.add(new AssetsPath(resPath, toPath));
				if (!fileNames[i].contains(".")) {
					File file=new File(toPath);
					file.mkdirs();
					searchingFileSize(resPath, toPath);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}