package com.book.bookshop.web;

import com.book.bookshop.utils.Code;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
class CodeController {
    //登录的验证码
    @RequestMapping("/getCode")
    public void validateCode(HttpServletRequest request, HttpServletResponse response) {
        Code.getCode(request,response);
    }
}
