package com.chhd.stock.http;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.chhd.stock.entity.News;
import com.chhd.stock.util.Consts;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CWQ on 2016/7/28.
 */
public class RequestNewsInfo {

    private Handler mHanlder;

    public RequestNewsInfo(Handler handler) {
        mHanlder = handler;
    }

    public void request(String id) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configTimeout(1000 * 6);

        RequestParams requestParams = new RequestParams();
        requestParams.addHeader(Consts.KEY_APIKEY, Consts.APIKEY);
        requestParams.addQueryStringParameter(Consts.KEY_ID, id);
        requestParams.addQueryStringParameter(Consts.KEY_PAGE, "" + 1);

        httpUtils.send(HttpRequest.HttpMethod.GET, Consts.NEWS_URL, requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    List<News> newss = new ArrayList<>();
                    String json = responseInfo.result;
                    Log.i("chhd", "onSuccess: " + json);
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("article");
                    newss.addAll(JSON.parseArray(jsonArray.toString(), News.class));

                    Message message = Message.obtain();
                    message.what = Consts.HANDLER_REQUEST_NEWS_INFO_SUCCESS;
                    message.obj = newss;
                    mHanlder.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                mHanlder.sendEmptyMessage(Consts.HANDLER_REQUEST_NEWS_FAIL);
                e.printStackTrace();
            }
        });
    }

    public void requestMore(String id, String page) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configTimeout(1000 * 6);

        RequestParams requestParams = new RequestParams();
        requestParams.addHeader(Consts.KEY_APIKEY, Consts.APIKEY);
        requestParams.addQueryStringParameter(Consts.KEY_ID, id);
        requestParams.addQueryStringParameter(Consts.KEY_PAGE, page);

        httpUtils.send(HttpRequest.HttpMethod.GET, Consts.NEWS_URL, requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    List<News> newss = new ArrayList<>();
                    String json = responseInfo.result;
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("article");
                    newss.addAll(JSON.parseArray(jsonArray.toString(), News.class));

                    Message message = Message.obtain();
                    message.what = Consts.HANDLER_REQUEST_MORE_NEWS_INFO_SUCCESS;
                    message.obj = newss;
                    mHanlder.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                mHanlder.sendEmptyMessage(Consts.HANDLER_REQUEST_NEWS_FAIL);
                e.printStackTrace();
            }
        });
    }
}
