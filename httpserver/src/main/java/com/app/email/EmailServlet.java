package com.app.email;

import com.hcy.httpserver.core.RequestObject;
import com.hcy.httpserver.core.ResponseObject;
import javaxx.servlet.Servlet;
import javaxx.servlet.ServletRequest;
import javaxx.servlet.ServletResponse;

import javax.mail.Message;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Properties;

/**
 * @Auther: 简单DI年华
 * @Date: 19-1-15 13:27
 * @Description: 开发的产品 servlet 发送邮件的app 不属于服务器项目的本体 输入产品
 * @version 1.0
 * @since 1.0
 */

public class EmailServlet  implements Servlet {

    /**
     * 实现了规范接口
     * @param servletRequest
     * @param servletResponse
     * @throws Exception
     */
    public void serivice(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {

        String send = servletRequest.getParameterValue("send");
        String text = servletRequest.getParameterValue("text");
        String title = servletRequest.getParameterValue("title");


        //转个码
        send =  URLDecoder.decode(send,"utf-8");


        Properties prop = new Properties();
        prop.setProperty("mail.host", "smtp.qq.com");
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.smtp.port", "25");
        prop.setProperty("mail.smtp.auth", "true");
        //使用JavaMail发送邮件的5个步骤
        //1、创建session
        Session session = Session.getInstance(prop);
        //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
        session.setDebug(true);
        //2、通过session得到transport对象
        Transport ts = session.getTransport();
        //3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
        ts.connect("smtp.qq.com", "775656764@qq.com", "vnqfxqrmnupcbbhj");
        //4、创建邮件
        Message message = createSimpleMail(session,send,text,title);
        //5、发送邮件
        ts.sendMessage(message, message.getAllRecipients());
        ts.close();

        StringBuffer pw = servletResponse.getWriter();
        pw.append("<div style=\"text-align: center;font-size: 40px;color: red;\">  发送成功！！</div>");
    }

    /**
     *
     * @param session
     * @return
     * @throws Exception
     */
    public static MimeMessage createSimpleMail(Session session,String send,String text,String title)
            throws Exception {
        //创建邮件对象
        MimeMessage message = new MimeMessage(session);
        //指明邮件的发件人
        message.setFrom(new InternetAddress("775656764@qq.com"));
        //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(send));
        //邮件的标题
        message.setSubject(text);
        //邮件的文本内容
        message.setContent(title, "text/html;charset=UTF-8");
        //返回创建好的邮件对象
        return message;
    }
}