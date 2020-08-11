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
    dataway 会提供一个界面让我们配置接口，这一点类似 Swagger 只要jar包集成就可以实现接口配置。找到我们 springboot 项目的配置文件 application.properties&nbsp;
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
Dataway 需要两个数据表才能工作，下面是这两个数据表的简表语句。
```
    CREATE TABLE `interface_info` (
        `api_id`          int(11)      NOT NULL AUTO_INCREMENT   COMMENT 'ID',
        `api_method`      varchar(12)  NOT NULL                  COMMENT 'HttpMethod：GET、PUT、POST',
        `api_path`        varchar(512) NOT NULL                  COMMENT '拦截路径',
        `api_status`      int(2)       NOT NULL                  COMMENT '状态：0草稿，1发布，2有变更，3禁用',
        `api_comment`     varchar(255)     NULL                  COMMENT '注释',
        `api_type`        varchar(24)  NOT NULL                  COMMENT '脚本类型：SQL、DataQL',
        `api_script`      mediumtext   NOT NULL                  COMMENT '查询脚本：xxxxxxx',
        `api_schema`      mediumtext       NULL                  COMMENT '接口的请求/响应数据结构',
        `api_sample`      mediumtext       NULL                  COMMENT '请求/响应/请求头样本数据',
        `api_create_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `api_gmt_time`    datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
        PRIMARY KEY (`api_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COMMENT='Dataway 中的API';
    
    CREATE TABLE `interface_release` (
        `pub_id`          int(11)      NOT NULL AUTO_INCREMENT   COMMENT 'Publish ID',
        `pub_api_id`      int(11)      NOT NULL                  COMMENT '所属API ID',
        `pub_method`      varchar(12)  NOT NULL                  COMMENT 'HttpMethod：GET、PUT、POST',
        `pub_path`        varchar(512) NOT NULL                  COMMENT '拦截路径',
        `pub_status`      int(2)       NOT NULL                  COMMENT '状态：0有效，1无效（可能被下线）',
        `pub_type`        varchar(24)  NOT NULL                  COMMENT '脚本类型：SQL、DataQL',
        `pub_script`      mediumtext   NOT NULL                  COMMENT '查询脚本：xxxxxxx',
        `pub_script_ori`  mediumtext   NOT NULL                  COMMENT '原始查询脚本，仅当类型为SQL时不同',
        `pub_schema`      mediumtext       NULL                  COMMENT '接口的请求/响应数据结构',
        `pub_sample`      mediumtext       NULL                  COMMENT '请求/响应/请求头样本数据',
        `pub_release_time`datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间（下线不更新）',
        PRIMARY KEY (`pub_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COMMENT='Dataway API 发布历史。';
    
    create index idx_interface_release on interface_release (pub_api_id);
```
3. 第三步：配置数据源  
```
    # db
    spring.datasource.url=jdbc:mysql://localhost:3306/dataway?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    spring.datasource.username=root
    spring.datasource.password=root
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
    # druid
    spring.datasource.druid.name=datasource
    spring.datasource.druid.initial-size=3
    spring.datasource.druid.min-idle=3
    spring.datasource.druid.max-active=10
    spring.datasource.druid.max-wait=60000
    spring.datasource.druid.time-between-eviction-runs-millis=60000
    spring.datasource.druid.min-evictable-idle-time-millis=300000
    spring.datasource.druid.validationQuery=SELECT 1
    
    spring.datasource.druid.test-while-idle=true
    spring.datasource.druid.test-on-borrow=false
    spring.datasource.druid.test-on-return=false
    
    
    spring.datasource.druid.pool-prepared-statements=true
    spring.datasource.druid.max-pool-prepared-statement-per-connection-size=20
    spring.datasource.druid.filters=stat,wall,slf4j
    spring.datasource.druid.connection-properties=druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
    spring.datasource.druid.use-global-data-source-stat=true
    
    spring.datasource.druid.stat-view-servlet.login-username=root
    spring.datasource.druid.stat-view-servlet.login-password=root
    spring.datasource.druid.filter.stat.log-slow-sql=true
    spring.datasource.druid.filter.stat.slow-sql-millis=5000
    spring.datasource.druid.web-stat-filter.enabled=true
    spring.datasource.druid.web-stat-filter.exclusions='*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*'
```
4. 第四步：把数据源设置到 Hasor 容器中  
```
    package com.dataway.cn.config;
    
    import net.hasor.core.ApiBinder;
    import net.hasor.core.DimModule;
    import net.hasor.db.JdbcModule;
    import net.hasor.db.Level;
    import net.hasor.spring.SpringModule;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Component;
    
    import javax.sql.DataSource;
    
    /**
     * dataWay自动生成API配置
     * @author phil
     * @date 2020/05/18 11:42
     */
    @DimModule
    @Component
    public class ExampleModule implements SpringModule {
        @Autowired
        private DataSource dataSource = null;
    
        @Override
        public void loadModule(ApiBinder apiBinder) throws Throwable {
            // .DataSource form Spring boot into Hasor
            apiBinder.installModule(new JdbcModule(Level.Full, this.dataSource));
        }
    }
```
5. 第五步：在SprintBoot 中启用 Hasor  
```
@EnableHasor()
@EnableHasorWeb()
@SpringBootApplication(scanBasePackages = { "net.example.hasor" })
public class DatawayApplication {
    public static void main(String[] args) {
        SpringApplication.run(DatawayApplication.class, args);
    }
}
```
6. 第六步：启动应用（看到欢迎信息就算启动成功）
```
_    _                        ____              _
| |  | |                      |  _ \            | |
| |__| | __ _ ___  ___  _ __  | |_) | ___   ___ | |_
|  __  |/ _` / __|/ _ \| '__| |  _ < / _ \ / _ \| __|
| |  | | (_| \__ \ (_) | |    | |_) | (_) | (_) | |_
|_|  |_|\__,_|___/\___/|_|    |____/ \___/ \___/ \__|
```
7. 第七步：访问接口管理页面进行接口配置  
```
    http://localhost:8888/api/v1/interface-ui/
```
