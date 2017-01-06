package com.sharemob.tinchat.lib.photoview;

import java.util.ArrayList;


public class MyMessage {

	public int code;
	public String msg;
	
	public ArrayList<MyPersonal> list;

	@Override
	public String toString() {
		return "MyMessage [code=" + code + ", msg=" + msg + ", list=" + list + "]";
	}

	
	
}
