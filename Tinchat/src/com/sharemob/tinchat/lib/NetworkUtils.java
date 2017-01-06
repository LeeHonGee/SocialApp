/**
 *  文件名:NetworkUtils.java
 *  修改人:lihangjie
 *  创建时间:2015-10-13 下午2:52:26
 */
package com.sharemob.tinchat.lib;

import java.lang.reflect.Method;

import com.sharemob.tinchat.lib.common.AsyncHttpRequest;
import com.sharemob.tinchat.lib.common.AsyncHttpRequest.RequestCallback;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2015-10-13 下午2:52:26]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public final class NetworkUtils {

	public static void send(String url,String data,boolean encryption,final String className,final String methodName)
	{
		AsyncHttpRequest ahr = new AsyncHttpRequest();
		ahr.doRequest(url,data, encryption,new RequestCallback()
				{
					public void requestDidFinished(String body)
					{
						if (body != null)
						{
							try {
								Class<?> classType = Class.forName(className);
								Object object = classType.getConstructor(new Class[]{}).newInstance(new Object[]{});
								Method method = classType.getMethod(methodName, new Class[]{String.class});
								method.invoke(object,body);
							} catch (Exception e) {
								e.printStackTrace();
							} 
						}
					}
					public void requestDidFailed(){}
				});
	}
}
