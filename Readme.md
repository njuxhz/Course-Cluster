# 选课管理系统集群配置

## 1.数据库配置

本系统采用MySQL数据库，监听端口3306。数据库名为course，数据库表可由course-client工程下的course.sql导入，该sql文件包含了建表结构的SQL语句，定义了数据库的字符集和主键。

MySQL可以单独安装，也可以集成安装。在测试的时候，我直接采用xampp集成的MySQL（可选择性启动MySQL服务）。MySQL的用户名为root，密码为空（如数据库的用户名密码不同，需配置各项目resources目录下的application.properties。同时，由于Web项目中的Spring Batch采用了jdbcTemplate进行数据库的写操作，因此还需要在Web项目resources\batch\database.xml进行配置）。

## 2.选课管理系统集群配置

本系统的所有项目采用maven进行管理，导入项目方式可采用Intellij的maven导入。

本系统在测试时采用不同服务监听不同端口的方式模拟集群部署。实际集群部署只需将各个服务地址中的localhost改成实际IP地址即可。

整个集群由四个项目组成：
* [Eureka服务发现项目](https://github.com/njuxhz/Course-Cluster/tree/master/eureka-server)
* [学生微服务项目](https://github.com/njuxhz/Course-Cluster/tree/master/student-service)
* [成绩微服务项目](https://github.com/njuxhz/Course-Cluster/tree/master/score-service)
* [Web项目](https://github.com/njuxhz/Course-Cluster/tree/master/course-client)

各个微服务项目启动后向Eureka服务端注册（Eureka服务端地址为：http:\/\/localhost:1111，该端口可在Eureka服务发现项目resources目录下的application.properties中进行配置）。学生微服务注册在地址：http:\/\/localhost:2222，成绩微服务注册在地址：http:\/\/localhost:2223。学生和成绩微服务可注册多个实例，只要不是当前系统的使用端口即可，如2224，2225，……。微服务的注册地址配置在微服务项目resources目录下的application.properties。所有的注册服务均可以在http:\/\/localhost:1111的Eureka管理页面中呈现。

## 3.Web项目配置

由于Web项目需要启动在端口8080，因此不适合在该Web项目上创建其他端口的微服务。启动Web项目后只需访问http:\/\/localhost:8080即可显示index。除了前端的Angularjs逻辑外，基于Ribbon的负载均衡选择实现在Web项目的controller中。由于Web项目中已经向Ribbon配置了Eureka地址（resources目录下的application.properties），因此Ribbon可以基于Eureka返回的微服务地址进行选择，Web项目中配置的选择方式是随机选择（各服务的选择方式也配置在resources目录下的application.properties中）。

在Spring boot的后端，采用了Spring Batch处理上传的Excel格式文件（xls和xlsx均可）。但是需要在resources\batch\job.xml中配置跳过的行数（默认linkskip为1，即跳过开头标题行）。这里的Excel文件要求每行包含学生的学号、姓名和专业（不包括成绩，成绩在写入数据库时默认均是0。可在页面进行成绩修改）。

总成绩的计算方式为自动计算，实时显示给用户。这里的计算方式为平时成绩、大作业和期末成绩的和，保留两位小数，可在resources\static\show.html中的{{ | number: 2}}进行配置。
