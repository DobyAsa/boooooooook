package com.book.bookshop.web;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author:yizhongwei
 * @Date:4/14 22:39
 */
@Controller
public class Test {
    @RequestMapping("/test")
    public String toTest(){
        return "test";
    }
}
