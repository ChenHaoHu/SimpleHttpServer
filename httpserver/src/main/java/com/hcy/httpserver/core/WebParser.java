package com.hcy.httpserver.core;

import com.hcy.httpserver.util.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: 简单DI年华
 * @Date: 19-1-15 01:54
 * @Description: web.xml解析 在程序启动后就加载到 webData 里
 * @version 1.0
 * @since 1.0
 */
public class WebParser {

    public static Map<String,Map<String,String>> webData = new HashMap<String, Map<String, String>>();

    /**
     * 加载整个服务器下每个项目的 web.xml 文件
     */
    public static void init(){

        Logger.log("star init webData");
        //todo:path
        String path = "httpserver"+File.separator+
                "webapps";
        File webApps = new File(path);

        File[] files = webApps.listFiles();



        for (int i = 0; i < files.length; i++) {

            Map<String, String> map = parserWebXml(files[i].getName());


            if(map != null){
                webData.put(files[i].getName(),map);
            }
        }

        Logger.log("end init webData: servlet:"+webData.size());
    }



    /**
     * 获取单个 web.xml文件的信息 map key: 【url-pattern】 value:【servlet-class】
     * @param webAppName
     * @return map
     */
    public static Map<String,String> parserWebXml(String webAppName){

        //获取web.xml路径
        //TODO: webXmlPath
        String webXmlPath = "httpserver"+ File.separator+
                "webapps"+ File.separator+
                webAppName+ File.separator+
                "WEB-INF"+File.separator+"web.xml";

        //返回的 map map key: 【url-pattern】 value:【servlet-class】
        Map<String,String> servletMap = null;

        File file = new File(webXmlPath);
        //System.out.println(file.getPath());
        if (file.exists()){
            try {
                //创建 xml 解析器
                SAXReader saxReader = new SAXReader();
                //获取 document
                Document document = saxReader.read(file);
                //获取servlet节点元素
                List<Element> servletNodes = document.selectNodes("//web-app/servlet");
                //接收 servlet-name 与 servlet-class
                //map key: 【servlet-name】 value:【servlet-class】
                Map<String,String> servletInforMap = new HashMap<String, String>();
                //循环填充
                for (int i = 0; i < servletNodes.size(); i++) {
                    // key: 【servlet-name】
                    String key = servletNodes.get(i).selectSingleNode("servlet-name").getStringValue();
                    // value:【servlet-class】
                    String value = servletNodes.get(i).selectSingleNode("servlet-class").getStringValue();
                    // map key: 【servlet-name】 value:【servlet-class】
                    servletInforMap.put(key,value);
                }
                //获取所有 键
                Set<String> servletNames = servletInforMap.keySet();

                //获取servlet-mapping节点元素
                List<Element> servletMappingNodes = document.selectNodes("//web-app/servlet-mapping");
                //接收 url-pattern 与 servlet-class
                //map key: 【url-pattern】value:【servlet-class】
                servletMap = new HashMap<String, String>();
                for (int i = 0; i < servletInforMap.size(); i++) {
                    // 获取【servlet-name】
                    String servletName = servletMappingNodes.get(i).selectSingleNode("servlet-name").getStringValue();
                     // value:【servlet-class】
                    String value = servletInforMap.get(servletName);
                    // key: 【url-pattern】
                    String key = servletMappingNodes.get(i).selectSingleNode("url-pattern").getStringValue();
                    // map key: 【url-pattern】 value:【servlet-class】
                    servletMap.put(key,value);
                }


            } catch (DocumentException e) {
                e.printStackTrace();
            }

            return servletMap;
        }else{
            return null;
        }



    }
}
