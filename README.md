
# middleware-message

一个演示与对比常见消息中间件（RocketMQ / RabbitMQ / Kafka / Pulsar 等）集成示例与使用指南的多模块示例工程。

目录结构与要点概览

- `doc/`：各中间件的说明文档与选型、常见问题（如 `1.RocketMQ.md`、`2.RabbitMQ.md`、`3.Kafka.md`、`4.Apache Pulasar.md`、`常见消息中间件选型.md` 等）。
- `rocketmq/`, `rabbitmq/`, `kafka/`, `pulsar/`：每个中间件对应的独立 Spring Boot 示例模块，包含 producer/consumer、配置类与 `docker-compose.yml`（可用于快速启动中间件环境）。

模块快速索引（示例）

- RocketMQ
	- 启动类：`rocketmq/src/main/java/com/std/message/rocketmq/RocketMQApplication.java`
	- 生产者：`rocketmq/src/main/java/com/std/message/rocketmq/producer/RocketMQProducer.java`
	- 消费者：`rocketmq/src/main/java/com/std/message/rocketmq/consumer/RocketMQConsumer.java`
	- Docker Compose：`rocketmq/docker-compose.yml`，配置文件：`rocketmq/broker.conf`
- RabbitMQ
	- 启动类：`rabbitmq/src/main/java/com/std/message/rabbitmq/RabbitMQApplication.java`
	- 配置：`rabbitmq/src/main/java/com/std/message/rabbitmq/config/RabbitMQConfig.java`
	- Docker Compose：`rabbitmq/docker-compose.yml`
- Kafka
	- 启动类及示例：`kafka/src/main/java/com/std/message/kafka/*`
	- Docker Compose：`kafka/docker-compose.yml`
- Pulsar
	- 启动类：`pulsar/src/main/java/com/std/message/pulsar/PulsarApplication.java`
	- 配置：`pulsar/src/main/java/com/std/message/pulsar/config/PulsarConfig.java`
	- Docker Compose：`pulsar/docker-compose.yml`

快速开始（本地开发）

前提：已安装 JDK、Maven、Docker 与 Docker Compose。

1) 在项目根构建（跳过测试以加快速度）：

```bash
mvn -T1C -DskipTests package
```

2) 启动示例中间件（示例：RocketMQ）

```bash
# 在项目根目录运行
docker-compose -f rocketmq/docker-compose.yml up -d
```

其他模块同理：

```bash
docker-compose -f rabbitmq/docker-compose.yml up -d
docker-compose -f kafka/docker-compose.yml up -d
docker-compose -f pulsar/docker-compose.yml up -d
```

3) 运行示例应用（以 RocketMQ 模块为例）

```bash
# 使用 Maven 在模块中直接运行（会自动构建依赖模块）
mvn -pl rocketmq -am spring-boot:run

# 或运行已打包的 Jar（当你已执行 package）
java -jar rocketmq/target/*.jar
```

4) 查看日志与测试

- 各模块下的 `src/test/java/.../ApiTest.java` 包含简单的示例/集成测试（可作为调用示例）。
- 日志输出会显示在控制台，生产者/消费者消息发送与接收可通过日志验证。

配置提示

- 各模块的连接配置通常在 `src/main/resources/application.yml` 中，请根据自己本地或容器的 broker 地址修改（例如 `spring.rabbitmq.host`、`spring.kafka.bootstrap-servers`、`pulsar.serviceUrl`、`rocketmq.name-server` 等）。
- 生产环境请注意：消费者幂等性、消息重试策略、死信队列与监控告警。

贡献与许可

- 欢迎提交 issue 与 PR。该仓库采用 Maven 多模块管理，遵循标准的 Spring Boot 项目结构。
- 若需添加新的中间件示例，请在仓库根下增加新的模块目录并添加相应的 `docker-compose.yml` 与示例代码。

附：文档位置

- 详细使用说明与常见问题：`doc/` 目录下的各 Markdown 文件（参见 `doc/1.RocketMQ.md`、`doc/常见消息中间件选型.md` 等）。

----
生成/更新于：2025-10-31

