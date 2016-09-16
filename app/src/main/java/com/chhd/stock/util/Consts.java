package com.chhd.stock.util;

/**
 * Created by CWQ on 2016/7/9.
 */
public class Consts {
    // MyApplication
    public static final String APPLICATION_ID = "7de61313d6aa06aecb19e7a43f99933b";
    public static final int LOGOUT = -1;
    public static final int LOGIN = 0;
    // RegisterActivity
    public static final int ERROR_USERNAME_ALREADY_EXISTS = 202;
    public static final int ERROR_NETWORK_IS_NOT_AVAILABLE = 9016;
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    // DetailActivity
    public static final String KEY_STOCK_ID = "stockID";
    public static final String KEY_USER = "user";
    public static final String ACTION_UPDATE_USER_INFO = "update_user_info";
    // StockAdapter
    public static final int LISTVIEW_ITEM_HEAD = 0;
    public static final int LISTVIEW_ITEM_STOCK = 1;
    public static final int HANDLER_UPDATE_CURRENT_BANNER_INDEX = 0;
    // GraphPagerAdapter
    public static final String KEY_URLS = "urls";
    public static final String KEY_CURRENT_INDEX = "current_index";
    // StockFragment
    public static final String QUERY = "query";
    public static final int HANDLER_REQUEST_STOCK_INFO_FAIL = -2;
    public static final int HANDLER_USER_NO_DATA = -1;
    public static final int HANDLER_REQUEST_STOCK_INFO_SUCCESS = 0;
    public static final int HANDLER_REFRESH_STOCK_LISTVIEW = 1;
    public static final String KEY_STOCK = "stock";
    // NewsFragmet
    public static final String SHARES = "shares";
    public static final String ACTION_UPDATE_BANNERS = "update_banners";
    public static final int HANDLER_ALREADY_LOADING_FINISH = -2;
    public static final int HANDLER_REQUEST_NEWS_FAIL = -1;
    public static final int HANDLER_REQUEST_NEWS_INFO_SUCCESS = 0;
    public static final int HANDLER_REQUEST_MORE_NEWS_INFO_SUCCESS = 1;
    public static final int HANDLER_REFRESH_NEWS_LISTVIEW = 2;
    public static final String KEY_NEWS = "news";
    public static final String KEY_BANNERS = "banners";
    // MeFragment
    public static final int LOADING_TIME = 1000;
    public static final String KEY_REMEMBER = "remember";
    public static final String KEY_AUTO_LOGIN = "auto_login";
    public static final String ACTION_MEFRAGMENT_ONCREATEVIEW = "MeFragment_onCreateView";
    public static final String ACTION_STATUS_CHANGE = "status_change";
    public static final int REQUEST_FORM_REGISTERACTIVITY = 0;
    public static final int REQUEST_FORM_GALLERY = 1;
    public static final int REQUEST_FORM_CAMERA = 2;
    public static final int REQUEST_FORM_CROP = 3;
    public static final int REQUEST_FORM_PASSWRODACTIVITY = 4;
    public static final int REQUEST_SUCCESS = 1;
    public static final int ERROR_USERNAME_OR_PASSWORD_INCORRECT = 101;
    // PasswordActivity
    public static final int ERROR_OLD_PASSWORD_INCORRECT = 210;
    // RequestStockInfo
    public static final String STOCKS_URL = "http://apis.baidu.com/apistore/stockservice/stock";
    public static final String APIKEY = "3419f807c8ee9c839a87b8d2459ddc3a";
    public static final int MAXIMUM = 10;
    public static final String KEY_APIKEY = "apikey";
    public static final String KEY_STOCKID = "stockid";
    public static final String KEY_LIST = "list";
    public static final String KEY_RETDATA = "retData";
    public static final String KEY_STOCKINFO = "stockinfo";
    public static final String STOCKIDS =
            "sz000541," + // 深证交易所 佛山照明
//                    "sz002024," +// 深证交易所 苏宁云商
//                    "sz000001," +// 深证交易所 平安银行
//                    "sz000063," +// 深证交易所 中兴通讯
//                    "sz000055," +// 深证交易所 方大集团
                    "sh601988," +// 上海交易所 中国银行
//                    "sh601390," +// 上海交易所 中国中铁
//                    "sh600000," +// 上海交易所 浦发银行
//                    "sh601857," +// 上海交易所 中国石油
//                    "sh600036," +// 上海交易所 招商银行
//                    "sh600055," +// 上海交易所 华润万东
                    "sh600050";// 上海交易所 中国联通
    // RequestNewsInfo
    public static final String NEWS_URL = "http://apis.baidu.com/3023/news/channel";
    public static final String KEY_ID = "id";
    public static final String KEY_PAGE = "page";
    // SharedPreferencesUtil
    public static final String CONFIG = "config";
    public static final String KEY_FIRST_START = "first_start";
    // TitleBarView
    public static final String NAMESPACE = "http://schemas.android.com/apk/res/com.chhd.stock";
}
