# 一、平台介绍
在gitee和github里面基本上找不到一款好用、功能完善、且开放源码的网盘系统，有一些可以免费试用但是只提供安装包，于是在利用业务时间开发了一套轻量级且易于上手的网盘系统，主要基于目前主流的前后端分离和微服务架构模式开发，里面涉及很多的解决方案，`适合没有项目经验的同学学习`。网盘系统的核心目的有两个，第一：提供客户端给业务系统集成，统一管理业务系统的文件；第二：提供在线协调办公、管理个人文件的功能。系统会陆续更新和完善一些功能。

# 二、部署架构说明
![输入图片说明](https://images.gitee.com/uploads/images/2020/0414/201846_f0368b18_798389.png "部署架构3.png")

# 三、技术栈说明
#### 前端技术
* ①vue.js+ElementUI 作为基础技术框架
* ②WebUploader.js做切块上传框架

#### 后端技术
* ①Maven+SpringBoot+SpringDataJPA作为基础架构
* ②Dubbo+Zookeeper作为服务治理架构
* ③Nacos作为配置中心
* ④Redis做分布式缓存、过期监听
* ⑤Zookeeper做分布式锁
* ⑥WebSocket+Netty做消息推送
* ⑦Solr做全文检索引擎
* ⑧FastDFS做分布式文件系统
* ⑨基于Redis+token+自定义注解实现接口幂等性

# 四、功能说明
#### 一、网盘系统
* ①上传功能：主要是针对大文件的切块上传、秒传、文件夹上传
* ②下载功能：主要是大文件的切块下载；多文件（夹）合并、压缩下载
* ③文件分享：文件分享包括好友分享、私密链接分享、分享文件的转存
* ④相册管理：可以建立不同的相册来管理图片，并且可以图片在线预览功能
* ⑤回收站：删除的文件进入回收站，可以进行还原或者彻底删除
* ⑥推送功能：主要是好友分享消息推送、过期消息推送、容量更新推送
* ⑦分布式锁：主要是基于Zookeeper实现分布式锁，保证高并发情况下系统的数据安全
* ⑧过期监听：主要是基于Redis过期事件实现监听功能，包括：分享失效监听、删除过期监听等
* ⑨日志采集：通过AOP埋点的方式进行采集用户请求日志，并远程传输到日志服务端；自定义Dubbo的Filter实现`链路ID`的生成
* ⑩文件搜索：集成Solr框架实现全文搜索功能
* ⑪文件存储：集成FastDFS框架实现文件分布式存储
* ⑫其他琐碎功能：比如，复杂、移动、预览、删除、重命名、在线创建、在线编辑、编辑历史版本留痕等等
* ⑬Office在线编辑：<font color=blue> **后期更新** </font>
* ⑭h5版本客户端：<font color=red> **预计4月25号更新** </font>
* ⑮c/s版本客户端：<font color=red> **后期更新** </font>
* ⑯业务系统API客户端：<font color=red>【 **已经完成**】 </font>
* ⑰图片新增水印：<font color=red> **后期更新**</font>
* ⑱图片在线裁剪：<font color=red> **后期更新**</font>

#### 二、后台系统
* ①组件管理：主要管理文件的预览和编辑组件
* ②类型管理：主要是管理文件的格式、对应的图标、对应的预览和编辑组件
* ③日志管理：存储和展示业务系统的操作日志记录，并且可以根据`追踪ID`来关联所有的日志信息

# 五、系统安装步骤
参考：https://gitee.com/college996/zwz-netdisk/wikis

后期会编写一个自动化脚本，做到快速本地部署

# 六、版本更新说明
#### 2020-04-14第一个版本更新
**1、更新的功能点说明**
* ①开发了针对业务系统集成的客户端，netdisk-client、netdisk-client-provider
* ②完善的接口幂等性，redisTemplate执行LUA脚本保证原子性，【判断key是否存在+保存值】这两步是有原子性问题，netdisk-web-perpc/src/main/java/com.micro.idempotence.NoRepeatAop类
* ③日志采集，增加了开关（Nacos配置：busilog.state=off），netdisk-log-api/src/main/java/com.micro.logs.aop.CollectionLogAop
* ④完善了Redis功能，新增redis密码，避免遭遇攻击（Nacos配置：redispwd=xxxx）
* ⑤完善了切块临时记录存储问题，合并切块接口调用完成之后里面删除Redis里面的切块记录，避免大量切块记录过期造成的压力


**2、如果想获取该版本，则更新以下操作**
* ①拉取后端代码
* ②更新前端工程：zwz-disk-manage（新增了应用系统文件管理），更新地址：https://gitee.com/college996/zwz-disk-manage.git
* ③Nacos配置文件，需要获取**nacos_config_20200414.zip**，可以从网盘分享里面获取，或者从上面的附件获取
* ④安装Redis时，需要设置密码，参考：wiki里面的redis安装

# 七、对接业务系统的sdk说明【4月14号发布的】
**1、功能说明**

参考我写的一篇文章：
https://mp.weixin.qq.com/s?__biz=MzUzMzE3MDcwMQ==&mid=2247483693&idx=1&sn=f81a094019bb3d88cf6bb035e7c932d9&chksm=faa95e4fcdded7593ab31dc8c9920f79290b07bc006ac7f5d4396e66cfec8c3aa6aae5a82769&token=545547741&lang=zh_CN#rd

**2、如何集成**

第一步：运行netdisk-service-provider.jar

第二步：运行netdisk-client-provider.jar

第三步：业务注册，首先需要让管理员到后台系统新增一条记录，然后拿到APPID（**后台系统->应用管理模块**）

第四步：业务系统，采用原生的方式集成，导入disk-client-javasdk.jar包或者对应其坐标
```xml
<dependency>
    <groupId>com.micro</groupId>
    <artifactId>netdisk-client-javasdk</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
	
```
示例代码：
```java
    public static void main(String[] args){
        List<HostBean> hosts=new ArrayList<>();
        hosts.add(new HostBean("127.0.0.1", 8015, 1));
        FileService fs=FileFactory.createFileService(hosts,"ddd");

        //检查md5是否存在
        String appId="11111";
        String filemd5="xxxx";        
        int count=fs.checkFileByMd5(appId, filemd5);
    }
```
第五步：业务系统，采用SpringBoot starter的方式集成，导入netdisk-spring-boot-starter.jar的坐标
```xml
<dependency>
    <groupId>com.micro</groupId>
    <artifactId>netdisk-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

application.properties
```properties
    netdisk.server.host=127.0.0.1:8015
```
示例代码：
```java
@Autowired
private FileService fs;

@RequestMapping("/upload")
public String upload() throws InterruptedException{
    String appId="11111";
    String filemd5="xxxx";
    //检查md5是否存在
    int count=fs.checkFileByMd5(appId, filemd5);
}

```
**3、API接口说明**

由于接口太多，这里列出来不太方便，大家请看接口文件，里面的接口参数写的很清楚：

netdisk-client-javasdk/src/main/java/com.micro.netdisk.javasdk.service.FileService

# 八、其他相关地址说明


* `慕课网专栏`：https://www.imooc.com/read/73
* 前端工程（网盘）：https://gitee.com/college996/zwz-disk.git
* 前端工程（后台）：https://gitee.com/college996/zwz-disk-manage.git
* 网盘系统`在线演示`：[http://106.15.248.223/disk](http://106.15.248.223/disk)
* 网盘后台`在线演示`：[http://106.15.248.223/disk-manage](http://106.15.248.223/disk-manage)



# 微信公众号开通了，坚持手写有技术含量的原创文章
【作者写专栏和做项目也不容易，如果觉得对您有帮助可以帮忙订阅一下专栏，当做请作者喝杯咖啡，非常感谢 】

感谢各位老铁订阅了慕课网上面的专栏，由于慕课网的专栏留言是需要官方审核通过之后我才能看到，因此可能会延时，但是我收到留言基本上都会第一时间给大家回复。通过留言，我发现好多小伙伴还是有疑问的，为了方便帮助各位老铁解答相关疑问，`zwyjavaee`这是我的微信号，专栏内容、网盘系统、技术上有疑问的，都可以互相沟通，本人也是本着交朋友的态度去跟大家交流，所以希望文明交流！【加的时候，备注`慕课网`或者`码云`吧】

![输入图片说明](https://images.gitee.com/uploads/images/2020/0407/231510_6b308b9b_798389.jpeg "二维码.jpg")