package com.lmzz;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/testfile")
    public void testfile(){
        SaxXmlTest.fileAna();
    }

    @GetMapping("/testStream")
    public void testStream(){
        SaxXmlTest.streamAna();
    }

}
