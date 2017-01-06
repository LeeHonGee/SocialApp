/**
 *  文件名:CacheMemory.java
 *  修改人:lihangjie
 *  创建时间:2015-11-22 下午10:13:52
 */
package com.sharemob.tinchat.modules.danmu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * <一句话功能简述>
 *
 * @author lihangjie
 * version [版本号,2015-11-22 下午10:13:52]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public final class CacheMemory {

	public static List<DanmuItem> list_danmu =Collections.synchronizedList( new  ArrayList< DanmuItem>()); 
}
