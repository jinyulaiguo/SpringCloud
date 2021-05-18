# Ribbon
Ribbon是Netflix发布的开源项目，提供在服务消费端实现负载均衡调用服务提供者，从注册中心读取所有可用的服务提供者，在客户端每次调用接口时采用如轮询负载均衡算法选出一个服务提供者调用，因此，Ribbon是一个**客户端负载均衡器**。

## 一. 解决了什么问题？
从服务的消费者侧实现了负载均衡的能力。
**负载均衡**:一种技术解决方案。用来在多个资源（一般是服务器）中分配负载，达到最优化资源使用，避免过载。用来解决互联网分布式系统的大流量、高并发和高可用的问题。

## 二. 怎么解决这种问题？

### 1. 使用RestTemplate实现服务之间的调用
较为繁琐，了解即可，后面是使用feign来统一实现服务之间的调用

### 2. 提供了多种负载均衡策略。
| 策略类 |　　	命名|	描述 |
|----|----|----|
|RandomRule|	随机策略|	随机选择server
|RoundRobinRule|	轮询策略|	轮询选择， 轮询index，选择index对应位置的Server（default）
|RetryRule|	重试策略|	对选定的负载均衡策略机上重试机制，在一个配置时间段内当选择Server不成功，则一直尝试使用subRule的方式选择一个可用的server；
|BestAvailableRule|	最低并发策略|	逐个考察server，如果server断路器打开，则忽略，再选择其中并发链接最低的server
|AvailabilityFilteringRule|	可用过滤策略|	过滤掉一直失败并被标记为circuit tripped的server，过滤掉那些高并发链接的server（active connections超过配置的阈值）或者使用一个AvailabilityPredicate来包含过滤server的逻辑，其实就就是检查status里记录的各个Server的运行状态；
|ResponseTimeWeightedRule|	响应时间加权重策略|	根据server的响应时间分配权重，响应时间越长，权重越低，被选择到的概率也就越低。响应时间越短，权重越高，被选中的概率越高，这个策略很贴切，综合了各种因素，比如：网络，磁盘，io等，都直接影响响应时间
|ZoneAvoidanceRule|	区域权重策略|	综合判断server所在区域的性能，和server的可用性，轮询选择server并且判断一个AWS Zone的运行性能是否可用，剔除不可用的Zone中的所有server

### 3. 可以针对某一个服务配置不同的负载均衡策略。
   ```yaml
  server-provider: #微服务id
    ribbon:
      NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule #随机策略
   ```
  **全局设置优先级高于application.yml设置**

## 二. 如何使用？
我们这里不需要添加Ribbon的依赖，因为Eureka Client包里面帮我们引入Ribbon相关的依赖。

###1. 项目结构：
   * provider:
       * server-provider01  端口9001
       * server-provider02  端口9002
   * consumer
       * server-consumer    端口9000
    
###2. 使用RestTemplate实现服务之间的调用
 调用server-consumer/template/getName进行测试。非重点，主要要是RestTemplate的简单使用

###3. 负载均衡策略
 调用server-consumer/product/getName。期望结果：打印的IP与配置的策略相同

