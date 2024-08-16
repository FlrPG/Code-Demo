package com.lmz.dp.factory.factoryMethod;

import com.lmz.dp.factory.product.AbstractProduct;
import com.lmz.dp.factory.product.BProduct;

class BFactory extends AbstractFactory {

    @Override
    public AbstractProduct create() {
        return new BProduct();
    }
}