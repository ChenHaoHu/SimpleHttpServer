package com.hcy.httpserver.core;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;

/**
 * @Auther: 简单DI年华
 * @Date: 19-1-14 23:35
 * @Description: server.xml配置文件解析
 * @version 1.0
 * @since 1.0
 */
public class ServerParser {

    //配置文件路径
    private static String serverXmlPath =  "httpserver"+ File.separator +"conf"+ File.separator +"server.xml";

    /**
     * 获取服务器端口号
     * @return
     */
    public static int getport(){

        //创建解析器
        SAXReader saxReader = new SAXReader();

        //默认端口为8080
        int port = 8080;
        //通过解析器的read方法将配置文件读到内存中 并返回对像树
        try {
            Document document = saxReader.read(serverXmlPath);
            //获取节点
            Element element = (Element)document.selectSingleNode("//connector");
            //获取port
             port = Integer.parseInt(element.attributeValue("port"));

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return port;
    }
}
