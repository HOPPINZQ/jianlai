# hoppinzq的微服务架构
![hoppinzq](https://images.gitee.com/uploads/images/2021/0918/092145_1c983e1b_5294558.png "dignitas.png")

#### 介绍
本项目是hoppinzq.com的后台框架，框架，框架！不是业务代码！语言是Java8，各个模块使用springBoot作为脚手架搭建。

#### 模块预览
+ 1、hoppinzq-service : 该模块负责服务的内部注册，如果业务模块需要注册服务以供其他模块或者第三方调用，需要引入该模块或者jar包。
+ 2、hoppinzq-client : 该模块负责服务调用，如果业务模块或者第三方需要调用通过hoppinzq-service注册的服务，需要引入该模块或者jar包。
+ 3、hoppinzq-service-common ：见名知意，为上面两个模块的公共部分。

  **以上三个模块可以按需引入，或者合并为一个独立的服务模块，由于不依赖除spring外的jar包，你也可以将其打成jar包作为外部依赖**
+ 4、hoppinzq-service-core : 服务的注册中心模块，依赖于hoppinzq-service。该模块会通过hoppinzq-service去注册内部服务，其中包括注册服务，其他模块的服务需要通过hoppinzq-client去调用注册中心的注册服务来注册其内部服务的副本。所有服务将在一个List里面存根。
+ 5、hoppinzq-api-service : 业务模块（为什么起这个名字？因为该模块集成了一个zq-api中间件）。
+ 6、hoppinzq-service-gateway ：zq框架独有网关模块，将作为一个新的git项目讲解它。

#### 安装教程
1.  从git上拉取代码
2.  为hoppinzq-api-service配置数据源，空数据库即可，因为目前没有对数据库的操作。
3.  运行hoppinzq-service-core跟hoppinzq-api-service模块即可，控制台会打印返回注册服务接口的URL

#### 架构设计


#### 说明
*  在该项目里，为什么采用Jetty作为web服务器而不是SpringBoot内置的Tomcat？这会增加我学习或使用的成本吗？
> 不，首先两者的实现都是遵循JavaServlet规范，因此在SpringBoot项目使用Jetty替换Tomcat你基本不需要改动一行代码。下面是采用Jetty的原因：  
1、Jetty是轻量的Web服务器，而Tomcat是重量级服务器。在很大的web项目使用Tomcat是完全没有问题，但是现在的项目经过分布式的拆分，每个模块负责的内容都少得多。这些模块
都去使用Tomcat作为Web服务器就是高射炮打蚊子，而用轻量级服务器Jetty能节省很多内存跟空间，这些节约的内存对服务器是极其宝贵的。  
2、Jetty的扩展性高，因为Jetty的架构是基于Handler来实现的，主要的扩展功能都可以用Handler来实现，扩展简单，你可以通过实现Filter接口很容易加一些自己的
功能：如代理转发、cookie、重定向至403、认证支持等。  
3、 Tomcat默认采用BIO处理IO请求，在处理静态资源时，性能较差。Jetty默认采用当前非常流行的NIO去处理IO请求，在处理静态资源时，性能较高，
因而可以同时处理大量连接而且可以长时间保持连接。  
> 单就第三点就可以宣布Tomcat死刑，不要忘了，本项目主要实现了各个服务之间的远程调用，而服务的通讯就是通过序列化的二进制流。NIO就是异步IO，能够保证在并发的情况下服务
> 的通讯不会被单个IO流阻塞，这就足够了。  

> ps:  你可以通过下面的方式移除SpringBoot默认的Tomcat服务器并启用(SpringBoot内置的)jetty服务器
> 
> ```<dependency>```  
> &emsp;&emsp;&emsp;```<groupId>org.springframework.boot</groupId>```  
> &emsp;&emsp;&emsp;```<artifactId>spring-boot-starter-web</artifactId>```  
> &emsp;&emsp;&emsp;```<exclusions>```  
> &emsp;&emsp;&emsp;&emsp;&emsp;```<exclusion>```  
> &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;```<groupId>org.springframework.boot</groupId>```  
> &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;```<artifactId>spring-boot-starter-tomcat</artifactId>```  
> &emsp;&emsp;&emsp;&emsp;&emsp;```</exclusion>```  
> &emsp;&emsp;&emsp;```</exclusions>```  
> ```</dependency>```  
>```<dependency>```  
> &emsp;&emsp;&emsp;```<groupId>org.springframework.boot</groupId>```  
> &emsp;&emsp;&emsp;```<artifactId>spring-boot-starter-jetty</artifactId>```  
> ```</dependency>```

*  在该项目里，服务是如何注册的？
> 1、在注册之前，你先要思考服务是如何存储的，在该项目里，服务是存储在一个线程安全的集合里，那么注册其实就是把服务的一些细节存入该集合内。下面就让我回答你的问题
> ：服务注册在本项目分为两种，一种是通过自定义@ServiceRegister注解自注册，被该注解环绕的类在启动时会被注册到Spring容器
>内，因为只有服务类被实例化，我们才能通过反射获取这些服务类的信息。然后你要给每个需要进行服务注册的模块注册ProxyServlet这个Servlet，
> 通过重写其内部的createServiceWrapper方法来将被注册到Spring容器内的@ServiceRegister环绕服务类增进服务集合。  
>2、能被注册进集合的服务并不是服务类，而是服务包装类ServiceWrapper。服务类在进行注册的时候，需要包装服务用户验证方式、服务鉴权方式、服务调用过程跟踪方式以及服务类，
> 你还可以拓展一些其他的参数包装在包装类内，如服务详情，服务提供者信息等等。  
>  3、注册中心模块就是通过上面的过程注册其内部服务，其中包括注册服务RegisterServer。因此，首先启动注册中心模块是很有必要的，因为外部服务就是通过RegisterServer的insert方法
进行注册。注意，根据配置是否开启严格模式，注册外部服务时注册中心的行为会有不同。在严格模式下，不允许新增相同的外部服务（你可以通过update更新服务）；反之，会覆盖相同的服务。注册进注册中心失败的服务将会进行注册重试。
> 值得注意：注册进注册中心的并不是服务类，而是模块内部注册服务的一份副本。因此，对于外部服务而言，注册中心只是起到了存根的功能，而不具备服务的真正注册或者服务代理转发。
![服务注册](https://images.gitee.com/uploads/images/2021/0917/170045_35e9a3be_5294558.png "服务注册.png")
* 在该项目里，服务是如何被发现的？
> 服务发现很简单，即把模块内部维护的服务集合的服务细节通过返回Html流或者一个接口暴露。在ProxyServlet类内，假如请求并没有被服务端正确解析，该请求将被我视为不是该框架的客户端发起
>的，此时将返回描述服务接口的HTML。你可以重写respondServiceHtml方法来实现你自己的服务发现。本项目的注册中心暴露了一个服务接口，你可以在com.hoppinzq.service.controller.ServiceController找到它。  
![服务发现](https://images.gitee.com/uploads/images/2021/0917/171131_966f0c9b_5294558.png "服务发现png.png")
* 在该项目里，服务之间是如何调用的？  
> 1、你首先要知道在分布式项目里，各个服务调用的方式都有那些，Feign跟HttpClient都是不错的选择，两个都是通过模拟HTTP请求，你也可以通过原生
的Socket去建立服务之间的连接，通过为服务端客户端约定一种自定义协议来通讯。这些都是可以实现两个服务互相调用的。  
2、本项目的服务之间通过http通讯，协议是在http报文后追加一段序列化的二进制数据流。各个模块之间严格按照这种通讯协议通讯，如果数据无法
被解析，那么这次请求将被我视为不是zq框架发起的，将在ProxyServlet的service方法抛出EOF异常而不是由Jetty抛出400响应码。  
3、本框架调用过程是这样的：假设有客户端A要调用服务端B的一个服务的一个方法，客户端A将会创建一个服务接口类的代理对象，然后客户端A在调用接口的方法时会被代理对象拦截，
由代理对象创建一个临时会话，然后将服务方法、传参、调用方信息序列化为一段二进制码，由Http发送该序列化的二进制码。服务端通过反序列化二进制码
得到客户端A封装的服务类、方法、参数、调用方信息等信息。服务端先通过服务类去其注册中心查询有无该服务,如果有服务，就拿到调用方信息去跟该服务注册时的用户验证
手段跟鉴权手段比对。鉴权成功后，拿到调用方信息中的类+方法+参数通过反射去调用服务的方法，获取返回数据。该过程的结果、异常都会通过序列化二进制流
返还给调用方。
![调用流程](https://images.gitee.com/uploads/images/2021/0918/003248_d6090f12_5294558.png "过程.png")
4、假设服务端有一个服务需要传入一个输入流，那么客户端在代理的时候会将其用一个标志类InputStreamArgument替换。服务端在反序列化的时候通过判断标志类InputStreamArgument判断是否重新获取http输出流作为传参。
5、假设服务端有个服务方法只是对传参通过set方法等进行了修改，然后返回了void。遇到这种服务方法，必须在注册该服务前的时候将监听方式（SetterModificationManager）通过setModificationManager包装进来，然后客户端会去解析该类提供的修改列表，以便在客户端模拟服务端的修改过程。
![4跟5](https://images.gitee.com/uploads/images/2021/0922/161625_5352860e_5294558.png "407daf2737f11f790bfacfea8b1cd9d.png")
  
  




