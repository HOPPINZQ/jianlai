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


#### 使用说明

1.  xxxx
2.  xxxx
3.  xxxx

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
