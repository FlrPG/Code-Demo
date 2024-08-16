package com.flr;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.flr.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;


@SpringBootTest(classes = RedisApplication.class)
public class RedisSerializableTest {
    @Resource(name = "DefaultRedisTemplate")
    private RedisTemplate<Object, Object> redisTemplate;

    @Resource(name = "DefaultStrRedisTemplate")
    private RedisTemplate<String, Object> defaultStrRedisTemplate;

    @Resource(name = "Jackson2RedisTemplate")
    private RedisTemplate<String, Object> jackson2RedisTemplate;

    @Resource(name = "GenericJackson2RedisTemplate")
    private RedisTemplate<String, Object> genericJackson2RedisTemplate;

    @Test
    public void testDefault() {
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

    @Test
    public void testStrDefault() {
        String redisTemplateStringKey = "defaultStr_redisTemplateStringKey";
        String redisTemplateUserObjectKey = "defaultStr_redisTemplateUserObjectKey";
        String redisTemplateUserArrayObjectKey = "defaultStr_redisTemplateUserArrayObjectKey";
        String redisTemplateJSONObjectKey = "defaultStr_redisTemplateJSONObjectKey";
        String redisTemplateJSONArrayKey = "defaultStr_redisTemplateJSONArrayKey";

        //序列化String类型和反序列化String类型
        defaultStrRedisTemplate.opsForValue().set(redisTemplateStringKey, "austin");
        String austin = (String) defaultStrRedisTemplate.opsForValue().get(redisTemplateStringKey);
        System.out.println("stringGet: " + austin);

        //序列化Object对象类型和反序列化Object对象类型 (User对象)
        User user = new User("123", "austin", 25);
        defaultStrRedisTemplate.opsForValue().set(redisTemplateUserObjectKey, user);
        User userGet = (User) defaultStrRedisTemplate.opsForValue().get(redisTemplateUserObjectKey);
        System.out.println("userGet: " + userGet);

        //序列化Object对象数组类型和反序列化Object对象数组类型 (User[]对象数组)
        User user1 = new User("1", "austin1", 25);
        User user2 = new User("2", "austin2", 25);
        User[] userArray = new User[]{user1, user2};
        defaultStrRedisTemplate.opsForValue().set(redisTemplateUserArrayObjectKey, userArray);
        User[] userArrayGet = (User[]) defaultStrRedisTemplate.opsForValue().get(redisTemplateUserArrayObjectKey);
        System.out.println("userArrayGet: " + userArrayGet);

        //序列化JSONObject对象类型和反序列化JSONObject对象类型
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "123");
        jsonObject.put("name", "austin");
        jsonObject.put("age", 25);
        defaultStrRedisTemplate.opsForValue().set(redisTemplateJSONObjectKey, jsonObject);
        JSONObject jsonObjectGet = (JSONObject) defaultStrRedisTemplate.opsForValue().get(redisTemplateJSONObjectKey);
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
        defaultStrRedisTemplate.opsForValue().set(redisTemplateJSONArrayKey, jsonArray);
        JSONArray jsonArrayGet = (JSONArray) defaultStrRedisTemplate.opsForValue().get(redisTemplateJSONArrayKey);
        System.out.println("jsonArrayGet: " + jsonArrayGet);

    }

