#开发文档-Java

## 前言

此文档为浙江省妇幼门诊医生站产品开发中关于Java开发的指导手册,内容包括开发环境搭建指南,业务模块开发指南,望参与的开发者同事仔细阅读该手册,充分了解项目整体情况和开发注意事项

## 一、开发环境搭建
* 基础开发工具
    - *JDK1.8*
    - *IDEA2018及以上版本(推荐)*
    - *Tomcat8* 
* 基础开发框架
    - [BBP4系列框架wiki地址](https://wiki.bsoft.work/display/SSDEV/BBP)
    - [手把手视频教学](https://wiki.bsoft.work/pages/viewpage.action?pageId=23691771)
    - [RPC服务开发及调试](https://wiki.bsoft.work/pages/viewpage.action?pageId=35225660)
* 代码仓库地址    
    
* 数据库地址

## 二、模块开发指南
* 包名说明
   - 浙大妇院项目在src/main/java/com/bsoft/ctms目录下,以下为几个重要目录的说明
       - **service** 业务功能实现
           - *xxxService.java* rpc服务入口,需在spring-service.xml中通过以下方式定义
           ```xml
           <service class="com.bsoft.ctms.service.diagnosis.DiagnosisService" id="diagnosisService"/>
            ```
            注册为rpc的方法,需要在方法上设置注解@RpcService,调用xxxModule实现业务功能,Module对象通过@Autowired注入
           - *xxxModule.java* 业务实现类,完成业务逻辑,第三方接口调用及数据持久化等操作
           - *pojo* 数据库表对应的实体类
           - *dao* 提供实体的CURD操作
       - **util** 通用工具类(业务无关)
       - **func** 核心业务功能
       - **config** 配置相关
       - **outside** 调用外部系统接口HttpClient/WebService
       - **inside**  提供对外调用接口
       - **common** 公共服务
       
* 数据库事务  
    rpc Service中,事务可通过两种方式启用,事务启用后,方法成功运行提交事务,抛出任何异常时回滚事务
    - 自动事务  
         rpc方法名称以save/update/delete/remove开头的,会自动启用事务
    - 手动事务
         通过在rpc方法上设置注解@Transactional手动开启事务  
         
   注意:事务只能作用在service的rpc方法上,除非特别需要,否则不允许在module层处理事务
   
* 快速开发助手  
  考虑项目中引入了mybatis作为持久层框架,为更好的简化开发,
  额外引入了mybatis plus,结合idea插件quickStart plus,可方便快捷的完成持久层开发,自动生成实体和dao类  
  示例:以MS_CF01表为例,自动生成到ctms/service/business下的pojo和dao目录,pojo类名为MsCf01,dao名称为MsCf01Dao
```java
/**
 * 门诊_门诊处方表(MsCf01)实体类
 *
 * @author YangLi
 * @since 2020-05-04 08:39:03
 */
@Getter
@Setter
@NoArgsConstructor
@TableName("MS_CF01")
public class MsCf01 {
    /**
     * 处方识别 | 通过该字段和MS_CF02关联
     */
    @TableId(value = "CFSB", type = IdType.INPUT)
    private Long cfsb;
    /**
     * 机构ID
     */
    @TableField("JGID")
    private String jgid;
    
    
}
```
```java
public interface MsCf01Dao extends BaseMapper<MsCf01> {

}
```
* 主键生成策略  
  由于省妇保项目是按业务逐步替换原有的CS版本的HIS系统,所以数据库会于CS系统共同使用,所以主键生成策略需和CS系统保持一致,
  在mybatis plus基础上,自定义主键生成器CtmsIdGenerator,根据pojo类名对应的表名称自动获取主键,同时也可以通过设置主键内存缓存提升主键获取性能
  ```java
  /**
     * 省妇保项目主键生成器
     * 规则:根据表名的前两位或者第一个下划线前的值作为前缀prefix,
     * 如果prefix值为GY,查询GY_IDENTITY表,否则查询GY_IDENTITY_${prefix}表
     */
  public class CtmsIdGenerator implements IdentifierGenerator {
      /**
       * 内存缓存数量
       */
      private static final int CACHE_COUNT = 20;
  
      /**
       * 实现内存高速缓存
       */
      private static final Map<String, Object> cacheKeys = new ConcurrentHashMap<>();
  
      
    }
  ```
  
* 分页查询  
  **[重要]** 网页渲染速度受页面数据量的影响较大,故列表类的数据展示,除非显示内容可预测,不会随业务运行持续增加,否则都
  需要采用分页形式!  
  - 后端分页实现示例
  ```java
  public class Service{
      /**
         * 查询疾病信息(带分页)
         *
         * @param page   分页参数 {start: 1, limit: 20}
         * @param params 查询条件
         * @return 分页返回对象见
         */
        @RpcService
        public Result queryPageDisease(Page page, Map<String, Object> params) {
            // 1.设置分页信息
            PageHelper.startPage(page.getStart(), page.getLimit());
            // 2.调用业务查询
            List<GyJbbm> list = exampleModule.queryDisease(params);
            // 3.生成分页对象,返回前端
            PageInfo<GyJbbm> pageInfo = new PageInfo<>(list);
            return new Result<>(pageInfo);
        }
      
      /**
        * 查询疾病信息(不带分页)
        *
        * @param params 查询条件
        * @return Result
        */
        @RpcService
        public Result queryDisease(Map<String, Object> params) {
          List<GyJbbm> list = exampleModule.queryDisease(params);
          return new Result<>(list);
        }
  }
  ```
  **注意事项*  
    * RpcService不支持参数重载,即不能定义两个方法名称一样,但是参数类型或者个数不一样的Rpc方法
    * 方法是否使用分页,需要在开发态就确定好
* 系统级信息
  - 机构科室用户
  ```java
  // 后端获取示例
  ```
  - 系统参数
  ```java
  // 后端获取示例
  
  ```
   - 字典获取
   字典大类定义在Dic.java文件中,命名规则为ctms.dmlb.{dmlb},其中ctms.dmlb.为固定前缀,标识从GY_DMZD获取;  
   字典前后端使用方式同框架字典
  ```java
  public class Util{
      // 后端使用示例
      Dictionary dic = DictionaryController.instance().get(Dic.REG_TIME_CONTROL);
      String text = dic.getText(key);
  }
  ```
    
* 内存缓存使用注意事项  
  内存缓存建议继承框架基础类AbstractController或者通过google guava提供的cache类实现,同时缓存需要考虑集群环境下的同步问题  
  [框架缓存管理](https://wiki.bsoft.work/pages/viewpage.action?pageId=35225668)
