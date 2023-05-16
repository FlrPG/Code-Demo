package com.lmzz;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;

public class SaxXmlTest {
    public static void main(String[] args) {
        fileAna();


    }

    public static void fileAna() {
        try {
            SAXReader saxReader = new SAXReader();
//            File file = FileUtil.file(ResourceUtil.getResource("xml/Test.xml"));
            File file = new File("C:\\Users\\lmzz\\IdeaProjects\\Code-Demo\\Code-Review\\Test-Dom4j\\src\\main\\resources\\xml\\Test.xml");
            Document document = saxReader.read(file);
            int i = 1 / 0;
            System.out.println("test break");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void streamAna() {
        SAXReader saxReader = new SAXReader();
        InputStream stream = null;
        try {
            stream = ResourceUtil.getStream("xml/Test.xml");
            Document document = saxReader.read(stream);
            int i = 1 / 0;
            System.out.println("test break");
        } catch (DocumentException e) {
            IoUtil.close(stream);
            System.out.println(e.getMessage());
        }
    }

}
