# CS209 Final Project Report

##### 李昱纬12112513 廖辰益12111719

### Part 1 数据库结构

本项目使用了相较于PostgreSQL更加轻量化的MySQL数据库存储数据。

为了满足本次Project的数据展示要求，本着数据库建立的"第三范式"（Third Normal Form）原则，即尽量减少在不同表之间使用外键联系，我们总共通过MySQL建立了7个表，彼此之间没有外键联系。

questions表（即为thread构建的表）为最主要的表，包含了，每个非主键列都直接与主键（question_id）相关联，服务于大多数设计要求。

user表记录了

![image-20230512223341935](C:\Users\HUAWEI\AppData\Roaming\Typora\typora-user-images\image-20230512223341935.png)

###Part 2 项目架构结构设计

### Part 3 成果与代码片段展示

### Part 4 总结分析

