package com.lmz.dp.factory.factoryMethod;

import com.lmz.dp.factory.product.AbstractProduct;

public class Client {

   /*
   //mode1
   public void method() {
        // 创建Product（传入AFactory，生产AProduct）
        AbstractProduct product = this.createProduct(new AFactory());

        // 使用Product
        //...
    }

    private AbstractProduct createProduct(Object factory) {
        if (factory instanceof AFactory) {
            return ((AFactory) factory).create();
        } else if (factory instanceof BFactory) {
            return ((BFactory) factory).create();
        }
        return null;
    }*/


/*
    //mode2
    public void method() {
        // 创建Product（传入AFactory，生产AProduct）
        AbstractProduct product = this.createProduct(new AFactory());

    }

    private AbstractProduct createProduct(AbstractFactory factory) {
        return factory.create();
    }
*/


    //mode3
    private AbstractFactory factory;

    public Client(AbstractFactory factory) {
        this.factory = factory;
    }

    private void method() {
        AbstractProduct abstractProduct = this.factory.create();
    }


/*
    public class ClientTest {
        public void useClient() {
            // 反射创建具体Factory
            AbstractFactory factory = XmlUtil.getBean("factory");
            Client client = new Client(factory);
            client.method(); // 内部执行AbstactProduct product = this.factory.create()
        }
    }

    <!-- 配置信息：在XML中定义Bean -->
    <bean id="factory" class="com.xxx.xxx.CFactory"/>

    */
}




