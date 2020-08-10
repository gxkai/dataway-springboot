# 项目备注
## 一、dataWay的使用
1. 第一步：引入相关依赖
```
    <!--dataWay-->
    <dependency>
        <groupId>net.hasor</groupId>
        <artifactId>hasor-spring</artifactId>
        <version>${net-hasor.version}</version>
    </dependency>
    <dependency>
        <groupId>net.hasor</groupId>
        <artifactId>hasor-dataway</artifactId>
        <version>${net-hasor.version}</version>
    </dependency>
```
2. 第二步：配置 Dataway，并初始化数据表
    dataway 会提供一个界面让我们配置接口，这一点类似 Swagger 只要jar包集成就可以实现接口配置。找到我们 springboot 项目的配置文件 application.properties