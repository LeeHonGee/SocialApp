package com.sharemob.tinchat.modules.feeling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.component.AppActivity;
import com.sharemob.tinchat.lib.LocalUtils;
import com.sharemob.tinchat.lib.MyApplication;
import com.sharemob.tinchat.lib.catloading.CatLoadingView;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest.RequestCallback;
import com.sharemob.tinchat.modules.dao.cache.CacheManager;
import com.sharemob.tinchat.modules.feeling.CardSlidePanel.CardSwitchListener;


/**
 * 卡片Fragment
 *
 * @author xmuSistone
 */
public class CardFragment extends Fragment {

	private static final String TAG="CardFragment";
    private CardSwitchListener cardSwitchListener;
//    private static final byte BORING=0;
//    private static final byte LOVING=1;
    private static final byte LoadingDismiss=1;
    private static final String URL_GET_Feeling="http://"+CacheManager.IP+"/api/user?cmd=10011";
    private static final String URL_Send_Feeling="http://"+CacheManager.IP+"/api/user?cmd=10046";
    private  View rootView,centerBtn;
    private TextView card_title;
    private TextView card_feeling_count;
    private int love_num=0;
    private int love_order=0;
    private int love_total=0;
    private  CardSlidePanel slidePanel =null;
    private CatLoadingView loadingView;
    private List<FeelingEntity> list = new LinkedList<FeelingEntity>();
    
    private Handler handler=new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		switch (msg.what) {
			case LoadingDismiss:
					if(loadingView!=null){
						loadingView.dismiss();
					}
				break;
			default:
				break;
			}
    	};
    };
    
    private void loadData(){
    	loadingView=new CatLoadingView(getActivity());
    	loadingView.show();
    	 Map<String,Object> params=new HashMap<String, Object>();
         params.put("uid", CacheManager.getInstance().userInfo.uid);
         LocalUtils.requestHttp(10011, params, new RequestCallback() {
 			@Override
 			public void requestDidFinished(String body) {
 				ConverterEntity(body);
 			}
 			@Override
 			public void requestDidFailed() {
 				
 			}
 		});
    }
    
    public void ConverterEntity(String json){
    	try {
    		if("{}".equals(json)){
    			return;
    		}
				list.clear();
				JSONArray array=new JSONArray(json);
				for(int i=0;i<array.length();i++){
					FeelingEntity entity=new FeelingEntity();
					JSONObject object=array.getJSONObject(i);
					entity.uid=object.getString("uid");
					entity.time=object.getString("time");
					entity.signature=object.getString("signature");
					entity.voice=object.getString("voice");
					entity.birthday=object.getString("birthday");
					entity.location=object.getString("location");
					entity.nickname=object.getString("nickname");
					entity.age=LocalUtils.calculateDatePoor(entity.birthday);
					entity.avatar=object.getString("avatar");
					if(object.has("count")){
						entity.count=object.getInt("count");
					}
					if(object.has("photobook")){
						JSONArray photobooks=object.getJSONArray("photobook");
						for(int k=0;k<photobooks.length();k++){
							String photobook_url=String.format("%s%s", CacheManager.getInstance().userInfo.server_addr,photobooks.getString(k));
							entity.photobook.add(photobook_url);
						}
					}
					list.add(entity);
			}
			love_total=list.size();
			initView(rootView);
			setCardTitleLove();
//	    	if(loadingView!=null){
//	    		handler.sendEmptyMessageDelayed(LoadingDismiss,3000);
//	    	}
	    	loadingView.dismiss();
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	loadData();
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	View bottomLayout=rootView.findViewById(R.id.card_bottom_layout);
    	slidePanel = (CardSlidePanel) rootView .findViewById(R.id.image_slide_panel);
        slidePanel.setBottomLayout(bottomLayout);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        rootView = LocalUtils.viewFromInflater(getActivity(), R.layout.card_slide_layout);
        card_title=(TextView) rootView.findViewById(R.id.card_title);
        card_feeling_count=(TextView) rootView.findViewById(R.id.card_feeling_count);
        centerBtn = rootView.findViewById(R.id.card_center_btn);
        MyApplication.getInstance().addActivity(this.getActivity());
    }
    
    public void LookUserSpace(final String uid){
    	centerBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("uid", uid);
				LocalUtils.enterAppActivity(getActivity(), map, "MyspaceActivity");
			}
		});
    }
    
    public void setCardTitleLove(){
    	card_title.setText(String.format("(%d/%d)心动", love_order,love_total));
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        return rootView;
    }
    
    private void initView(View rootView) {
        cardSwitchListener = new CardSwitchListener() {
            @Override
            public void onShow(int index) {
            	FeelingEntity entity=list.get(index);
            	LookUserSpace(entity.uid);
            	String nickname=entity.nickname;
            	Log.d("CardFragment", "正在显示-" + nickname);
            }
            @Override
            public void onCardVanish(int index, int type) {
            	FeelingEntity entity=list.get(index);
					love_order++;
					setCardTitleLove();
					switch (type) {
					case CardSlidePanel.VANISH_TYPE_LEFT:
						
						break;
					case CardSlidePanel.VANISH_TYPE_RIGHT:{
						love_num++;
						card_feeling_count.setText(String.valueOf(love_num));
//						doSendLoving();
					}
						break;
					default:
						break;
					}
	                Log.d("CardFragment", "正在消失-" + entity.nickname+ " 消失type=" + type);
	                
	                if(love_order==love_total){
	                	love_order=0;
	                	love_num=0;
	                	loadData();
	                }
            }
            @Override
            public void onItemClick(View cardView, int index) {
            	FeelingEntity entity=list.get(index);
            	Log.d("CardFragment", "卡片点击-" + entity.nickname);
            }
        };
        slidePanel.setCardSwitchListener(cardSwitchListener);
        slidePanel.fillData(list);
    }

    
    private void doSendLoving(){
    	String url=String.format("%s&meID=%s&otherID=%s", URL_Send_Feeling,"dc8a4e75f874779b3bb4ddca185d18c2","dc8a4e75f874779b3bb4ddca185d18c2");
		CacheManager.requestHttp(url,new RequestCallback() {
			public void requestDidFinished(String body) {
				System.out.println(body);
			}
			public void requestDidFailed() {}
		});
    }
}

final class FeelingEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	String uid;
	String nickname;
	String avatar;
	String signature;
	String voice;
	String time;
	String birthday;
	String age;
	String location;
	ArrayList<CharSequence> photobook=new ArrayList<CharSequence>();
	int sex;
	int count;
	
}
