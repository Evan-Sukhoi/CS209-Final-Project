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







###Part 2 项目架构结构设计

### Part 3 成果与代码片段展示

### Part 4 总结分析

