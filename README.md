# Nacos PostgreSQL 数据源插件

[![Maven Central](https://img.shields.io/maven-central/v/com.pig4cloud.plugin/nacos-datasource-plugin-pg.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/com.pig4cloud.plugin/nacos-datasource-plugin-pg/)

Nacos 从 2.2.0 版本开始，可通过 SPI 机制注入多数据源实现插件。引入对应数据源实现后，可在 Nacos 启动时通过读取 `application.properties` 配置文件中 `spring.datasource.platform` 配置项选择加载对应多数据源插件。

> 注意：Nacos 官方默认实现 MySQL、Derby，其他类型数据库接入需要参考文档自行扩展。

## 版本对应关系

| Nacos 版本      | 插件版本  |
|---------------|-------|
| 2.2.0 - 2.3.0 | 0.0.2 |
| 2.3.1 - 2.3.2 | 0.0.3 |
| 2.4.0 - 2.4.3 | 0.0.4 |
| 2.5.0 -       | 0.0.5 |
| 3.0.0 -       | 0.0.6 |
| 3.0.1.0 -     | 0.0.7 |

## 使用方法

### 1. 添加依赖

> 注意：依赖已上传 Maven 中央仓库，请勿使用阿里云代理

```xml
<dependency>
    <groupId>com.pig4cloud.plugin</groupId>
    <artifactId>nacos-datasource-plugin-postgresql</artifactId>
    <version>${nacos.plugin.version}</version>
</dependency>

<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
```

### 2. 初始化数据库

执行 SQL 脚本初始化数据库表结构：./sql/nacos-pg.sql

### 3. 配置数据源

在 Nacos 的配置文件中添加以下配置：

```yaml
db:
  num: 1
  url:
    0: jdbc:postgresql://127.0.0.1:5432/postgres
  user: postgres
  password: postgres
  pool:
    config:
      driver-class-name: org.postgresql.Driver
```

### 4. 指定数据源平台

在 Nacos 的配置文件中设置数据源平台为 PostgreSQL：

```yaml
spring:
  datasource:
    platform: postgresql
```

## 贡献指南

欢迎提交 Issue 或 Pull Request 来帮助改进这个项目。

## 许可证

本项目采用 [Apache 2.0 许可证](LICENSE)。

![Nacos 插件化实现
](https://minio.pigx.top/oss/202212/1671179590.jpg)

![](https://minio.pigx.top/oss/202212/1671180565.png)
