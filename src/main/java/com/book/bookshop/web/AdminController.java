package com.book.bookshop.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.book.bookshop.entity.Admin;
import com.book.bookshop.entity.Book;
import com.book.bookshop.entity.User;
import com.book.bookshop.service.AdminService;
import com.book.bookshop.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author:yizhongwei
 * @Date:3/8 15:08
 * 管理员控制层
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private BookService bookService;
    //管理员登录
    @ResponseBody
    @PostMapping("/login")
    public String login(Admin admin , HttpSession session){
        return adminService.loginCheck(admin,session);
    }

    //去后台管理首页
    @RequestMapping("/toBookAdmin")
    public String toAllBooks (Model model,HttpSession session){
        List<Book> bookList = bookService.list(new QueryWrapper<>());
        for (Book book :bookList){
            if (book.getCategory().toString().equals("SELECTTED")) book.setCate("精选图书");
            if (book.getCategory().toString().equals("RECOMMEND")) book.setCate("推荐图书");
            if (book.getCategory().toString().equals("BARGAGIN")) book.setCate("特价图书");
        }
        model.addAttribute("bookList",bookList);
        return "admin/bookAdmin";
    }
}
