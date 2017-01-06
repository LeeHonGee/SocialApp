/**
 *  文件名:WXEntryActivity.java
 *  修改人:lihangjie
 *  创建时间:2015-9-10 下午2:46:26
 */
package com.sharemob.tinchat.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.sharemob.tinchat.thirdparty.weixin.WXConstant;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2015-9-10 下午2:46:26]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WXConstant.api.handleIntent(getIntent(), this);
	}
	
	 protected void onNewIntent(Intent intent) {
         super.onNewIntent(intent);
         setIntent(intent);
         WXConstant.api.handleIntent(intent, this);
     }
	public void onReq(BaseReq arg0) {
		  Toast.makeText(this, "请先安装微信应用", Toast.LENGTH_SHORT).show();
	}

//	public void setWX_share_state(int state){
//	
//		org.cocos2dx.lib.Cocos2dxAPIBridge.getInstance().setWX_SHARE_STATE(state);
//	}
	
	@Override
	public void onResp(BaseResp resp){
			switch (resp.errCode) {
				case BaseResp.ErrCode.ERR_OK: {
					//"分享成功";
//					setWX_share_state(WXSHARE_STATE.OK);
				}
				break;
				case BaseResp.ErrCode.ERR_USER_CANCEL:
					//"分享取消";
//					setWX_share_state(WXSHARE_STATE.CANCEL);
					break;
				case BaseResp.ErrCode.ERR_AUTH_DENIED:
					//"分享被拒绝";
//					setWX_share_state(WXSHARE_STATE.DENIED);
					break;
				default:
					//"分享返回";
//					setWX_share_state(WXSHARE_STATE.BACK);
					break;
			}
			this.finish();
	}

}
