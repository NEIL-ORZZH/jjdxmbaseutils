package com.dou361.utils;

import android.content.Context;

/**
 * ========================================
 * <p/>
 * 版 权：深圳市晶网科技控股有限公司 版权所有 （C） 2015
 * <p/>
 * 作 者：陈冠明
 * <p/>
 * 个人网站：http://www.dou361.com
 * <p/>
 * 版 本：1.0
 * <p/>
 * 创建日期：2015/12/25
 * <p/>
 * 描 述：用户sdk初始化配置
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class UtilsManager {

    private static UtilsManager instance;
    private Context appContext;
    private String userKey;

    /** 初始化sdk userKey为sdk的key，当前还没有用到，可空 */
    private UtilsManager(Context appContext, String userKey) {
        this.appContext = appContext;
        this.userKey = userKey;
    }

    public static UtilsManager getInstance() {
        if (instance == null) {
            throw new RuntimeException("UtilsManager not initialized!");
        } else {
            return instance;
        }
    }

    /**
     * 初始化sdk，需要在Application的oncreate()方法中调用
     */
    public static void init(Context appContext, String userKey) {
        instance = new UtilsManager(appContext, userKey);
    }

    /**
     * 获取程序Application中的上下文
     */
    public Context getAppContext() {
        return appContext;
    }

    /**
     * 设置一级标签名称 默认是dou361
     */
    public void setFristTag(String flag) {
        LogUtils.setFristTag(flag);
    }

    /**
     * 设置开发环境 默认是false,正式环境无日志输出
     */
    public void setDebugEnv(boolean flag) {
        LogUtils.setPrintLog(flag);
    }

    /**
     * 设置日志输出等级 默认是log.e()等级输出
     */
    public void setLogLevel(int flag) {
        LogUtils.setLogLevel(flag);
    }

}
