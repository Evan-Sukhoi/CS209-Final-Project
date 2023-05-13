# CS209 Final Project Report

##### 李昱纬12112513 廖辰益12111719

### Part 1 数据库结构

本项目使用了相较于PostgreSQL更加轻量化的MySQL数据库存储数据。

为了满足本次Project的数据展示要求，本着数据库建立的"第三范式"（Third Normal Form）原则，即尽量减少在不同表之间使用外键联系，我们总共通过MySQL建立了7个表，彼此之间没有外键联系。

questions表（即为thread构建的表）为最主要的表，包含了共计655条数据。在收集数据时，我们多样化地采集了赞同数、活跃度、热度不同维度的question，既有历史上的高赞问题，也有近期的问题。表中，每个非主键列都直接与主键（question_id）相关联，服务于大多数设计要求。

user表记录了所有thread中所有用户，以及他们活跃的次数。

comments和answers表的body列，记录了所有thread中评论和回答的内容，连同questions表中的body列，用于分析高级要求部分的经常讨论的 Java API，并将结果保存在了hot_api表中。

tags表记录了所有thread中的tags和tags组合，统计了它们的浏览量和点赞数，以及出现的次数。tags_java_related统计了和java标签一起出现的tags以及次数。

数据库可视化如下：

![image-20230512223341935](C:\Users\HUAWEI\AppData\Roaming\Typora\typora-user-images\image-20230512223341935.png)

部分数据库内容展示：

![image-20230513000214622](C:\Users\HUAWEI\AppData\Roaming\Typora\typora-user-images\image-20230513000214622.png)





### Part 2 项目架构结构设计

以下是我们项目的结构：

java目录：
在controller目录下，DemoController类是SpringBoot框架的Controller，在访问页面时返回相应的视图；DataRestController类是SpringBoot框架的RestController，用于处理前端发送的请求。

在databsase目录下，adddata类用于数据库数据的爬取。

在model目录下，HotApi类与数据库中的hot_api表对应，Questions类与数据库中的questions表对应，Tags类与数据库中的tags表对应，TagsJavaRelated类与数据库中的tags_java_related表对应，Users类与数据库中的users表对应,    未完。

在repository目录下,Repositories接口继承JpaRepository，用于查询数据库各表中的数据。

在service目录下，Services用于处理各种事务，里面的各方法处理从数据库获取的数据，然后被RestController调用。

Application类用于启动整个SpringBoot框架，让所有程序开始运行。

StackOverflowApi类定义了一些从网站上爬取数据的方法，在adddata类中使用。

resources目录：
static目录用于存储html页面所需的各种静态资源。template目录下是不同页面对应的html文件，Homepage.html展现的是主页面,Answerspage.html展现与答案相关数据的页面,Tagspage.html展现与标签相关数据的页面,,Userspage.html展现与用户相关数据的页面,,Apispage.html展现与Api相关数据的页面。

### Part 3 成果与代码片段展示
Numbers of Answers部分：

    展示没有answers的百分比：

    展示answers数量的最大值和平均值：

    展示answers数的分布：

Accepted Answers部分：

    展示有accepted answer的百分比：

    展示问题从提出到解决时间间隔的分布：

    展示含有 non-accepted answer 的 upvote 数高于 accepted answer 的问题的百分比：


Tags部分：

    展示哪些 tags 经常和 Java tag 一起出现：

    展示哪些 tags 或 tag 的组合得到最多的 upvotes：
    
    展示哪些 tags 或 tag 的组合得到最多的 views：

Users部分：

    展示参与 Thread 讨论的用户数量的分布：

    从问题回答者和评论者两个角度进行统计：

    展示哪些用户参与 java thread 讨论最活跃：

Frequently discuss Java Apis部分：

    展示哪些 Java APIs 在 Stack Overflow 上最频繁被讨论：


### Part 4 总结分析

