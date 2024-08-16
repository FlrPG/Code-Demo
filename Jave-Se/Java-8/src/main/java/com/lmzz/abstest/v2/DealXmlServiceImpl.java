package com.lmzz.abstest.v2;

public class DealXmlServiceImpl implements DealXmlService {
    @Override
    public void dealEle(String xml) {
//        this.checkXml(xml);
//        this.dealXml(xml);
        MergeEleDocServiceImpl eleDocService = new MergeEleDocServiceImpl();
        eleDocService.checkXml(xml);
        eleDocService.dealXml(xml);
    }


    @Override
    public void dealCa(String xml, String path) {
//        this.checkXml(xml);
//        this.dealXml(xml);
        MergeEleCaServiceImpl caService = new MergeEleCaServiceImpl();
        caService.checkXml(xml);
        caService.dealXml(xml);
    }
}
