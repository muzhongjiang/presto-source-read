trino-370

## Development

See [DEVELOPMENT](.github/DEVELOPMENT.md) for information about code style,
development process, and guidelines.

See [CONTRIBUTING](.github/CONTRIBUTING.md) for contribution requirements.

## Security

See the project [security policy](.github/SECURITY.md) for
information about reporting vulnerabilities.

## Build requirements

* Mac OS X or Linux
* Java 11.0.11+, 64-bit
* Docker

## Building Trino

1、启动docker（删除docker模块之后就不需要启动docker了）
2、执行build命令：
```shell
export JAVA_HOME=/Users/muzhongjiang/software/jdk-11.0.12.jdk/Contents/Home
./mvnw clean install -DskipTests  -X
```

Trino 有一套全面的测试，这些测试需要相当长的时间才能运行，因此被上述命令禁用。 
当您提交拉取请求时，这些测试由 CI 系统运行。 我们建议仅对您更改的代码区域在本地运行测试。

## 在 IntelliJ IDEA 中启动Trino
参考development文档。。。

### Overview

* Open the File menu and select Project Structure
* In the SDKs section, ensure that JDK 11 is selected (create one if none exist)
* In the Project section, ensure the Project language level is set to 11

### 运行测试服务器   
运行 Trino 进行开发的最简单方法是运行 `TpchQueryRunner` 类。 它将启动配置了 TPCH 连接器的服务器的开发版本。 
然后，您可以使用 CLI 对该服务器执行查询。 许多其他连接器都有自己的 `*QueryRunner` 类，您可以在处理特定连接器时使用它们。



### 运行完整的服务器   
`io.trino.server.DevelopmentServer`
Trino 附带了示例配置，可以开箱即用地进行开发。 使用以下选项创建运行配置：

* Main Class: `io.trino.server.DevelopmentServer`
* VM Options: `-ea -Dconfig=etc/config.properties -Dlog.levels-file=etc/log.properties -Djdk.attach.allowAttachSelf=true`
* Working directory: `/Users/muzhongjiang/storage/git/github/Presto/presto-source-read/testing/trino-server-dev`
* Use classpath of module: `trino-server-dev`

The working directory should be the `trino-server-dev` subdirectory. In
IntelliJ, using `$MODULE_DIR$` accomplishes this automatically.


### 运行 CLI

方式1： 
    `client/trino-cli/target/trino-cli-*-executable.jar`
方式2：
    `io.trino.cli.Trino`

执行一个查询：
   ` SELECT * FROM system.runtime.nodes;`


### 查询mysql  
1、配置catalog：
```properties
connector.name=mysql
connection-url=jdbc:mysql://tencent:3306
connection-user=root
connection-password=root&password@168
```
2、执行 `SHOW TABLES FROM mysql.test;`



