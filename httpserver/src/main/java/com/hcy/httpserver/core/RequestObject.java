package com.hcy.httpserver.core;


import javaxx.servlet.ServletRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: 简单DI年华
 * @Date: 19-1-15 12:09
 * @Description: 请求封装
 * @version 1.0
 * @since 1.0
 */
public class RequestObject implements ServletRequest {

    //请求的URI
    private String uriWithNoParameters = null;


    //请求的参数
    private Map<String,String[]> parameterMap = new HashMap<String,String[]>();

    /**
     * 获取appname
     * @return
     */
    public String getAppName(){
        String appName = uriWithNoParameters.split("/")[1];

        return appName;
    }

    /**
     * 获取 请求的路径
     * @return
     */
    public String getUrlPattern(){

        String appName = getAppName();
        String pattern = uriWithNoParameters.substring(appName.length()+1);

        return pattern;
    }


    /**
     * 构造函数 解析 requestURI
     * @param requestURI
     */
    public RequestObject(String requestURI ){
        //request的几种可能
        // /email/index.html?
        // /email/index.html?xx=
        // /email/index.html?xx=123
        // /email/index.html?xx=123&yy=321&zz=abc
        // /email/index.html?xx=123&yy=321&zz=abc&zz=cba

        //判断是否有参数
        if(requestURI.contains("?")){
            //有参数的话
            //进行分割 ？
             uriWithNoParameters = requestURI.split("[?]")[0];
            String parameters = requestURI.split("[?]")[1];

            //对参数进行解析 利用 【&】 号分割
         String[] paras =  parameters.split("[&]");

            for (int i = 0; i < paras.length; i++) {
                //用 【=】 分割获取 key value
               String[] keyAndValue =  paras[i].split("[=]");
                String key = "";
                String value = "";
                if(keyAndValue.length > 1){
                   key = keyAndValue[0];
                   value = keyAndValue[1];
              }else{
                   key = keyAndValue[0];
                   value = "";
              }

                //添加至 map
                if(parameterMap.get(key) == null){
                    //之前没有这个参数
                    String[] values = new String[1];
                    values[0] = value;

                    parameterMap.put(key,values);
                }else{
                    //之前有这个参数
                    String[] values = parameterMap.get(key);
                    //数组中添加新的新的value
                    String[] valusesNew = new String[values.length+1];
                    System.arraycopy(values,0,valusesNew,0,values.length);
                    valusesNew[values.length] = value;
                    parameterMap.put(key,valusesNew);
                }
            }

        }else{
            //无参数
            //不做处理
             uriWithNoParameters = requestURI;
        }



    }


    /**
     * 获取单个参数值
     * @param key
     * @return
     */
    public String getParameterValue(String key){

        String[] strings = parameterMap.get(key);

        if( strings!=null && strings.length == 1 ){
            return strings[0];
        }

        return null;
    }

    /**
     * 获取数组参数值
     * @param key
     * @return
     */
    public String[] getParameterValues(String key){

        String[] strings = parameterMap.get(key);

        if( strings!=null  ){
            return strings;
        }

        return null;
    }

    public String getUriWithNoParameters() {
        return uriWithNoParameters;
    }

    public void setUriWithNoParameters(String uriWithNoParameters) {
        this.uriWithNoParameters = uriWithNoParameters;
    }

    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, String[]> parameterMap) {
        this.parameterMap = parameterMap;
    }

}
