项目简介，觉得有用可以star，fork一下，感谢！

## 自定义简单 Springboot

### custom-spring

#### common

基础工具类，如JsonUtil，RefectUtil等

```xml
<!--maven依赖-->
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
</dependency>
```

#### orm

自定义数据库表相关注解，如Table，Column，Id，Select等

基本的Mapper接口，如BaseMapper<T>。

枚举类，如IdType。

代理类，如AnalyticMethod，MapperProxyFactory。

配置类，如SqlConfig，SqlSession。

数据库工具类

```xml
<!--maven依赖-->
<dependency>
    <groupId>org.answer.wx</groupId>
    <artifactId>common</artifactId>
    <version>${project.parent.version}</version>
</dependency>
<dependency>
    <groupId>cglib</groupId>
    <artifactId>cglib</artifactId>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
```

#### spring

自定义spring相关注解，如Aspect，Before，Autowired，Bean，Component，Configuration等核心注解。

自定义Bean工厂，如BeanFactory，AbstractActionFactory。

自定义spring上下文，如ApplicationContext，ActionApplicationContext，AnnotationConfigApplicationContext等。

自定义处理器，如HandlerMapping，HandlerMethod等。

自定义数据库处理，如OrmConfigure，SqlSessionFactory等

自定义事务管理，如Transactional，TransactionProxy等

自定义web功能，如RequestMapping，Controller注解，DispatchServlet分发器等

```xml
<!--maven依赖-->
<dependency>
    <groupId>org.apache.tomcat.embed</groupId>
    <artifactId>tomcat-embed-core</artifactId>
</dependency>
<dependency>
    <groupId>org.answer.wx</groupId>
    <artifactId>common</artifactId>
    <version> ${project.parent.version}</version>
</dependency>
<dependency>
    <groupId>org.answer.wx</groupId>
    <artifactId>orm</artifactId>
    <version> ${project.parent.version}</version>
</dependency>
```

#### spring-boot

服务容器配置，如ServerConfig，TomcatServer等

spring项目入口，如SpringApplication

```xml
<!--maven依赖-->
<dependency>
    <groupId>org.answer.wx</groupId>
    <artifactId>spring</artifactId>
    <version> ${project.parent.version}</version>
</dependency>
```

### 父工程pom文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.answer.wx</groupId>
    <artifactId>custom-spring</artifactId>
    <version>1.0.2-SNAPSHOT</version>
    <modules>
        <module>orm</module>
        <module>common</module>
        <module>spring</module>
        <module>springboot</module>
    </modules>
    <packaging>pom</packaging>
    <description>手写springboot+orm</description>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>

    <dependencyManagement>
        <dependencies>
            <!--内置tomcat容器-->
            <dependency>
                <groupId>org.apache.tomcat.embed</groupId>
                <artifactId>tomcat-embed-core</artifactId>
                <version>8.5.69</version>
            </dependency>

            <!--cglib反射-->
            <dependency>
                <groupId>cglib</groupId>
                <artifactId>cglib</artifactId>
                <version>3.2.9</version>
            </dependency>

            <!--mysql连接-->
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>8.0.25</version>
            </dependency>

            <!--日志-->
            <dependency>
                <groupId>log4j</groupId>
                <artifactId>log4j</artifactId>
                <version>1.2.14</version>
            </dependency>
        </dependencies>
    </dependencyManagement>

</project>
```

# 新建springboot项目

## custom-project

#### pom.xml

导入custom-spring中打包的springboot

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>custom-project</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.answer.wx</groupId>
            <artifactId>springboot</artifactId>
            <version>1.0.2-SNAPSHOT</version>
        </dependency>
    </dependencies>
</project>
```

#### asapect

测试切面

#### controller

测试增删改查

#### entity

数据表映射实体

#### mapper

数据操作mapper

#### service

测试服务类

#### MyApplication

程序入口

#### 测试图片

<img width="1295" alt="image" src="https://user-images.githubusercontent.com/97394220/201307842-08dc8225-82af-4611-8047-9d4c2da47624.png">
<img width="1139" alt="image" src="https://user-images.githubusercontent.com/97394220/201307993-174ab02b-ab8c-4282-b0b9-7201f964bada.png">
<img width="1131" alt="image" src="https://user-images.githubusercontent.com/97394220/201308041-472e6de7-a8a7-46d7-ba22-6200455c40cb.png">
