# Eureka

**Eureka是一个SpringCloud服务治理组件，管理服务之间的调用信息，类似于中介，分为服务端和客户端，服务端的角色是注册中心，客户端是微服务，分为提供者和消费者**

## 1.Eureka Server

### 1.1 引入Eureka server

pom引入依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

配置文件application.yaml

```yaml
server:
  port: 8080

eureka:
  instance:
    hostname: localhost
  client:
    #     声明是否将自己的信息注册到 Eureka 服务器上 默认true 多节点时设置为true
    registerWithEureka: false
    #     是否到 Eureka 服务器中抓取注册信息 默认true 多节点时设置为true
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/

spring:
  application:
    name: eurka-service
```

### 1.2 启动Eureka server

在SpringBoot启动类上加@EnableEurekaServer注解

```java
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }
}
```

### 1.3 高可用

通过运行多个实例并要求它们相互注册，可以使Eureka更具弹性并可以使用。多节点之间相互注册发现，并拉取对方注册信息

## 2.Eureka Client

### 2.1 引入Eureka Client

pom引入依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

配置文件application.yaml

```yaml
server:
  port: 8082

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8080/eureka
    registerWithEureka: true
    fetchRegistry: true

spring:
  application:
    name: web-service
```

### 2.2 启动Eureka Client在Eureka注册

```java
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
```

## 3.配置拓展

### 3.1 客户端配置

```
#续约发送间隔，心跳间隔（默认30秒）
eureka.instance.lease-renewal-interval-in-seconds=30
#表示eureka client间隔多久去拉取服务注册信息（默认30秒），对于api-gateway，如果要迅速获取服务注册状态，可以缩小该值，比如5秒
eureka.client.registry-fetch-interval-seconds=30
#续约到期时间（默认90秒） 90秒没有上报心跳，则被视为服务已不可用
eureka.instance.lease-expiration-duration-in-seconds=90
```

### 3.2 服务端配置


```
#关闭自我保护模式（默认开启） 一分钟之内超过85%(可配置)的客户端服务不可用，则不再清除客户端服务
eureka.server.enable-self-preservation=false
#失效服务间隔（默认60秒） 每隔60秒清除一次不可用客户端服务
eureka.server.eviction-interval-timer-in-ms=6000
```

### 3.3 自我保护模式延伸

CAP理论作为分布式系统的基础理论,它描述的是一个分布式系统在以下三个特性中：

- 一致性（**C**onsistency）
- 可用性（**A**vailability）
- 分区容错性（**P**artition tolerance）

最多满足其中的两个特性。分布式系统要么满足CA,要么CP，要么AP。无法同时满足CAP。

Eureka在CAP理论当中属于AP ， 也就说当产生网络分区时，Eureka保证系统的可用性，但不保证系统里面数据的一致性

默认情况下，Eureka Server在一定时间内，没有接收到某个微服务心跳，会将某个微服务注销（90S）。但是当网络故障时，微服务与Server之间无法正常通信，上述行为就非常危险，因为微服务正常，不应该注销。

Eureka Server通过自我保护模式来解决整个问题，当Server在短时间内丢失过多客户端时，那么Server会进入自我保护模式，会保护注册表中的微服务不被注销掉。当网络故障恢复后，退出自我保护模式。

## Eureka 健康检查

由于server和client通过心跳保持 服务状态，而只有状态为UP的服务才能被访问。看eureka界面中的status。

比如心跳一直正常，服务一直UP，但是此服务DB连不上了，无法正常提供服务。

此时可以手动设置client的状态

### 开启手动控制

在client端配置：将自己真正的健康状态传播到server。

```yaml
eureka:
  client:
    healthcheck:
      enabled: true
```

### Client端配置Actuator

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```



### 改变健康状态的Service

```java
@Service
public class HealthStatusService implements HealthIndicator{

	private Boolean status = true;

	public void setStatus(Boolean status) {
		this.status  = status;
	}

	@Override
	public Health health() {
		if(status){
			return new Health.Builder().up().build();
        }
		return new Health.Builder().down().build();
	}

	public String getStatus() {
		return this.status.toString();
	}
}
```

### 测试用的Controller

```java
public class Health{
	@GetMapping("/health")
	public String health(@RequestParam("status") Boolean status) {
		
		healthStatusSrv.setStatus(status);
		return healthStatusSrv.getStatus();
	}
}
```





















