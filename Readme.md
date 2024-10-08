# # DreamLife
SpringBoot 3 + MyBatis + Thymeleaf = DreamLife 公共博客网站

## 环境依赖
1. 操作系统：不依赖于具体操作系统。
2. 软件：
    - VScode 或 IDEA2024 等已配置的集成开发环境
    - SpringBoot 3.2.6
    - Java 17
    - MyBatis 3.0.3
    - MySQL 8.0.26
    - Maven 3.8.1
    - 其余项目配置无需个人配置，由项目自动配置


## 配置

<!-- - 使用本地配置

```bash
mvn spring-boot:run -Dspring.profiles.active=local
```

- 使用服务器配置

```bash
mvn spring-boot:run -Dspring.profiles.active=prod
``` -->

环境变量

```bash
export DB_URL=jdbc:mysql://localhost:3306/<你的库名>
export DB_USERNAME=<你的用户名>
export DB_PASSWORD=<你的密码>
export MAIL_USERNAME=<你的邮箱>
export MAIL_PASSWORD=<你的邮箱密码>
```

注意，在配置本项目中，请优先检查 java 版本，很多windows中java为1.8，配置请参考 https://blog.csdn.net/ZhangXS9722/article/details/139598018

## 主要参考项目
[一个个人博客系统](https://gitee.com/laozibabac/myblog)

## 开发者
[laozibabac](https://gitee.com/laozibabac)
[duanyao](https://gitee.com/duan-yao)
[nnyilun](https://gitee.com/nnyilun)

## 特别感谢
JetBrains