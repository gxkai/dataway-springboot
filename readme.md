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
2. 第二步：配置 Dataway，并初始化数据表&nbsp;
&emsp;dataway 会提供一个界面让我们配置接口，这一点类似 Swagger 只要jar包集成就可以实现接口配置。找到我们 springboot 项目的配置文件 application.properties&nbsp;
```
# 是否启用 Dataway 功能（必选：默认false）
HASOR_DATAQL_DATAWAY=true

# 是否开启 Dataway 后台管理界面（必选：默认false）
HASOR_DATAQL_DATAWAY_ADMIN=true

# dataway  API工作路径（可选，默认：/api/）
HASOR_DATAQL_DATAWAY_API_URL=/api/

# dataway-ui 的工作路径（可选，默认：/interface-ui/）
HASOR_DATAQL_DATAWAY_UI_URL=/interface-ui/

# SQL执行器方言设置（可选，建议设置）
HASOR_DATAQL_FX_PAGE_DIALECT=mysql
```