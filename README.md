# [SimpleHttpServer](https://github.com/ChenHaoHu/SimpleHttpServer)
A simple httpserver like Tomcat, just build some functions

简单实现一个http服务器



![](cat.svg) 



[![](https://img.shields.io/github/license/ChenHaoHu/SimpleHttpServer.svg)]([![](https://img.shields.io/github/forks/ChenHaoHu/SimpleHttpServer.svg)](https://github.com/ChenHaoHu/SimpleHttpServer)) [![](https://img.shields.io/github/stars/ChenHaoHu/SimpleHttpServer.svg)](https://github.com/ChenHaoHu/SimpleHttpServer) ![](https://img.shields.io/github/issues/ChenHaoHu/SimpleHttpServer.svg) ![](https://img.shields.io/badge/JAVA-PRO-red.svg) 





### 进度

> 2019/1/14 v1.0 实现静态页面请求处理
>
> 2019/1/15 v1.0 实现动态servlet请求
>
> 2019/6/11-12 v2.0 实现NIO（参考Doug Lea 大佬的 Scalable IO in Java，ppt在项目根目录）



### 实现要点

- [x] 实现静态页面深路径访问

- [x] 实现Servlet访问

- [x] 封装了规范  --> 模仿 SUN 公司 ，几个简单的 interface

- [x] 实现了Servlet实例池

- [x] 实现了参数解析

- [x] 多线程处理请求

- [x] 改成了NIO的形式
