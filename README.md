# jerryWebSpider

## 项目简介

jerryWebSpider是一个java爬虫实例集合，基于springboot构建，目前内含对tuwan网妹子图的爬虫。

## 项目模块 

### 一、tuwanSpider

tuwan接口更新，此爬虫目前已失效。

~~提供对tuwan网妹子图、音乐的抓取及下载功能，程序主要逻辑集中在task包下的TuwanSpiderTask类与TuwanImageDownloadTask类，入口为TuwanSpiderController。~~

![](https://raw.githubusercontent.com/jrhu05/jerryWebSpider/master/pic/tuwan.jpg)

### 二、lesheSpider

提供对leshe网妹子图的抓取及下载功能，程序主要逻辑集中在task包下的LesheSpiderTask类与LesheImageDownloadTask类，入口为LesheSpiderController。

![](https://raw.githubusercontent.com/jrhu05/jerryWebSpider/master/pic/leshe.jpg)

## 目录结构

![](https://raw.githubusercontent.com/jrhu05/jerryWebSpider/master/pic/structure.jpg)

## 运行说明

### 一、tuwanSpider运行说明

将代码clone到本地后。

1、使用navicat等工具新建mysql数据库，名称自定；

2、将db目录下的my_spider.sql导入数据库（该sql已经包括截止2018-12-06爬取到的最新数据）；

3、将项目导入idea或其他集成开发工具；

4、修改springboot配置文件application-dev.yml中的数据库配置及图包保存地址tuwan:imageStorePath；

5、启动项目；

6、图包地址爬取：访问http://你的IP:8088/tuwanSpider/startSpider?start=0&endLine=1500 即可对tuwan网id从0到1500的相册进行爬取；

7、图包批量下载：访问http://你的IP:8088/tuwanSpider/startDownLoadImageZipPackage 即可对前一步爬取到的图包进行下载；

8、项目打包及服务器部署运行请自行搜索

### 二、lesheSpider运行说明

将代码clone到本地后。

1、使用navicat等工具新建mysql数据库，名称自定；

2、将db目录下的my_spider.sql导入数据库（该sql已经包括截止2018-12-17爬取到的最新数据），如在其他步骤中已经导入过该数据库则无需新建数据和导入数据库；

3、将项目导入idea或其他集成开发工具；

4、修改springboot配置文件application-dev.yml中的数据库配置及图包保存地址leshe:imageStorePath；

5、启动项目；

6、图包地址爬取：访问http://你的IP:8088/lesheSpider/startSpider 即可对配置文件中默认的喵写真，少女秩序，风之领域，轻兰映画四个大类进行爬取；

7、图包批量下载：访问http://你的IP:8088/lesheSpider/startDownLoadImageZipPackage 即可对前一步爬取到的图包进行下载；

8、项目打包及服务器部署运行请自行搜索

## 其他

以上案例、代码及说明仅供测试使用，请勿用于商业用途。如需转载请注明出处，如代码运行或测试过程中发现问题或bug请发起issues。

资源打包下载见

http://blog.hytcshare.com/post/tuwan-spider.html
