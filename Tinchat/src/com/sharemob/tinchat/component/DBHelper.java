package com.sharemob.tinchat.component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.sharemob.tinchat.lib.common.SMGlobal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper {

		private static DBHelper instance=new DBHelper();
		public static final String DATABASE_NAME = "tinchat.sqlite";  
		public static final String ACCOUNT_TABLE = "account";
		public static final String DYNAMIC_TABLE = "dynamic";
		public static final String GIFT_TABLE = "gift";
		public static final String IMAGEURL_TABLE = "imageUrl";
		public static final String LINKMAN_TABLE = "linkman";
		public static final String MOODARTICLE_TABLE = "moodArticle";
		public static final String NOTICE_TABLE = "notice";
		public static final String SOCIAL_TABLE = "social";
		public static final String VISITOR_TABLE = "visitor";
		
		public static DBHelper getInstance(){
			if(instance==null){
				instance=new DBHelper();
			}
			return instance;
		}
		
		public enum FIRST{
			YES,NO
		}
		public enum UploadingLaunch{
			Never,Need
		}
		
	    private SQLiteDatabase db;
	    private static final String SQL_CREATE_ACCOUNT = "CREATE TABLE IF NOT EXISTS "
	            +ACCOUNT_TABLE+"(" +
        		"ID INTEGER PRIMARY KEY AUTOINCREMENT," +
        		"unionid VARCHAR(32)  NULL," +
          		"sex INTEGER  NULL," +
          		"nickname VARCHAR(32)  NULL," +
          		"channel INTEGER NULL,"+
          		"birthday date  NULL); "; 
	    

	    private DBHelper() {
	    	Context context=SMGlobal.getInstance().context;
	    	String DataBasePath=String.format("%s%s%s", DATABASE_NAME,File.separator,context.getFilesDir());
	    	this.db=context.openOrCreateDatabase(DataBasePath, Context.MODE_PRIVATE, null);
	    }

	    public SQLiteDatabase getDB(){
	    	return this.db;
	    }
	    
	    public void insertBySQL(ContentValues initialValues,String tableName){
	         StringBuffer attributeNames=new StringBuffer();
	         StringBuffer attributeValues=new StringBuffer(); 
	         Object[] objects=new Object[initialValues.size()];
	         int index=0;
	         int size=initialValues.size();
	        		
	         for(String key:initialValues.keySet()){
	        	 attributeNames.append(key);
	        	 attributeValues.append("?");
	        	 if(index<size-1){
	        		 attributeNames.append(",");
	        		 attributeValues.append(",");
	        	 }
	        	 objects[index]=initialValues.get(key);
	        	 index++;
	         }
	         String sql=String.format("INSERT INTO %s(%s) VALUES(%s)", tableName,attributeNames,attributeValues);
	         db.execSQL(sql,objects);
	     }
	   
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	    {
//	    	db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
	    }

	    
	    public List<ContentValues> query(String table, String[] columns,Object[] type,String selection,
	            String[] selectionArgs, String groupBy, String having,
	            String orderBy){
	    	Cursor cursor =db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
	    	List<ContentValues> list=new ArrayList<ContentValues>();
	    	while(cursor.moveToNext()){ 
	    		ContentValues object=new ContentValues();
	    		for(int i=0;i<columns.length;i++){
	    			int columnIndex=cursor.getColumnIndex(columns[i]);
	    			int FiledType=cursor.getType(columnIndex);
	    			String key=columns[i];
	    			if(FiledType==Cursor.FIELD_TYPE_STRING){
	    				String value=cursor.getString(columnIndex);
	    				object.put(key, value);
	    			}else if(FiledType==Cursor.FIELD_TYPE_FLOAT){
	    				float value=cursor.getFloat(columnIndex);
	    				object.put(key, value);
	    			}else if(FiledType==Cursor.FIELD_TYPE_INTEGER){
	    				int value=cursor.getInt(columnIndex);
	    				object.put(key, value);
	    			}else if(FiledType==Cursor.FIELD_TYPE_BLOB){
	    				byte[] value=cursor.getBlob(columnIndex);
	    				object.put(key, value);
	    			}else if(FiledType==Cursor.FIELD_TYPE_NULL){
	    			}
	    		}
	    		list.add(object);
	    	}
	    	return list;
	    }
	    
		public void update(ContentValues values,String tableName,String condition) {
			StringBuffer setValues=new StringBuffer(); 
		    int index=0;
		    int size=values.size();
			for(String key:values.keySet()){
				setValues.append(String.format("%s=%s",key,values.get(key)));
				if(index<size-1){
					setValues.append(",");
				}
				index++;
			}
			String sql=String.format("UPDATE %s SET %s WHERE %s", tableName,setValues.toString(),condition);
			db.execSQL(sql);
		}

		public boolean isExistAccount(String tableName,String condition) {				
			String sql=String.format("SELECT * FROM %s WHERE %s;", tableName,condition);
			Cursor cursor = db.rawQuery(sql, null);
			boolean flag = cursor.moveToNext();
			return flag ;
		}
	    
	    public void close(){  
	        db.close();  
	    }  
}
