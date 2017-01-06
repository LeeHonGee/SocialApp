package com.sharemob.tinchat.lib.common;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sharemob.tinchat.R;
import com.sharemob.tinchat.lib.MyApplication;

/**
 *
 * @author lihangjie
 * version [版本号,2015-9-12 下午2:26:15]
 * @see    [相关类/方法]
 * @since  [产品/模块版本]
 *
 */
public final class ExitHelper {

    private static final int INTERVAL = 1500;
    private static boolean EXIT_FLAG = false;

    private static Handler exitHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            EXIT_FLAG = false;
        }
    };

    public static boolean checkExit(Context context, int keyCode) {
        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return false;
        }

        if (EXIT_FLAG) {
            EXIT_FLAG = false;
            exitHandler.removeMessages(0);
//            ImageLoader.getInstance().clearDiscCache();
//  			ImageLoader.getInstance().clearMemoryCache();
//  			ImageLoader.getInstance().destroy();
  			MyApplication.getInstance().exit();
            android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            EXIT_FLAG = true;
            String appName=String.format("再按一次退出%s", context.getString(R.string.app_name));
            Toast.makeText(context,appName, Toast.LENGTH_SHORT).show();
            exitHandler.sendEmptyMessageDelayed(0, INTERVAL);
        }
        return true;
    }

    private ExitHelper() {}
}
