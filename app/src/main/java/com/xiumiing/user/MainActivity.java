package com.xiumiing.user;

import android.content.Context;
import android.os.Build;
import android.os.UserHandle;
import android.os.UserManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.blankj.utilcode.util.ReflectUtils;
import com.blankj.utilcode.util.Utils;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.init(this);
        openFlymeParallelSpace();
    }

    int FLAG_MANAGED_PROFILE = 0x00000020; // 创建影子用户必要的flag
    private static final String FLYME_PARALLEL_SPACE_USER_NAME = "FlymeParallelSpace";// 指定影子用户UserName
    private UserManager mUserManager;
    private Object mFlymeParallelSpaceUserInfo = null; // multi-open UserInfo

    public void openFlymeParallelSpace() {
        mUserManager = (UserManager) getSystemService(Context.USER_SERVICE);

        UserHandle userHandle = android.os.Process.myUserHandle();
        int getIdentifier = ReflectUtils.reflect(userHandle).method("getIdentifier").get();
        mFlymeParallelSpaceUserInfo = ReflectUtils
                .reflect(mUserManager)
                .method("createProfileForUser",
                        FLYME_PARALLEL_SPACE_USER_NAME,
                        FLAG_MANAGED_PROFILE,
                        getIdentifier);
        int userId = ReflectUtils.reflect(mFlymeParallelSpaceUserInfo).field("id").get();
        Object iActivityManager = ReflectUtils.reflect("android.app.ActivityManagerNative").method("getDefault").get();
        Boolean isOk = ReflectUtils.reflect(iActivityManager).method("startUserInBackground", userId).get();
        Log.d(TAG, "startUserInBackground() userId = " + userId + " | isOk = " + isOk);
    }
}
