package com.xiumiing.user;

import android.content.Context;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.ReflectUtils;
import com.blankj.utilcode.util.Utils;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.init(this);

    }

    int FLAG_MANAGED_PROFILE = 0x00000020; // 创建影子用户必要的flag
    private static final String FLYME_PARALLEL_SPACE_USER_NAME = "FlymeParallelSpace";// 指定影子用户UserName
    private UserManager mUserManager;
    private Object mFlymeParallelSpaceUserInfo = null; // multi-open UserInfo

    public void openFlymeParallelSpace() {

        UserHandle userHandle = android.os.Process.myUserHandle();
        int getIdentifier = ReflectUtils.reflect(userHandle).method("getIdentifier").get();
        Log.d(TAG, "getIdentifier = " + getIdentifier);

        mUserManager = (UserManager) getSystemService(Context.USER_SERVICE);
        Log.d(TAG, "mUserManager = "+mUserManager.getUserName());
        mFlymeParallelSpaceUserInfo = ReflectUtils
                .reflect(mUserManager)
                .method("createProfileForUser",
                        FLYME_PARALLEL_SPACE_USER_NAME,
                        FLAG_MANAGED_PROFILE,
                        getIdentifier);
        int userId = ReflectUtils.reflect(mFlymeParallelSpaceUserInfo).field("id").get();
        Log.d(TAG, "userId = " + userId);

        Object iActivityManager = ReflectUtils.reflect("android.app.ActivityManagerNative").method("getDefault").get();
        Log.d(TAG, "iActivityManager = " + iActivityManager);

        Boolean isOk = ReflectUtils.reflect(iActivityManager).method("startUserInBackground", userId).get();

        Log.d(TAG, "startUserInBackground() userId = " + userId + " | isOk = " + isOk);
    }

    public void onClick(View view) {
        try {
        openFlymeParallelSpace();
        } catch (Exception e) {
//            LogUtils.e(e);
        }


    }
}
