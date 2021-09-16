# hoppinzq的微服务架构

#### 介绍
本项目是hoppinzq.com的后台框架的基本骨架，语言是Java8，各个模块使用springBoot作为脚手架搭建。

#### 模块预览
+ 1、hoppinzq-service : 该模块负责服务的内部注册，如果业务模块需要注册服务以供其他模块或者第三方调用，需要引入该模块或者jar包。
+ 2、hoppinzq-client : 该模块负责服务调用，如果业务模块或者第三方需要调用通过hoppinzq-service注册的服务，需要引入该模块或者jar包。
+ 3、hoppinzq-service-common ：见名知意，为上面两个模块的公共部分。

  **以上三个模块可以按需引入，或者合并为一个独立的服务模块**
+ 4、hoppinzq-service-core : 服务的注册中心模块，依赖于hoppinzq-service。该模块会通过hoppinzq-service去注册内部服务，其中包括注册服务，其他模块的服务需要通过hoppinzq-client去调用注册中心的注册服务来注册其内部服务的副本。所有服务将在一个List里面存根。
+ 5、hoppinzq-api-service : 业务模块。

#### 安装教程
1.  从git上拉取代码
2.  运行hoppinzq-service-core跟hoppinzq-api-service模块即可，控制台会打印返回注册服务接口html的请求URL

#### 架构设计


#### 说明
*  在该项目里，为什么采用Jetty作为web服务器而不是SpringBoot内置的Tomcat？这会增加我学习的成本吗？
> 不，首先两者的实现都是遵循JavaServlet规范，因此在SpringBoot项目使用Jetty替换Tomcat你基本不需要改动一行代码。下面是采用Jetty的原因：  
1、Jetty是轻量的Web服务器，而Tomcat是重量级服务器。在很大的web项目使用Tomcat是完全没有问题，但是既然出现了分布式，每个模块负责的内容都少得多。这些模块
都去使用Tomcat作为Web服务器就是高射炮打蚊子，而用轻量级服务器能节省很多内存跟空间，这些节约的内存对服务器是极其宝贵的。  
2、Jetty的扩展性高，因为Jetty的架构是基于Handler来实现的，主要的扩展功能都可以用Handler来实现（实现Filter接口也可），扩展简单，你可以很容易加一些自己的
功能：如代理、cookie、重定向、认证支持等。  
3、 Tomcat默认采用BIO处理IO请求，在处理静态资源时，性能较差。Jetty默认采用NIO结束在处理IO请求上更占优势，在处理静态资源时，性能较高，
而且可以同时处理大量连接而且可以长时间保持连接。但就这几点就可以宣布Tomcat死刑，不要忘了，本项目是各个服务之间的调用，而服务的远程调用就是通过序列化二进制
流去调用服务跟接收响应的。
*  在该项目里，服务是如何注册的？
> 1、在注册之前，你先要思考服务是如何存储的，在该项目里，服务是存储在一个集合内，那么注册其实就是把服务的一些细节存入该集合内。下面就让我
回答你的问题：服务注册在本项目分为两种，一种是通过@ServiceRegister注解自注册，被该注解环绕的类将被注册到Spring容器
内。然后你要给每个需要注册服务的模块注册ProxyServlet，通过重写其内部的createServiceWrapper方法来将被注册到Spring容器内的
@ServiceRegister环绕服务类保存进服务集合。  
2、注册中心模块就是通过上面的过程注册其内部服务，其中包括注册服务RegisterServer。因此，首先启动注册中心模块是很有必要的。。。待续
  
  