    @Test
    public void testStrJackson2() {
        String redisTemplateStringKey = "Jackson2_redisTemplateStringKey";
        String redisTemplateUserObjectKey = "Jackson2_redisTemplateUserObjectKey";
        String redisTemplateUserArrayObjectKey = "Jackson2_redisTemplateUserArrayObjectKey";
        String redisTemplateJSONObjectKey = "Jackson2_redisTemplateJSONObjectKey";
        String redisTemplateJSONArrayKey = "Jackson2_redisTemplateJSONArrayKey";

        //序列化String类型和反序列化String类型
        jackson2RedisTemplate.opsForValue().set(redisTemplateStringKey, "austin");
        String austin = (String) jackson2RedisTemplate.opsForValue().get(redisTemplateStringKey);
        System.out.println("stringGet: " + austin);

        //序列化Object对象类型和反序列化Object对象类型 (User对象)
        User user = new User("123", "austin", 25);
        jackson2RedisTemplate.opsForValue().set(redisTemplateUserObjectKey, user);
//        Object o = jackson2RedisTemplate.opsForValue().get(redisTemplateUserObjectKey);
//        User userGet = (User) jackson2RedisTemplate.opsForValue().get(redisTemplateUserObjectKey);
//        System.out.println("userGet: " + userGet);

        //序列化Object对象数组类型和反序列化Object对象数组类型 (User[]对象数组)
        User user1 = new User("1", "austin1", 25);
        User user2 = new User("2", "austin2", 25);
        User[] userArray = new User[]{user1, user2};
        jackson2RedisTemplate.opsForValue().set(redisTemplateUserArrayObjectKey, userArray);
//        User[] userArrayGet = (User[]) jackson2RedisTemplate.opsForValue().get(redisTemplateUserArrayObjectKey);
//        System.out.println("userArrayGet: " + userArrayGet);

        //序列化JSONObject对象类型和反序列化JSONObject对象类型
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "123");
        jsonObject.put("name", "austin");
        jsonObject.put("age", 25);
        jackson2RedisTemplate.opsForValue().set(redisTemplateJSONObjectKey, jsonObject);
//        JSONObject jsonObjectGet = (JSONObject) jackson2RedisTemplate.opsForValue().get(redisTemplateJSONObjectKey);
//        System.out.println("jsonObjectGet: " + jsonObjectGet);

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
        jackson2RedisTemplate.opsForValue().set(redisTemplateJSONArrayKey, jsonArray);
//        JSONArray jsonArrayGet = (JSONArray) jackson2RedisTemplate.opsForValue().get(redisTemplateJSONArrayKey);
//        System.out.println("jsonArrayGet: " + jsonArrayGet);

    }

    @Test
    public void testStrGenericJackson2() {
        String redisTemplateStringKey = "genericJackson2_redisTemplateStringKey";
        String redisTemplateUserObjectKey = "genericJackson2_redisTemplateUserObjectKey";
        String redisTemplateUserArrayObjectKey = "genericJackson2_redisTemplateUserArrayObjectKey";
        String redisTemplateJSONObjectKey = "genericJackson2_redisTemplateJSONObjectKey";
        String redisTemplateJSONArrayKey = "genericJackson2_redisTemplateJSONArrayKey";


        //序列化String类型和反序列化String类型
        genericJackson2RedisTemplate.opsForValue().set(redisTemplateStringKey, "austin");
        String austin = (String) genericJackson2RedisTemplate.opsForValue().get(redisTemplateStringKey);
        System.out.println("stringGet: " + austin);

        //序列化Object对象类型和反序列化Object对象类型 (User对象)
        User user = new User("123", "austin", 25);
        genericJackson2RedisTemplate.opsForValue().set(redisTemplateUserObjectKey, user);
        User userGet = (User) genericJackson2RedisTemplate.opsForValue().get(redisTemplateUserObjectKey);
        System.out.println("userGet: " + userGet);

        //序列化Object对象数组类型和反序列化Object对象数组类型 (User[]对象数组)
        User user1 = new User("1", "austin1", 25);
        User user2 = new User("2", "austin2", 25);
        User[] userArray = new User[]{user1, user2};
        genericJackson2RedisTemplate.opsForValue().set(redisTemplateUserArrayObjectKey, userArray);
        User[] userArrayGet = (User[]) genericJackson2RedisTemplate.opsForValue().get(redisTemplateUserArrayObjectKey);
        System.out.println("userArrayGet: " + userArrayGet);

        //序列化JSONObject对象类型和反序列化JSONObject对象类型
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "123");
        jsonObject.put("name", "austin");
        jsonObject.put("age", 25);
        genericJackson2RedisTemplate.opsForValue().set(redisTemplateJSONObjectKey, jsonObject);
        JSONObject jsonObjectGet = (JSONObject) genericJackson2RedisTemplate.opsForValue().get(redisTemplateJSONObjectKey);
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
        genericJackson2RedisTemplate.opsForValue().set(redisTemplateJSONArrayKey, jsonArray);
        JSONArray jsonArrayGet = (JSONArray) genericJackson2RedisTemplate.opsForValue().get(redisTemplateJSONArrayKey);
        System.out.println("jsonArrayGet: " + jsonArrayGet);

    }


    @Test
    public void testLongGenericJackson2() {
        String genericJackson2_LONG = "genericJackson2_Long";
        String genericJackson2_DOUBLE = "genericJackson2_Double";

        //序列化 Long 类型和反序列化 Long 类型
        genericJackson2RedisTemplate.opsForValue().set(genericJackson2_LONG, 10121313131311L);
        long austin = Long.parseLong(String.valueOf(genericJackson2RedisTemplate.opsForValue().get(genericJackson2_LONG)));
        System.out.println("stringGet: " + austin);

        genericJackson2RedisTemplate.opsForValue().set(genericJackson2_DOUBLE, 10.119324214141241d);
        Double austin2 = (Double) (genericJackson2RedisTemplate.opsForValue().get(genericJackson2_DOUBLE));
        System.out.println("stringGet: " + austin2);


    }

    /** 测试 Hash 操作 */
    @Test
    public void testGenericHash() {
        genericJackson2RedisTemplate.opsForHash().put("KEY","FIELD","test2");
        genericJackson2RedisTemplate.opsForHash().put("KEY","FIELD1","test1");
        genericJackson2RedisTemplate.opsForHash().put("KEY","FIELD2","test1");

        Set<Object> key = genericJackson2RedisTemplate.opsForHash().keys("KEY");
        System.out.println(JSONUtil.toJsonStr(key));

        Map<Object, Object> entries = genericJackson2RedisTemplate.opsForHash().entries("KEY");
        System.out.println(JSONUtil.toJsonStr(entries));

        List<Object> values = genericJackson2RedisTemplate.opsForHash().values("KEY");
        System.out.println(JSONUtil.toJsonStr(values));

    }
}

