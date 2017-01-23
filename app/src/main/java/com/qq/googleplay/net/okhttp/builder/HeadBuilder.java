package com.qq.googleplay.net.okhttp.builder;

import com.qq.googleplay.net.okhttp.OkHttpUtils;
import com.qq.googleplay.net.okhttp.request.OtherRequest;
import com.qq.googleplay.net.okhttp.request.RequestCall;

public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
