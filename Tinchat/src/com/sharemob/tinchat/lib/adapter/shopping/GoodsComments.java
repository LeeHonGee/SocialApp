package com.sharemob.tinchat.lib.adapter.shopping;

import java.io.Serializable;
import java.util.ArrayList;

final class GoodsComments implements Serializable {
	private static final long serialVersionUID = 0x1L;
	ArrayList<CharSequence> urls=new ArrayList<CharSequence>();
    String attach_photos;
    String content;
    String nickname;
    int grade;
    int id;
    long time;
}