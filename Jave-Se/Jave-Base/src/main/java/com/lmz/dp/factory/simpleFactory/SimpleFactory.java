package com.lmz.dp.factory.simpleFactory;

import com.lmz.dp.factory.product.AProduct;
import com.lmz.dp.factory.product.AbstractProduct;
import com.lmz.dp.factory.product.BProduct;

public class SimpleFactory {

    public static AbstractProduct create(String productType) {
        // 变化点：在未来新增Product类型时，这里会产生变化，变化意味着修改，而修改可能引入bug
        if ("aProduct".equals(productType)) {
            return new AProduct();
        } else if ("bProduct".equals(productType)) {
            return new BProduct();
        } else if (true){
            // ...
        }
        throw new RuntimeException("产品类型错误");
    }
}