package com.sharemob.tinchat.modules.feeling;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.ImageUtils;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.common.GlobalParams;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;

public class CardItemView extends LinearLayout {

	public  ImageView avatar,avatar_01,avatar_02,avatar_03,avatar_04;
    private ImageView btn_signature_voice;
    private TextView tv_feeling_location;
    private TextView tv_feeling_signature;
    private TextView tv_feeling_time;
    private TextView tv_feeling_sex_age;
    private TextView nickname;
    private TextView imageNumTv;
    private TextView likeNumTv;
    private Context context;
    
    public CardItemView(Context context) {
        this(context, null);
        this.context=context;
    }

    public CardItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context=context;
    }

    public CardItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.card_slide_item, this);
        View view=this;
        avatar = (ImageView) view.findViewById(R.id.card_image_view);
        avatar_01=(ImageView) view.findViewById(R.id.avatar_01);
        avatar_02=(ImageView) view.findViewById(R.id.avatar_02);
        avatar_03=(ImageView) view.findViewById(R.id.avatar_03);
        avatar_04=(ImageView) view.findViewById(R.id.avatar_04);
        tv_feeling_signature=(TextView) LocalUtils.getFindViewById(view,R.id.tv_feeling_signature);
        tv_feeling_location=(TextView)LocalUtils.getFindViewById(view,R.id.tv_feeling_location);
        tv_feeling_time=(TextView) LocalUtils.getFindViewById(view,R.id.tv_reg_time);
        tv_feeling_sex_age=(TextView) LocalUtils.getFindViewById(view,R.id.tv_feeling_sex_age);
        nickname = (TextView) LocalUtils.getFindViewById(view,R.id.card_nickname);
        imageNumTv = (TextView)LocalUtils.getFindViewById(view,R.id.card_pic_num);
        likeNumTv = (TextView) LocalUtils.getFindViewById(view,R.id.card_like);
        btn_signature_voice=(ImageView)view.findViewById(R.id.btn_signature_voice);
        btn_signature_voice.setBackgroundResource(R.anim.play_voice_animation);
    }

    
    public void fillData(final FeelingEntity entity) {
		imageNumTv.setText(String.valueOf(entity.count));
		final String avatar_url=String.format("%s%s", CacheManager.getInstance().userInfo.server_addr,entity.avatar);
		final ArrayList<CharSequence> photobooks=entity.photobook;
//		photobooks.clear();
		ImageView [] avatarIDS=new ImageView[]{avatar,avatar_01,avatar_02,avatar_03,avatar_04};
		photobooks.add(0, avatar_url);
		for(int i=0;i<photobooks.size()&&i<5;i++){
			ImageView tempIV=avatarIDS[i];
			final int index=i;
			tempIV.setScaleType(ImageView.ScaleType.CENTER_CROP);
			ImageLoader.getInstance().displayImage(photobooks.get(i).toString(),tempIV, GlobalParams.getInstance().round_options);
			tempIV.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					ImageUtils.imageBrower(context,index,photobooks);
				}
			});
//			avatar.setOnClickListener(new OnClickListener() {
//				public void onClick(View arg0) {
//					ImageUtils.imageBrower(context,index,photobooks);
//				}
//			});
		}
		
		
		btn_signature_voice.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if("".equals(entity.voice)){
					btn_signature_voice.setEnabled(false);
					Toast.makeText(context, "暂时找不到语音介绍", Toast.LENGTH_SHORT).show();
				}else{
					String voice_url=String.format("%s%s", CacheManager.getInstance().userInfo.server_addr.replace("img", "voice"),entity.voice);
					System.out.println("voice:"+voice_url);
					LocalUtils.AsyncLoadingVoice(context, btn_signature_voice, voice_url);
				}
			}
		});
        
		nickname.setText(entity.nickname);
        tv_feeling_time.setText( String.format("注册时间:%s", entity.time));
        if("".equals(entity.signature)){
        	tv_feeling_signature.setText("内心独白:该用户真懒,什么都没写.");
        }else{
        	tv_feeling_signature.setText(String.format("内心独白:%s",entity.signature));
        }
        if("".equals(entity.location)){
        	tv_feeling_location.setText("暂时不公开当前位置");
        }else{
        	tv_feeling_location.setText(entity.location);
        }
        
        
        tv_feeling_sex_age.setText(entity.age);
    }
}
