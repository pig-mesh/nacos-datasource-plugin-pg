# Nacos PostgreSQL 数据源插件

[![Maven Central](https://img.shields.io/maven-central/v/com.pig4cloud.plugin/nacos-datasource-plugin-postgresql.svg?style=flat-square)](https://maven.badges.herokuapp.com/maven-central/com.pig4cloud.plugin/nacos-datasource-plugin-postgresql)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## 项目介绍

本插件为 Nacos（2.2.0 版本及以上）提供 PostgreSQL 数据库的数据源支持。通过 SPI 机制实现，您只需在 `application.properties` 配置文件中修改 `spring.datasource.platform` 属性即可启用 PostgreSQL 数据库。

> Nacos 官方默认支持 MySQL 和 Derby 数据库，本插件扩展了对 PostgreSQL 数据库的支持。

## 版本兼容性

| Nacos 版本        | 插件版本  |
|-----------------|-------|
| 2.2.0 - 2.3.0   | 0.0.2 |
| 2.3.1 - 2.3.2   | 0.0.3 |
| 2.4.0 - 2.4.3   | 0.0.4 |
| 2.5.0 -         | 0.0.5 |
| 3.0.0 -         | 0.0.6 |
| 3.0.1.0 -       | 0.0.7 |

## 快速开始

### 1. 添加依赖

在项目的 `pom.xml` 中添加以下依赖（注意：依赖已上传至 Maven 中央仓库，请勿使用阿里云代理）：

```xml
<dependency>
    <groupId>com.pig4cloud.plugin</groupId>
    <artifactId>nacos-datasource-plugin-postgresql</artifactId>
    <version>${plugin.version}</version>
</dependency>

<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
```

### 2. 执行 SQL 脚本初始化

执行 SQL 脚本初始化数据库表结构，脚本位置：`./sql/nacos-pg.sql`


### 3. 配置数据源

在 Nacos 配置文件中添加以下配置：

```properties
spring.sql.init.platform=postgresql
db.num=1
db.url.0=jdbc:postgresql://127.0.0.1:5432/postgres
db.user=postgres
db.password=postgres
db.pool.config.driver-class-name=org.postgresql.Driver
```

## 参与贡献

我们欢迎所有形式的贡献，如果您有任何改进建议或功能扩展，请提交 Pull Request。

## 开源协议

本项目采用 Apache License 2.0 开源协议 - 详情请参见 [LICENSE](LICENSE) 文件。

![Nacos 插件化实现](https://minio.pigx.top/oss/202212/1671179590.jpg)

![](https://minio.pigx.top/oss/202212/1671180565.png)
