package com.book.bookshop.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.book.bookshop.entity.*;
import com.book.bookshop.entity.enums.Category;
import com.book.bookshop.service.AdminService;
import com.book.bookshop.service.BookService;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * @Author:yizhongwei
 * @Date:3/8 15:08
 * 管理员控制层
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
//    Logger logger = LoggerFactory.getLogger(Object.class);
//    Logger l = Logger.getLogger(AdminController.class)
    @Autowired
    private AdminService adminService;
    @Autowired
    private BookService bookService;

    //管理员登录
    @ResponseBody
    @PostMapping("/login")
    public String login(Admin admin, HttpSession session) {
        return adminService.loginCheck(admin, session);
    }

    //去后台管理首页
    @RequestMapping("/toBookAdmin")
    public String toAllBooks(Model model, HttpSession session) {
        return "admin/bookAdmin";
    }

    //【分页】【全部书籍】【后台】
    @RequestMapping("/getBookListByPage")
    public String getOrderListData(HttpSession session, Model model, Integer page, Integer pageSize) {
        Page pages = new Page<Book>(page, pageSize);
        IPage<Book> iPage = bookService.page(pages, new QueryWrapper<>());
        List<Book> bookList = iPage.getRecords();
        for (Book book : bookList) {
            if (book.getCategory().toString().equals("SELECTTED")) book.setCate("精选图书");
            if (book.getCategory().toString().equals("RECOMMEND")) book.setCate("推荐图书");
            if (book.getCategory().toString().equals("BARGAGIN")) book.setCate("特价图书");
        }
        model.addAttribute("bookList", bookList);
        model.addAttribute("pre", page - 1);
        model.addAttribute("next", page + 1);
        model.addAttribute("cur", page);
        model.addAttribute("pages", iPage.getPages());
        model.addAttribute("pageSize", pageSize);
        return "admin/allBooksData";
    }


    //删除图书
    @ResponseBody
    @RequestMapping("/deleteBook")
    public String deleteBook(Integer bookId) {
        boolean b = bookService.removeById(bookId);
        if (b)
            return "success";
        else return "fail";

    }

    @RequestMapping("/toAddBook")
    public String toAddBook() {
    return "admin/addBook";
    }


    //添加商品
    @RequestMapping("/addBook")
    public String toAddBook(Book book, MultipartFile bookPic) throws IOException {
/*        System.out.println(book.getIsbn());
        System.out.println(book.getCate());
        System.out.println(book.getNewPrice());
        System.out.println(book.getAuthor());
        System.out.println(book.getName());
        System.out.println(bookPic.getOriginalFilename());
        System.out.println(book.getPublisher());*/
        String bookPicName = bookPic.getOriginalFilename();
        if (book.getCate().equals("精选图书")) book.setCategory(Category.SELECTTED);
        if (book.getCate().equals("推荐图书")) book.setCategory(Category.RECOMMEND);
        if (book.getCate().equals("特价图书")) book.setCategory(Category.BARGAGIN);
        book.setPublishDate(new Date());
        book.setAuthorLoc("中国");
        book.setImgUrl(bookPicName);
        String filePath = "D:/images/";
        File dest = new File(filePath + bookPicName);
        bookPic.transferTo(dest);
        bookService.save(book);
        return "admin/bookAdmin";
    }
}
