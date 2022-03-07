package com.book.bookshop.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.book.bookshop.entity.Book;
import com.book.bookshop.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


/**
 * @Author:yizhongwei
 * @Date:1/11 16:37
 */
@Controller
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;

    //跳转到首页
    @RequestMapping("/index")
    public String toindex() {
        return "index";
    }

    //获取图书信息
    @RequestMapping("/getBookData")
    public String getBookData(Model model, Integer page, Integer category) {
        //mybatis-plus 分页
        Page pages = new Page<>(page, 4);
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category", category);
        IPage<Book> iPage = bookService.page(pages, queryWrapper);
        model.addAttribute("bookList", iPage.getRecords());
        model.addAttribute("pre", iPage.getCurrent() - 1);
        model.addAttribute("next", iPage.getCurrent() + 1);
        model.addAttribute("category", category);
        model.addAttribute("cur",iPage.getCurrent());
        model.addAttribute("last",iPage.getPages());
        return "bookData";
    }

    //图书列表页
    @RequestMapping("/bookList")
    public String bookList(String category, Model model){
        model.addAttribute("category",category);
        return "books_list";
    }
    //获取图书列表数据
    @RequestMapping("/getBookListData")
    public String getBookListData(String category, Integer page,Integer pageSize, Model model){
        Page pages = new Page<Book>(page, pageSize);
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category", category);
        IPage<Book> iPage = bookService.page(pages, queryWrapper);
        model.addAttribute("bookList", iPage.getRecords());
        model.addAttribute("pre", iPage.getCurrent() - 1);
        model.addAttribute("next", iPage.getCurrent() + 1);
        model.addAttribute("category", category);
        model.addAttribute("cur",iPage.getCurrent());
        model.addAttribute("pages",iPage.getPages());
        //pageSize回传至前端页面booksListData.html
        model.addAttribute("pageSize",pageSize);
        return "booksListData";
    }

    //商品详情页
    @RequestMapping("/detail")
    public String bookDetail(Integer id,Model model){
        Book book = bookService.getById(id);
        model.addAttribute("book",book);
        return "details";
    }

    //根据图书名模糊搜索图书
    @RequestMapping("/searchBook")
    public String search(String inputBookName, Model model){
        System.out.println(inputBookName);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("name",inputBookName);
        List<Book> bookList = bookService.list(queryWrapper);
        System.out.println(bookList);
        model.addAttribute("bookList",bookList);
        return "searchPage";
    }
}
