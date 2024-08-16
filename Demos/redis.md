# Redis
## 常用命令
```redis
SET XX 123
exists XX
TYPE XX
GET XX
DEL XX
KEYS *

EXPIRE XX 5
TTL XX

select 0
flushdb
flushall
dbsize
```

## RedisTemplate 序列化
1. JdkSerializationRedisSerializer (默认序列化策略)
   > 默认序列化策略的问题：RedisTemplate入库key,value 二进制存储
   >
   >
   >        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();

2. Jackson2JsonRedisSerializer
   >         ```java
   >         RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
   >            redisTemplate.setKeySerializer(RedisSerializer.string());
   >            Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
   >            redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
   >         ```
   >
   >         存储String-Object：
   >
   >         Jackson2_redisTemplateUserObjectKey
   >
   >         {"k":"123","k1":"austin","k3":25}

3. GenericJackson2JsonRedisSerializer

   > ```
   >         RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
   >         GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
   >         redisTemplate.setKeySerializer(RedisSerializer.string());
   >         redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
   > ```
   >
   > 存储String-Object：
   >
   > genericJackson2_redisTemplateUserObjectKey
   >
   > {"@class":"com.flr.domain.User","k":"123","k1":"austin","k3":25}

GenericJackson2JsonRedisSerializer 与 Jackson2JsonRedisSerializer 区别

> GenericJackson2JsonRedisSerializer 序列化时存储实例类信息，取值时强转对象不会有误



```java

```

发布订阅
消息队列
分布式锁
