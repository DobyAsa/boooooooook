# 书籍交易平台

#### 软件架构
后端：springboot、mybatis-plus
前端：html、thymeleaf、layui


#### 安装教程
1.  执行script中的SQL文件，这是数据库的表，端口为3306。
2.  沙箱支付的参数需要修改。
配置文件在src/main/resources/application.yml

#### 使用说明

1.  运行主启动类。
2.  浏览器输入http://localhost:8000/book/book/index进入主页
3.  后台管理员账密：admin
4.  图片路径默认为D盘D:/images，在src/main/java/com/book/bookshop/config/WebMvcConfig.java中可以修改

