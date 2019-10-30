package com.lenny.module.udesk;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import cn.udesk.UdeskSDKManager;
import cn.udesk.config.UdeskConfig;
import udesk.core.UdeskConst;

/**
 * Created by lenny on 2017/2/22.
 */

class UdeskModule extends ReactContextBaseJavaModule {
    private WritableMap response = Arguments.createMap();
    private String appId, appKey, appDomain;
    private ReactApplicationContext mReactContext;
    UdeskConfig.Builder builder = new UdeskConfig.Builder();
    String token;
    public UdeskModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;
    }
    
    @Override
    public String getName() {
        return "RNUdesk";
    }
    
    @ReactMethod
    public void initSDK(String appDomain, String appKey, String appId) {
        this.appDomain = appDomain;
        this.appKey = appKey;
        this.appId = appId;
        UdeskSDKManager.getInstance().initApiKey(mReactContext.getApplicationContext(), this.appDomain, this.appKey, this.appId);
    }

    @ReactMethod
    public void setUserInfo(final ReadableMap options, final Callback callback) {
        Map<String, String> info = new HashMap<>();
       this.token = options.getString("sdk_token");
        if (this.token == null) {
            this.token = UUID.randomUUID().toString();
        }
        //token 必填
        info.put(UdeskConst.UdeskUserInfo.USER_SDK_TOKEN, token);
        //以下信息是可选
        if (hasAndNotEmpty(options, "nick_name")) {
            info.put(UdeskConst.UdeskUserInfo.NICK_NAME, options.getString("nick_name"));
        }
        if (hasAndNotEmpty(options, "email")) {
            info.put(UdeskConst.UdeskUserInfo.EMAIL, options.getString("email"));
        }
        if (hasAndNotEmpty(options, "cellphone")) {
            info.put(UdeskConst.UdeskUserInfo.CELLPHONE, options.getString("cellphone"));
        }
        if (hasAndNotEmpty(options, "description")) {
            info.put(UdeskConst.UdeskUserInfo.DESCRIPTION, options.getString("description"));
        }

        Map<String, String> fields = new HashMap<>();
        info.put("TextField_84621", this.token);
        builder.setDefualtUserInfo(info);
        builder.setDefinedUserTextField(fields);
    }
    

    
    @ReactMethod
    public void entryChat() {
        UdeskSDKManager.getInstance().entryChat(mReactContext.getApplicationContext(),builder.build(),this.token);
    }

    
    public static @NonNull boolean hasAndNotEmpty(@NonNull final ReadableMap target,
                                                  @NonNull final String key)
    {
        if (!target.hasKey(key))
        {
            return false;
        }
        
        final String value = target.getString(key);
        
        return !TextUtils.isEmpty(value);
    }

    
}

