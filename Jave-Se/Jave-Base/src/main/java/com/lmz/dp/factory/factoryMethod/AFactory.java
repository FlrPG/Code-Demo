package com.lmz.dp.factory.factoryMethod;

import com.lmz.dp.factory.product.AProduct;
import com.lmz.dp.factory.product.AbstractProduct;

class AFactory extends AbstractFactory {

    @Override
    public AbstractProduct create() {
        return new AProduct();
    }
}