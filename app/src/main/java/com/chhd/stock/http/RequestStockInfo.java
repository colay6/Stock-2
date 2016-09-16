package com.chhd.stock.http;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.chhd.stock.entity.Stock;
import com.chhd.stock.entity.UserData;
import com.chhd.stock.util.Consts;
import com.chhd.util.LogUtil;
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
 * Created by CWQ on 2016/7/10.
 */
public class RequestStockInfo {

    private Handler mHandler;
    private List<Stock> mStocks = new ArrayList<>();
    private int mMinimum;
    private int mSuccessFrequency;
    private int mFailFrequency;

    public RequestStockInfo(Handler handler) {
        mHandler = handler;
    }

    public void request(String stockID) {

        try {
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.configTimeout(1000 * 6);

            RequestParams requestParams = new RequestParams();
            requestParams.addHeader(Consts.KEY_APIKEY, Consts.APIKEY);
            requestParams.addQueryStringParameter(Consts.KEY_STOCKID, stockID);
            requestParams.addQueryStringParameter(Consts.KEY_LIST, "1");

            httpUtils.send(HttpRequest.HttpMethod.GET, Consts.STOCKS_URL, requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    try {
                        List<Stock> stocks = new ArrayList<>();

                        String json = responseInfo.result;
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray stockinfoJSONArray = jsonObject.getJSONObject(Consts.KEY_RETDATA).getJSONArray(Consts.KEY_STOCKINFO);
                        stocks = JSON.parseArray(stockinfoJSONArray.toString(), Stock.class);

                        Message message = Message.obtain();
                        message.what = Consts.HANDLER_REQUEST_STOCK_INFO_SUCCESS;
                        message.obj = stocks;
                        mHandler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    mHandler.sendEmptyMessage(Consts.HANDLER_REQUEST_STOCK_INFO_FAIL);
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void request(List<UserData> userDatas) {
        if (userDatas.size() == 0) {
            mHandler.sendEmptyMessage(Consts.HANDLER_USER_NO_DATA);
            return;
        }

        mStocks.clear();
        mMinimum = 0;
        mSuccessFrequency = 0;

        mMinimum = (int) Math.ceil((double) userDatas.size() / Consts.MAXIMUM);

        StringBuilder stockSB = new StringBuilder();
        int len = userDatas.size() / Consts.MAXIMUM;
        for (int i = 1; i <= len; i++) {
            for (int j = 0 + (i - 1) * Consts.MAXIMUM; j < Consts.MAXIMUM + (i - 1) * Consts.MAXIMUM; j++) {
                stockSB.append(userDatas.get(j).getStockID() + ",");
            }
            stockSB.delete(stockSB.length() - 1, stockSB.length());
            send(stockSB);
            stockSB.delete(0, stockSB.length());
        }
        for (int i = len * Consts.MAXIMUM; i < userDatas.size(); i++) {
            stockSB.append(userDatas.get(i).getStockID() + ",");
        }
        stockSB.delete(stockSB.length() - 1, stockSB.length());
        send(stockSB);
    }

    private void send(StringBuilder stockSB) {
        try {
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.configTimeout(1000 * 6);

            RequestParams requestParams = new RequestParams();
            requestParams.addHeader(Consts.KEY_APIKEY, Consts.APIKEY);
            requestParams.addQueryStringParameter(Consts.KEY_STOCKID, stockSB.toString());
            requestParams.addQueryStringParameter(Consts.KEY_LIST, "1");


            httpUtils.send(HttpRequest.HttpMethod.GET, Consts.STOCKS_URL, requestParams, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    try {

                        String json = responseInfo.result;
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray stockinfoJSONArray = jsonObject.getJSONObject(Consts.KEY_RETDATA).getJSONArray(Consts.KEY_STOCKINFO);
                        mStocks.addAll(JSON.parseArray(stockinfoJSONArray.toString(), Stock.class));
                        mSuccessFrequency++;
                        if (mSuccessFrequency == mMinimum) {
                            Message message = Message.obtain();
                            message.what = Consts.HANDLER_REQUEST_STOCK_INFO_SUCCESS;
                            message.obj = mStocks;
                            mHandler.sendMessage(message);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    mFailFrequency++;
                    if (mFailFrequency == 1) {
                        mHandler.sendEmptyMessage(Consts.HANDLER_REQUEST_STOCK_INFO_FAIL);
                    }
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
