Nacos 从 2.2.0 版本开始,可通过 SPI 机制注入多数据源实现插件,并在引入对应数据源实现后,便可在 Nacos 启动时通过读取 application.properties 配置文件中 spring.datasource.platform 配置项选择加载对应多数据源插件.

![Nacos 插件化实现
](https://minio.pigx.top/oss/202212/1671179590.jpg)

> Nacos 官方默认实现 MySQL、Derby ，其他类型数据库接入需要参考下文自己扩展。

![](https://minio.pigx.top/oss/202212/1671180565.png)

## 自定义 PostgreSQL 插件

### 1. 添加 postgresql 插件

> 依赖已上传 maven 中央仓库，请勿使用阿里云代理

```xml
<dependency>
	<groupId>com.pig4cloud.plugin</groupId>
	<artifactId>nacos-datasource-plugin-pg</artifactId>
	<version>0.0.1</version>
</dependency>

<dependency>
	<groupId>org.postgresql</groupId>
	<artifactId>postgresql</artifactId>
</dependency>
```

### 2. 导入 nacos postgresql 数据库脚本

./sql/nacos-pg.sql

### 3. 配置 nacos 数据源链接信息

```yaml
db:
  num: 1
  url:
    0: jdbc:postgresql://172.27.0.5:5432/pigxx_config
  user:
    0: postgres
  password:
    0: 123456
  pool:
    config:
      driver-class-name: org.postgresql.Driver
```

### 4. 指定 nacos 数据源平台

```yaml
spring:
  datasource:
    platform: postgresql
```
![](https://minio.pigx.vip/oss/202212/1671184577.png)

