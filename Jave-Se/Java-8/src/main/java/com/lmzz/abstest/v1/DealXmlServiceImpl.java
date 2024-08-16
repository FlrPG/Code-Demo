package com.lmzz.abstest.v1;

public class DealXmlServiceImpl implements DealXmlService {
    @Override
    public void dealEle(String xml) {
        this.checkXml(xml);
        this.dealXml(xml);
    }


    @Override
    public void dealCa(String xml, String path) {
        this.checkXml(xml);
        this.dealXml(xml);
    }

    private void checkXml(String xml) {
        if ("1".equals(xml)) {

        } else if ("2".equals(xml)) {

        }

    }

    private void dealXml(String xml) {
        if ("1".equals(xml)) {

        } else if ("2".equals(xml)) {

        }
    }
}
