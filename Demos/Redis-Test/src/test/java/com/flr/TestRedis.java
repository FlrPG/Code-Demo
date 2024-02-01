package com.flr;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.junit.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

//@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestRedis.class)
@SpringBootApplication
public class TestRedis {
    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void redisTemplateSerializeTest() {
        String redisTemplateStringKey = "redisTemplateStringKey";
        String redisTemplateUserObjectKey = "redisTemplateUserObjectKey";
        String redisTemplateUserArrayObjectKey = "redisTemplateUserArrayObjectKey";
        String redisTemplateJSONObjectKey = "redisTemplateJSONObjectKey";
        String redisTemplateJSONArrayKey = "redisTemplateJSONArrayKey";

        //序列化String类型和反序列化String类型
        redisTemplate.opsForValue().set(redisTemplateStringKey, "austin");
        String austin = (String) redisTemplate.opsForValue().get(redisTemplateStringKey);
        System.out.println("stringGet: " + austin);

        //序列化Object对象类型和反序列化Object对象类型 (User对象)
        User user = new User("123", "austin", 25);
        redisTemplate.opsForValue().set(redisTemplateUserObjectKey, user);
        User userGet = (User) redisTemplate.opsForValue().get(redisTemplateUserObjectKey);
        System.out.println("userGet: " + userGet);

        //序列化Object对象数组类型和反序列化Object对象数组类型 (User[]对象数组)
        User user1 = new User("1", "austin1", 25);
        User user2 = new User("2", "austin2", 25);
        User[] userArray = new User[]{user1, user2};
        redisTemplate.opsForValue().set(redisTemplateUserArrayObjectKey, userArray);
        User[] userArrayGet = (User[]) redisTemplate.opsForValue().get(redisTemplateUserArrayObjectKey);
        System.out.println("userArrayGet: " + userArrayGet);

        //序列化JSONObject对象类型和反序列化JSONObject对象类型
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "123");
        jsonObject.put("name", "austin");
        jsonObject.put("age", 25);
        redisTemplate.opsForValue().set(redisTemplateJSONObjectKey, jsonObject);
        JSONObject jsonObjectGet = (JSONObject) redisTemplate.opsForValue().get(redisTemplateJSONObjectKey);
        System.out.println("jsonObjectGet: " + jsonObjectGet);

        //序列化JSONArray对象类型和反序列化JSONArray对象类型
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("id", "1");
        jsonObject1.put("name", "austin1");
        jsonObject1.put("age", 25);
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("id", "1");
        jsonObject2.put("name", "austin2");
        jsonObject2.put("age", 25);
        jsonArray.add(jsonObject1);
        jsonArray.add(jsonObject2);
        redisTemplate.opsForValue().set(redisTemplateJSONArrayKey, jsonArray);
        JSONArray jsonArrayGet = (JSONArray) redisTemplate.opsForValue().get(redisTemplateJSONArrayKey);
        System.out.println("jsonArrayGet: " + jsonArrayGet);
    }


}
