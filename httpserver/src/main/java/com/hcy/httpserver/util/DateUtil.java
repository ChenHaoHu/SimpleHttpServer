package com.hcy.httpserver.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @Auther: 简单DI年华
 * @Date: 19-1-14 23:06
 * @Description: 时间工具类
 * @version 1.0
 * @since 1.0
 *
 */
public class DateUtil {

  private static   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

    /**
     * 获取当前时间 格式：yyyy-MM-dd HH:mm:ss SSS
     * @return
     */
  public static String getCurrentTime(){
      return dateFormat.format(new Date());
  }
}
