/**
  * Copyright 2017 JessYan
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *      http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package me.jessyan.mvparms.demo.app;

import android.content.Context;
import android.net.ParseException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.jess.arms.utils.ArmsUtils;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.exceptions.CompositeException;
import me.jessyan.rxerrorhandler.handler.listener.ResponseErrorListener;
import retrofit2.HttpException;
import timber.log.Timber;

/**
 * ================================================
 * 展示 {@link ResponseErrorListener} 的用法
 * <p>
 * Created by JessYan on 04/09/2017 17:18
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class ResponseErrorListenerImpl implements ResponseErrorListener {

    @Override
    public void handleResponseError(Context context, Throwable t) {
        Timber.tag("Catch-Error").w(t.getMessage());
        //这里不光是只能打印错误,还可以根据不同的错误作出不同的逻辑处理
        String msg = "未知错误";
        msg = getErrorMessage(t, msg);
        ArmsUtils.makeText(context,msg);
        Map<String,Object> map = new HashMap<>();
        switch (msg){
            case "网络不可用":
                map.put("type", "1");
                ArmsUtils.showTagView(map);
                break;
            case "请求网络超时":
                map.put("type", "2");
                ArmsUtils.showTagView(map);
                break;
            case "数据解析错误":
                map.put("type", "3");
                ArmsUtils.showTagView(map);
                break;
            case "服务器发生错误":
                map.put("type", "4");
                ArmsUtils.showTagView(map);
                break;
            case "请求地址不存在":
                map.put("type", "5");
                ArmsUtils.showTagView(map);
                break;
            case "请求被服务器拒绝":
                map.put("type", "6");
                ArmsUtils.showTagView(map);
                break;
            case "请求被重定向到其他页面":
                map.put("type", "7");
                ArmsUtils.showTagView(map);
                break;
            case "未知错误":
                map.put("type", "8");
                ArmsUtils.showTagView(map);
                break;
        }
    }

    public String getErrorMessage(Throwable t, String msg){
        String abc = "";
        if (t instanceof CompositeException) {
            List<Throwable> throwables = ((CompositeException)t).getExceptions();
            for(Throwable tt:throwables){
                msg = getErrorMessage(tt, msg);
            }
        }else if (t instanceof UnknownHostException) {
            msg = "网络不可用";
        } else if (t instanceof SocketTimeoutException) {
            msg = "请求网络超时";
        } else if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            msg = convertStatusCode(httpException);
        } else if (t instanceof JsonParseException || t instanceof ParseException || t instanceof JSONException || t instanceof JsonIOException) {
            msg = "数据解析错误";
        }
        return msg;
    }

    private String convertStatusCode(HttpException httpException) {
        String msg;
        if (httpException.code() == 500) {
            msg = "服务器发生错误";
        } else if (httpException.code() == 404) {
            msg = "请求地址不存在";
        } else if (httpException.code() == 403) {
            msg = "请求被服务器拒绝";
        } else if (httpException.code() == 307) {
            msg = "请求被重定向到其他页面";
        } else {
            msg = httpException.message();
        }
        return msg;
    }

}
